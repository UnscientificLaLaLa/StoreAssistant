package com.zhaohao.storeassistant.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zhaohao.storeassistant.R;
import com.zhaohao.storeassistant.adapter.GoodsAdapter;
import com.zhaohao.storeassistant.adapter.SaleAdapter;
import com.zhaohao.storeassistant.database.DataHelper;
import com.zhaohao.storeassistant.database.GoodsData;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends AppCompatActivity implements View.OnClickListener{

    List<GoodsData> sales;
    SaleAdapter adapter;
    private double howToPay = 0;

    private Button scanBt;
    private Button payBt;
    private Button openAliPayBt;
    private Button openWechatBt;
    private RecyclerView saleRv;
    private TextView bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        (scanBt=findViewById(R.id.saleScanCodeBt)).setOnClickListener(this);
        (payBt=findViewById(R.id.salePayBt)).setOnClickListener(this);
        (openAliPayBt=findViewById(R.id.openAliPay)).setOnClickListener(this);
        (openWechatBt=findViewById(R.id.openWeChat)).setOnClickListener(this);
        saleRv=findViewById(R.id.SaleRv);
        bill=findViewById(R.id.saleBillTv);

        sales = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRv.setLayoutManager(layoutManager);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if((keyCode == KeyEvent.KEYCODE_BACK))
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SaleActivity.this);
            dialog.setTitle("取消本次交易？");
            dialog.setMessage("本次交易将被取消！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GoodsData data;
                    while(!sales.isEmpty()){
                        data = sales.get(0);
                        sales.remove(0);
                        data.setNumber(data.getNumber()+1);
                        data.update(data.getId());
                    }
                    finish();
                }
            });
            dialog.setNegativeButton("不", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.saleScanCodeBt:
                new IntentIntegrator(this).initiateScan();
                break;
            case R.id.salePayBt:
                AlertDialog.Builder dialog = new AlertDialog.Builder(SaleActivity.this);
                dialog.setTitle("确认已支付？");
                dialog.setMessage(howToPay+"元");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("不", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.openAliPay:
                if(!openAlipay()){
                    Toast.makeText(this, "没有装支付宝", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.openWeChat:
                if(!openWeixin()){
                    Toast.makeText(this, "没有装微信", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                DataHelper helper = new DataHelper();
                GoodsData sale = helper.checkGoodsByCode(result.getContents());
                if(sale==null){
                    Toast.makeText(this, "条码有误", Toast.LENGTH_LONG).show();
                } else if(sale.getNumber()<=0) {
                    Toast.makeText(this, "没货了", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, ""+sale.getGoodsCode(), Toast.LENGTH_LONG).show();
                    sales.add(0,sale);
                    sale.setNumber(sale.getNumber()-1);
                    sale.update(sale.getId());
                    adapter = new SaleAdapter(sales);
                    saleRv.setAdapter(adapter);
                    howToPay+=sale.getPrice();
                    bill.setText("共计： "+howToPay+"元");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean openWeixin(){
        if(!isAppAvilible(this,"com.tencent.mm"))
        {
            Toast.makeText(SaleActivity.this,"微信未安装",Toast.LENGTH_LONG).show();
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        startActivity(intent);
        return true;
    }

    public boolean openAlipay(){
        if(!isAppAvilible(this,"com.eg.android.AlipayGphone"))
        {
            Toast.makeText(SaleActivity.this,"支付宝未安装",Toast.LENGTH_LONG).show();
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cmp = new ComponentName("com.eg.android.AlipayGphone", "com.alipay.mobile.quinox.splash.ShareDispenseActivity");
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        startActivity(intent);
        return true;
    }

    public static boolean isAppAvilible(Context context, String pkg) {

        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(pkg)) {
                    return true;
                }
            }
        }
        return false;
    }

}
