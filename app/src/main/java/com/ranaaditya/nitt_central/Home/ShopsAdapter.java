package com.ranaaditya.nitt_central.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ranaaditya.nitt_central.Models.ShopModel;
import com.ranaaditya.nitt_central.Payment.Payment;
import com.ranaaditya.nitt_central.Payment.PaymentActivity;
import com.ranaaditya.nitt_central.R;

import java.util.ArrayList;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<ShopModel> shops;

    public ShopsAdapter(Context mContext, ArrayList<ShopModel> shops) {
        this.mContext = mContext;
        this.shops = shops;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shops_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(position==0)
            holder.shopImage.setImageResource(R.drawable.store);
        if(position==1)
            holder.shopImage.setImageResource(R.drawable.juiceimage);
        if (position==2)
            holder.shopImage.setImageResource(R.drawable.cakeimage);

        holder.shopName.setText(shops.get(position).getName());
        holder.shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, PaymentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("payment_upi_id",shops.get(position).getUpi());
                intent.putExtra("payment_note","111118114");
                intent.putExtra("payment_name",shops.get(position).getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView shopName;
        de.hdodenhof.circleimageview.CircleImageView shopImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName=itemView.findViewById(R.id.shop_name);
            shopImage=itemView.findViewById(R.id.shop_image);
        }
    }
}
