package com.example.motormeet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CarActivity extends AppCompatActivity {

    DatabaseHelper db;
    String carId;

    TextView txtMarca, txtModelo, txtAno, txtCambio, btnEditar; // btnEditar adicionado
    Button btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        db = new DatabaseHelper(this);
        carId = getIntent().getStringExtra("CAR_ID");

        txtMarca = findViewById(R.id.participante1);
        txtModelo = findViewById(R.id.Modelo);
        txtAno = findViewById(R.id.Ano);
        txtCambio = findViewById(R.id.Cambio);
        btnExcluir = findViewById(R.id.button2);

        // Vínculo do botão Editar (TextView no seu XML)
        btnEditar = findViewById(R.id.location_filter5);

        // Ação de Editar
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(CarActivity.this, AddCarActivity.class);
            intent.putExtra("CAR_ID_EDIT", carId); // Envia o ID para edição
            startActivity(intent);
        });

        // Ação de Excluir
        btnExcluir.setOnClickListener(v -> {
            Integer deletedRows = db.deleteCar(carId);
            if(deletedRows > 0){
                Toast.makeText(CarActivity.this, "Carro excluído", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CarActivity.this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.backicon).setOnClickListener(v -> finish());
    }

    // Mover o carregamento para onResume para atualizar ao voltar da edição
    @Override
    protected void onResume() {
        super.onResume();
        carregarDetalhes();
    }

    private void carregarDetalhes() {
        Cursor cursor = db.getCarById(carId);
        if(cursor.moveToFirst()){
            // Verifique os índices conforme seu CREATE TABLE
            // 0: id, 1: marca, 2: modelo, 3: ano, 4: cambio
            txtMarca.setText(cursor.getString(1));
            txtModelo.setText(cursor.getString(2));
            txtAno.setText(cursor.getString(3));
            txtCambio.setText(cursor.getString(4));
        } else {
            // Caso o carro tenha sido excluído ou não encontrado
            finish();
        }
        cursor.close();
    }
}