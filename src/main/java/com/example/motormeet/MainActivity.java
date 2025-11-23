package com.example.motormeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        TextView seeMoreGarage = findViewById(R.id.subtitle2);
        seeMoreGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GarageActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout audiCard = findViewById(R.id.card);
        audiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CarActivity.class);
                startActivity(intent);
            }
        });

        Button continueButton = findViewById(R.id.button3);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        TextView seeMoreEvents = findViewById(R.id.subtitle6);
        seeMoreEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExploreEventsActivity.class);
                startActivity(intent);
            }
        });

        TextView seeMoreMyEvents = findViewById(R.id.subtitle7);
        seeMoreMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyEventsActivity.class);
                startActivity(intent);
            }
        });

        TextView seeMoreMySubscriptions = findViewById(R.id.subtitle7_2);
        seeMoreMySubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MySubscriptionsActivity.class);
                startActivity(intent);
            }
        });
    }
}
