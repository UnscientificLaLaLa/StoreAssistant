package com.zhaohao.storeassistant.api;

import org.json.JSONException;
import org.json.JSONObject;

public class CodeSearch {
    private static final String appkey = "447a317e3bbf88bb****************";
    private static final String pkg = "com.zhaohao.storeassistant";
    private static final String url = "http://api.juheapi.com/jhbar/bar?appkey="+appkey+"&pkg="+pkg+"&barcode=";
    private static final String urlend = "&cityid=1";

    private String code;
    private String name;
    private String json;

    public CodeSearch(String code){
        this.code = code;
    }

    public String getUrl(){
        return url+code+urlend;
    }

    public void setJson(String json){
        this.json=json;
        getNameByJson();
    }

    private void getNameByJson(){
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.getInt("error_code")!=0)
                name= "ERROR CODE " + jsonObject.getInt("error_code");
            JSONObject jsonObject1= jsonObject.getJSONObject("result");
            JSONObject jsonObject2= jsonObject1.getJSONObject("summary");
            name= jsonObject2.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            name= "JSON ERROR";
        }
    }

    public String getName() {
        return name;
    }
}
