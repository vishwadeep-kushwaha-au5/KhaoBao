package com.example.khaobao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khaobao.Common.Common;
import com.example.khaobao.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText _edtPhoneNumber, _edtPassword;
    Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        _edtPassword = (MaterialEditText)findViewById(R.id._edtPassword);
        _edtPhoneNumber = (MaterialEditText)findViewById(R.id._edtPhoneNumber);
        btnLogIn = (Button)findViewById(R.id.btnLogIn);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Check if user not exist in database
                        if(dataSnapshot.child(_edtPhoneNumber.getText().toString()).exists()) {

                            mDialog.dismiss();
                            //Get User Information
                            User user = dataSnapshot.child(_edtPhoneNumber.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(_edtPassword.getText().toString())) {
                                {
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "Sign In Failed !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(SignIn.this, "User doesnot exist !!! ", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
