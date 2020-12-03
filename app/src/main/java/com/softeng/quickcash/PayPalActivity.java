package com.softeng.quickcash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class PayPalActivity extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final int PAYPAL_REQUEST_CODE = 555;
    public static final String PAYPAL_CLIENT_ID = "AS_cduc1WCppkch57dlMrtdvo4IR_qnL6HhLh5PHgsQEeidwUi9UEwEx1DXnraC93e6U7N5OR8VaXS2W";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    private String amount = "";
    private String postID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        getBundleData();

        startPayPalService();
    }

    private void startPayPalService() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);
    }

    private void getBundleData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //existing task, show data from db
            postID = bundle.getString("postID");
            userName = bundle.getString("userName");
            showInfoOnUI();
        }
    }

    private void showInfoOnUI() {
        ((TextView)findViewById(R.id.payeeUserName)).setText(userName);
    }


    /**
     * runs when a "pay" is clicked
     */
    public void processPayment(View view) {
        EditText amountEditText = findViewById(R.id.edtAmount);
        amount = amountEditText.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"CAD",
                "Employment Pay",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        paymentSuccessful();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    private void paymentSuccessful(){
        String userId = UserStatusData.getUserID(this);

        //path where you want to write data to
        String path = "users/"+ userId +"/TaskPosts/" + postID + "/completed";

        new DbWrite<Boolean>(path,true,db) {
            @Override
            public void writeResult(Boolean userdata) {
                paymentSetTotalPayed();
            }
        };

    }

    private void paymentSetTotalPayed(){
        String userId = UserStatusData.getUserID(this);

        String path = "users/"+ userId +"/TaskPosts/" + postID + "/totalPayed";

        new DbWrite<String>(path,amount,db) {
            @Override
            public void writeResult(String userdata) {
                paymentSetDeleted();
            }
        };

    }

    private void paymentSetDeleted(){
        String userId = UserStatusData.getUserID(this);

        String path = "users/"+ userId +"/TaskPosts/" + postID + "/postDeleted";

        new DbWrite<Boolean>(path,true,db) {
            @Override
            public void writeResult(Boolean userdata) {
                finishThisActivity();
            }
        };

    }

    private void finishThisActivity() {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
}
