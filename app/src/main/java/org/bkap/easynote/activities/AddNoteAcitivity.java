package org.bkap.easynote.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bkap.easynote.models.Note;

public class AddNoteAcitivity extends AppCompatActivity {
    EditText et_Title, et_Description;

    Note mNote;
    String mTitle, mDescription;
    long mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_Title = (EditText) findViewById(R.id.et_title);
        et_Description = (EditText) findViewById(R.id.et_description);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                mTitle = et_Title.getText().toString();
                mDescription = et_Description.getText().toString();
                mDate = System.currentTimeMillis();
                mNote = new Note(mTitle, mDescription, mDate);
                MyDatabase db = new MyDatabase(getApplication());
                db.setNote(mTitle, mDescription, mDate);
                MainActivity.listNote.add(mNote);
                MainActivity.adapter.notifyDataSetChanged();

            }
        });

    }

}
