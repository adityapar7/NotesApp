package com.example.notesapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    public String uId;
    public Note note;

    public Users(){

    }

    public Users(String uId, Note note){
        this.note=note;
        this.uId=uId;
    }
}
