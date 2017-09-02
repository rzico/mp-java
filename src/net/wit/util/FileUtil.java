package net.wit.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作工具
 * @author Administrator
 */
public class FileUtil {

	protected static Log log = LogFactory.getLog(FileUtil.class);

	private static final int BUFFER_SIZE = 16 * 1024;

	/**
	 * 缓存操作文件拷贝 由原至目标
	 * @param
	 */
	public static boolean copyFile(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 解压缩文件
	 * @author Ryan.xu
	 */
	public static List<String> Extract(String sZipPathFile, String sDestPath) {
		// int iCompressLevel; // 压缩比 取值范围为0~9
		// boolean bOverWrite = true; // 是否覆盖同名文件 取值范围为True和False
		// ArrayList allFiles = new ArrayList();
		// String sErrorMessage;
		List<String> allFileName = new ArrayList<String>();
		FileInputStream fins = null;
		ZipInputStream zins = null;
		try {
			// 先指定压缩档的位置和档名，建立FileInputStream对象
			fins = new FileInputStream(sZipPathFile);
			// 将fins传入ZipInputStream中
			zins = new ZipInputStream(fins);
			ZipEntry ze = null;
			byte ch[] = new byte[256];
			while ((ze = zins.getNextEntry()) != null) {
				File zfile = new File(sDestPath + ze.getName());
				if (zfile.exists()) {
					continue;
				}
				File fpath = new File(zfile.getParentFile().getPath());
				if (ze.isDirectory()) {
					if (!zfile.exists())
						zfile.mkdirs();
					zins.closeEntry();
				} else {
					if (!fpath.exists())
						fpath.mkdirs();
					FileOutputStream fouts = new FileOutputStream(zfile);
					try {
						int i;
						allFileName.add(zfile.getAbsolutePath());
						while ((i = zins.read(ch)) != -1)
							fouts.write(ch, 0, i);
						zins.closeEntry();
					} finally {
						fouts.close();
					}
				}
			}
		} catch (Exception e) {
		} finally {
			if (null != fins) {
				try {
					fins.close();
				} catch (IOException e) {
				}
			}
			if (null != zins) {
				try {
					zins.close();
				} catch (IOException e) {
				}
			}
		}
		// allFiles.clear();
		return allFileName;
	}

	public static String getType(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	public static void write2File(final String organTreeFilePath, final String content) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(organTreeFilePath)), "UTF-8"));
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static long uniqueID = System.currentTimeMillis();// length=13

	private static Random oRandom = new Random();

	/**
	 * 18位唯一值序列号
	 * @return
	 */
	public static synchronized String getUniqueID() {
		uniqueID++;
		return String.valueOf((uniqueID * 100000 + oRandom.nextInt(99999)));// 这样可以从主键中反馈出时间来，否则按上面的是反馈不出来的。
	}

	private static String getWebRoot() {
		return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
	}

	/**
	 * 通过判断图片的宽度和高度来确定是否是图片
	 * @param formatName 文件后缀
	 * @return
	 */
	private boolean isImage(String formatName) {

		if (StringUtils.isEmpty(formatName)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^(.jpg|.jpeg|.png|.gif|.bmp)$");
		Matcher matcher = pattern.matcher(formatName.toLowerCase());
		if (matcher.matches()) {
			return true;
		}
		return false;
	}
}
