package net.wit.entity.weixin;

public class Category {

    /**
     * first_class	一级类目名称
     * second_class	二级类目名称
     * third_class	三级类目名称
     * first_id	一级类目的ID编号
     * second_id	二级类目的ID编号
     * third_id	三级类目的ID编号
     */
    private String first_class;
    private String second_class;
    private String third_class;
    private long first_id;
    private long second_id;
    private long third_id;


    public String getFirst_class() {
        return first_class;
    }

    public void setFirst_class(String first_class) {
        this.first_class = first_class;
    }

    public String getSecond_class() {
        return second_class;
    }

    public void setSecond_class(String second_class) {
        this.second_class = second_class;
    }

    public String getThird_class() {
        return third_class;
    }

    public void setThird_class(String third_class) {
        this.third_class = third_class;
    }

    public long getFirst_id() {
        return first_id;
    }

    public void setFirst_id(long first_id) {
        this.first_id = first_id;
    }

    public long getSecond_id() {
        return second_id;
    }

    public void setSecond_id(long second_id) {
        this.second_id = second_id;
    }

    public long getThird_id() {
        return third_id;
    }

    public void setThird_id(long third_id) {
        this.third_id = third_id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "first_class='" + first_class + '\'' +
                ", second_class='" + second_class + '\'' +
                ", third_class='" + third_class + '\'' +
                ", first_id=" + first_id +
                ", second_id=" + second_id +
                ", third_id=" + third_id +
                '}';
    }
}
