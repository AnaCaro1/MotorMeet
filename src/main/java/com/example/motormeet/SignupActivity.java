package com.example.motormeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText; // Importante
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referências aos campos do XML (usando os IDs que estão no seu layout)
        EditText emailEditText = findViewById(R.id.eventnome); // ID do campo de email
        EditText passEditText = findViewById(R.id.cpf);       // ID do campo de senha
        Button createAccountButton = findViewById(R.id.button);
        final CheckBox termsCheckbox = findViewById(R.id.checkBox);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String senha = passEditText.getText().toString();

                if (email.equals("") || senha.equals("")) {
                    Toast.makeText(SignupActivity.this, "Preencha Email e Senha.", Toast.LENGTH_SHORT).show();
                }
                else if (!termsCheckbox.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Você deve aceitar os termos para continuar.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // TUDO CERTO: Passar dados para a próxima tela (AddInfo)
                    Intent intent = new Intent(SignupActivity.this, AddInfoActivity.class);
                    intent.putExtra("EMAIL_KEY", email); // Enviando o email
                    intent.putExtra("SENHA_KEY", senha); // Enviando a senha
                    startActivity(intent);
                }
            }
        });
    }
}