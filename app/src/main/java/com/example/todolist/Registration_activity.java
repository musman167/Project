package com.example.todolist;

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
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration_activity extends AppCompatActivity {

    private EditText registration_email_var, registration_password_var;
    private Button registration_button_var;
    private TextView registration_question_var, heading_register;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activity);
        mAuth=FirebaseAuth.getInstance();
        registration_email_var=findViewById(R.id.registration_email);
        registration_password_var=findViewById(R.id.registration_password);
        registration_button_var=findViewById(R.id.button_registration);
        registration_question_var=findViewById(R.id.registration_last_text);
        heading_register=findViewById(R.id.register_text);
        loader= new ProgressDialog(this);
        registration_question_var.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_activity.this, login_Activity.class);
                startActivity(intent);
            }
        });

        registration_button_var.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registration_email_var.getText().toString().trim();
                String password = registration_password_var.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    registration_email_var.setError("Email required");
                    return;
                }  if (TextUtils.isEmpty(email)) {
                    registration_password_var.setError("Password required");
                    return;
                } else
                {
                    loader.setMessage("Registration in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Intent intent = new Intent(Registration_activity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else
                            {

                                String error = task.getException().toString();
                                Toast.makeText(Registration_activity.this,"Registration failed" + error, Toast.LENGTH_LONG).show();

                            }
                            loader.dismiss();
                        }
                    });
                }


                }

        });
    }

}