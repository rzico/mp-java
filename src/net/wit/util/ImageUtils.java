/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.springframework.util.Assert;

/**
 * Utils - 图片处理(支持JDK、GraphicsMagick、ImageMagick)
 * 
 * @author rsico Team
 * @version 3.0
 */
public final class ImageUtils {

	/**
	 * 处理类型
	 */
	private enum Type {

		/** 自动 */
		auto,

		/** jdk */
		jdk,

		/**
		 * GraphicsMagick
		 */
		graphicsMagick,

		/**
		 * ImageMagick
		 */
		imageMagick
	}

	/** 处理类型 */
	private static Type type = Type.auto;

	/** GraphicsMagick程序路径 */
	private static String graphicsMagickPath;

	/** ImageMagick程序路径 */
	private static String imageMagickPath;

	/** 背景颜色 */
	private static final Color BACKGROUND_COLOR = Color.white;

	/** 目标图片品质(取值范围: 0 - 100) */
	private static final int DEST_QUALITY = 100;

	static {
		if (graphicsMagickPath == null) {
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.indexOf("windows") >= 0) {
				String pathVariable = System.getenv("Path");
				if (pathVariable != null) {
					String[] paths = pathVariable.split(";");
					for (String path : paths) {
						File gmFile = new File(path.trim() + "/gm.exe");
						File gmdisplayFile = new File(path.trim() + "/gmdisplay.exe");
						if (gmFile.exists() && gmdisplayFile.exists()) {
							graphicsMagickPath = path.trim();
							break;
						}
					}
				}
			}
		}

		if (imageMagickPath == null) {
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.indexOf("windows") >= 0) {
				String pathVariable = System.getenv("Path");
				if (pathVariable != null) {
					String[] paths = pathVariable.split(";");
					for (String path : paths) {
						File convertFile = new File(path.trim() + "/convert.exe");
						File compositeFile = new File(path.trim() + "/composite.exe");
						if (convertFile.exists() && compositeFile.exists()) {
							imageMagickPath = path.trim();
							break;
						}
					}
				}
			}
		}

		if (type == Type.auto) {
			try {
				IMOperation operation = new IMOperation();
				operation.version();
				IdentifyCmd identifyCmd = new IdentifyCmd(true);
				if (graphicsMagickPath != null) {
					identifyCmd.setSearchPath(graphicsMagickPath);
				}
				identifyCmd.run(operation);
				type = Type.graphicsMagick;
			} catch (Throwable e1) {
				try {
					IMOperation operation = new IMOperation();
					operation.version();
					IdentifyCmd identifyCmd = new IdentifyCmd(false);
					identifyCmd.run(operation);
					if (imageMagickPath != null) {
						identifyCmd.setSearchPath(imageMagickPath);
					}
					type = Type.imageMagick;
				} catch (Throwable e2) {
					type = Type.jdk;
				}
			}
		}
	}

	/**
	 * 不可实例化
	 */
	private ImageUtils() {
	}

	/**
	 * 等比例图片缩放
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param destWidth
	 *            目标宽度
	 * @param destHeight
	 *            目标高度
	 */
	public static void zoom(File srcFile, File destFile, int destWidth, int destHeight) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);
		Assert.state(destWidth > 0);
		Assert.state(destHeight > 0);
		if (type == Type.jdk) {
			Graphics2D graphics2D = null;
			ImageOutputStream imageOutputStream = null;
			ImageWriter imageWriter = null;
			try {
				BufferedImage srcBufferedImage = ImageIO.read(srcFile);
				int srcWidth = srcBufferedImage.getWidth();
				int srcHeight = srcBufferedImage.getHeight();
				int width = destWidth;
				int height = destHeight;
				if (srcHeight >= srcWidth) {
					width = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
				} else {
					height = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
				}
				BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, destWidth, destHeight);
				graphics2D.drawImage(srcBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), (destWidth / 2) - (width / 2), (destHeight / 2) - (height / 2), null);

				imageOutputStream = ImageIO.createImageOutputStream(destFile);
				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality((float) (DEST_QUALITY / 100.0));
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
				if (imageOutputStream != null) {
					try {
						imageOutputStream.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			IMOperation operation = new IMOperation();
			operation.thumbnail(destWidth, destHeight);
			operation.gravity("center");
			operation.background(toHexEncoding(BACKGROUND_COLOR));
			operation.extent(destWidth, destHeight);
			operation.quality((double) DEST_QUALITY);
			operation.addImage(srcFile.getPath());
			operation.addImage(destFile.getPath());
			if (type == Type.graphicsMagick) {
				ConvertCmd convertCmd = new ConvertCmd(true);
				if (graphicsMagickPath != null) {
					convertCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IM4JavaException e) {
					e.printStackTrace();
				}
			} else {
				ConvertCmd convertCmd = new ConvertCmd(false);
				if (imageMagickPath != null) {
					convertCmd.setSearchPath(imageMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IM4JavaException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 等比例图片缩放
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param destWidth
	 *            目标宽度
	 * @param destHeight
	 *            目标高度
	 */
	public static void crop(File srcFile, File destFile,int sourceX,int sourceY, int destWidth, int destHeight) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);
		Assert.state(destWidth > 0);
		Assert.state(destHeight > 0);
		if (type == Type.jdk) {
			Graphics2D graphics2D = null;
			ImageOutputStream imageOutputStream = null;
			ImageWriter imageWriter = null;
			try {
				BufferedImage srcBufferedImage = ImageIO.read(srcFile);
				BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, destWidth, destHeight);
				int srcWidth = srcBufferedImage.getWidth(); // 源图宽度
		        int srcHeight = srcBufferedImage.getHeight(); // 源图高度
				Image image = srcBufferedImage.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);
				ImageFilter cropFilter = new CropImageFilter(sourceX,sourceY, destWidth, destHeight);
				Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				graphics2D.drawImage(img , 0,0, null);

				imageOutputStream = ImageIO.createImageOutputStream(destFile);
				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality((float) (DEST_QUALITY / 100.0));
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
				if (imageOutputStream != null) {
					try {
						imageOutputStream.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			IMOperation operation = new IMOperation();
			operation.quality((double) DEST_QUALITY);
			operation.addImage(srcFile.getPath());
			operation.addImage(destFile.getPath());
			operation.crop(destWidth, destHeight, sourceX, sourceY);
			if (type == Type.graphicsMagick) {
				ConvertCmd convertCmd = new ConvertCmd(true);
				if (graphicsMagickPath != null) {
					convertCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IM4JavaException e) {
					e.printStackTrace();
				}
			} else {
				ConvertCmd convertCmd = new ConvertCmd(false);
				if (imageMagickPath != null) {
					convertCmd.setSearchPath(imageMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IM4JavaException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 添加水印
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param watermarkFile
	 *            水印位置
	 * @param alpha
	 *            水印透明度
	 */
	public static void addWatermark(File srcFile, File destFile, File watermarkFile,int x,int y, int alpha) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);
		Assert.state(alpha >= 0);
		Assert.state(alpha <= 100);
		if (watermarkFile == null || !watermarkFile.exists()) {
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if (type == Type.jdk) {
			Graphics2D graphics2D = null;
			ImageOutputStream imageOutputStream = null;
			ImageWriter imageWriter = null;
			try {
				BufferedImage srcBufferedImage = ImageIO.read(srcFile);
				int srcWidth = srcBufferedImage.getWidth();
				int srcHeight = srcBufferedImage.getHeight();
				BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, srcWidth, srcHeight);
				graphics2D.drawImage(srcBufferedImage, 0, 0, null);
				graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100F));

				BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
				int watermarkImageWidth = watermarkBufferedImage.getWidth();
				int watermarkImageHeight = watermarkBufferedImage.getHeight();

				graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);

				imageOutputStream = ImageIO.createImageOutputStream(destFile);
				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality(DEST_QUALITY / 100F);
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
				if (imageOutputStream != null) {
					try {
						imageOutputStream.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			String gravity = "SouthEast";
			IMOperation operation = new IMOperation();
			operation.gravity(gravity);
			operation.dissolve(alpha);
			operation.quality((double) DEST_QUALITY);
			operation.addImage(watermarkFile.getPath());
			operation.addImage(srcFile.getPath());
			operation.addImage(destFile.getPath());
			if (type == Type.graphicsMagick) {
				CompositeCmd compositeCmd = new CompositeCmd(true);
				if (graphicsMagickPath != null) {
					compositeCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					compositeCmd.run(operation);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IM4JavaException e) {
					e.printStackTrace();
				}
			} else {
				CompositeCmd compositeCmd = new CompositeCmd(false);
				if (imageMagickPath != null) {
					compositeCmd.setSearchPath(imageMagickPath);
				}
				try {
					compositeCmd.run(operation);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IM4JavaException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 初始化
	 */
	public static void initialize() {
	}

	/**
	 * 转换颜色为十六进制代码
	 * 
	 * @param color
	 *            颜色
	 * @return 十六进制代码
	 */
	private static String toHexEncoding(Color color) {
		String R, G, B;
		StringBuffer stringBuffer = new StringBuffer();
		R = Integer.toHexString(color.getRed());
		G = Integer.toHexString(color.getGreen());
		B = Integer.toHexString(color.getBlue());
		R = R.length() == 1 ? "0" + R : R;
		G = G.length() == 1 ? "0" + G : G;
		B = B.length() == 1 ? "0" + B : B;
		stringBuffer.append("#");
		stringBuffer.append(R);
		stringBuffer.append(G);
		stringBuffer.append(B);
		return stringBuffer.toString();
	}

}