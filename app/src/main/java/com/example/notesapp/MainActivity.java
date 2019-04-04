package com.example.notesapp;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mDates= new ArrayList<>();
    private ArrayList<String> mImageUrls= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Started");

        initImageBitmaps();
    }

    private void initImageBitmaps(){
        Log.d(TAG, "preparing bitmaps");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        Date date1= new Date(2019, 4, 1);
        mDates.add(date1.toString());

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mDates.add(date1.toString());

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mDates.add(date1.toString());

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mDates.add(date1.toString());

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mDates.add(date1.toString());

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mDates.add(date1.toString());

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mDates.add(date1.toString());

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mDates.add(date1.toString());

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mDates.add(date1.toString());

        Log.d(TAG,"added to lists");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "init recyclerView");
        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        MyAdapter adapter= new MyAdapter(mDates,mImageUrls,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}