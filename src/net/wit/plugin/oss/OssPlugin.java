
package net.wit.plugin.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import net.wit.FileInfo;
import net.wit.entity.PluginConfig;
import net.wit.plugin.StoragePlugin;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;

/**
 * Plugin - 阿里云存储
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Component("ossPlugin")
public class OssPlugin extends StoragePlugin {

	@Override
	public String getName() {
		return "阿里云存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "OSS++";
	}

	@Override
	public void upload(String path, MultipartFile file, String contentType) throws IOException {
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String accessId = pluginConfig.getAttribute("accessId");
			String accessKey = pluginConfig.getAttribute("accessKey");
			String bucketName = pluginConfig.getAttribute("bucketName");
			String endpoint = pluginConfig.getAttribute("endpoint");
			InputStream inputStream = file.getInputStream();
			try {
				OSSClient ossClient = new OSSClient(endpoint,accessId, accessKey);
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(contentType);
				objectMetadata.setContentLength(file.getSize());
				ossClient.putObject(bucketName, StringUtils.removeStart(path, "/"), inputStream, objectMetadata);
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {
				inputStream.close();
			}
		} else {
			throw new IOException("无效插件");
		}
	}

	@Override
	public String getUrl(String path) {
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String urlPrefix = pluginConfig.getAttribute("urlPrefix");
			return urlPrefix + path;
		}
		return null;
	}

	@Override
	public List<FileInfo> browser(String path) {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		PluginConfig pluginConfig = getPluginConfig();
		if (pluginConfig != null) {
			String accessId = pluginConfig.getAttribute("accessId");
			String accessKey = pluginConfig.getAttribute("accessKey");
			String bucketName = pluginConfig.getAttribute("bucketName");
			String urlPrefix = pluginConfig.getAttribute("urlPrefix");
			try {
				OSSClient ossClient = new OSSClient(accessId, accessKey);
				ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
				listObjectsRequest.setPrefix(StringUtils.removeStart(path, "/"));
				listObjectsRequest.setDelimiter("/");
				ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
				for (String commonPrefix : objectListing.getCommonPrefixes()) {
					FileInfo fileInfo = new FileInfo();
					fileInfo.setName(StringUtils.substringAfterLast(StringUtils.removeEnd(commonPrefix, "/"), "/"));
					fileInfo.setUrl(urlPrefix + "/" + commonPrefix);
					fileInfo.setIsDirectory(true);
					fileInfo.setSize(0L);
					fileInfos.add(fileInfo);
				}
				for (OSSObjectSummary ossObjectSummary : objectListing.getObjectSummaries()) {
					if (ossObjectSummary.getKey().endsWith("/")) {
						continue;
					}
					FileInfo fileInfo = new FileInfo();
					fileInfo.setName(StringUtils.substringAfterLast(ossObjectSummary.getKey(), "/"));
					fileInfo.setUrl(urlPrefix + "/" + ossObjectSummary.getKey());
					fileInfo.setIsDirectory(false);
					fileInfo.setSize(ossObjectSummary.getSize());
					fileInfo.setLastModified(ossObjectSummary.getLastModified());
					fileInfos.add(fileInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fileInfos;
	}

}