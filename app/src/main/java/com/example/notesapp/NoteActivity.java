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

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_content);

        Log.d(TAG, "Note Activity has been entered.");

        //sets the value of mContent
        mContent=findViewById(R.id.content);

        //put the information in the edit texts
        key= getIntent().getStringExtra("Key");
        Log.d(TAG, "Key is "+key);
        String content= getIntent().getStringExtra("Content");
        Log.d(TAG, "Content is "+ content);

        //if this is true then the note has to be updated and not added
        if (content!=null){
            mContent.setText(content);
        }

        //reference to current Database state of affairs
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //holds the userID
        final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "User ID is "+ userId);

        //declare the button
        mButton=findViewById(R.id.button);

        //when the button is clicked, it will submit the note to the firebase and then change the activity to
        //Main Activity, the recycler view of notes
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

                //method that will submit the Note changes/new Notes to Firebase
                submit(userId, title, content);

                //change the intent back to main activity after the note is saved
                Intent intent= new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //method that submits a NEW NOTE to Firebase
    private void submit(final String userId, final String title, final String content) {
        Log.d(TAG, "new note has been created");

        //will add a user if needed, but is mainly for adding new notes to users
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            String thing=null;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //checks if the user is already in the database
                if (dataSnapshot.hasChild(userId)){
                    Log.d(TAG, "User exists");
                    thing="hello";
                }

                //checks if the current note is already in the database
                if (dataSnapshot.child(userId).child("Notes").hasChild(key)){
                    updateNote(userId, key, content);
                    Log.d(TAG, "Note has been successfully updated.");
                    return;
                }

                Log.d(TAG,"thing is : "+ thing);
                //if the user does not exist
                if (thing==null){
                    //add a new user
                    Log.d(TAG,"attempting to make a user");
                    //put the child in the database
                    mDatabase.child("Users").child(userId).setValue(userId);
                    Log.d(TAG, "new user made");
                    //add unique ID for note
                    String noteKey= mDatabase.child("Users/"+userId+"/Notes/").push().getKey();
                    Log.d(TAG, "KEY1: "+ noteKey);
                    Log.d(TAG, "Note ID added 1");
                    //Add content to the random key
                    mDatabase.child("Users/"+userId+"/Notes/"+noteKey+"/").setValue(content);
                    Log.d(TAG,"Note added 1");
                }

                //if the user already exists, a new note needs to be added
                else{
                    Log.d(TAG,"user already exists, attempting to add new Note");
                    //push the new note on to the database
                    //add unique ID for note
                    String noteKey= mDatabase.child("Users/"+userId+"/Notes/").push().getKey();
                    Log.d(TAG, "KEY2: "+ noteKey);
                    Log.d(TAG, "Note ID added 2");
                    //add content to the random key
                    mDatabase.child("Users/"+userId+"/Notes/"+noteKey+"/").setValue(content);
                    Log.d(TAG,"Note added 2");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //updates an already EXISTING note to FireBase
    private void updateNote(final String userID, final String key, final String content){
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if the note key does not exist
                if (!dataSnapshot.hasChild(key)){
                    Log.d(TAG, "Entered updateNote even though the note doesn't exist");
                    return;
                }

                //updates the content at the specified note location in the database
                mDatabase.child("Users/"+userID+"/Notes/"+key+"/").setValue(content);
                Log.d(TAG,"Existing note updated");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "upDate Note onCancelled method called");
            }
        });
    }

}