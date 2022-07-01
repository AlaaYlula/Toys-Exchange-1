package com.example.toys_exchange;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.UserWishList;
import com.braintreepayments.cardform.view.CardForm;


public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private TextView toyName;
    private TextView toyCost;
    private Button btn;

    Toy toy;

    String toyId;
    AlertDialog.Builder alertBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_payment);
        setContentView(R.layout.payment_card);

        CardForm cardForm = findViewById(R.id.card_form);
        Button buy = findViewById(R.id.btnBuy);

        // https://www.codingdemos.com/android-credit-card-form-tutorial/
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentActivity.this);

        // Make the CVV number invisible
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        Intent intent = getIntent();

         toyId = intent.getStringExtra("toyId");

        getToy();

        buy.setOnClickListener(view->{
             if (cardForm.isValid()) {
               //  deleteToyFromAPI();
                 alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
                 alertBuilder.setTitle("Confirm before purchase");
                 alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                         "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                         "Card CVV: " + cardForm.getCvv() + "\n" +
                         "Postal code: " + cardForm.getPostalCode() + "\n" +
                         "Phone number: " + cardForm.getMobileNumber());
                 alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                         // Delete From the Toys
                         deleteToyFromAPI();
                         Toast.makeText(PaymentActivity.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
                     }
                 });
                 alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                     }
                 });
                 AlertDialog alertDialog = alertBuilder.create();
                 alertDialog.show();
             }else {
                 Toast.makeText(PaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
             }
         });
//        toyName = findViewById(R.id.item_name);
//      //  toyName.setText(toy.getToyname());
//
//        toyCost = findViewById(R.id.item_cost);
//        //toyCost.setText(toy.getPrice().toString());
//
//
//        btn = findViewById(R.id.buy_toy);
//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                deleteToyFromAPI();
//            }
//        });
    }

//    public void onClickRadioButton(View view) {
//        boolean checked = ((RadioButton) view).isChecked();
//
//        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
//
//        switch (view.getId()) {
//            case R.id.creditCard:
//                if(checked)
//                    relativeLayout.setVisibility(View.VISIBLE);
//                break;
//            case R.id.cash:
//                if(checked)
//                    relativeLayout.setVisibility(View.INVISIBLE);
//                break;
//        }
//    }
    public void getToy(){
        Amplify.API.query(
                ModelQuery.list(Toy.class),
                toys -> {
                    //  Log.i(TAG, "getUserName: -----------------------------------<>"+accounts.getData());
                    for (Toy toyRec :
                            toys.getData()) {
                        if (toyRec.getId().equals(toyId)) {
                            toy = toyRec;

                        }
                    }
                    runOnUiThread(()->{
//                        toyName.setText(toy.getToyname());
//                        toyCost.setText(toy.getPrice().toString());

                    });
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    public void deleteToyFromAPI() {
//        Amplify.API.mutate(ModelMutation.delete(toy),
//                response -> {
//                Log.i(TAG, "toy deleted from API:");
//                    Toast toast = Toast.makeText(getApplicationContext(), "the item deleted", Toast.LENGTH_SHORT);
//                    toast.show();
//                },
//                error -> Log.e(TAG, "Delete failed", error)
//        );

        Amplify.API.query(
                ModelQuery.list(UserWishList.class),
                toys -> {
                    if(toys.hasData()) {
                        for (UserWishList toyRec :
                                toys.getData()) {
                            if(toyRec.getToy().getId().equals(toy.getId()))                                     {
                                Amplify.API.mutate(ModelMutation.delete(toyRec),
                                        response ->{
                                            Log.i(TAG, "Toy wishList deleted " + response);
                                        },
                                        error -> Log.e(TAG, "toy failed", error)
                                );
                            }
                        }
                    }

                    runOnUiThread(()->{
                        Amplify.API.mutate(ModelMutation.delete(toy),
                                response ->{
                                    // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                    Log.i(TAG, "Toy deleted " + response);
                                    runOnUiThread(()->{
                                        Toast.makeText(getApplicationContext(), "Toy Deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this,MainActivity.class));
                                    });
                                },
                                error -> Log.e(TAG, "delete failed", error)
                        );
                    });

                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }
}