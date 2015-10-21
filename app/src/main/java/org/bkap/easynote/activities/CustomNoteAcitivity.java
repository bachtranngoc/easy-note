package org.bkap.easynote.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bkap.easynote.models.Note;

public class CustomNoteAcitivity extends AppCompatActivity {
    EditText et_Title, et_Description;
    Note mNote;
    String mTitle, mDescription;
    long mDate;
    int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_Title = (EditText) findViewById(R.id.et_title);
        et_Description = (EditText) findViewById(R.id.et_description);

        //lấy intent gọi Activity này
        Intent callerIntent = getIntent();
        //có intent rồi thì lấy Bundle dựa vào MyPackage
        Bundle packageFromCaller =
                callerIntent.getBundleExtra("customNote");
        //Có Bundle rồi thì lấy các thông số dựa vào soa, sob
        mTitle = packageFromCaller.getString("title");
        mDescription = packageFromCaller.getString("des");
        mPosition = packageFromCaller.getInt("mPosition");
        et_Title.setText(mTitle);
        et_Description.setText(mDescription);

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

                new UpdateSyntask().execute(mTitle, mDescription, String.valueOf(mDate));

//                MyDatabase db = new MyDatabase(getApplication());
//                db.setNote(mTitle, mDescription, mDate);

                MainActivity.listNote.add(mNote);
                MainActivity.listNote.remove(mPosition);
                MainActivity.adapter.notifyDataSetChanged();

            }
        });

    }

    //remove 1
    private class UpdateSyntask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            MyDatabase db = new MyDatabase(getApplicationContext());

            // Reading all contacts
            Log.d("Update: ", "UPDATING ...");

            db.updateNote(params[0], params[1], params[2]);
            return null;
        }
    }
}
