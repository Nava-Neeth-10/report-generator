package com.example.report_generator_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.io.*;

import android.database.Cursor;
import android.provider.OpenableColumns;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.Scanner;

public class HomeActivity extends AppCompatActivity {

    String bdata="";
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TabLayout tab_bar=findViewById(R.id.tab_bar);
        ViewPager tab_view=findViewById(R.id.tab_view);
        PagerAdapter pa= new PageAdapter(getSupportFragmentManager(),3);
        tab_view.setAdapter(pa);
        tab_bar.setupWithViewPager(tab_view);
    }

    public void generate_full_report(View view)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/full_report", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(folder, "data.csv");
                    FileOutputStream f= new FileOutputStream(file);
                    f.write(response.getBytes());
                    f.close();
                    Toast.makeText(HomeActivity.this,"Download Completed",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, error ->{Toast.makeText(HomeActivity.this, "Server Connection Failed!!!", Toast.LENGTH_LONG).show();});
        queue.add(stringRequest);
    }
    public void send_mail(View view)
    {
        Button sendm=view.findViewById(R.id.send_email);
        sendm.setEnabled(false);
        EditText mail_to=findViewById(R.id.mail_to);
        EditText mail_subject=findViewById(R.id.mail_subject);
        EditText mail_body=findViewById(R.id.mail_body);
        String to=mail_to.getText().toString();
        String subject=mail_subject.getText().toString();
        String body=mail_body.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        Toast.makeText(HomeActivity.this,"Sending Mail...",Toast.LENGTH_LONG).show();
        System.out.println(bdata);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/send_mail?to="+to+"&subject="+subject+"&body="+ URLEncoder.encode(body)+"&attachment="+URLEncoder.encode(bdata), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(HomeActivity.this,"Mail Sent",Toast.LENGTH_LONG).show();
                mail_to.setText("");
                mail_subject.setText("");
                mail_body.setText("");
                ImageButton attachno = findViewById(R.id.attachno);
                attachno.performClick();
                attachno.setVisibility(View.INVISIBLE);
                sendm.setEnabled(true);
            }
        }, error ->{Toast.makeText(HomeActivity.this, "Server Connection Failed!!!", Toast.LENGTH_LONG).show();sendm.setEnabled(true);});
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
    public void logout(View view) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void attachments(View view){
        i=0;
        Intent myFileIntet;
        myFileIntet=new Intent(getIntent().ACTION_OPEN_DOCUMENT);
        myFileIntet.addCategory(Intent.CATEGORY_OPENABLE);
        String [] mimeTypes = {"text/*","application/pdf"};
        myFileIntet.setType("*/*");
        myFileIntet.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        ImageButton attachno = findViewById(R.id.attachno);
        startActivityForResult(myFileIntet,1);
        attachno.setVisibility(View.VISIBLE);
    }

    public void attachno(View view){
        TextView nameView = (TextView) findViewById(R.id.documentd);
        bdata = "";
        nameView.setText("");
        ImageButton attachno = findViewById(R.id.attachno);
        attachno.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            i=1;
            Uri uri = data.getData();
            try {
                Toast.makeText(HomeActivity.this, "File Attached", Toast.LENGTH_LONG).show();
                bdata=readTextFromUri(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        TextView nameView = findViewById(R.id.documentd);
        nameView.setText(returnCursor.getString(nameIndex));
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}