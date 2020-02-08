package com.ranaaditya.nitt_central;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ranaaditya.nitt_central.API.Api;
import com.ranaaditya.nitt_central.Form.FormsActivity;
import com.ranaaditya.nitt_central.Fragments.AdminFragment;
import com.ranaaditya.nitt_central.Fragments.FestFragment;
import com.ranaaditya.nitt_central.Fragments.OthersFragment;
import com.ranaaditya.nitt_central.Fragments.ShopsFragment;
import com.ranaaditya.nitt_central.Home.HomeActivity;
import com.ranaaditya.nitt_central.Maps.MapsActivity;
import com.ranaaditya.nitt_central.Models.LoginResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    final int UPI_PAYMENT = 0;
    private List<Integer> list=new ArrayList<>();

    EditText roll,pass;
    Button login;
    ActionBarDrawerToggle toggle;
    AdminFragment adminFragment=AdminFragment.newInstance();
    OthersFragment othersFragment=OthersFragment.newInstance();
    ShopsFragment shopsFragment=ShopsFragment.newInstance();
    FestFragment festFragment=FestFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        roll = findViewById(R.id.loginwebmail);
        pass=findViewById(R.id.loginwebmailpassword);
        login=findViewById(R.id.loginbutton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("!!!!!!!!!!!!!!!!!","fuck");
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.base_url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                Api api = retrofit.create(Api.class);
                Call<LoginResponseModel> call = api.login(roll.getText().toString(),pass.getText().toString());
                call.enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                        if(response.body().getCode()==200)
                        {
                            editor.putString("Token",response.body().getToken());
                            editor.commit();
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), FormsActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        // this has fucked up basdly !!!
        // items are not responding properly !!
        // need to be chacked !!!
        DrawerLayout drawerLayout=findViewById(R.id.drawermain);
         toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) return true;
        int itemid=item.getItemId();
        switch (itemid){
            case  R.id.admin : {
                //fragmentTransaction.add(R.id.mainlayout,adminFragment).addToBackStack(null).commit(); break;
                FragmentTransaction(R.id.mainlayout,adminFragment);
                break;
            }
            case R.id.council : { FragmentTransaction(R.id.mainlayout,festFragment);
                break; }
            case R.id.academics : {FragmentTransaction(R.id.mainlayout,othersFragment);
                break; }
            case  R.id.shops : { FragmentTransaction(R.id.mainlayout,shopsFragment);
                break; }
            case R.id.etracking : {
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
            case  R.id.applogout : {finish();}
        }
return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    void FragmentTransaction(int res_id, Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(res_id,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }


}
