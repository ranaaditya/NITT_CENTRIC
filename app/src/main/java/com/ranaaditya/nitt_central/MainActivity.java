package com.ranaaditya.nitt_central;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.ranaaditya.nitt_central.Fragments.AdminFragment;
import com.ranaaditya.nitt_central.Home.HomeActivity;
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
    FragmentManager fragmentManager=getSupportFragmentManager();
    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
    AdminFragment adminFragment=AdminFragment.newInstance();
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
                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
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
            case  R.id.admin : {fragmentTransaction.add(R.id.mainlayout,adminFragment).addToBackStack(null).commit(); break;}
            case R.id.council : {Intent intent= new Intent(MainActivity.this,test.class); startActivity(intent); break;}
            case R.id.academics : {Intent intent= new Intent(MainActivity.this,test.class); startActivity(intent); break;}
            case  R.id.shops : {Intent intent= new Intent(MainActivity.this,test.class); startActivity(intent); break;}
            case  R.id.applogout : {Intent intent= new Intent(MainActivity.this,test.class); startActivity(intent); break;}
        }
return super.onOptionsItemSelected(item);
    }


}
