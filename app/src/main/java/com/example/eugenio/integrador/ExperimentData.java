package com.example.eugenio.integrador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import static android.os.SystemClock.sleep;

public class ExperimentData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_data);

        String newString = getIntent().getExtras().getString("id");

        Toast.makeText(ExperimentData.this,"id recibido es: "+newString,Toast.LENGTH_SHORT).show();

    }
public void onClick(View view)
{

    Intent intent = new Intent(ExperimentData.this,ImageData.class);
    startActivity(intent);
}

}
