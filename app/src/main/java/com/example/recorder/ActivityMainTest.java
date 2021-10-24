package com.example.recorder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ActivityMainTest extends AppCompatActivity {
    private MediaRecorder audioRecorder;
    private String audioPath = Environment.getExternalStorageDirectory() + File.separator + "haha" + File.separator + "audio";
    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permission -> {
                boolean result = true;
                for(Map.Entry<String, Boolean> set: permission.entrySet()) {
                    result = result & set.getValue();
                }
                if(result) {
                    System.out.println("All permissions granted");
                } else {
                    System.out.println("Please accept permissions");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] neededPermissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissionLauncher.launch(neededPermissions);

        audioRecorder = new MediaRecorder();

        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);


        File audioFile = new File(audioPath);
        try {
            if(audioFile.exists()) {
                audioFile.delete();
            }
            if(audioFile.exists() == false) {
                audioFile.createNewFile();
                audioRecorder.setOutputFile(audioFile.getPath());
                audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                audioRecorder.prepare();
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        final Button btnStop = findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("stopping....");
                audioRecorder.stop();
                audioRecorder.release();
            }
        });

        final Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("starting...");
                audioRecorder.start();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        System.out.println(requestCode);
        for(int iP = 0; iP < permissions.length; iP++) {
            System.out.println(permissions[iP] + " " + grantResults[iP]);
        }

    }

    private void createFile() {
        String pathname = Environment.getExternalStorageDirectory() + File.separator + "haha" + File.separator + "test";
        try {
            File file = new File(pathname);

            if(file.exists() == false) {
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                byte[] strs = new String("salut").getBytes();
                out.write(strs);
                out.flush();
                out.close();
                out = null;
            } else {
                System.out.println("file already exists");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}