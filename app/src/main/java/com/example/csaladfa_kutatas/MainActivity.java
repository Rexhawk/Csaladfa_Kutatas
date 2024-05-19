package com.example.csaladfa_kutatas;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    private static final String CHANNEL_ID = "default";
    private FirebaseFirestore db;

    private EditText firstNameEditText, lastNameEditText, birthPlaceEditText, birthDateEditText, parentIdEditText, searchEditText;
    private Button addMemberButton, updateMemberButton, deleteMemberButton, searchButton, logoutButton, settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        birthPlaceEditText = findViewById(R.id.birthPlace);
        birthDateEditText = findViewById(R.id.birthDate);
        parentIdEditText = findViewById(R.id.parentId);
        searchEditText = findViewById(R.id.searchEditText);

        Button addMemberButton = findViewById(R.id.addMemberButton);
        Button updateMemberButton = findViewById(R.id.updateMemberButton);
        Button deleteMemberButton = findViewById(R.id.deleteMemberButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button settingsButton = findViewById(R.id.settings_button);

        addMemberButton.setOnClickListener(v -> addMember());
        updateMemberButton.setOnClickListener(v -> updateMember());
        deleteMemberButton.setOnClickListener(v -> deleteMember());
        searchButton.setOnClickListener(v -> searchMember());
        logoutButton.setOnClickListener(v -> logout());
        settingsButton.setOnClickListener(v -> openSettings());

        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Ellenőrizni, hogy a notification be van-e kapcsolva
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean notificationsEnabled = preferences.getBoolean("notifications", false);

        if (notificationsEnabled) {
            // Figyelünk a Firestore változásokra
            db.collection("familyMembers").addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            showNotification("Új családtag hozzáadva", dc.getDocument().getId());
                            break;
                        case MODIFIED:
                            showNotification("Családtag módosítva", dc.getDocument().getId());
                            break;
                        case REMOVED:
                            showNotification("Családtag törölve", dc.getDocument().getId());
                            break;
                    }
                }
            });
        }
    }

    private void showNotification(String title, String content) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private void saveDataLocally() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean saveData = preferences.getBoolean("saveData", false);

        if (saveData) {
            db.collection("familyMembers")
                    .limit(4)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            File file = new File(getExternalFilesDir(null), "family_data.txt");
                            try (FileWriter writer = new FileWriter(file)) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FamilyMember member = document.toObject(FamilyMember.class);
                                    writer.write(member.toString() + "\n");
                                }
                                Toast.makeText(MainActivity.this, "Adatok sikeresen mentve", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Log.w(TAG, "Hiba történt az adatok mentésekor", e);
                                Toast.makeText(MainActivity.this, "Hiba történt az adatok mentésekor", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "Hiba történt az adatok lekérdezésekor", task.getException());
                            Toast.makeText(MainActivity.this, "Hiba történt az adatok lekérdezésekor", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void addMember() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String birthPlace = birthPlaceEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();
        String parentId = parentIdEditText.getText().toString();

        if (!isValidDate(birthDate)) {
            Toast.makeText(MainActivity.this, "Érvénytelen születési dátum formátum", Toast.LENGTH_SHORT).show();
            return;
        }

        FamilyMember member = new FamilyMember(
                firstName,
                lastName,
                birthPlace,
                birthDate,
                null,
                null,
                Arrays.asList(parentId),
                Arrays.asList(""),
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList()
        );

        CompletableFuture.runAsync(() -> {
            db.collection("familyMembers").add(member)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Dokumentum hozzáadva azonosítóval: " + documentReference.getId());
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Dokumentum sikeresen hozzáadva!", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Hiba történt a dokumentum hozzáadásakor", e);
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Hiba történt a dokumentum hozzáadásakor!", Toast.LENGTH_SHORT).show());
                    });
        });
    }

    private void updateMember() {
        String parentId = parentIdEditText.getText().toString();

        if (parentId.isEmpty()) {
            Toast.makeText(MainActivity.this, "Kérlek, add meg a módosítani kívánt szülő azonosítóját", Toast.LENGTH_SHORT).show();
            return;
        }

        CompletableFuture.runAsync(() -> {
            db.collection("familyMembers").whereArrayContains("parentIds", parentId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = document.getReference();
                                docRef.update(
                                        "firstName", firstNameEditText.getText().toString(),
                                        "lastName", lastNameEditText.getText().toString(),
                                        "birthPlace", birthPlaceEditText.getText().toString(),
                                        "birthDate", birthDateEditText.getText().toString()
                                ).addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Dokumentum sikeresen frissítve: " + docRef.getId());
                                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Dokumentum sikeresen frissítve!", Toast.LENGTH_SHORT).show());
                                }).addOnFailureListener(e -> {
                                    Log.w(TAG, "Hiba történt a dokumentum frissítésekor", e);
                                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Hiba történt a dokumentum frissítésekor!", Toast.LENGTH_SHORT).show());
                                });
                            }
                        } else {
                            Log.w(TAG, "Nem található dokumentum a megadott azonosítóval.");
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Nem található dokumentum a megadott azonosítóval.", Toast.LENGTH_SHORT).show());
                        }
                    });
        });
    }

    private void deleteMember() {
        String parentId = parentIdEditText.getText().toString();

        if (parentId.isEmpty()) {
            Toast.makeText(MainActivity.this, "Kérlek, add meg a törölni kívánt szülő azonosítóját", Toast.LENGTH_SHORT).show();
            return;
        }

        CompletableFuture.runAsync(() -> {
            db.collection("familyMembers").whereArrayContains("parentIds", parentId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = document.getReference();
                                docRef.delete().addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Dokumentum sikeresen törölve: " + docRef.getId());
                                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Dokumentum sikeresen törölve!", Toast.LENGTH_SHORT).show());
                                }).addOnFailureListener(e -> {
                                    Log.w(TAG, "Hiba történt a dokumentum törlésekor", e);
                                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Hiba történt a dokumentum törlésekor!", Toast.LENGTH_SHORT).show());
                                });
                            }
                        } else {
                            Log.w(TAG, "Nem található dokumentum a megadott azonosítóval.");
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Nem található dokumentum a megadott azonosítóval.", Toast.LENGTH_SHORT).show());
                        }
                    });
        });
    }

    private void searchMember() {
        String searchQuery = searchEditText.getText().toString();

        if (searchQuery.isEmpty()) {
            Toast.makeText(MainActivity.this, "Kérlek, add meg a keresési kifejezést", Toast.LENGTH_SHORT).show();
            return;
        }

        CompletableFuture.runAsync(() -> {
            db.collection("familyMembers").whereEqualTo("id", searchQuery).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FamilyMember member = document.toObject(FamilyMember.class);
                                Log.d(TAG, "Családtag: " + member.getFirstName() + " " + member.getLastName());
                                runOnUiThread(() -> Toast.makeText(MainActivity.this, "ID alapján megtalált családtag: " + member.getFirstName() + " " + member.getLastName(), Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            db.collection("familyMembers").whereEqualTo("firstName", searchQuery).get()
                                    .addOnCompleteListener(nameTask -> {
                                        if (nameTask.isSuccessful() && !nameTask.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot document : nameTask.getResult()) {
                                                FamilyMember member = document.toObject(FamilyMember.class);
                                                Log.d(TAG, "Családtag: " + member.getFirstName() + " " + member.getLastName());
                                                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Név alapján megtalált családtag: " + member.getFirstName() + " " + member.getLastName(), Toast.LENGTH_SHORT).show());
                                            }
                                        } else {
                                            Log.w(TAG, "Nem található dokumentum a megadott keresési kifejezéssel.");
                                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Nem található dokumentum a megadott keresési kifejezéssel.", Toast.LENGTH_SHORT).show());
                                        }
                                    });
                        }
                    });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Értesítési engedély megadva", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Értesítési engedély megtagadva", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default Channel";
            String description = "Notification channel for app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void logout() {
        // Navigálás a bejelentkezési képernyőre
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}