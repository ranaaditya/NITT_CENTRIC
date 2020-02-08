package com.ranaaditya.nitt_central.FormDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ranaaditya.nitt_central.Form.FormsAdapter;
import com.ranaaditya.nitt_central.Models.LinkModel;
import com.ranaaditya.nitt_central.R;

import java.util.ArrayList;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.ViewHolder> {

    Context mContext;
    ArrayList<LinkModel> links;

    public LinksAdapter(Context mContext, ArrayList<LinkModel> links) {
        this.mContext = mContext;
        this.links = links;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.form_list, parent, false);
        return new LinksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.name.setText(links.get(position).getName());
            holder.url.setText(links.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,url;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.link_name);
            url=itemView.findViewById(R.id.link_url);
        }
    }
}
