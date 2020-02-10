package com.notesmanagement;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


public class NotesManagementDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "notesmdb.db";
    private static final String DATABASE_TABLE = "notesmtables";
    private static final String COLOUM_ID = "_id";
    private static final String COLOUM_TITLE = "_title";
    private static final String COLOUM_CONTENT = "_content";
    private static final String COLOUM_DATE_OF_CREATION = "_dateOfCreation";
    private static final String COLOUM_TIME = "_time";




    public NotesManagementDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + DATABASE_TABLE +"("
                + COLOUM_ID + " INTEGER PRIMARY KEY, " + COLOUM_TITLE + " TEXT, "
                + COLOUM_CONTENT + " TEXT, " + COLOUM_DATE_OF_CREATION + " TEXT, "
                + COLOUM_TIME + " TEXT"+")";
        db.execSQL(query);


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion>=newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);


    }


    public void addNoteInDatabase(Notes notes) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOUM_TITLE, notes.get_title());
        contentValues.put(COLOUM_CONTENT, notes.get_content());
        contentValues.put(COLOUM_DATE_OF_CREATION, notes.get_dateOfCreation());
        contentValues.put(COLOUM_TIME, notes.get_time());
        SQLiteDatabase db =this.getWritableDatabase();

        long ID = db.insert(DATABASE_TABLE, null, contentValues);
        Log.d("inserted", " ID " + ID);
        db.close();

    }

    public Notes getOneNote(long id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(DATABASE_TABLE, new String[] {COLOUM_ID, COLOUM_TITLE, COLOUM_CONTENT, COLOUM_DATE_OF_CREATION, COLOUM_TIME}, COLOUM_ID+"=?",
                new String[] {String.valueOf(id)},null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        return new Notes(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4));

    }


    public List<Notes> getListOfNotes()
    {
        SQLiteDatabase db= this.getReadableDatabase();
        List<Notes> allnotes=new ArrayList<>();

        String query= "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor=db.rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do {
                Notes notes=new Notes();
                notes.set_id(cursor.getLong(0));
                notes.set_title(cursor.getString(1));
                notes.set_content(cursor.getString(2));
                notes.set_dateOfCreation(cursor.getString(3));
                notes.set_time(cursor.getString(4));

                allnotes.add(notes);
            }while (cursor.moveToNext());
        } return allnotes;
    }


public void deleteNote(long id)
{
    SQLiteDatabase db =this.getWritableDatabase();
    db.delete(DATABASE_TABLE, COLOUM_ID+"=?", new String[]{String.valueOf(id)});
    db.close();
}

public int editNote(Notes n)
{
    SQLiteDatabase db =this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLOUM_TITLE, n.get_title());
    contentValues.put(COLOUM_CONTENT, n.get_content());
    contentValues.put(COLOUM_DATE_OF_CREATION, n.get_dateOfCreation());
    contentValues.put(COLOUM_TIME, n.get_time());

  return db.update(DATABASE_TABLE, contentValues, COLOUM_ID+"=?",new String[]{String.valueOf(n.get_id())});
}





}