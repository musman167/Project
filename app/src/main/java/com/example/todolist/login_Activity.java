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

public class login_Activity extends AppCompatActivity {
    

    private EditText login_email_var, login_password_var;
    private Button login_button_var;
    private TextView login_question_var, heading_login;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        
        login_email_var=findViewById(R.id.login_email);
        login_password_var=findViewById(R.id.login_password);
        login_button_var=findViewById(R.id.button_login);
        login_question_var=findViewById(R.id.login_last_text);
        heading_login=findViewById(R.id.login_text);
        mAuth=FirebaseAuth.getInstance();
        loader= new ProgressDialog(this);
        login_question_var.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_Activity.this, Registration_activity.class);
                startActivity(intent);
            }
        });
        login_button_var.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email_var.getText().toString().trim();
                String password = login_password_var.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    login_email_var.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    login_password_var.setError("Password Required");
                    return;
                }else {
                    loader.setMessage("Login is progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(login_Activity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }else {
                                String error = task.getException().toString();
                                Toast.makeText(login_Activity.this, "Login failed" + error, Toast.LENGTH_LONG).show();
                                loader.dismiss();
                            }
                        }
                    });

                }
            }
        });
    }


}