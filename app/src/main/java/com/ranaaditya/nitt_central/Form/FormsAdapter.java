package com.ranaaditya.nitt_central.Form;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ranaaditya.nitt_central.FormDetails.FormDetailsActivity;
import com.ranaaditya.nitt_central.Home.ShopsAdapter;
import com.ranaaditya.nitt_central.Models.FormModel;
import com.ranaaditya.nitt_central.R;

import java.util.ArrayList;

public class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<FormModel> forms;

    public FormsAdapter(Context mContext, ArrayList<FormModel> forms) {
        this.mContext = mContext;
        this.forms = forms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.form_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.formName.setText(forms.get(position).getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, FormDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("formid",forms.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        TextView formName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.form_list_card);
            formName=itemView.findViewById(R.id.form_name);
        }
    }
}
