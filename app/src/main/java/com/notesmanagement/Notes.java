package com.notesmanagement;

import androidx.annotation.NonNull;

public class Notes {
    private long _id;
    private String _title;
    private String _content;
    private String _dateOfCreation;


    public Notes() {
    }

    public Notes(String _title, String _content, String _dateOfCreation) {
        this._title = _title;
        this._content = _content;
        this._dateOfCreation = _dateOfCreation;
    }

    public Notes(long id, String title, String content, String dateOfCreation) {
        this._id = id;
        this._title = title;
        this._content = content;
        this._dateOfCreation = dateOfCreation;

    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public String get_dateOfCreation() {
        return _dateOfCreation;
    }

    public void set_dateOfCreation(String _dateOfCreation) {
        this._dateOfCreation = _dateOfCreation;
    }


    @NonNull
    @Override
    public String toString() {
        return "Notes{" +
                "_id=" + _id +
                ", _title='" + _title + '\'' +
                ", _content='" + _content + '\'' +
                ", _dateOfCreation='" + _dateOfCreation + '\'' +
                '}';
    }
}
