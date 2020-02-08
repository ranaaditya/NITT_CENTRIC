package com.ranaaditya.nitt_central.FormDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.Constants;
import com.ranaaditya.nitt_central.API.Api;
import com.ranaaditya.nitt_central.Models.RequiredModel;
import com.ranaaditya.nitt_central.Payment.PaymentActivity;
import com.ranaaditya.nitt_central.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class UploadsAdapter extends RecyclerView.Adapter<UploadsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<RequiredModel> required;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    UploadInterface uploadInterface;
    SharedPreferences sharedPreferences;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    public UploadsAdapter(Context mContext, ArrayList<RequiredModel> required) {
        this.mContext = mContext;
        this.required = required;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.upload_list, parent, false);
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
//                    uploadInterface.getUpload(position);
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
            Toast.makeText(mContext,filePath.toString(),Toast.LENGTH_SHORT).show();
            String path = getPath(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.store);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api myapi=retrofit.create(Api.class);
            Call<String> mCall=myapi.test(1,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMTExMTgxMTQiLCJwYXNzd29yZCI6IkJ0ZWNoMTQiLCJpYXQiOjE1ODExOTAwODh9.jPEKLBEi0NjakkiLcswhE-hZKABMWqYa8q6pEnSRAZ4",imageString);
            mCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
//            File file= new File(path);
//            RequestBody fileReqBody = RequestBody.create(MediaType.parse("*/*"), file);
//            MultipartBody.Part part= MultipartBody.Part.createFormData("file",file.getName(),fileReqBody);
//            RequestBody token= RequestBody.create(MediaType.parse("text/plain"),"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMTExMTgxMTQiLCJwYXNzd29yZCI6IkJ0ZWNoMTQiLCJpYXQiOjE1ODExOTAwODh9.jPEKLBEi0NjakkiLcswhE-hZKABMWqYa8q6pEnSRAZ4");
//            RequestBody formId=RequestBody.create(MediaType.parse("text/plain"),"1");
//            Call<String> call=myapi.submitDoc(part);
//            call.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//
//                }
//            });


//            //Uploading code
//            try {
//                String uploadId = UUID.randomUUID().toString();
//
//                //Creating a multi part request
//                new MultipartUploadRequest(mContext, uploadId, "http://localhost:8000/updateForm")
//                        .addFileToUpload(path, "image") //Adding file
//                        .addParameter("name", "test") //Adding text parameter to the request
//                       // .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//
//            } catch (Exception exc) {
//                Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            }
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = mContext.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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
