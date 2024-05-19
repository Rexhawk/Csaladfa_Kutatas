package com.example.csaladfa_kutatas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText birthPlaceEditText;
    private EditText birthDateEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        birthPlaceEditText = findViewById(R.id.birthPlace);
        birthDateEditText = findViewById(R.id.birthDate);

        Button registerButton = findViewById(R.id.register_button);
        Button logoutButton = findViewById(R.id.logout_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String birthPlace = birthPlaceEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || birthPlace.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Kérlek töltsd ki az összes mezőt", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            saveUserData(firstName, lastName, birthPlace, birthDate);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Regisztráció sikertelen.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserData(String firstName, String lastName, String birthPlace, String birthDate) {
        String userId = generateId();

        FamilyMember member = new FamilyMember(
                firstName,
                lastName,
                birthPlace,
                birthDate,
                null,
                null,
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList()
        );

        db.collection("familyMembers").document(userId).set(member)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Dokumentum hozzáadva azonosítóval: " + userId);
                    Toast.makeText(RegisterActivity.this, "Regisztráció sikeres!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Hiba történt a dokumentum hozzáadásakor", e);
                    Toast.makeText(RegisterActivity.this, "Hiba történt a dokumentum hozzáadásakor!", Toast.LENGTH_SHORT).show();
                });
    }

    private String generateId() {
        Random random = new Random();
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i == 4) {
                idBuilder.append('-');
            } else {
                char letter = (char) (random.nextInt(26) + 'A');
                idBuilder.append(letter);
            }
        }
        return idBuilder.toString();
    }

    private void logout() {
        // Navigálás a bejelentkezési képernyőre
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}