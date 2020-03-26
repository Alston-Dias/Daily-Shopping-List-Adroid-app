package com.alstondias.dailyshoppinglsit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    
    private EditText email;
    private  EditText pass;
    private Button BtnLogin;
    private TextView SignUp;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onResume() {
        super.onResume();

        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        email= findViewById(R.id.email_login);
        pass = findViewById(R.id.password_login);
        BtnLogin = findViewById(R.id.btn_login);
        SignUp = findViewById(R.id.signup_txt);
        mAuth= FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               
                
                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();
                
                if(TextUtils.isEmpty(mEmail))
                {
                    email.setError("Required Field..");
                }
                
                if(TextUtils.isEmpty(mPass))
                {
                    pass.setError("Required Field..");
                }

                mDialog.setMessage("Processing..");
                mDialog.show();;

                mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            Toast.makeText(getApplicationContext(),"Successful", Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Failed to Log in",Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });
        
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });


    }
}
