package com.example.recorder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class activity_main extends AppCompatActivity {
    private MediaRecorder audioRecorder;
    private static String audioPath = Environment.getExternalStorageDirectory() + File.separator + "haha" + File.separator + "audio.3gp";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 300;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 400;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        addListeners();


    }

    private void startRecording() {
        File audioFile = new File(audioPath);
        if(audioFile.exists()) {
            audioFile.delete();
        }
        try {
            if(audioFile.exists() == false) {
                audioFile.createNewFile();
            }
        } catch(Exception ex) {
            System.out.println("Fail to create audio file");
        }

        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        ///audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setOutputFile(audioFile.getPath());
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            audioRecorder.prepare();
        } catch (IOException e) {
            System.out.println("Prepare failed");
        }

        audioRecorder.start();
    }

    private void addListeners() {
        final Button btnStop = findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Stopping....");
                audioRecorder.stop();
                audioRecorder.release();
            }
        });

        final Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Starting...");
                startRecording();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("REQUEST CODE: " + requestCode);
        switch(requestCode) {
            case  REQUEST_RECORD_AUDIO_PERMISSION:
                    permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    break;
        }

        if(!permissionToRecordAccepted) finish();
    }
}