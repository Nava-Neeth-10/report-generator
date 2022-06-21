package com.example.report_generator_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    TextView username_error,password_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        username_error=findViewById(R.id.username_error);
        password_error=findViewById(R.id.password_error);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                    username_error.setText("");
                else if(s.length()<5)
                    username_error.setText("Username cannot have less than 5 letters!!!");
                else if(s.length()>20)
                    username_error.setText("Username cannot have more than 20 letters!!!");
                else if(!Pattern.matches("\\w*",s))
                    username_error.setText("Username cannot contain spaces or special characters!!!");
                else
                    username_error.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                    password_error.setText("");
                else if(s.length()<5)
                    password_error.setText("Password cannot have less than 5 letters!!!");
                else if(s.length()>20)
                    password_error.setText("Password cannot have more than 20 letters!!!");
                else if(!Pattern.matches("\\S*",s))
                    password_error.setText("Password cannot contain spaces!!!");
                else
                    password_error.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void Register(View view){
        Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) throws Exception {
        String un=username.getText().toString().trim();
        String pw=password.getText().toString().trim();
        if(un.equals("test") && pw.equals("test")){       /*for testing next activities*/
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        int verification = 0;
        if (username_error.getText().toString().equals("")) {
            if (!username.getText().toString().equals(""))
                verification++;
            else
                username_error.setText("Username cannot be empty!!!");
        }
        if(password_error.getText().toString().equals("")) {
            if (!password.getText().toString().equals(""))
                verification++;
            else
                password_error.setText("Password cannot be empty!!!");
        }
        if(verification==2) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/login?username=" + un + "&password=" + encrypt(pw), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Logged in")) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else if(response.equals("User not found"))
                        username_error.setText("User not found!!!");
                    else
                        password_error.setText("Invalid Username/password!!!");
                }
            }, error -> {
                Toast.makeText(MainActivity.this, "Server Connection Failed!!!", Toast.LENGTH_LONG).show();
            });
            queue.add(stringRequest);
        }
        else
            Toast.makeText(MainActivity.this, "Please fill valid details!!!", Toast.LENGTH_LONG).show();
    }
    public String encrypt(String content) throws Exception
    {
        String key="Report_Generator";
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes("UTF-8")));
    }
}