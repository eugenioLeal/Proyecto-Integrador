package com.example.eugenio.integrador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.SystemClock.sleep;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExperimentData extends AppCompatActivity {
    ListView listView;
    String url = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/imagenesHard.php";
    RequestQueue queue;
    Image[] imageArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_data);
        listView = findViewById(R.id.list_view2);
        queue = com.android.volley.toolbox.Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ExperimentData.this, response.toString(), Toast.LENGTH_SHORT).show();
                        Boolean answer;
                        JSONArray data;
                        int id;
                        String nombre;
                        ArrayAdapter<Image> adapter;
                        try {
                            answer = response.getBoolean("answer");
                            data = response.getJSONArray("data");
                            imageArr = new Image[data.length()];
                            for(int i = 0; i < data.length();i++){
                                JSONObject iter = data.getJSONObject(i);
                                id = iter.getInt("id");
                                nombre = iter.getString("nombre");
                                imageArr[i] = new Image(id,nombre);
                            }
                            adapter = new ArrayAdapter<Image>(ExperimentData.this,
                                    android.R.layout.simple_list_item_1, imageArr);

                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position,
                                                        long id) {

                                    String item = ((TextView)view).getText().toString();

                                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ExperimentData.this,ExperimentData.class);
                                    Toast.makeText(ExperimentData.this,"mandar id:  "+id,Toast.LENGTH_SHORT).show();

                                    intent.putExtra("id", String.valueOf(id));
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
                        Toast.makeText(ExperimentData.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);

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
