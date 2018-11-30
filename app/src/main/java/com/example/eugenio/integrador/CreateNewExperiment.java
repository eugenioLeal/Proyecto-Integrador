package com.example.eugenio.integrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNewExperiment extends AppCompatActivity {
    EditText nombreEdit;
    String token;
    String numbre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_experiment);
        nombreEdit = (EditText) findViewById(R.id.nombreExp);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("crm", 0);
        token = pref.getString("token",null);
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
    }
    public void onClickCreate(View v) {
        // put Extras or upload to database
        Intent intent = new Intent(this, Experiments.class);
        startActivity(intent);
    }
}
