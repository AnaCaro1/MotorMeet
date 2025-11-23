package com.example.motormeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa o Banco de Dados
        db = new DatabaseHelper(this);

        // Referências aos componentes do XML (usando seus IDs originais)
        emailInput = findViewById(R.id.eventnome); // Seu campo de email
        passwordInput = findViewById(R.id.cpf);    // Seu campo de senha

        Button loginButton = findViewById(R.id.button); // Botão Entrar
        Button createAccountButton = findViewById(R.id.button2);
        TextView createAccountSubtitle = findViewById(R.id.subtitle);
        TextView forgotPasswordLink = findViewById(R.id.link);

        // --- LÓGICA DE LOGIN (NOVO) ---
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = db.checkEmailPassword(email, password);
                    if(checkUser) {
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        // Redireciona para a tela principal
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Encerra a tela de login para não voltar nela com 'voltar'
                    } else {
                        Toast.makeText(LoginActivity.this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // -----------------------------

        // Seus listeners originais
        View.OnClickListener createAccountListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener forgotPasswordListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PwdActivity.class);
                startActivity(intent);
            }
        };

        createAccountButton.setOnClickListener(createAccountListener);
        createAccountSubtitle.setOnClickListener(createAccountListener);
        forgotPasswordLink.setOnClickListener(forgotPasswordListener);
    }
}