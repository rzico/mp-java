package net.wit.controller.model;

import net.wit.entity.Notice;
import net.wit.entity.Xdict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XDictModel extends BaseModel implements Serializable {

    private String name;

    public void bind(Xdict xdict) {
        this.name = xdict.getName();
    }

    public static List<XDictModel> bindList(List<Xdict> xdicts) {
        List<XDictModel> ms = new ArrayList<XDictModel>();
        for (Xdict xdict:xdicts) {
            XDictModel m = new XDictModel();
            m.bind(xdict);
            ms.add(m);
        }
        return ms;
    }

}