package com.zhaohao.storeassistant.Activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.zhaohao.storeassistant.R;
import org.litepal.LitePal;

public class FunctionSelectActivity extends AppCompatActivity {

    protected final int PERMS_REQUEST_CODE = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_select);

        //请求权限
        String[] permissions=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
        requestPermissions(permissions, PERMS_REQUEST_CODE);


        //创建数据库
        LitePal litePal = new LitePal();
        litePal.getDatabase();

        //设置按钮和功能
        Button saleBt = findViewById(R.id.saleFunctionBt);
        Button goodsBt = findViewById(R.id.goodsFunctionBt);
        saleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionSelectActivity.this,
                        SaleActivity.class);
                startActivity(intent);
            }
        });

        goodsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionSelectActivity.this,
                        GoodsActivity.class);
                startActivity(intent);
            }
        });
    }
}
