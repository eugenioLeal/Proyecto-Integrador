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

        String idExperimento = getIntent().getExtras().getString("id");

        Toast.makeText(ExperimentData.this,"id recibido es: "+idExperimento,Toast.LENGTH_SHORT).show();

    }
public void onClickAddImage(View view)
{
    String idExperimento = getIntent().getExtras().getString("id");
    Intent intent = new Intent(ExperimentData.this,Menu.class);
    intent.putExtra("id", String.valueOf(idExperimento));
    startActivity(intent);
}

}
