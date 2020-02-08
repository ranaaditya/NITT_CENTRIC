package com.ranaaditya.nitt_central.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ranaaditya.nitt_central.API.Api;
import com.ranaaditya.nitt_central.Models.FormModel;
import com.ranaaditya.nitt_central.Models.ShopModel;
import com.ranaaditya.nitt_central.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormsActivity extends AppCompatActivity {

    RecyclerView forms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        forms=findViewById(R.id.forms_recycler);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);
        Call<ArrayList<FormModel>> call=api.getForms();
        call.enqueue(new Callback<ArrayList<FormModel>>() {
            @Override
            public void onResponse(Call<ArrayList<FormModel>> call, Response<ArrayList<FormModel>> response) {
                FormsAdapter adapter=new FormsAdapter(getApplicationContext(),response.body());
                forms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                forms.setAdapter(adapter);
                Toast.makeText(getApplicationContext(),"fuck",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<FormModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
