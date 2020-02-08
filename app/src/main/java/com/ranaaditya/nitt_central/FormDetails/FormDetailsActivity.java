package com.ranaaditya.nitt_central.FormDetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ranaaditya.nitt_central.API.Api;
import com.ranaaditya.nitt_central.Models.FormModel;
import com.ranaaditya.nitt_central.R;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormDetailsActivity extends AppCompatActivity implements UploadInterface {
    RecyclerView links,files;
    UploadsAdapter adapter2;
    private final   int REQUEST_CODE=99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_details);
        links=findViewById(R.id.links_recycler);
        files=findViewById(R.id.from_upload_recycler);
        int formid=getIntent().getIntExtra("formid",0);
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
                LinksAdapter adapter1=new LinksAdapter(getApplicationContext(),response.body().getLinks());
                links.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                links.setAdapter(adapter1);
                adapter2=new UploadsAdapter(FormDetailsActivity.this,response.body().getRequired());
            }

            @Override
            public void onFailure(Call<FormModel> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE:{
                if(resultCode==RESULT_OK){
                    Uri fileUri=data.getData();
                    String path=fileUri.getPath(); // path of the file selected
                    if(fileUri!=null) {
                        File uploadfile = new File(String.valueOf(fileUri)); //selected file  is in File format so may you can use it as it is

                        // TODO use library here for upoading this file  over server
                    }

                }
                break;
            }
        }

        adapter2.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void getUpload(int i) {

        Intent fileIntent=new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("*.pdf");
        startActivityForResult(fileIntent,REQUEST_CODE);


    }
}
