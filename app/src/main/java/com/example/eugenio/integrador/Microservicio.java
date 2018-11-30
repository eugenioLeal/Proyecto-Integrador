package com.example.eugenio.integrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Microservicio extends AppCompatActivity {

    String postUrl;
    RequestQueue queue;
    AdapterView.OnItemClickListener itemClickListener = null;
    String renderedContent;
    HashMap<String,String> contentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microservicio);
        postUrl = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/CMS/wp-json/wp/v2/posts";
        queue = com.android.volley.toolbox.Volley.newRequestQueue(this);
        contentMap = new HashMap<String,String>();
        sendRequest();
    }

    public void sendRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(postUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++) {
                    try {
                        // Get the category number of each post.
                        JSONObject post = response.getJSONObject(i);
                        JSONArray categories = post.getJSONArray("categories");
                        // Initialized the category value as one means that the post is uncategorized
                        Integer category = 1;
                        try {
                            category = categories.getInt(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JSONObject title = post.getJSONObject("title");
                        String renderedTitle = title.getString("rendered");
                        // Add title to each post in their respective categories ListView
//                        if(category == 5) { // text
//                            categoriesChildren.get(0).add(renderedTitle);
//                        } else if (category == 4) { // video
//                            categoriesChildren.get(2).add(renderedTitle);
//                        } else if (category == 3) { // streetview
//                            categoriesChildren.get(3).add(renderedTitle);
//                        } else if (category == 2) { // image
//                            categoriesChildren.get(1).add(renderedTitle);
//                        }
                        // Get the HTML Content of each post as a string
                        JSONObject content = post.getJSONObject("content");
                        renderedContent = content.getString("rendered");
                        contentMap.put(renderedTitle,renderedContent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void toastIt(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

}
