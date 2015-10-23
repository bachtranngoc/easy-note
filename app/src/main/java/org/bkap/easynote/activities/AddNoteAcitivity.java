package org.bkap.easynote.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.bkap.easynote.models.Note;

public class AddNoteAcitivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_AUDIO_RECORD = 2;
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
        toolbar.inflateMenu(R.menu.add_note_actions);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.capture_photo:
                        doCapturePhoto();
                        break;
                    case R.id.record_audio:
                        doCaptureAudio();
                        break;
                }
                return true;
            }
        });
//        setSupportActionBar(toolbar);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_IMAGE_CAPTURE) {
                FrameLayout thumbnailLayout = (FrameLayout)findViewById(R.id.thumbnail_layout);
                final ImageView photoThumbnail = (ImageView)findViewById(R.id.captured_photo_thumbnail);
                final Uri photoUri = data.getData();
                photoThumbnail.post(new Runnable() {
                    @Override
                    public void run() {
                        photoThumbnail.setImageURI(photoUri);
                    }
                });
                thumbnailLayout.setVisibility(View.VISIBLE);
            } else if(requestCode == REQUEST_AUDIO_RECORD) {
                FrameLayout audioLayout = (FrameLayout)findViewById(R.id.audio_layout);
                audioLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void doCapturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, )
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void doCaptureAudio() {
        Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (recordAudioIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(recordAudioIntent, REQUEST_AUDIO_RECORD);
        }
    }

}
