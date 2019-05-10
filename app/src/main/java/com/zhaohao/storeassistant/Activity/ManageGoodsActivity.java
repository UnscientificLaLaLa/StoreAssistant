package com.zhaohao.storeassistant.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhaohao.storeassistant.R;
import com.zhaohao.storeassistant.database.DataHelper;
import com.zhaohao.storeassistant.database.GoodsData;

import org.litepal.LitePal;

public class ManageGoodsActivity extends AppCompatActivity {

    private EditText codeEt;
    private EditText nameEt;
    private EditText priceEt;
    private EditText numberEt;
    GoodsData goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_goods);

        codeEt=findViewById(R.id.manageGoodsCode);
        nameEt=findViewById(R.id.manageGoodsName);
        priceEt=findViewById(R.id.manageGoodsPrice);
        numberEt=findViewById(R.id.manageGoodsNumber);
        Button deleteBt = findViewById(R.id.manageGoodsDeleteBt);
        Button updateBt = findViewById(R.id.manageGoodsUpdate);

        Intent intent = getIntent();

        DataHelper helper = new DataHelper();
        goods = helper.checkGoodsByCode(intent.getStringExtra("code"));
        if(goods!=null){
            codeEt.setText(goods.getGoodsCode());
            nameEt.setText(goods.getGoodsName());
            priceEt.setText(""+goods.getPrice());
            numberEt.setText(""+goods.getNumber());
        } else {
            Toast.makeText(ManageGoodsActivity.this,
                    "参数有误",Toast.LENGTH_LONG).show();
            finish();
        }

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goods!=null) {
                    LitePal.delete(GoodsData.class, goods.getId());
                    finish();
                }
            }
        });

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goods!=null){
                    String code = codeEt.getText().toString();
                    String name = nameEt.getText().toString();
                    String price= priceEt.getText().toString();
                    String number=numberEt.getText().toString();
                    DataHelper help = new DataHelper();
                    if(!help.isCode(codeEt.getText().toString())){
                        Toast.makeText(ManageGoodsActivity.this,
                                "条形码错误！",Toast.LENGTH_LONG).show();
                    } else if(code.length()==0
                            || name.length()==0
                            || price.length()==0
                            || number.length()==0){
                        Toast.makeText(ManageGoodsActivity.this,
                                "请把信息补充完整！",Toast.LENGTH_LONG).show();
                    } else{
                        int id = goods.getId();
                        goods = new GoodsData();
                        goods.setGoodsCode(code);
                        goods.setGoodsName(name);
                        goods.setPrice(Double.parseDouble(price));
                        goods.setNumber(Integer.parseInt(number));
                        goods.update(id);
                        Toast.makeText(ManageGoodsActivity.this,
                                "修改成功",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }
}
