package com.ubermenschalone.unotebot.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int mUserId;
    String mName;
    private LinkedHashMap<String, Note> mNotes = new LinkedHashMap<>();

    public User(int userId){
        this.mUserId = userId;
    }

    public void addNote(String noteText){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        Note note = new Note(noteText);
        mNotes.put(randomUUIDString, note);
    }

    public LinkedHashMap<String, Note> getNotes(){
        return mNotes;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}