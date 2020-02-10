package com.ranaaditya.nitt_central.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class PaymentActivity extends AppCompatActivity {
    private   String UPI_ID;
    final int UPI_PAYMENT = 0;
    private   String NAME;
    private   String  AMOUNT;
    private   String NOTE;
    boolean isready;
    int toid;
    public   Payment payment;
    Button paymentbutton;
    ConstraintLayout parent;
    EditText payment_amount;
    TextView heading;
    int method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payment=new Payment();
        heading=findViewById(R.id.paymentheading);

        final SharedPreferences sharedPreferences
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        payment_amount=findViewById(R.id.payamount);
        paymentbutton=findViewById(R.id.paybutton);
        parent=findViewById(R.id.payment_parent);

        Intent paymentinten  = getIntent();
        UPI_ID=paymentinten.getStringExtra("payment_upi_id");
       // AMOUNT=paymentinten.getStringExtra("payment_amount");

        AMOUNT=payment_amount.getText().toString();
        NOTE=paymentinten.getStringExtra("payment_note");

        toid=paymentinten.getIntExtra("id",1);

        method=paymentinten.getIntExtra("method",1);

        NAME=paymentinten.getStringExtra("payment_name");
        switch (NAME){
            case "Shop3":
                parent.setBackground(getResources().getDrawable(R.drawable.store));
                break;
            case "Shop1":
                parent.setBackground(getResources().getDrawable(R.drawable.juiceimage));
                break;
            case "Shop2":
                parent.setBackground(getResources().getDrawable(R.drawable.cakeimage));
                break;
        }

        if(heading!=null){
            heading.append(NAME+"BY"+NOTE);
        }
        isready=paymentinten.getBooleanExtra("payment_ready",true);
        if(isready){
            paymentbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(method==1) {
                        AMOUNT = payment_amount.getText().toString();
                        payusingupi(AMOUNT, UPI_ID, NAME, NOTE, PaymentActivity.this);
                    }
                    else {
                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Api.base_url)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        Api api = retrofit.create(Api.class);
                        Call<String> call=api.walletPayment(sharedPreferences.getString("Token",""),toid,Integer.parseInt(AMOUNT));
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(getApplicationContext(),"Payment Done",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList,this);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                       upiPaymentDataOperation(dataList,this);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList,this);
                }
                break;
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public void upiPaymentDataOperation(ArrayList<String> data,Context context) {
        if (isConnectionAvailable(context)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(context, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(context, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    void payusingupi(String amount,String upiId,String name,String note,Context context){

        Uri uri=Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();

        Intent upi_payment_intent=new Intent(Intent.ACTION_VIEW);
        upi_payment_intent.setData(uri);
        Intent chooser=Intent.createChooser(upi_payment_intent,"pay with");
        if (null!=chooser.resolveActivity(context.getPackageManager())){
            Activity activity = (Activity)context;

            activity.startActivityForResult(chooser,UPI_PAYMENT);
        }else{
            Toast.makeText(context,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }


}
