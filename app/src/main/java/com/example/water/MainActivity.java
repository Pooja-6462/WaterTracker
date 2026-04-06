package com.example.water;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etAmount;
    Button btnAdd, btnHistory, btnReminder, btnSettings, btnStats;
    Button btn100, btn250, btn500;
    TextView tvStatus, tvStreak, tvTip;
    WaterCircleView waterCircleView;
    DBHelper dbHelper;
    SharedPreferences prefs;
    String today;
    int dailyGoal;

    String[] tips = {
            "Drink a glass of water now!",
            "Start your day with water!",
            "Water boosts your energy!",
            "Stay hydrated, stay healthy!",
            "Drink water before every meal!",
            "Water helps your skin glow!",
            "8 glasses a day keeps doctor away!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect views
        etAmount = findViewById(R.id.etAmount);
        btnAdd = findViewById(R.id.btnAdd);
        btnHistory = findViewById(R.id.btnHistory);
        btnReminder = findViewById(R.id.btnReminder);
        btnSettings = findViewById(R.id.btnSettings);
        btnStats = findViewById(R.id.btnStats);
        btn100 = findViewById(R.id.btn100);
        btn250 = findViewById(R.id.btn250);
        btn500 = findViewById(R.id.btn500);
        tvStatus = findViewById(R.id.tvStatus);
        tvStreak = findViewById(R.id.tvStreak);
        tvTip = findViewById(R.id.tvTip);
        waterCircleView = findViewById(R.id.waterCircleView);

        dbHelper = new DBHelper(this);
        prefs = getSharedPreferences("WaterPrefs", MODE_PRIVATE);
        today = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        // Random tip
        int tipIndex = (int)(Math.random() * tips.length);
        tvTip.setText(tips[tipIndex]);

        updateProgress();
        updateStreak();

        // Quick buttons
        btn100.setOnClickListener(v -> addWater(100));
        btn250.setOnClickListener(v -> addWater(250));
        btn500.setOnClickListener(v -> addWater(500));

        // Custom add
        btnAdd.setOnClickListener(v -> {
            String input = etAmount.getText().toString().trim();
            if (!input.isEmpty()) {
                addWater(Integer.parseInt(input));
                etAmount.setText("");
            } else {
                Toast.makeText(this, "Please enter amount!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
        btnReminder.setOnClickListener(v ->
                startActivity(new Intent(this, RemainderActivity.class)));
        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
        btnStats.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
    }

    void addWater(int amount) {
        dbHelper.addIntake(amount, today);
        updateProgress();
        updateStreak();
        Toast.makeText(this, "Added " + amount + " ml! 💧",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dailyGoal = prefs.getInt("goal", 2000);
        updateProgress();
        updateStreak();
    }

    void updateProgress() {
        int total = dbHelper.getTodayTotal(today);
        dailyGoal = prefs.getInt("goal", 2000);
        float percent = (total / (float) dailyGoal) * 100f;
        if (percent > 100) percent = 100;
        waterCircleView.setProgress(percent, total, dailyGoal);
        tvStatus.setText(total + " ml / " + dailyGoal + " ml");
    }

    void updateStreak() {
        int streak = prefs.getInt("streak", 0);
        int lastTotal = dbHelper.getTodayTotal(today);
        if (lastTotal >= prefs.getInt("goal", 2000)) {
            streak = prefs.getInt("streak", 0) + 1;
            prefs.edit().putInt("streak", streak).apply();
        }
        tvStreak.setText(streak + " Days");
    }
}