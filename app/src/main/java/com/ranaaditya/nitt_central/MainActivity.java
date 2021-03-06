package com.ranaaditya.nitt_central;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ranaaditya.nitt_central.API.Api;
import com.ranaaditya.nitt_central.Form.FormsActivity;
import com.ranaaditya.nitt_central.Fragments.AdminFragment;
import com.ranaaditya.nitt_central.Fragments.FestFragment;
import com.ranaaditya.nitt_central.Fragments.OthersFragment;
import com.ranaaditya.nitt_central.Fragments.RegisterFragment;
import com.ranaaditya.nitt_central.Fragments.ShopsFragment;
import com.ranaaditya.nitt_central.Home.HomeActivity;
import com.ranaaditya.nitt_central.Maps.MapsActivity;
import com.ranaaditya.nitt_central.Models.LoginResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.bharatx.simple.BharatXTransactionManager;

public class MainActivity extends AppCompatActivity {
    final int UPI_PAYMENT = 0;
    private List<Integer> list=new ArrayList<>();
    boolean isloggedin=false;
    SharedPreferences loggedin;

    EditText roll,pass;
    Button login;
    ActionBarDrawerToggle toggle;
    AdminFragment adminFragment=AdminFragment.newInstance();
    OthersFragment othersFragment=OthersFragment.newInstance();
    ShopsFragment shopsFragment=ShopsFragment.newInstance();
    FestFragment festFragment=FestFragment.newInstance();
    RegisterFragment registerFragment=RegisterFragment.newInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loggedin=getSharedPreferences("loggedstatus",MODE_PRIVATE);
        Log.d("STATUS", String.valueOf(loggedin.getBoolean("loggedstatus",true)));
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
                            editor.putBoolean("loggedstatus",true);
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

        startPaymentTransaction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this function needs to be tested
        //not very confirm about its vulnerability

//        if (loggedin.getBoolean("loggedstatus",false));
//        if (isloggedin) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.drawermenu, menu);
//        }

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
                //Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                //startActivity(intent);
                scanQr();
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


    public void viewForgotPAssword(View view) {
        FragmentTransaction(R.id.mainlayout,registerFragment);
    }

    public void viewRegisterClicked(View view) {
        FragmentTransaction(R.id.mainlayout,registerFragment);
    }

    public void logout(View view) {
        finish();
    }

    public void startPaymentTransaction() {

        BharatXTransactionManager.initialize(
                this, "testSimplePartnerId", "testSimpleApiKey", Color.parseColor("#000000"));

        long amountInPaise = 100;
        String phoneNumber = "9999999999";
        String userId = "123456";
        String transactionId = "12345";

        BharatXTransactionManager.startTransaction(
                this,
                transactionId, // optional - pass null if you don't want this param
                userId, // optional - pass null if you don't want this param
                phoneNumber,
                amountInPaise,
                // optional - you can remove this param
                new BharatXTransactionManager.TransactionListener() {
                    @Override
                    public void onSuccess() {
                        // on success
                    }
                    @Override
                    public void onFailure() {
                        // on failure - optional, you can remove this
                        Log.d("Payment Failed", "FAILED");
                    }
                    @Override
                    public void onCancelled() {
                        // on cancelled - optional, you can remove this
                    }
                });
    }
    private void scanQr() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result =  IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                    // Open URL
                    Intent browserIntent =
                            new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(this, "Scan a valid QR", Toast.LENGTH_LONG).show();
                    scanQr();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
