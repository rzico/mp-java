package net.wit.ueditor;


import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.StorageManager;
import net.wit.plugin.StoragePlugin;
import net.wit.service.PluginConfigService;
import net.wit.service.PluginService;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class MyBinaryUploader {


    public MyBinaryUploader() {
    }


    public static final State save(HttpServletRequest request, Map<String, Object> conf,StoragePlugin oss) {

        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
        if(!ServletFileUpload.isMultipartContent(request)) {
            return new BaseState(false, 5);
        } else {
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            if(isAjaxUpload) {
                upload.setHeaderEncoding("UTF-8");
            }


            try {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                List<MultipartFile> fileList = multipartRequest.getFiles(conf.get("fieldName").toString());
                if(fileList == null || fileList.size() == 0){
                    return new BaseState(false, 7);
                }else{
                    for(int i=0;i<fileList.size();i++){
                        MultipartFile multipartFile = fileList.get(0);
                        String savePath = (String)conf.get("savePath");
                        String originFileName = multipartFile.getOriginalFilename();
                        String suffix = FileType.getSuffixByFilename(originFileName);
                        originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
                        savePath = savePath + suffix;
                        long maxSize = ((Long)conf.get("maxSize")).longValue();
                        if(!validType(suffix, (String[])conf.get("allowFiles"))) {
                            return new BaseState(false, 8);
                        } else {
                            savePath = PathFormat.parse(savePath, originFileName);

                            //String physicalPath = (String)conf.get("rootPath") + savePath;
                            //InputStream is =multipartFile.getInputStream();

                            oss.upload(savePath,multipartFile,oss.getMineType(suffix));

                            State storageState = new BaseState(true);  //StorageManager.saveFileByInputStream(is, physicalPath, maxSize);
                            //is.close();
                            if(storageState.isSuccess()) {
                                storageState.putInfo("url", oss.getUrl(savePath));   //"/"+PathFormat.format(savePath));
                                storageState.putInfo("type", suffix);
                                storageState.putInfo("original", originFileName + suffix);
                            }


                            return storageState;
                        }
                    }


                    return null;
                }
            }  catch (IOException var15) {
                return new BaseState(false, 4);
            }catch (Exception var16) {
                return new BaseState(false, 6);
            }
        }
    }


    private static boolean validType(String type, String[] allowTypes) {
        List list = Arrays.asList(allowTypes);
        return list.contains(type);
    }
}
