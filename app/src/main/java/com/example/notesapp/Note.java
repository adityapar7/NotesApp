package com.example.notesapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Note {

    private String title,
            content;

    public Note() {

    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("title", title);
        return result;
    }
}

