package com.ranaaditya.nitt_central.FormDetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ranaaditya.nitt_central.API.Api;
import com.ranaaditya.nitt_central.Models.FormModel;
import com.ranaaditya.nitt_central.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormDetailsActivity extends AppCompatActivity {
    RecyclerView links,files;
    UploadsAdapter adapter2;
    private final   int REQUEST_CODE=99,PICK_PDF_REQUEST=1;
    static int curform;
    FormModel form;
    public static final String UPLOAD_URL = "http://localhost:8000/updateForm";
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_details);
        links=findViewById(R.id.links_recycler);
        files=findViewById(R.id.from_upload_recycler);
        int formid=getIntent().getIntExtra("formid",1);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);
        Call<FormModel> call=api.specificForm(formid);
        call.enqueue(new Callback<FormModel>() {
            @Override
            public void onResponse(Call<FormModel> call, Response<FormModel> response) {
                Log.d("!!!!!!!!!!!!",response.body().getLinks().get(0).getName());
                form=response.body();
                LinksAdapter adapter1=new LinksAdapter(getApplicationContext(),response.body().getLinks());
                links.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                links.setAdapter(adapter1);
                adapter2=new UploadsAdapter(FormDetailsActivity.this,response.body().getRequired());
                files.setLayoutManager(new LinearLayoutManager(FormDetailsActivity.this));
                files.setAdapter(adapter2);
            }

            @Override
            public void onFailure(Call<FormModel> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//        }
        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    Uri fileUri = data.getData();
                    String path = fileUri.getPath(); // path of the file selected
                    if (fileUri != null) {
                        File uploadfile = new File(String.valueOf(fileUri)); //selected file  is in File format so may you can use it as it is

                        // TODO use library here for upoading this file  over server
                    }

                }
                break;
            }
        }

        adapter2.onActivityResult(requestCode, resultCode, data);

    }
    public void uploadMultipart() {
        //getting name for the image

        //getting the actual path of the image
        String path = getPath(filePath);

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name",form.getRequired().get(curform).getName()) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public void getUpload(int i) {
        curform=i;
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }
}
