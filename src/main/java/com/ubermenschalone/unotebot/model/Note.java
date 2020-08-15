package com.ubermenschalone.unotebot.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private String mNote;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd HH:mm");

    public Note(String note){
        this.mNote = note;
    }

    public String getmDate() {
        return dateFormat.format(new Date());
    }

    public String getmNote() {
        return mNote;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
    }
}
