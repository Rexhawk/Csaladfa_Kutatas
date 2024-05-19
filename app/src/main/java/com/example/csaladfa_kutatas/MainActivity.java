package com.example.csaladfa_kutatas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;

    private EditText firstNameEditText, lastNameEditText, birthPlaceEditText, birthDateEditText, parentIdEditText;
    private Button addMemberButton, updateMemberButton, deleteMemberButton;

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

        addMemberButton = findViewById(R.id.addMemberButton);
        updateMemberButton = findViewById(R.id.updateMemberButton);
        deleteMemberButton = findViewById(R.id.deleteMemberButton);

        addMemberButton.setOnClickListener(v -> addMember());
        updateMemberButton.setOnClickListener(v -> updateMember());
        deleteMemberButton.setOnClickListener(v -> deleteMember());
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

    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}