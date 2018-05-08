package net.wit.plat.weixin.pojo;

/**
 * Created by Jinlesoft on 2018/5/8.
 */
public class FuncInfo {
    private Category funcscope_category;

    public Category getFuncscope_category() {
        return funcscope_category;
    }

    public void setFuncscope_category(Category funcscope_category) {
        this.funcscope_category = funcscope_category;
    }

    public static  class Category{
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
