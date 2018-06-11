package net.wit.controller.model;

import net.wit.entity.model.AppletSowingMapModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppletSowingModel extends BaseModel implements Serializable {

    List<AppletSowingMapModel> appletSowingMapModels = new ArrayList<>();

    public List<AppletSowingMapModel> getAppletSowingMapModels() {
        return appletSowingMapModels;
    }

    public void setAppletSowingMapModels(List<AppletSowingMapModel> appletSowingMapModels) {
        this.appletSowingMapModels = appletSowingMapModels;
    }
}
