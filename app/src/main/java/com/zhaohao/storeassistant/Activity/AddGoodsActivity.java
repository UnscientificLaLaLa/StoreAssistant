package com.zhaohao.storeassistant.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zhaohao.storeassistant.R;
import com.zhaohao.storeassistant.api.CodeSearch;
import com.zhaohao.storeassistant.database.DataHelper;
import com.zhaohao.storeassistant.database.GoodsData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AddGoodsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int UPDATE_PROGRESS = 0x1234;

    private EditText codeEt;
    private EditText nameEt;
    private EditText priceEt;
    private EditText numberEt;
    private Button scanBt;
    private Button addBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);

        codeEt=findViewById(R.id.addGoodsCode);
        nameEt=findViewById(R.id.addGoodsName);
        priceEt=findViewById(R.id.addGoodsPrice);
        numberEt=findViewById(R.id.addGoodsNumber);
        (scanBt = findViewById(R.id.addGoodsScanBt)).setOnClickListener(this);
        (addBt = findViewById(R.id.addGoodsSureBt)).setOnClickListener(this);
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
                codeEt.setText(goodsCode);
                Toast.makeText(this, "Scanned: " + goodsCode, Toast.LENGTH_LONG).show();
                getName(goodsCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addGoodsScanBt:
                new IntentIntegrator(AddGoodsActivity.this).initiateScan();
                break;
            case R.id.addGoodsSureBt:
                String code = codeEt.getText().toString();
                String name = nameEt.getText().toString();
                String price= priceEt.getText().toString();
                String number=numberEt.getText().toString();
                DataHelper help = new DataHelper();
                GoodsData goods = help.checkGoodsByCode(code);
                if(!help.isCode(codeEt.getText().toString())){
                    Toast.makeText(AddGoodsActivity.this,
                            "条形码错误！",Toast.LENGTH_LONG).show();
                }else if(goods!=null){
                    Toast.makeText(AddGoodsActivity.this,
                            "该货物已存在，请在管理下进行操作！",Toast.LENGTH_LONG).show();
                } else if(code.length()==0
                        || name.length()==0
                        || price.length()==0
                        || number.length()==0){
                    Toast.makeText(AddGoodsActivity.this,
                            "请把信息补充完整！",Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddGoodsActivity.this);
                    dialog.setTitle("检查添加信息是否正确！");
                    dialog.setMessage("商品条码 "+code+"\n"+
                            "商品名称 "+name+"\n"+
                            "商品单价"+price+"\n"+
                            "商品数量"+number);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String code = codeEt.getText().toString();
                            String name = nameEt.getText().toString();
                            String price= priceEt.getText().toString();
                            String number=numberEt.getText().toString();
                            GoodsData goods = new GoodsData();
                            goods.setGoodsCode(code);
                            goods.setGoodsName(name);
                            goods.setPrice(Double.parseDouble(price));
                            goods.setNumber(Integer.parseInt(number));
                            goods.save();
                            Toast.makeText(AddGoodsActivity.this,
                                    "添加成功", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    dialog.setNegativeButton("修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
                break;
            default:
        }
    }

    private void getName(String code) {
        final CodeSearch codeSearch = new CodeSearch(code);
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(codeSearch.getUrl());
                    connection = (HttpURLConnection) url.openConnection();
                    //设置请求方法
                    connection.setRequestMethod("GET");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);

                    //返回输入流
                    InputStream in = connection.getInputStream();

                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    codeSearch.setJson(result.toString());
                    show(codeSearch.getName());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {//关闭连接
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 展示
     *
     * @param result
     */
    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("aaa",result);
                nameEt.setText(result);
            }
        });
    }

}
