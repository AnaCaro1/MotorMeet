package com.example.motormeet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExploreEventsActivity extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout containerEventos; // O layout onde vamos adicionar os cards

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_events);

        db = new DatabaseHelper(this);

        // Este ID 'layoutvertical' deve ser o LinearLayout dentro do ScrollView no seu XML
        containerEventos = findViewById(R.id.layoutvertical);

        ImageView backButton = findViewById(R.id.backicon5);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExploreEventsActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarEventos();
    }

    private void carregarEventos() {
        containerEventos.removeAllViews(); // Limpa a lista para não duplicar

        Cursor cursor = db.getAllEvents();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nenhum evento encontrado.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                // Índices: 0=id, 1=name, 2=location, 3=date, 4=description...
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String location = cursor.getString(2);
                String date = cursor.getString(3);

                // Inflar o layout do item (criado no passo 3)
                View view = LayoutInflater.from(this).inflate(R.layout.item_event, containerEventos, false);

                TextView txtTitle = view.findViewById(R.id.eventTitleItem);
                TextView txtLocation = view.findViewById(R.id.eventLocationItem);
                TextView txtDate = view.findViewById(R.id.eventDateItem);

                txtTitle.setText(name);
                txtLocation.setText(location);
                txtDate.setText(date);

                // Clique no card para ver detalhes (AboutEvent)
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(ExploreEventsActivity.this, AboutEventActivity.class);
                    // Pode passar o ID aqui para carregar os detalhes lá:
                    // intent.putExtra("EVENT_ID", id);
                    startActivity(intent);
                });

                containerEventos.addView(view);
            }
        }
    }
}