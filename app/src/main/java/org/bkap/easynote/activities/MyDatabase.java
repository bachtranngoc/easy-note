package org.bkap.easynote.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import org.bkap.easynote.models.Note;
import org.bkap.easynote.sqlitehelper.SQLiteAssetHelper;


public class MyDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "simplenote.sqlite";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<Note> getNote() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Note> listNote = new ArrayList<Note>();
        Note mNote;
        String mTitle, mDes, mImage;
        long mDate;
        String query = "select * from note";
        Log.d("mQuery", query);
        Cursor cursor = db.rawQuery(query, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            mTitle = cursor.getString(cursor.getColumnIndex("title"));
            mDes = cursor.getString(cursor.getColumnIndex("content"));
            mDate = cursor.getLong(cursor.getColumnIndex("addedTime"));
            mImage = cursor.getString(cursor.getColumnIndex("image"));

            mNote = new Note(mTitle, mDes, mDate);
            mNote.setImage(mImage);

            listNote.add(mNote);
            cursor.moveToNext();
        }
//        if(listVija==null){
//
//        }

        cursor.close();
        return listNote;
    }

    public void insertNote(Note note) {
        SQLiteDatabase db = getReadableDatabase();
//        String sql_Query = "INSERT INTO note(title, content, addedTime) VALUES('"+title+"','"+descrip+"',"+date+")";
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("image", note.getImage());
        values.put("addedTime", note.getAddedDate());
        db.insert("note", null, values);
//        db.execSQL(sql_Query);
    }

    public void updateNote(Note note){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("addedTime", note.getAddedDate());
        int ret = db.update("note", values, "addedTime = ?", new String[]{String.valueOf(note.getAddedDate())});
        if(ret==0){
            //fail
            Log.d("update", "Fail!");
            Log.d("update", note.getTitle());
            Log.d("update", note.getContent());
            Log.d("update", String.valueOf(note.getAddedDate()));
        }else {
            //success
            Log.d("update", "Success!");
        }
    }

    public void deleteNote(String date) {
        SQLiteDatabase db = getReadableDatabase();
        String sql_Query = "DELETE FROM note WHERE addedTime = "+date;
        db.execSQL(sql_Query);
    }
    public void deleteAllNote() {
        SQLiteDatabase db = getReadableDatabase();
        String sql_Query = "DELETE FROM note";
        db.execSQL(sql_Query);
    }

}