package com.itheima.pojo;

import java.io.Serializable;
import java.util.Date;

public class OrderList implements Serializable {

    private String member;
    private String setmeal;
    private Date orderDate;
    private String orderType;

    public OrderList() {
    }

    public OrderList(String member, String setmeal) {
        this.member = member;
        this.setmeal = setmeal;
    }

    public OrderList(String member, String setmeal, Date orderDate, String orderType) {
        this.member = member;
        this.setmeal = setmeal;
        this.orderDate = orderDate;
        this.orderType = orderType;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getSetmeal() {
        return setmeal;
    }

    public void setSetmeal(String setmeal) {
        this.setmeal = setmeal;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
