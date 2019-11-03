package com.datsea.pos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    String payable_amount;

    TextView nameTextview;
    TextView balancetextView;
    TextView rfidtextView;
    TextView upitv;
    String rfid;
    String int_amt;
    Uri uri;
    String name,upi;
    String req_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        rfidtextView = findViewById(R.id.textView);
        balancetextView = findViewById(R.id.balance_textView);
        nameTextview = findViewById(R.id.name_textView);
        upitv = findViewById(R.id.upi_textView);
        nameTextview.setVisibility(View.INVISIBLE);
        rfidtextView.setVisibility(View.INVISIBLE);
        balancetextView.setVisibility(View.INVISIBLE);
        upitv.setVisibility(View.INVISIBLE);
        final DatabaseReference rfidRef = FirebaseDatabase.getInstance().getReference();
        rfidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                rfid = (String) dataSnapshot.child("rfid").getValue();

                if(TextUtils.isEmpty(rfid)){

                }else {
                    req_status = (String) dataSnapshot.child("user_Data").child(rfid.trim()).child("req_status").getValue();
                    if("1".equals(req_status)){
                        rfid = rfid.trim();
                        findViewById(R.id.next_txt).setVisibility(View.INVISIBLE);
                        rfidtextView.setText("Id : "+rfid);
                        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user_Data");
                        userRef.child(rfid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                name = (String) dataSnapshot.child("name").getValue();
                                upi = (String) dataSnapshot.child("upi").getValue();
                                int_amt = (String) dataSnapshot.child("amount").getValue();

                                //  uri = Uri.parse("upi://pay?pa="+upi+"&pn="+ name+"&am="+int_amt+"&cu=INR&url=url");
                                rfidtextView.setVisibility(View.VISIBLE);
                                String balance = (String) dataSnapshot.child("balance").getValue();
                                nameTextview.setText("Name : "+name);
                                balancetextView.setText("Balance : "+balance);
                                upitv.setVisibility(View.VISIBLE);
                                nameTextview.setVisibility(View.VISIBLE);
                                uri = Uri.parse("upi://pay?pa=7697678073@upi&pn=Company name&am="+int_amt+"&cu=INR&url=url");
                                rfidtextView.setText(""+uri);
                                nameTextview.setText(name);
                                upitv.setText(upi);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//Now magic starts here
                                intent.setClassName("in.org.npci.upiapp","in.org.npci.upiapp.HomeActivity");
                                finish();
                                startActivityForResult(intent,1);
                                userRef.child(rfid).child("req_status").setValue("0");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else {

                    }
                }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void getUserData(String rfid) {

        DatabaseReference rfidRef = FirebaseDatabase.getInstance().getReference("user_Data");
        rfidRef.child(rfid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 name = (String) dataSnapshot.child("name").getValue();
                 upi = (String) dataSnapshot.child("upi").getValue();


              //  uri = Uri.parse("upi://pay?pa="+upi+"&pn="+ name+"&am="+int_amt+"&cu=INR&url=url");

                rfidtextView.setVisibility(View.VISIBLE);
                 String balance = (String) dataSnapshot.child("balance").getValue();
              //  nameTextview.setText("Name : "+name);
                balancetextView.setText("Balance : "+balance);
                upitv.setVisibility(View.VISIBLE);
                nameTextview.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
