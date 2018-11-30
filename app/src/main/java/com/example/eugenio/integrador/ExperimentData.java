package com.example.eugenio.integrador;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.SystemClock.sleep;

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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import java.io.InputStream;

public class ExperimentData extends AppCompatActivity {
    ListView listView;
    String url = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/imagenes.php";
    RequestQueue queue;
    Image[] imageArr;
    String token;
    String experimento_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_data);
        //adapter
        listView = findViewById(R.id.list_view2);
        //set adapter
        queue = com.android.volley.toolbox.Volley.newRequestQueue(this);
    }
    public void onStart() {
        super.onStart();
        reload();
    }
    public void reload(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("crm", 0);
        token = pref.getString("token",null);

        experimento_id = getIntent().getExtras().getString("id");

        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Boolean answer;
                        JSONArray data;
                        int id;
                        String nombre;
                        String created_at;
                        String fecha, hora;
                        ArrayAdapter<Image> adapter;
                        String imageUrl;
                        ListView list =findViewById(R.id.list_view);
                        try {
                            Toast.makeText(ExperimentData.this, stringResponse, Toast.LENGTH_SHORT).show();
                            JSONObject response = new JSONObject(stringResponse);
                            answer = response.getBoolean("answer");
                            data = response.getJSONArray("data");
                            imageArr = new Image[data.length()];
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
                                imageUrl = iter.getString("url");
                                imageArr[i] = new Image(id,nombre,fecha,hora,imageUrl);
                            }
                            String[] from = {"listview_image", "listview_title"};
                            int[] to = {R.id.listview_image, R.id.listview_item_title};
//                              dapter = new ArrayAdapter<Image>(ExperimentData.this,
//                                    android.R.layout.simple_list_item_1, imageArr);
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                            StrictMode.setThreadPolicy(policy);
                            CustomAdapter simpleAdapter = new CustomAdapter(getBaseContext(),imageArr);

                            listView.setAdapter(simpleAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position,
                                                        long id) {
                                    Intent intent = new Intent(ExperimentData.this,ImageData.class);
                                    int image_id = imageArr[position].id;
                                    Toast.makeText(ExperimentData.this,"mandar id:  "+image_id,Toast.LENGTH_SHORT).show();

                                    intent.putExtra("id", String.valueOf(image_id));
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ExperimentData.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(ExperimentData.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new Hashtable<String, String>();
                            // Le enexamos los parametros
                            params.put("token", token);
                            params.put("experimento_id", experimento_id);

                            // regresamos los parametros
                            return params;
                        }
                    };
        queue.add(stringRequest);

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

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
