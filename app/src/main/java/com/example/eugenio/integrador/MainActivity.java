package com.example.eugenio.integrador;

import android.app.ProgressDialog;
import android.content.Context;
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

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String MICROSERVICIO;
    EditText username, password;
    String usernameStr, passwordStr;
    SharedPreferences sp;
    AdaptadorDB adaptadorDB;
    String LLAVE_USER;
    String LLAVE_PASS;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        pref = getApplicationContext().getSharedPreferences("crm", Context.MODE_PRIVATE);
        username.setText(pref.getString("username",""));
        MICROSERVICIO = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/login.php";
        LLAVE_USER = "user";
        LLAVE_PASS = "password";
        adaptadorDB = new AdaptadorDB(this);
        adaptadorDB.open();
    }
    public void onClickLogin(View v) {
        usernameStr = username.toString().trim();
        passwordStr = password.toString().trim();
        if(!usernameStr.equals("") && !passwordStr.equals("")) {

            final ProgressDialog cargadno = ProgressDialog.show(this, "Logineando...", "Logineando...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, MICROSERVICIO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Cerramos la barra de progreso
                            cargadno.dismiss();
                            // Mostramos un mensaje como respuesta
                            boolean answer = true;
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                            String token = "";
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("answer"))
                                    answer = false;
                                else {
                                    token = obj.getString("basicAuthToken");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                answer = false;
                            }
                            if (answer) {
                                Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, Experiments.class);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("token", token);
                                editor.commit();
                                intent.putExtra("token", token);
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
                            Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    usernameStr = username.getText().toString();
                    passwordStr = password.getText().toString();
                    Map<String, String> params = new Hashtable<String, String>();
                    // Le enexamos los parametros
                    params.put(LLAVE_USER, usernameStr);
                    params.put(LLAVE_PASS, passwordStr);

                    // regresamos los parametros
                    return params;
                }
            };

            // Usamos Volley para crear la cola de peticiones
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            // Anexamos el request a la cola
            requestQueue.add(stringRequest);
        }
        else {
            Toast.makeText(MainActivity.this, "Llena todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    public void register(View v) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }
}
