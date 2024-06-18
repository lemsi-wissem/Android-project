package com.example.projetandroidreservation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddRestaurantActivity extends AppCompatActivity {

    private static final String TAG = "AddRestaurantActivity";

    private EditText nameEditText;
    private EditText descriptionEditText;
    private Button addButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        nameEditText = findViewById(R.id.name_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        addButton = findViewById(R.id.add_button);

        db = FirebaseFirestore.getInstance();

        addButton.setOnClickListener(v -> addRestaurant());
    }

    private void addRestaurant() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créez un nouvel objet Restaurant
        Restaurant restaurant = new Restaurant(name, description);

        // Ajoutez le restaurant à Firebase Firestore
        db.collection("restaurants")
                .add(restaurant)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Restaurant added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Terminez l'activité pour revenir à la liste des restaurants
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding restaurant", e);
                    Toast.makeText(this, "Failed to add restaurant", Toast.LENGTH_SHORT).show();
                });
    }
}
