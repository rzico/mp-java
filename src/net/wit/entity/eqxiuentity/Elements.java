package net.wit.entity.eqxiuentity;

/**
 * Created by Eric on 2018/2/24.
 */
public class Elements {

        private String content;
        private Css css;
        private Properties properties;
        private long id;
        private long pageId;
        private long sceneId;
        private int num;
        private String name;
        private int isEditable;
        public void setCss(Css css) {
            this.css = css;
        }
        public Css getCss() {
            return css;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
        public Properties getProperties() {
            return properties;
        }

        public void setId(long id) {
            this.id = id;
        }
        public long getId() {
            return id;
        }

        public void setPageId(long pageId) {
            this.pageId = pageId;
        }
        public long getPageId() {
            return pageId;
        }

        public void setSceneId(long sceneId) {
            this.sceneId = sceneId;
        }
        public long getSceneId() {
            return sceneId;
        }

        public void setNum(int num) {
            this.num = num;
        }
        public int getNum() {
            return num;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setIsEditable(int isEditable) {
            this.isEditable = isEditable;
        }
        public int getIsEditable() {
            return isEditable;
        }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
