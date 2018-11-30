package com.example.eugenio.integrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.widgets.ProgressDialog;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class CreateNewExperiment extends AppCompatActivity {
    EditText nombreEdit;
    String token;
    String MICROSERVICIO = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/crearExperimentos.php";
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nombreEdit = findViewById(R.id.new_username);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_experiment);
        nombreEdit = (EditText) findViewById(R.id.nombreExp);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("crm", 0);
        token = pref.getString("token",null);
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
    }
    public void onClickCreate(View v) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MICROSERVICIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Mostramos un mensaje como respuesta
                        Toast.makeText(CreateNewExperiment.this, response, Toast.LENGTH_LONG).show();
                        String token = "";
                        Boolean answer = true;
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("answer"))
                                answer = false;
                        }
                        catch(Exception e) {
                            Toast.makeText(CreateNewExperiment.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            answer = false;
                        }
                        if(answer) {
                            Intent intent = new Intent(CreateNewExperiment.this,Experiments.class);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Monstramos el mensaje de error
                        Toast.makeText(CreateNewExperiment.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                nombre = nombreEdit.getText().toString();
                Map<String, String> params = new Hashtable<String, String>();
                // Le enexamos los parametros
                params.put("nombre", nombre);
                params.put("token", token);

                // regresamos los parametros
                return params;
            }
        };

        // Usamos Volley para crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Anexamos el request a la cola
        requestQueue.add(stringRequest);
    }
}
