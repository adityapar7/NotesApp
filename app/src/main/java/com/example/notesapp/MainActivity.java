package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String TAG2="Main.initRecyclerView()";
    //vars
    private ArrayList<String> mTitles= new ArrayList<>();
    private ArrayList<String> mContents= new ArrayList<>();
    private ArrayList<String> mImageUrls= new ArrayList<>();
    FloatingActionButton mButton;
    DatabaseReference mFirebaseReference;
    private String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Started");

        //clear the titles and Contents ArrayList so we don't get multiple of the same note on RecyclerView
        mTitles.clear();
        mContents.clear();

        initImageBitmaps();

        mButton=findViewById(R.id.newButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initImageBitmaps(){
        Log.d(TAG, "preparing bitmaps");

        //if the user does not have an Id yet, covering all bases
        if (userId==null){
            return;
        }

        //get list of notes from Firebase
        mFirebaseReference= FirebaseDatabase.getInstance().getReference("Users/"+userId+"/Notes");
        mFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //fill the Title and Content arrays
                for (DataSnapshot dst: dataSnapshot.getChildren()){
                    //adds keys and content values to the arrays
                    mTitles.add(dst.getKey());
                    mContents.add(dst.getValue().toString());

                    Log.d(TAG, "Key: " + dst.getKey());
                    Log.d(TAG, "Value: " + dst.getValue().toString());

                    //adds a picture for each Note
                    mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
                }
                Log.d(TAG,"Size of titles: " + mTitles.size());
                Log.d(TAG,"Size of contents: " + mContents.size());
                Log.d(TAG, "ArrayList of Titles: "+ mTitles.toString());
                Log.d(TAG, "ArrayList of Contents: "+ mContents.toString());

                initRecyclerView(mTitles,mImageUrls,mContents);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Database Error has occurred.");
            }
        });

        Log.d(TAG,"added to lists");



    }
    //Create the recycler view
    private void initRecyclerView(ArrayList<String> mTitles, ArrayList<String> mImageUrls, ArrayList<String> mContents){
        Log.d(TAG, "init recyclerView");
        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        MyAdapter adapter= new MyAdapter(mTitles, mImageUrls, mContents,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG,"Successful init recyclerView");



        Log.d(TAG2,"Size of titles: " + mTitles.size());
        Log.d(TAG2,"Size of contents: " + mContents.size());
        Log.d(TAG2,"Context: " + this.toString());
        Log.d(TAG2, "ArrayList of Titles: "+ mTitles.toString());
        Log.d(TAG2, "ArrayList of Contents: "+ mContents.toString());
    }

}