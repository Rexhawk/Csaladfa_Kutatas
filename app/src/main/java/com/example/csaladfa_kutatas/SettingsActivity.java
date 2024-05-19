package com.example.csaladfa_kutatas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private TextView userDataTextView;
    private Switch saveDataSwitch, notificationsSwitch;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        userDataTextView = findViewById(R.id.user_data);
        saveDataSwitch = findViewById(R.id.save_data_switch);
        notificationsSwitch = findViewById(R.id.notifications_switch);
        backButton = findViewById(R.id.back_button);

        // Megjelenítjük a felhasználói adatokat
        if (currentUser != null) {
            String userData = "Email: " + currentUser.getEmail();
            userDataTextView.setText(userData);
        }

        // Betöltjük a mentett beállításokat
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        saveDataSwitch.setChecked(preferences.getBoolean("saveData", false));
        notificationsSwitch.setChecked(preferences.getBoolean("notifications", false));

        // Beállítások mentése
        saveDataSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("saveData", isChecked);
            editor.apply();
        });

        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("notifications", isChecked);
            editor.apply();
        });

        backButton.setOnClickListener(v -> finish());
    }
}