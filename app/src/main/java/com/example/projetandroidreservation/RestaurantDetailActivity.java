package com.example.projetandroidreservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantDetailActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantDetail";
    private TextView restaurantName;
    private TextView restaurantDescription;
    private Button reserveButton;
    private FirebaseFirestore db;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        restaurantName = findViewById(R.id.restaurant_name_detail);
        restaurantDescription = findViewById(R.id.restaurant_description_detail);
        reserveButton = findViewById(R.id.reserve_button);

        db = FirebaseFirestore.getInstance();

        // Get restaurant ID from intent
        restaurantId = getIntent().getStringExtra("restaurantId");
        if (restaurantId != null) {
            loadRestaurantDetails(restaurantId);
        } else {
            Log.e(TAG, "No restaurant ID found in intent");
        }

        reserveButton.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailActivity.this, ReserveActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            startActivity(intent);
        });
    }

    private void loadRestaurantDetails(String restaurantId) {
        db.collection("restaurants").document(restaurantId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Restaurant restaurant = document.toObject(Restaurant.class);
                                if (restaurant != null) {
                                    restaurantName.setText(restaurant.getName());
                                    restaurantDescription.setText(restaurant.getDescription());
                                } else {
                                    Log.e(TAG, "Error converting document to Restaurant object");
                                }
                            } else {
                                Log.e(TAG, "No such document");
                            }
                        } else {
                            Log.e(TAG, "Get failed with ", task.getException());
                        }
                    }
                });
    }
}
