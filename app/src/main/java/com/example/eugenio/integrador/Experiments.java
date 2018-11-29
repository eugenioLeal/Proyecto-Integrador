package com.example.eugenio.integrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

public class Experiments extends AppCompatActivity {
//    RecycleElement[] experiments;
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    ListView listView;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiments);
        listView = findViewById(R.id.list_view);
        data = new String[]{"Experimento1 11/28/18","Experimento2 11/29/18"};
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
