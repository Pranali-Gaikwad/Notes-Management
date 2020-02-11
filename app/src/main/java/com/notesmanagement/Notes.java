package com.notesmanagement;

public class Notes {
    private long _id;
    private String _title;
    private String _content;
    private String _dateOfCreation;
    private String _time;

    Notes(){}

    public Notes(String title, String content, String dateOfCreation, String time) {
        this._title = title;
        this._content = content;
        this._dateOfCreation = dateOfCreation;
        this._time = time;
    }

    public Notes(long id, String title, String content, String dateOfCreation, String time) {
        this._id = id;
        this._title = title;
        this._content = content;
        this._dateOfCreation = dateOfCreation;
        this._time = time;
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

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "_id=" + _id +
                ", _title='" + _title + '\'' +
                ", _content='" + _content + '\'' +
                ", _dateOfCreation='" + _dateOfCreation + '\'' +
                ", _time='" + _time + '\'' +
                '}';
    }
}
