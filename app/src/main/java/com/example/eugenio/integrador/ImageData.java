package com.example.eugenio.integrador;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class ImageData extends AppCompatActivity {

    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues;
    ArrayList<String> xAxis1;
    BarEntry values;
    BarChart chart;
    BarData data;
    String token;
    RequestQueue queue;
    String imagen_id;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_data);
        String url = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/imagenesDatos.php";
        queue = com.android.volley.toolbox.Volley.newRequestQueue(this);
        chart = (BarChart) findViewById(R.id.grafico);
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

        imagen_id = getIntent().getExtras().getString("id");

        String imgId = getIntent().getExtras().getString("id");
        Toast.makeText(ImageData.this,"img id: "+imgId,Toast.LENGTH_SHORT).show();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("crm", 0);
        token = pref.getString("token",null);


        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        try {
                            JSONObject response = new JSONObject(stringResponse);
                            Toast.makeText(ImageData.this,response.toString(),Toast.LENGTH_SHORT).show();
                            //Log.d("s","Response: " + response);
                            JSONArray array = response.getJSONArray("data");
                            Toast.makeText(ImageData.this,"array: "+ array.toString(),Toast.LENGTH_SHORT).show();

                            int[] colores = new int[array.length()];
//{"answer":true,"data":[{"name":"Beige","color":"#f5f5dc","value":0.8535},{"name":"DarkSlateGray","color":"#2f4f4f","value":0.087},{"name":"DarkGray","color":"#a9a9a9","value":0.0595}]}
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                xAxis1.add(object.getString("name"));
                                yValues.add(new BarEntry(Float.valueOf(object.getString("value")),i));
                                colores[i]=Color.parseColor(object.getString("color"));

                            }
                            BarDataSet barDataSet1 = new BarDataSet(yValues, "Colores");

                            barDataSet1.setColors(colores);

                            yAxis = new ArrayList<>();
                            yAxis.add(barDataSet1);
                            String names[] = xAxis1.toArray( new String[xAxis1.size()] );
                            data = new BarData(names, yAxis);
                            chart.setData(data);
                            chart.animateXY(2000, 2000);
                            chart.invalidate();
                            pd.hide();
                        }
                        catch(Exception e){
                            Log.d("s","Error: " + e.toString());
                            Toast.makeText(ImageData.this,e.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("s","Error: " + error.toString());
                    }
                }
            ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                // Le enexamos los parametros
                params.put("token", token);
                params.put("id", imagen_id);

                // regresamos los parametros
                return params;
            }
        };
        queue.add(stringRequest);
        // Access the RequestQueue through your singleton class.
    }
}