package com.example.rohit.rat_mouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    App_Adapter app_adapter;
    List<String> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setRecyclerView();
    }

    private void setRecyclerView()
    {
        data.add("https://www.google.com");
        data.add("https://aj019.github.io/anuj.com/share/index.html");
        data.add("https://rohitagarwal3011.github.io/test/control/share.html");
        app_adapter = new App_Adapter(data,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(app_adapter);
        app_adapter.notifyDataSetChanged();
    }
}
