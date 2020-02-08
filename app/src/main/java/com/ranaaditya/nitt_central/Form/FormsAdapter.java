package com.ranaaditya.nitt_central.Form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ranaaditya.nitt_central.Home.ShopsAdapter;
import com.ranaaditya.nitt_central.Models.FormModel;
import com.ranaaditya.nitt_central.R;

import java.util.ArrayList;

public class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.ViewHolder>{

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
        return new FormsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.formName.setText(forms.get(position).getName());
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
