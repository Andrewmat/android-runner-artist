package com.example.andre.runnerartist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class InsertProfileActivity extends AppCompatActivity {

    private EditText edvInsertName;
    private Button btnInsertProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_profile);
        edvInsertName = (EditText) findViewById(R.id.edvInsertName);
        btnInsertProfile = (Button) findViewById(R.id.btnInsertProfile);

        btnInsertProfile.setOnClickListener(v -> {
            Intent in = new Intent();
            in.putExtra("profileName", edvInsertName.getText().toString());
            setResult(RESULT_OK, in);
            finish();
        });
    }
}
