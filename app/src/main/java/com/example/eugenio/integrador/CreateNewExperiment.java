package com.example.eugenio.integrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateNewExperiment extends AppCompatActivity {
    EditText nombreEdit;
    String numbre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_experiment);
        nombreEdit = (EditText) findViewById(R.id.nombreExp);
    }
    public void onClickCreate(View v) {
        // put Extras or upload to database
        Intent intent = new Intent(this, Experiments.class);
        startActivity(intent);
    }
}
