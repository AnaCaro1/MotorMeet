package com.example.motormeet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText inputNome, inputLocal, inputData, inputDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        db = new DatabaseHelper(this);

        // Vincular IDs do XML (activity_add_event.xml)
        inputNome = findViewById(R.id.eventnome);
        inputLocal = findViewById(R.id.localevent);
        inputData = findViewById(R.id.data);
        inputDescricao = findViewById(R.id.descricao);

        ImageView backButton = findViewById(R.id.backicon4);
        backButton.setOnClickListener(v -> finish());

        Button publishButton = findViewById(R.id.button);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = inputNome.getText().toString();
                String local = inputLocal.getText().toString();
                String data = inputData.getText().toString();
                String desc = inputDescricao.getText().toString();

                // Recuperar utilizador logado
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                String emailDono = prefs.getString("email", null);

                if(emailDono == null){
                    Toast.makeText(AddEventActivity.this, "Erro de sessão. Faça login novamente.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(nome.isEmpty() || local.isEmpty() || data.isEmpty()){
                    Toast.makeText(AddEventActivity.this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean insert = db.addEvent(nome, local, data, desc, emailDono);
                    if(insert){
                        Toast.makeText(AddEventActivity.this, "Evento publicado!", Toast.LENGTH_SHORT).show();
                        finish(); // Volta para a tela anterior
                    } else {
                        Toast.makeText(AddEventActivity.this, "Erro ao publicar evento", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}