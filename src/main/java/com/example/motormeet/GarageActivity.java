package com.example.motormeet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GarageActivity extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout containerCarrosExpostos; // O container vertical 'carsexposed'
    Button btnAdicionarCarro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        db = new DatabaseHelper(this);

        // Container onde vamos adicionar os carros dinamicamente
        containerCarrosExpostos = findViewById(R.id.carsexposed);

        // Botão Adicionar (ID button3 no seu XML)
        btnAdicionarCarro = findViewById(R.id.button3);
        btnAdicionarCarro.setOnClickListener(v -> {
            startActivity(new Intent(GarageActivity.this, AddCarActivity.class));
        });

        // Botão voltar
        findViewById(R.id.backicon7).setOnClickListener(v -> finish());

        ImageView btnProfile = findViewById(R.id.iconProfile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(GarageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCarrosUsuario();
    }

    private void carregarCarrosUsuario() {
        // Limpa os views existentes (ex: os placeholders fixos do XML) para não duplicar
        containerCarrosExpostos.removeAllViews();

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");

        Cursor cursor = db.getCarsByUser(email);

        if(cursor.getCount() == 0){
            // Opcional: Mostrar msg "Nenhum carro cadastrado"
        } else {
            while(cursor.moveToNext()){
                // Pegar dados do banco
                String id = cursor.getString(0);
                String marca = cursor.getString(1);
                String modelo = cursor.getString(2);
                String ano = cursor.getString(3);

                // Criar View do card dinamicamente
                // Vamos inflar um layout simples de card ou criar na mão.
                // Para simplificar, vou criar um layout simples de item aqui mesmo.

                View cardView = getLayoutInflater().inflate(R.layout.item_carro_dinamico, null); // *Precisamos criar este XML pequeno

                TextView textModelo = cardView.findViewById(R.id.itemModelo);
                TextView textMarca = cardView.findViewById(R.id.itemMarca);
                TextView textAno = cardView.findViewById(R.id.itemAno);

                textModelo.setText(modelo);
                textMarca.setText(marca);
                textAno.setText(ano);

                // Clique no Card -> Abre Detalhes
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(GarageActivity.this, CarActivity.class);
                    intent.putExtra("CAR_ID", id);
                    startActivity(intent);
                });

                // Adiciona ao container da tela
                containerCarrosExpostos.addView(cardView);
            }
        }
    }
}