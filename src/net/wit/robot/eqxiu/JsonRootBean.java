package net.wit.robot.eqxiu;

import java.util.List;

/**
 * Created by Eric on 2018/2/24.
 */
public class JsonRootBean {

        private boolean success;
        private int code;
        private String msg;
        private String obj;
        private String map;
        private List<HList> list;
        public void setSuccess(boolean success) {
            this.success = success;
        }
        public boolean getSuccess() {
            return success;
        }

        public void setCode(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getMsg() {
            return msg;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }
        public String getObj() {
            return obj;
        }

        public void setMap(String map) {
            this.map = map;
        }
        public String getMap() {
            return map;
        }

        public void setList(List<HList> list) {
            this.list = list;
        }
        public List<HList> getList() {
            return list;
        }

    }
