package com.example.notesapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NoteActivity extends AppCompatActivity {

    String TAG="NotesActivity";

    private DatabaseReference mDatabase;

    EditText mDate;
    EditText mContent;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_content);
        Log.d(TAG, "Note has been entered.");

        //reference to current state of affairs
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //holds the userID
        final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "user id is "+ userId);
        //declare the button
        mButton=findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define contents/titles
                mDate=findViewById(R.id.title);
                mContent=findViewById(R.id.content);

                //holds the content and the date
                String title= mDate.getText().toString().trim();
                String content= mContent.getText().toString().trim();

                Log.d(TAG, "content has been entered");
                Log.d(TAG, "Title: "+ title);
                Log.d(TAG, "Content: " + content);
                //method that will submit the post
                submit(userId, title, content);
                Intent intent= new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //method that submits the update to Firebase
    private void submit(final String userId, final String title, final String content) {
        Log.d(TAG, "new note has been created");

        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            String thing=null;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)){
                    Log.d(TAG, "User exists");
                    thing="hello";
                }
                Log.d(TAG,"thing is : "+ thing);
                //if the user does not exist
                if (thing==null){
                    //add a new user
                    Log.d(TAG,"attempting to make a user");
                    //put the child in the database
                    mDatabase.child("Users").child(userId).setValue(userId);
                    Log.d(TAG, "new user made");
                    //add note to the content
                    mDatabase.child("Users/"+userId+"/Note/"+title).setValue(content);
                    Log.d(TAG, "User and note added");
                }
                else{
                    Log.d(TAG,"user already exists, attempting to add new Note");
                    //push the new note on to the database
                    mDatabase.child("Users/"+userId+"/Note/"+title).setValue(content);
                    Log.d(TAG,"Note added");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}