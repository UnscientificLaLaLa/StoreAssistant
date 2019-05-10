package com.zhaohao.storeassistant.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaohao.storeassistant.Activity.ManageGoodsActivity;
import com.zhaohao.storeassistant.R;
import com.zhaohao.storeassistant.database.GoodsData;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{
    private List<GoodsData> mGoodsList;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        View goodsView;
        TextView goodsCode;
        TextView goodsName;
        TextView goodsPrice;
        TextView goodsNumber;

        public ViewHolder(View view){
            super(view);
            goodsView=view;
            goodsCode = view.findViewById(R.id.goodsAdapterCode);
            goodsName   = view.findViewById(R.id.goodsAdapterName);
            goodsPrice  = view.findViewById(R.id.goodsAdapterPrice);
            goodsNumber = view.findViewById(R.id.goodsAdapterNumber);

        }
    }

    public GoodsAdapter(List<GoodsData> GoodsList){
        mGoodsList = GoodsList;
    }

    @Override
    public GoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_goods,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GoodsAdapter.ViewHolder holder, final int position){
        GoodsData g = mGoodsList.get(position);
        holder.goodsCode.setText("Code:"+g.getGoodsCode());
        holder.goodsName.setText(g.getGoodsName());
        holder.goodsNumber.setText("Number:"+g.getNumber());
        holder.goodsPrice.setText("Price:"+g.getPrice());
        holder.goodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                GoodsData g = mGoodsList.get(position);
                Toast.makeText(view.getContext(), g.getGoodsName(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), ManageGoodsActivity.class);
                intent.putExtra("code",""+g.getGoodsCode());
                Toast.makeText(view.getContext(),""+g.getId(),Toast.LENGTH_LONG).show();
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return mGoodsList.size();
    }

}
