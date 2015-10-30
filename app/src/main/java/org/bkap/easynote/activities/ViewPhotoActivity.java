package org.bkap.easynote.activities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import java.io.File;

public class ViewPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Uri photoUri = intent.getData();
        ImageView notePhoto = (ImageView)findViewById(R.id.note_photo);
        notePhoto.setImageURI(photoUri);
    }
}
