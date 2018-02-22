package net.wit.controller.model;

import net.wit.entity.Order;
import net.wit.entity.OrderLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderLogModel extends BaseModel implements Serializable {

    /**  类型 */
    private OrderLog.Type type;

    /**  说明 */
    private String content;

    /**  时间 */
    private Date createDate;

    public OrderLog.Type getType() {
        return type;
    }

    public void setType(OrderLog.Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void bind(OrderLog orderLog) {
        this.createDate = orderLog.getCreateDate();
        this.type = orderLog.getType();
        this.content = orderLog.getContent();
    }


    public static List<OrderLogModel> bindList(List<OrderLog> orderLogs) {
        List<OrderLogModel> ms = new ArrayList<OrderLogModel>();
        for (OrderLog orderLog:orderLogs) {
            OrderLogModel m = new OrderLogModel();
            m.bind(orderLog);
            ms.add(m);
        }
        return ms;
    }


}