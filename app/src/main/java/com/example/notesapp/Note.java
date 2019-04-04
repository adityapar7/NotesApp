package com.example.notesapp;

import java.util.Date;

public class Note {
    private Date date;
    private String content;

    public Note(Date date, String content){
        this.date=date;
        this.content=content;
    }

    public Date getDate(){
        return date;
    }

    public String getContent(){
        return content;
    }

}
