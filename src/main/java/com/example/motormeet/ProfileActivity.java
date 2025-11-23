package com.example.motormeet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView txtEmail;
    EditText editPhone, editCpf, editPassword;
    Button btnSalvar, btnDeletar;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new DatabaseHelper(this);

        // Recuperar email da sessão
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = prefs.getString("email", null);

        if (userEmail == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Vincular componentes
        txtEmail = findViewById(R.id.profileEmail); // Email não é editável (é a chave)
        editPhone = findViewById(R.id.profilePhone);
        editCpf = findViewById(R.id.profileCpf);
        editPassword = findViewById(R.id.profilePassword);
        btnSalvar = findViewById(R.id.btnSaveProfile);
        btnDeletar = findViewById(R.id.btnDeleteProfile);

        // Botão voltar
        findViewById(R.id.backicon_profile).setOnClickListener(v -> finish());

        // Carregar dados iniciais
        carregarDados();

        // --- AÇÃO DE SALVAR EDIÇÃO ---
        btnSalvar.setOnClickListener(v -> {
            String newPhone = editPhone.getText().toString();
            String newCpf = editCpf.getText().toString();
            String newPass = editPassword.getText().toString();

            if(newPass.isEmpty()){
                Toast.makeText(this, "A senha não pode ser vazia", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean update = db.updateUser(userEmail, newPass, newPhone, newCpf);
            if(update){
                Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- AÇÃO DE DELETAR CONTA ---
        btnDeletar.setOnClickListener(v -> confirmDelete());
    }

    private void carregarDados() {
        Cursor cursor = db.getUserData(userEmail);
        if (cursor.moveToFirst()) {
            // Índices baseados na tabela users: email(0), password(1), phone(2), cpf(3)
            txtEmail.setText(cursor.getString(0));
            editPassword.setText(cursor.getString(1));
            editPhone.setText(cursor.getString(2));
            editCpf.setText(cursor.getString(3));
        }
        cursor.close();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Conta")
                .setMessage("Tem certeza? Isso apagará sua conta, carros e eventos permanentemente.")
                .setPositiveButton("Sim, excluir", (dialog, which) -> {

                    Boolean delete = db.deleteUser(userEmail);
                    if(delete){
                        // Limpar Sessão
                        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();

                        Toast.makeText(ProfileActivity.this, "Conta excluída.", Toast.LENGTH_LONG).show();

                        // Voltar para Login
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpa histórico
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Erro ao excluir.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}