package com.ranaaditya.nitt_central.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ranaaditya.nitt_central.R;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    private   String UPI_ID;
    private   String NAME;
    private   String  AMOUNT;
    private   String NOTE;
    boolean isready;
    private  final int UPI_PAYMENT = 0;
    public   Payment payment;
    Button paymentbutton;
    EditText payment_amount;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payment=new Payment();
        heading=findViewById(R.id.paymentheading);

        payment_amount=findViewById(R.id.payamount);
        paymentbutton=findViewById(R.id.paybutton);

        Intent paymentinten  = getIntent();
        UPI_ID=paymentinten.getStringExtra("payment_upi_id");
       // AMOUNT=paymentinten.getStringExtra("payment_amount");

        AMOUNT=payment_amount.getText().toString();
        NOTE=paymentinten.getStringExtra("payment_note");

        NAME=paymentinten.getStringExtra("payment_name");

        if(heading!=null){
            heading.append(NAME+"BY"+NOTE);
        }
        isready=paymentinten.getBooleanExtra("payment_ready",false);
        if(isready){
            paymentbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payment.payusingupi(AMOUNT,UPI_ID,NAME,NOTE,PaymentActivity.this);
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
                        payment.upiPaymentDataOperation(dataList,this);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                       payment.upiPaymentDataOperation(dataList,this);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    payment.upiPaymentDataOperation(dataList,this);
                }
                break;
        }
    }

}
