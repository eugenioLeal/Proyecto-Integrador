package com.example.eugenio.integrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Experiments extends AppCompatActivity {
//    RecycleElement[] experiments;
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
    String url;
    String token;
    RequestQueue queue;
    ListView listView;
    Experiment[] experimentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiments);
        reload();
    }
    protected void onStart() {
        super.onStart();
        reload();
    }
    public void reload(){
        listView = findViewById(R.id.list_view);
        queue = com.android.volley.toolbox.Volley.newRequestQueue(this);

        url = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/experimentos.php";

        SharedPreferences pref = getApplicationContext().getSharedPreferences("crm", 0);
        token = pref.getString("token",null);

        // From service
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String stringResponse) {
                        Toast.makeText(Experiments.this, stringResponse, Toast.LENGTH_SHORT).show();
                        Boolean answer;
                        JSONArray data;
                        int id;
                        String nombre,created_at,fecha,hora;
                        ArrayAdapter<Experiment> adapter;
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            answer = response.getBoolean("answer");
                            data = response.getJSONArray("data");
                            experimentArr = new Experiment[data.length()];
                            for(int i = 0; i < data.length();i++){
                                JSONObject iter = data.getJSONObject(i);
                                id = iter.getInt("id");
                                nombre = iter.getString("nombre");
                                created_at = iter.getString("created_at");
                                Log.d("created",created_at);
                                String[] splitted = created_at.split(" ");
                                fecha = splitted[0];
                                Log.d("fechaaaaaaa",fecha);
                                hora = splitted[1];
                                experimentArr[i] = new Experiment(id,nombre,fecha,hora);
                            }
                            adapter = new ArrayAdapter<Experiment>(Experiments.this,
                                    android.R.layout.simple_list_item_1, experimentArr);

                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position,
                                                        long id) {

                                    String item = ((TextView)view).getText().toString();

                                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Experiments.this,ExperimentData.class);
                                    int experimento_id = experimentArr[position].id;
                                    Toast.makeText(Experiments.this,"mandar id:  "+experimento_id,Toast.LENGTH_SHORT).show();
                                    intent.putExtra("id", String.valueOf(experimentArr[position].id));
                                    startActivity(intent);


                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(Experiments.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                // Le enexamos los parametros
                params.put("token", token);

                // regresamos los parametros
                return params;
            }
        };
        queue.add(stringRequest);

        // Hardcoded
//        data = new Experiment[]{
//                new Experiment(1,"myExperiment1","11/30/2018","12:26:18"),
//                new Experiment(2,"myExperiment2","11/29/2018","12:26:20"),
//        };





                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();



//        experiments = new RecycleElement[3];
//        experiments[0] = new RecycleElement("Experimento1","2018-11-16","Mexico",0);
//        mRecyclerView = findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new MyAdapter(this,experiments);
//        mRecyclerView.setAdapter(mAdapter);
//        String data[] = new String[];["Experimento1 11/28/18","Experimento2 11/29/18"];

    }
    public void onClickCreate(View v) {
        Intent intent = new Intent(this,CreateNewExperiment.class);
        startActivity(intent);
    }
}
