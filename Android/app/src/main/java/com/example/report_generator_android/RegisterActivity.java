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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class RegisterActivity extends AppCompatActivity {

    EditText first_name,last_name,email,username,password;
    TextView first_name_error,last_name_error,email_error,username_error,password_error;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        first_name=findViewById(R.id.first_name);
        last_name=findViewById(R.id.last_name);
        email=findViewById(R.id.email);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        first_name_error=findViewById(R.id.first_name_error);
        last_name_error=findViewById(R.id.last_name_error);
        email_error=findViewById(R.id.email_error);
        username_error=findViewById(R.id.username_error);
        password_error=findViewById(R.id.password_error);
        InputStream config_file = getResources().openRawResource(R.raw.config);
        byte[] temp = new byte[0];
        try {
            temp = new byte[config_file.available()];
            config_file.read(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String temp2 = new String(temp);
        String[] config=temp2.split("\n");
        first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp[];
                int x;
                for(x=0;x<config.length;x++) {
                    temp = config[x].split("=");
                    if (temp[0].equals("first_name") && !Pattern.matches(temp[1], s)) {
                        first_name_error.setText(temp[2]);
                        break;
                    }
                }
                if(x==config.length)
                    first_name_error.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp[];
                int x;
                for(x=0;x<config.length;x++) {
                    temp = config[x].split("=");
                    if (temp[0].equals("last_name") && !Pattern.matches(temp[1], s)) {
                        last_name_error.setText(temp[2]);
                        break;
                    }
                }
                if(x==config.length)
                    last_name_error.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp[];
                int x;
                for(x=0;x<config.length;x++) {
                    temp = config[x].split("=");
                    if (temp[0].equals("email") && !Pattern.matches(temp[1], s)) {
                        email_error.setText(temp[2]);
                        break;
                    }
                }
                if(x==config.length)
                    email_error.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp[];
                int x;
                for(x=0;x<config.length;x++) {
                    temp = config[x].split("=");
                    if (temp[0].equals("username") && !Pattern.matches(temp[1], s)) {
                        username_error.setText(temp[2]);
                        break;
                    }
                }
                if(x==config.length)
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
                String temp[];
                int x;
                for(x=0;x<config.length;x++) {
                    temp = config[x].split("=");
                    if (temp[0].equals("password") && !Pattern.matches(temp[1], s)) {
                        password_error.setText(temp[2]);
                        break;
                    }
                }
                if(x==config.length)
                    password_error.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void cancel(View view){
        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void reset(View view)
    {
        first_name.setText("");
        last_name.setText("");
        email.setText("");
        username.setText("");
        password.setText("");
    }

    public void submit(View view) throws Exception {
        int verification = 0;
        if (first_name_error.getText().toString().equals("")) {
            if (!first_name.getText().toString().equals(""))
                verification++;
            else
                first_name_error.setText("First Name cannot be empty!!!");
        }
        if (last_name_error.getText().toString().equals("")) {
            if (!last_name.getText().toString().equals(""))
                verification++;
            else
                last_name_error.setText("Last Name cannot be empty!!!");
        }
        if (email_error.getText().toString().equals("")) {
            if (!email.getText().toString().equals(""))
                verification++;
            else
                email_error.setText("Email cannot be empty!!!");
        }
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
        if(verification==5) {
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/register?first_name=" + first_name.getText().toString() + "&last_name=" + last_name.getText().toString() + "&email=" + email.getText().toString() + "&username=" + username.getText().toString() + "&password=" + encrypt(password.getText().toString()), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Successful registration")) {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else if(response.equals("Username not available"))
                        username_error.setText("Username already exists!!!");
                    else
                        email_error.setText("Email already exists!!!");
                }
            }, error -> {
                Toast.makeText(RegisterActivity.this, "Server Connection Failed!!!", Toast.LENGTH_LONG).show();
            });
            queue.add(stringRequest);
        }
        else
            Toast.makeText(RegisterActivity.this, "Please fill valid details!!!", Toast.LENGTH_LONG).show();
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