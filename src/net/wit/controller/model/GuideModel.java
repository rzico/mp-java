package net.wit.controller.model;
import net.wit.entity.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuideModel extends BaseModel implements Serializable {
    private Boolean steped1;
    private Boolean steped2;
    private Boolean steped3;
    private Boolean steped4;

    public Boolean getSteped1() {
        return steped1;
    }

    public void setSteped1(Boolean steped1) {
        this.steped1 = steped1;
    }

    public Boolean getSteped2() {
        return steped2;
    }

    public void setSteped2(Boolean steped2) {
        this.steped2 = steped2;
    }

    public Boolean getSteped3() {
        return steped3;
    }

    public void setSteped3(Boolean steped3) {
        this.steped3 = steped3;
    }

    public Boolean getSteped4() {
        return steped4;
    }

    public void setSteped4(Boolean steped4) {
        this.steped4 = steped4;
    }
}