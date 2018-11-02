package com.example.eugenio.integrador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText username, password, email;
    Button registerBtn;
    String usernameStr, passwordStr, emailStr;
    AdaptadorDB adaptadorDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.new_username);
        password = findViewById(R.id.new_password);
        email = findViewById(R.id.new_email);
        registerBtn = findViewById(R.id.registerBtn);
        adaptadorDB = new AdaptadorDB(this);
        adaptadorDB.open();
    }
    public void onClickRegister(View v) {
        usernameStr = username.getText().toString();
        passwordStr = password.getText().toString();
        emailStr = email.getText().toString();
        adaptadorDB.insertaUsuario(emailStr,usernameStr,passwordStr);
        SharedPreferences sharedPref = getSharedPreferences("crm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username",emailStr);
        editor.commit();
        Toast.makeText(this,"Datos de contacto guardados", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
    }
}
