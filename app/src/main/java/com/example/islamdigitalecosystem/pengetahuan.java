package com.example.islamdigitalecosystem;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.CustomerDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.List;

public class pengetahuan extends AppCompatActivity implements TransactionFinishedCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengetahuan);
        findViewById(R.id.bab3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickbab3();
            }
        });

        makePayment();
        findViewById(R.id.bab4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickbab4();
            }
        });
    }

    private void clickbab4() {
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest("101",20000, 1, "BAB III"));
        MidtransSDK.getInstance().startPaymentUiFlow(pengetahuan.this );

//        Toast.makeText(this, "Coba Click" , Toast.LENGTH_LONG).show();

    }

    private void makePayment() {
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl("https://ebisnis.herokuapp.com/index.php/")
                .setClientKey("SB-Mid-client-WrRqcP3WXQsheHxf")
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .buildSDK();
    }

    private void clickbab3() {
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest("101",20000, 1, "BAB III"));
        MidtransSDK.getInstance().startPaymentUiFlow(pengetahuan.this );

//        Toast.makeText(this, "Coba Click" , Toast.LENGTH_LONG).show();

    }
    public static CustomerDetails customerDetails(){
        CustomerDetails cd = new CustomerDetails();
        cd.setName("YOUR_PRODUCT");
        cd.setEmail("your_email@gmail.com");
        cd.setPhone("your_phone");
        return cd;
    }
    public static TransactionRequest transactionRequest(String id, int price, int qty, String name){
        TransactionRequest request =  new TransactionRequest(System.currentTimeMillis() + " " , 20000);
        //        request.setCustomerDetails(customerDetails());
        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);
        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false);
        creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_RBA);

        request.setCreditCard(creditCard);
        return request;
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if(result.isTransactionCanceled()){
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
        }else{
            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void bab1(View view) { Intent intent = new Intent(pengetahuan.this, bab1.class);
        startActivity(intent);
    }
}

