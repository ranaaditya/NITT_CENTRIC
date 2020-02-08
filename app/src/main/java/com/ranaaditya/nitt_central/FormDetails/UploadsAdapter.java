package com.ranaaditya.nitt_central.FormDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.ranaaditya.nitt_central.Models.RequiredModel;
import com.ranaaditya.nitt_central.Payment.PaymentActivity;
import com.ranaaditya.nitt_central.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class UploadsAdapter extends RecyclerView.Adapter<UploadsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<RequiredModel> required;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    UploadInterface uploadInterface;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    public UploadsAdapter(Context mContext, ArrayList<RequiredModel> required) {
        this.mContext = mContext;
        this.required = required;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.form_list, parent, false);
        return new UploadsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(required.get(position).getName());
        if(required.get(position).getType()==2)
        {
            holder.upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, PaymentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("payment_upi_id","rameshtejus@oksbi");
                    intent.putExtra("payment_note","111118114");
                    intent.putExtra("payment_name","Institute");
                    mContext.startActivity(intent);
                }
            });
        }
        else {
            holder.upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadInterface=new FormDetailsActivity();
                    uploadInterface.getUpload(position);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    ((Activity)mContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            });
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }

    @Override
    public int getItemCount() {
        return required.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Button upload;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.upload_name);
            upload=itemView.findViewById(R.id.upload_file);
        }
    }
}
