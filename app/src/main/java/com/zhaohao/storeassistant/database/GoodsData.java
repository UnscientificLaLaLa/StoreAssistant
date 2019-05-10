package com.zhaohao.storeassistant.database;

import org.litepal.crud.LitePalSupport;

public class GoodsData extends LitePalSupport {
    private int id;
    private String goodsCode;
    private String goodsName;
    private double price;
    private int number;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getGoodsCode(){
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode){
        this.goodsCode=goodsCode;
    }

    public String getGoodsName(){
        return goodsName;
    }

    public void setGoodsName(String goodsName){
        this.goodsName=goodsName;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price=price;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number=number;
    }
}
