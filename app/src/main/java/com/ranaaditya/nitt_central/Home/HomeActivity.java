package com.ranaaditya.nitt_central.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ranaaditya.nitt_central.API.Api;
import com.ranaaditya.nitt_central.Models.ShopModel;
import com.ranaaditya.nitt_central.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    LocationManager locationManager;
    int PERMISSION_ID = 44;
    Double lat,lon;
    TextView nearest;
    RecyclerView shops;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        final SharedPreferences sharedPreferences
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        nearest=findViewById(R.id.closest_shop);
        shops=findViewById(R.id.shops_recycler);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null)
                        {
                            lat=location.getLatitude();
                            lon=location.getLongitude();
                            Toast.makeText(getApplicationContext(),Double.toString(lat)+" "+Double.toString(lat),Toast.LENGTH_SHORT).show();
                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Api.base_url)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

                            Api api = retrofit.create(Api.class);
                            Call<ArrayList<ShopModel>> call=api.getShops(lat,lon,sharedPreferences.getString("Token",""));
                            call.enqueue(new Callback<ArrayList<ShopModel>>() {
                                @Override
                                public void onResponse(Call<ArrayList<ShopModel>> call, Response<ArrayList<ShopModel>> response) {
                                    nearest.setText(response.body().get(0).getName());
                                    ShopsAdapter adapter=new ShopsAdapter(getApplicationContext(),response.body());
                                    shops.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                                    shops.setAdapter(adapter);
                                }

                                @Override
                                public void onFailure(Call<ArrayList<ShopModel>> call, Throwable t) {
                                }
                            });
                        }
                    }
                });
    }
}
