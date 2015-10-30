package org.bkap.easynote.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.bkap.easynote.models.Note;

import java.io.File;
import java.io.IOException;

public class CustomNoteAcitivity extends AppCompatActivity {
    EditText et_Title, et_Description;
    Note mNote;
    private File photoTemp;
    private ImageView photoThumbnail;
    private Uri audioUri;
    String mTitle, mDescription, mAddedTime, mImage, mSound;
    long mDate;
    int mPosition;
    private boolean isToRemovePhoto, isToRemoveAudio, isPhotoAvail, isAudioAvail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        et_Title = (EditText) findViewById(R.id.et_title);
        et_Description = (EditText) findViewById(R.id.et_description);

        Intent callerIntent = getIntent();
        Bundle packageFromCaller =
                callerIntent.getBundleExtra("customNote");
        mTitle = packageFromCaller.getString("title");
        mDescription = packageFromCaller.getString("des");
        mImage = packageFromCaller.getString("image");
        mSound = packageFromCaller.getString("sound");
        mAddedTime = packageFromCaller.getString("addedTime");
        mPosition = packageFromCaller.getInt("mPosition");
        et_Title.setText(mTitle);
        et_Description.setText(mDescription);

//        setSupportActionBar(toolbar);

        if(mImage != null) {
            FrameLayout thumbnailLayout = (FrameLayout) findViewById(R.id.thumbnail_layout);
            ImageView photoThumbnail = (ImageView) findViewById(R.id.captured_photo_thumbnail);
            final Uri photoUri = Uri.fromFile(new File(mImage));
            photoThumbnail.setImageURI(photoUri);
            photoThumbnail.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewPhotoIntent = new Intent(CustomNoteAcitivity.this, ViewPhotoActivity.class);
                    viewPhotoIntent.setData(photoUri);
                    startActivity(viewPhotoIntent);
                }
            });
            thumbnailLayout.setVisibility(View.VISIBLE);
        }

        if(mSound != null) {
            FrameLayout audioLayout = (FrameLayout) findViewById(R.id.audio_layout);
            ImageView audioThumbnail = (ImageView) findViewById(R.id.recorded_audio_icon);
            audioThumbnail.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent playAudioIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSound));
                    startActivity(playAudioIntent);
                }
            });
            audioLayout.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File photo = null;
                if(isPhotoAvail) {
                    File photoDir = getDir("photos", Context.MODE_PRIVATE);
                    Log.d("AddNote", "photoDir: " + photoDir.getAbsolutePath());
                    photo = new File(photoDir, photoTemp.getName());
                    try {
                        FileUtils.copyFile(photoTemp, photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("AddNote", "photo exists: " + photo.exists() + "| " + photo.getAbsolutePath());
                }
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                mTitle = et_Title.getText().toString();
                mDescription = et_Description.getText().toString();
                mNote = new Note(mTitle, mDescription, mDate);
                if(isPhotoAvail) {
                    mNote.setImage(photo.getAbsolutePath());
                }
                if(isAudioAvail) {
                    mNote.setSound(audioUri.toString());
                }
                new UpdateSyntask().execute(mTitle, mDescription, mAddedTime);

                MainActivity.listNote.add(mNote);
                MainActivity.listNote.remove(mPosition);
                MainActivity.adapter.notifyDataSetChanged();
                finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == AddNoteAcitivity.REQUEST_IMAGE_CAPTURE) {
                isPhotoAvail = true;
                photoThumbnail = (ImageView)findViewById(R.id.captured_photo_thumbnail);
                final Uri photoUri = Uri.fromFile(photoTemp);
                photoThumbnail.setImageURI(photoUri);
                photoThumbnail.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent viewPhotoIntent = new Intent(CustomNoteAcitivity.this, ViewPhotoActivity.class);
                        viewPhotoIntent.setData(photoUri);
                        startActivity(viewPhotoIntent);
                    }
                });
                FrameLayout thumbnailLayout = (FrameLayout)findViewById(R.id.thumbnail_layout);
                thumbnailLayout.setVisibility(View.VISIBLE);
                Log.d("AddNote", "photoTemp exists: " + photoTemp.exists() + "| " + photoTemp.getAbsolutePath());
            } else if(requestCode == AddNoteAcitivity.REQUEST_AUDIO_RECORD) {
                isAudioAvail = true;
                ImageView audioThumbnail = (ImageView)findViewById(R.id.recorded_audio_icon);
                audioUri = data.getData();
                audioThumbnail.setOnClickListener(new ImageView.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent playAudioIntent = new Intent(Intent.ACTION_VIEW, audioUri);
                        startActivity(playAudioIntent);
                    }
                });
                FrameLayout audioLayout = (FrameLayout)findViewById(R.id.audio_layout);
                audioLayout.setVisibility(View.VISIBLE);
                Log.d("AddNote", "audio uri: " + data.getData().toString());
            }
        }
    }

    private void doCapturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File cachePhotoDir = new File(getExternalCacheDir(), "photos");
            if(!cachePhotoDir.exists()) {
                cachePhotoDir.mkdir();
            }
            photoTemp = new File(cachePhotoDir, System.currentTimeMillis() + ".jpg");
            if (photoTemp != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoTemp));
                startActivityForResult(takePictureIntent, AddNoteAcitivity.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void doCaptureAudio() {
        Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (recordAudioIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(recordAudioIntent, AddNoteAcitivity.REQUEST_AUDIO_RECORD);
        }
    }

    //remove 1
    private class UpdateSyntask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            MyDatabase db = new MyDatabase(getApplicationContext());

            Log.d("Update: ", "UPDATING ...");
            Note note = new Note(params[0], params[1], Long.parseLong(params[2]));
            db.updateNote(note);
            return null;
        }
    }
}
