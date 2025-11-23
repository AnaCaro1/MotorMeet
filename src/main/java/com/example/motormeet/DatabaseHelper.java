package com.example.motormeet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "MotorMeet.db";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        // Cria tabela de usuários
        MyDB.execSQL("create Table users(email TEXT primary key, password TEXT, phone TEXT, cpf TEXT)");

        // MUDANÇA IMPORTANTE: Cria tabela de carros
        // Adicionei as colunas que seu layout usa: marca, modelo, ano, cambio, etc.
        MyDB.execSQL("create Table cars(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "marca TEXT, " +
                "modelo TEXT, " +
                "ano TEXT, " +
                "cambio TEXT, " +
                "modificado INTEGER, " +
                "aceita_oferta INTEGER, " +
                "email_dono TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists cars");
        onCreate(MyDB);
    }

    // Método modificado para aceitar TODOS os dados
    public Boolean insertData(String email, String password, String phone, String cpf){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        contentValues.put("cpf", cpf);

        long result = MyDB.insert("users", null, contentValues);
        return result != -1;
    }

    // Checar se usuário existe (para Login)
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ? and password = ?", new String[] {email,password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Checar se email existe (para evitar duplicidade no cadastro)
    public Boolean checkEmail(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ?", new String[] {email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Atualizar Senha (Esqueci a senha)
    public Boolean updatePassword(String email, String newPassword){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        long result = MyDB.update("users", contentValues, "email=?", new String[]{email});
        return result != -1;
    }

    // --- MÉTODOS DE CARROS ---

    public Boolean addCar(String marca, String modelo, String ano, String cambio, boolean modificado, boolean oferta, String emailDono){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("marca", marca);
        contentValues.put("modelo", modelo);
        contentValues.put("ano", ano);
        contentValues.put("cambio", cambio);
        contentValues.put("modificado", modificado ? 1 : 0);
        contentValues.put("aceita_oferta", oferta ? 1 : 0);
        contentValues.put("email_dono", emailDono);

        long result = MyDB.insert("cars", null, contentValues);
        return result != -1;
    }

    public Cursor getCarsByUser(String email){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("Select * from cars where email_dono = ?", new String[]{email});
    }

    public Cursor getCarById(String id){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery("Select * from cars where id = ?", new String[]{id});
    }

    public Integer deleteCar(String id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete("cars", "id = ?", new String[]{id});
    }

    public Boolean updateCar(String id, String marca, String modelo, String ano, String cambio, boolean modificado, boolean oferta) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("marca", marca);
        contentValues.put("modelo", modelo);
        contentValues.put("ano", ano);
        contentValues.put("cambio", cambio);
        contentValues.put("modificado", modificado ? 1 : 0);
        contentValues.put("aceita_oferta", oferta ? 1 : 0);

        long result = MyDB.update("cars", contentValues, "id = ?", new String[]{id});
        return result != -1;
    }
}