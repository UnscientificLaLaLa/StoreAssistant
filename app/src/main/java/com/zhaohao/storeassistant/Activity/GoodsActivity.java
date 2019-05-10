package com.zhaohao.storeassistant.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zhaohao.storeassistant.R;
import com.zhaohao.storeassistant.adapter.GoodsAdapter;
import com.zhaohao.storeassistant.database.GoodsData;

import org.litepal.LitePal;

import java.util.List;

public class GoodsActivity extends AppCompatActivity {

    RecyclerView goodsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        Button scanBt =findViewById(R.id.goodsScanCodeBt);
        Button addBt =findViewById(R.id.addGoodsBt);
        goodsRv =findViewById(R.id.goodsRv);

        List<GoodsData> goodsData = LitePal.findAll(GoodsData.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        goodsRv.setLayoutManager(layoutManager);
        GoodsAdapter adapter = new GoodsAdapter(goodsData);
        goodsRv.setAdapter(adapter);

        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(GoodsActivity.this).initiateScan();
            }
        });
        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsActivity.this,
                        AddGoodsActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        List<GoodsData>goods = LitePal.findAll(GoodsData.class);
        GoodsAdapter adapter = new GoodsAdapter(goods);
        goodsRv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String goodsCode;
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                goodsCode = result.getContents();
                //Toast.makeText(this, "Scanned: " + goodsCode, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GoodsActivity.this,ManageGoodsActivity.class);
                intent.putExtra("code",goodsCode);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
