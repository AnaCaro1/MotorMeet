package com.example.motormeet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "MotorMeet.db";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        // Criando tabela com todas as colunas necessárias
        MyDB.execSQL("create Table users(email TEXT primary key, password TEXT, phone TEXT, cpf TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
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
}