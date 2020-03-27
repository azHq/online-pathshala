package com.example.onlinepathshala;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sslcommerz.library.payment.model.datafield.MandatoryFieldModel;
import com.sslcommerz.library.payment.model.dataset.TransactionInfo;
import com.sslcommerz.library.payment.model.util.CurrencyType;
import com.sslcommerz.library.payment.model.util.ErrorKeys;
import com.sslcommerz.library.payment.model.util.SdkCategory;
import com.sslcommerz.library.payment.model.util.SdkType;
import com.sslcommerz.library.payment.viewmodel.listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.viewmodel.management.PayUsingSSLCommerz;

public class Payment extends AppCompatActivity  {

    public String TAG="Main";
    EditText editText;
    String amount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        editText=findViewById(R.id.amount);
    }

    public void sendPayment(View view){

        amount=editText.getText().toString();
        MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("azazu5e01e07349b6a", "azazu5e01e07349b6a@ssl", amount, "azazu5e01e07019953", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST);

        PayUsingSSLCommerz.getInstance().setData(Payment.this, mandatoryFieldModel, new OnPaymentResultListener() {
            @Override
            public void transactionSuccess(TransactionInfo transactionInfo) {

                if (transactionInfo.getRiskLevel().equals("0")) {

                    Toast.makeText(getApplicationContext(),"Transaction succeeded",Toast.LENGTH_LONG).show();
                }

                else {
                    Log.e(TAG, "Transaction in risk. Risk Title : " + transactionInfo.getRiskTitle());
                    Toast.makeText(getApplicationContext(),"Transaction in risk. Risk Title : " + transactionInfo.getRiskTitle(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void transactionFail(String s) {
                Log.e(TAG, s);
            }


            @Override
            public void error(int errorCode) {
                switch (errorCode) {
// Your provides inf
                    case ErrorKeys.USER_INPUT_ERROR:
                        Log.e(TAG, "User Input Error");
                        Toast.makeText(getApplicationContext(),"User Input Error",Toast.LENGTH_LONG).show();
                        break;
// Internet is not connected.
                    case ErrorKeys.INTERNET_CONNECTION_ERROR:
                        Log.e(TAG, "Internet Connection Error");
                        break;
// Server is not giving valid data.
                    case ErrorKeys.DATA_PARSING_ERROR:
                        Log.e(TAG, "Data Parsing Error");
                        break;
// User press back button or canceled the transaction.
                    case ErrorKeys.CANCEL_TRANSACTION_ERROR:
                        Log.e(TAG, "User Cancel The Transaction");
                        break;
// Server is not responding.
                    case ErrorKeys.SERVER_ERROR:
                        Log.e(TAG, "Server Error");
                        break;
// For some reason network is not responding
                    case ErrorKeys.NETWORK_ERROR:
                        Log.e(TAG, "Network Error");
                        break;
                }
            }
        });
    }

}
