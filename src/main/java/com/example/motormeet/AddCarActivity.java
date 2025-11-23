package com.example.motormeet;

import android.content.SharedPreferences;
import android.database.Cursor; // Importante
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView; // Para mudar o título
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {

    DatabaseHelper db;
    AutoCompleteTextView inputMarca, inputModelo;
    EditText inputAno;
    Switch switchModificado, switchOferta;
    RadioGroup radioGroupCambio;
    Button btnAdicionar;
    TextView textTitulo;

    String idParaEditar = null; // Variável para controlar se é edição

    String[] marcas = {"Audi", "BMW", "Ferrari", "Maserati", "Mercedes", "Porsche", "Lamborghini"};
    Map<String, String[]> modelosPorMarca = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        configurarModelos();
        db = new DatabaseHelper(this);

        inputMarca = findViewById(R.id.eventnome);
        inputModelo = findViewById(R.id.localevent);
        inputAno = findViewById(R.id.data);
        switchModificado = findViewById(R.id.modificado);
        switchOferta = findViewById(R.id.oferta);
        radioGroupCambio = findViewById(R.id.radiogroup);
        btnAdicionar = findViewById(R.id.button);
        textTitulo = findViewById(R.id.Title); // Título da tela

        // Configuração dos Adapters (Igual ao anterior)
        ArrayAdapter<String> adapterMarcas = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, marcas);
        inputMarca.setAdapter(adapterMarcas);
        inputMarca.setOnClickListener(v -> inputMarca.showDropDown());
        inputMarca.setOnItemClickListener((parent, view, position, id) -> {
            String marcaSelecionada = (String) parent.getItemAtPosition(position);
            atualizarListaModelos(marcaSelecionada);
        });
        inputModelo.setOnClickListener(v -> inputModelo.showDropDown());

        findViewById(R.id.backicon2).setOnClickListener(v -> finish());

        // --- LÓGICA DE EDIÇÃO ---
        // Verifica se veio um ID da tela anterior
        if (getIntent().hasExtra("CAR_ID_EDIT")) {
            idParaEditar = getIntent().getStringExtra("CAR_ID_EDIT");
            preencherDadosParaEdicao(idParaEditar);
            textTitulo.setText("Editando carro"); // Muda o título visualmente
            btnAdicionar.setText("Salvar alterações"); // Muda o botão
        }
        // ------------------------

        btnAdicionar.setOnClickListener(v -> salvarOuAtualizarCarro());
    }

    // Preenche os campos com os dados do banco
    private void preencherDadosParaEdicao(String id) {
        Cursor cursor = db.getCarById(id);
        if (cursor.moveToFirst()) {
            String marca = cursor.getString(1);
            String modelo = cursor.getString(2);
            String ano = cursor.getString(3);
            String cambio = cursor.getString(4);
            int modificado = cursor.getInt(5);
            int oferta = cursor.getInt(6);

            // Preenche os textos
            inputMarca.setText(marca, false); // false para não filtrar a lista agora
            atualizarListaModelos(marca); // Carrega a lista de modelos correta
            inputModelo.setText(modelo, false);
            inputAno.setText(ano);

            // Preenche os Switches
            switchModificado.setChecked(modificado == 1);
            switchOferta.setChecked(oferta == 1);

            // Preenche o RadioButton
            if (cambio.equalsIgnoreCase("Automático")) {
                ((RadioButton)findViewById(R.id.radioButton2)).setChecked(true);
            } else {
                ((RadioButton)findViewById(R.id.radioButton)).setChecked(true);
            }
        }
        cursor.close();
    }

    private void salvarOuAtualizarCarro() {
        String marca = inputMarca.getText().toString();
        String modelo = inputModelo.getText().toString();
        String ano = inputAno.getText().toString();

        String cambio = "Manual";
        int selectedId = radioGroupCambio.getCheckedRadioButtonId();
        if(selectedId != -1){
            RadioButton radioButton = findViewById(selectedId);
            cambio = radioButton.getText().toString();
        }

        boolean isModificado = switchModificado.isChecked();
        boolean isOferta = switchOferta.isChecked();

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String emailDono = prefs.getString("email", null);

        if(marca.isEmpty() || modelo.isEmpty() || ano.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idParaEditar != null) {
            // --- ATUALIZAR (UPDATE) ---
            Boolean update = db.updateCar(idParaEditar, marca, modelo, ano, cambio, isModificado, isOferta);
            if(update){
                Toast.makeText(this, "Carro atualizado!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // --- CRIAR NOVO (INSERT) ---
            if(emailDono == null) {
                Toast.makeText(this, "Erro de sessão", Toast.LENGTH_SHORT).show();
                return;
            }
            Boolean insert = db.addCar(marca, modelo, ano, cambio, isModificado, isOferta, emailDono);
            if(insert){
                Toast.makeText(this, "Carro adicionado!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void configurarModelos() {
        modelosPorMarca.put("Audi", new String[]{"A3", "A4", "A5", "Q3", "Q5", "R8", "RS6"});
        modelosPorMarca.put("BMW", new String[]{"320i", "X1", "X5", "X6", "M3", "M4", "Z4"});
        modelosPorMarca.put("Ferrari", new String[]{"458 Italia", "488 GTB", "F8 Tributo", "Portofino", "Roma", "LaFerrari"});
        modelosPorMarca.put("Maserati", new String[]{"Ghibli", "Levante", "Quattroporte", "GranTurismo", "MC20"});
        modelosPorMarca.put("Mercedes", new String[]{"C180", "C200", "C300", "E-Class", "S-Class", "AMG GT"});
        modelosPorMarca.put("Porsche", new String[]{"911", "Cayenne", "Macan", "Panamera", "Taycan", "718 Boxster"});
        modelosPorMarca.put("Lamborghini", new String[]{"Huracán", "Aventador", "Urus", "Gallardo"});
    }

    private void atualizarListaModelos(String marca) {
        if (modelosPorMarca.containsKey(marca)) {
            String[] modelos = modelosPorMarca.get(marca);
            ArrayAdapter<String> adapterModelos = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, modelos);
            inputModelo.setAdapter(adapterModelos);
        } else {
            inputModelo.setAdapter(null);
        }
    }
}