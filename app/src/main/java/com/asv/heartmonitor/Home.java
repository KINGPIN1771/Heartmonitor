package com.asv.heartmonitor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.asv.heartmonitor.R;

public class Home extends AppCompatActivity {

    EditText ageEditText;
    LinearLayout rhrLinearLayout;
    TextView rhrTextView;
    LinearLayout thrLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpPermissions();

        ageEditText = findViewById(R.id.et_age);
        rhrLinearLayout = findViewById(R.id.ll_restingHeartRate);
        rhrTextView = findViewById(R.id.tv_restingHeartRate);
        thrLinearLayout = findViewById(R.id.ll_targetHeartRate);

        if (savedInstanceState != null) {
            ageEditText.setText(savedInstanceState.getString("storedAge"));
            rhrTextView.setText(savedInstanceState.getString("storedRHR"));
        }

        rhrLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRestingHeartRate();
            }
        });
        thrLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateTargetHeartRate();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String currentAge = ageEditText.getText().toString();
        outState.putString("storedAge", currentAge);
        String currentRHR = rhrTextView.getText().toString();
        outState.putString("storedRHR", currentRHR);
    }

    private void calculateTargetHeartRate() {
        String age = ageEditText.getText().toString();
        if (age.matches("")) {
            Toast.makeText(this, "Please enter your age!", Toast.LENGTH_SHORT).show();
            return;
        }

        String restingHeartRate = rhrTextView.getText().toString();
        if (restingHeartRate.matches("")) {
            Toast.makeText(this, "Please calculate your RHR first!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intentToCalculateTHR = new Intent(Home.this, MainActivity.class);
        intentToCalculateTHR.putExtra("age", age);
        intentToCalculateTHR.putExtra("restingHeartRate", restingHeartRate);

        startActivity(intentToCalculateTHR);
    }

    public void getRestingHeartRate() {
        Intent intentToGetRHR = new Intent(Home.this, MainActivity.class);
        startActivityForResult(intentToGetRHR, 1);
    }

    private void setUpPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] permissionsWeNeed = new String[]{Manifest.permission.CAMERA};
                requestPermissions(permissionsWeNeed, 88);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 88: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    Toast.makeText(this, "Permission for camera not granted. HeartBeat Monitor can't run.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String restingHeartRate = data.getStringExtra("restingHeartRate");
            rhrTextView.setText(restingHeartRate);
        }
    }
}
