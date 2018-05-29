package net.wit.plat.weixin.pojo;

import java.util.List;

/**
 * Created by Eric-Yang on 2018/5/15.
 */
public class SmallInformation {
    private AuthorizerInfo authorizerInfo;
    private List<FuncInfo> funcInfo;

    public AuthorizerInfo getAuthorizerInfo() {
        return authorizerInfo;
    }

    public void setAuthorizerInfo(AuthorizerInfo authorizerInfo) {
        this.authorizerInfo = authorizerInfo;
    }

    public List<FuncInfo> getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(List<FuncInfo> funcInfo) {
        this.funcInfo = funcInfo;
    }
}
