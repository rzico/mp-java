package net.wit.controller.model;

import net.wit.entity.Admin;
import net.wit.entity.Role;
import net.wit.entity.Shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminModel extends BaseModel implements Serializable {

    private Long id;

    /** 头像 */
    private String logo;

    /** 名称 */
    private String name;

    /** 店铺 */
    private String shopName;

    /** 角色 */
    private String roleName;

    /** 店铺ID */
    private Long shopId;

    /** 角色ID */
    private Long roleId;

    /** 手机号 */
    private String mobile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void bind(Admin admin) {
        this.id = admin.getId();
        this.name = admin.getName();
        if (admin.getShop()!=null) {
            this.shopId = admin.getShop().getId();
            this.shopName = admin.getShop().getName();
        } else {
            this.shopId = 0L;
            this.shopName = "未分配";
        }
        if (admin.getMember()!=null) {
            this.mobile = admin.getMember().getMobile();
            this.logo = admin.getMember().getLogo();
        } else {
            this.mobile = "未绑定";
        }
        if (admin.isOwner()) {
            this.roleName = "店主";
        } else {
            String s = "";
            for (Role role:admin.getRoles()) {
                if (s.equals("")) {
                    s = s + ",";
                }
                s = s +role.getName();
            }
            this.roleName = s;
        }
        if (admin.getRoles().size()>0) {
            this.roleId = admin.getRoles().get(0).getId();
        } else {
            this.roleId = 0L;
        }
    }

    public static List<AdminModel> bindList(List<Admin> admins) {
        List<AdminModel> ms = new ArrayList<AdminModel>();
        for (Admin admin:admins) {
            AdminModel m = new AdminModel();
            m.bind(admin);
            ms.add(m);
        }
        return ms;
    }
}