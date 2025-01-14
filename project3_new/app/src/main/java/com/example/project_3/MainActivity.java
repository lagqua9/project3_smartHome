package com.example.project_3;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button dangNhap, quenMK;
    TextView error;
    EditText taikhoan, matkhau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dangNhap = (Button) findViewById(R.id.dang_Nhap);
        quenMK = (Button) findViewById(R.id.quen);
        taikhoan = (EditText) findViewById(R.id.editTextText2);
        matkhau = (EditText) findViewById(R.id.editTextTextPassword);
        error = (TextView) findViewById(R.id.error);
        dangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taikhoanText = taikhoan.getText().toString().trim();
                String matkhauText = matkhau.getText().toString().trim();
                if(taikhoanText.equals("")&&matkhauText.equals("")){
                    error.setText("true");
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    error.setText("false");
                }
            }
        });
    }
}