package com.example.water;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    EditText etGoal;
    Button btnSave;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etGoal = findViewById(R.id.etGoal);
        btnSave = findViewById(R.id.btnSave);
        prefs = getSharedPreferences("WaterPrefs", MODE_PRIVATE);

        etGoal.setText(String.valueOf(prefs.getInt("goal", 2000)));

        btnSave.setOnClickListener(v -> {
            String input = etGoal.getText().toString().trim();
            if (!input.isEmpty()) {
                prefs.edit().putInt("goal", Integer.parseInt(input)).apply();
                Toast.makeText(this, "Goal Saved! ✅",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}