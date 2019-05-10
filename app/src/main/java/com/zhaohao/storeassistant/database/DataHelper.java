package com.zhaohao.storeassistant.database;

import org.litepal.LitePal;

import java.util.List;
import java.util.regex.Pattern;

public class DataHelper {

    public void addGoods(String code,String name,double price,int number){
        GoodsData data = new GoodsData();
        data.setGoodsCode(code);
        data.setGoodsName(name);
        data.setPrice(price);
        data.setNumber(number);
        data.save();
    }

    public GoodsData checkGoodsByCode(String code){
        List<GoodsData>datas = LitePal.select().where("goodsCode='"+code+"'").find(GoodsData.class);
        if(!datas.isEmpty()){
            return datas.get(0);
        }
        return null;
    }

    public boolean isCode(String code){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(code).matches();
    }
}
