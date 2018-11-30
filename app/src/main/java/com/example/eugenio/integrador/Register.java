package com.example.eugenio.integrador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText username, password, email;
    String usernameStr, passwordStr, emailStr;
    String MICROSERVICIO;
    String LLAVE_USER;
    String LLAVE_PASS;
    String LLAVE_EMAIL;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MICROSERVICIO = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/registro.php";
        LLAVE_USER = "user";
        LLAVE_PASS = "password";
        LLAVE_EMAIL = "email";
        username = findViewById(R.id.new_username);
        password = findViewById(R.id.new_password);
        email = findViewById(R.id.new_email);
        sp =  getApplicationContext().getSharedPreferences("crm", Context.MODE_PRIVATE);
    }
    public void onClickRegister(View v) {
        usernameStr = username.getText().toString();
        passwordStr = password.getText().toString();
        emailStr = email.getText().toString();
        if (!usernameStr.equals("")
                && !passwordStr.equals("")
                && !emailStr.equals("")){
            // Mostramos una barra de progreso
            final ProgressDialog cargadno = ProgressDialog.show(this, "Registrando...", "Registrando...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, MICROSERVICIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Cerramos la barra de progreso
                        cargadno.dismiss();
                        // Mostramos un mensaje como respuesta
                        Toast.makeText(Register.this, response, Toast.LENGTH_LONG).show();
                        String token = "";
                        Boolean answer = true;
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("answer"))
                                answer = false;
                        } catch (Exception e) {
                            answer = false;
                        }
                        if (answer) {
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Cerramos el dialogo de la barra de progreso
                        cargadno.dismiss();

                        // Monstramos el mensaje de error
                        Toast.makeText(Register.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    usernameStr = username.getText().toString();
                    passwordStr = password.getText().toString();
                    emailStr = email.getText().toString();
                    Map<String, String> params = new Hashtable<String, String>();
                    // Le enexamos los parametros
                    params.put(LLAVE_USER, usernameStr);
                    params.put(LLAVE_PASS, passwordStr);
                    params.put(LLAVE_EMAIL, emailStr);

                    // regresamos los parametros
                    return params;
                }
            };

            // Usamos Volley para crear la cola de peticiones
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            // Anexamos el request a la cola
            requestQueue.add(stringRequest);
                /*Toast.makeText(this,"Datos de contacto guardados", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);*/
            }
        else
        {
            Toast.makeText(Register.this, "Rellene todos los campos", Toast.LENGTH_LONG).show();
        }
    }
}
