package com.austinegwa64.clipboarddemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ClipboardManager clipboard;
    private int PICK_REQUEST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    public void pickMusic(View v) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, PICK_REQUEST);
    }

    public void playMusic(View v) {
        ClipData clip = clipboard.getPrimaryClip();
        if (clip == null) {
            Toast.makeText(this, "There is no clip!", Toast.LENGTH_LONG)
                    .show();
        } else {
            ClipData.Item item = clip.getItemAt(0);
            Uri song = item.getUri();
            if (song != null
                    && getContentResolver().getType(song).startsWith("audio/")) {
                startActivity(new Intent(Intent.ACTION_VIEW, song));
            } else {
                Toast.makeText(this, "There is no song!", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == PICK_REQUEST) {
            if (resultCode == RESULT_OK) {
                ClipData clip =
                        ClipData.newUri(getContentResolver(), "Some music",
                                data.getData());
                try {
                    clipboard.setPrimaryClip(clip);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception clipping Uri", e);
                    Toast.makeText(this, "Exception: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}