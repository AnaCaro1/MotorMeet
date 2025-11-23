package com.example.motormeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PwdActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        db = new DatabaseHelper(this);

        // Reutilizando o ID 'eventnome' que no XML de Pwd é o campo de email
        EditText emailInput = findViewById(R.id.eventnome);
        Button actionButton = findViewById(R.id.button);

        actionButton.setText("Redefinir para '123456'"); // Opcional: feedback visual

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();

                if(email.equals("")) {
                    Toast.makeText(PwdActivity.this, "Digite seu email", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = db.checkEmail(email);
                    if(checkUser) {
                        // Reseta a senha para uma padrão ou pega de um novo campo se você criar
                        Boolean update = db.updatePassword(email, "123456");
                        if(update){
                            Toast.makeText(PwdActivity.this, "Senha redefinida para 123456", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PwdActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PwdActivity.this, "Erro no sistema", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PwdActivity.this, "Email não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}