package net.wit.ueditor;

/**
 * Created by Xus on 2018/1/15.
 */

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

public class MyJsonManager {

    private final String jsonPath;
    private final String originalPath;
    private String fileName = "province.json";
    private String parentPath = null;
    private JSONObject jsonConfig = null;


    private MyJsonManager(String jsonPath, String fileName) throws FileNotFoundException, IOException {
        this.jsonPath = jsonPath.replace("\\", "/");
        this.fileName = fileName;
        this.originalPath = this.jsonPath;
        this.initEnv();
    }


    public static MyJsonManager getInstance(String jsonPath, String fileName) {
        try {
            return new MyJsonManager(jsonPath, fileName);
        } catch (Exception var4) {
            return null;
        }
    }


    public boolean valid() {
        return this.jsonConfig != null;
    }


    public JSONObject getAllConfig() {
        return this.jsonConfig;
    }


    private void initEnv() throws FileNotFoundException, IOException {
        File file = new File(this.originalPath);
        if(!file.isAbsolute()) {
            file = new File(file.getAbsolutePath());
        }

        this.parentPath = this.originalPath;
        String configContent = this.readFile(this.getConfigPath());

        try {
            JSONObject e = new JSONObject(configContent);
            this.jsonConfig = e;
        } catch (Exception var4) {
            this.jsonConfig = null;
        }
    }


    private String getConfigPath() {
        return this.parentPath + this.fileName;
    }

    private String readFile(String path) throws IOException {
        StringBuilder builder = new StringBuilder();

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
            BufferedReader bfReader = new BufferedReader(reader);
            String tmpContent = null;

            while((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }

            bfReader.close();
        } catch (UnsupportedEncodingException var6) {
            ;
        }

        return this.filter(builder.toString());
    }


    private String filter(String input) {
        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
    }

}
