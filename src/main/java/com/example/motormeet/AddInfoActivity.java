package com.example.motormeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddInfoActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);

        // Receber dados vindos da SignupActivity
        Intent intentAnterior = getIntent();
        String emailRecebido = intentAnterior.getStringExtra("EMAIL_KEY");
        String senhaRecebida = intentAnterior.getStringExtra("SENHA_KEY");

        // Referências aos campos DESTA tela
        EditText phoneInput = findViewById(R.id.eventnome); // ID do telefone no seu XML
        EditText cpfInput = findViewById(R.id.cpf);         // ID do CPF no seu XML
        Button finalizeButton = findViewById(R.id.button);

        finalizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneInput.getText().toString();
                String cpf = cpfInput.getText().toString();

                // Verifica se os dados essenciais estão aqui (Email/Senha vieram da outra tela?)
                if(emailRecebido == null || senhaRecebida == null){
                    Toast.makeText(AddInfoActivity.this, "Erro no fluxo de cadastro.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica duplicidade
                Boolean checkEmail = db.checkEmail(emailRecebido);
                if(!checkEmail){
                    // Salva TUDO no banco (Email, Senha, Telefone, CPF)
                    Boolean insert = db.insertData(emailRecebido, senhaRecebida, phone, cpf);
                    if(insert){
                        Toast.makeText(AddInfoActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                        // Vai para o Login ou Home
                        Intent intent = new Intent(AddInfoActivity.this, MainActivity.class);
                        // Limpa a pilha para o usuário não voltar ao cadastro com o botão "voltar"
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddInfoActivity.this, "Erro ao salvar cadastro.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddInfoActivity.this, "Usuário já cadastrado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}