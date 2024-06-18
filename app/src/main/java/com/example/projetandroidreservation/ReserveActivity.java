package com.example.projetandroidreservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReserveActivity extends AppCompatActivity {

    private static final String TAG = "ReserveActivity";
    private EditText nameEditText;
    private EditText timeEditText;
    private Button reserveButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String restaurantId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        nameEditText = findViewById(R.id.name_edit_text);
        timeEditText = findViewById(R.id.time_edit_text);
        reserveButton = findViewById(R.id.reserve_button);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get restaurant ID from intent
        restaurantId = getIntent().getStringExtra("restaurantId");

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            //nameEditText.setText(currentUser.getEmail()); // Optionally set the email as default name
        } else {
            Log.e(TAG, "No user is currently signed in.");
            finish();
        }

        reserveButton.setOnClickListener(v -> makeReservation());

    }

    private void makeReservation() {
        String name = nameEditText.getText().toString();
        String time = timeEditText.getText().toString();

        if (name.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("restaurantId", restaurantId);
        reservation.put("name", name);
        reservation.put("time", time);
        reservation.put("userId", userId); // Add user ID to reservation

        db.collection("reservations")
                .add(reservation)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Reservation added with ID: " + task.getResult());
                            Toast.makeText(ReserveActivity.this, "Reservation successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e(TAG, "Error adding reservation", task.getException());
                            Toast.makeText(ReserveActivity.this, "Error making reservation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
