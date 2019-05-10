package com.zhaohao.storeassistant.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaohao.storeassistant.R;
import com.zhaohao.storeassistant.database.GoodsData;

import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder>{

    private List<GoodsData> mSaleList;

    public SaleAdapter(List<GoodsData> SaleList){
        this.mSaleList = SaleList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View saleView;
        TextView saleName;
        TextView salePrice;

        public ViewHolder(View view){
            super(view);
            saleView=view;
            saleName   = view.findViewById(R.id.saleAdapterName);
            salePrice  = view.findViewById(R.id.saleAdapterPrice);
        }
    }

    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sale,parent,false);
        final SaleAdapter.ViewHolder holder = new SaleAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        GoodsData s = mSaleList.get(position);
        viewHolder.saleName.setText(s.getGoodsName());
        viewHolder.salePrice.setText(""+s.getPrice());
    }

    @Override
    public int getItemCount(){
        return mSaleList.size();
    }

}
