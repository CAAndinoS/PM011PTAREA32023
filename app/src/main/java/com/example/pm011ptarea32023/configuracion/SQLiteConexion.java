package com.example.pm011ptarea32023.configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pm011ptarea32023.transacciones.Transacciones;
public class SQLiteConexion extends SQLiteOpenHelper {
    //Constructor de clase
    // public SQLiteConexion() {
    //     super();
    //
    // }

    //Contructor de clase con parametros
    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //lista de las tablas a crear
        sqLiteDatabase.execSQL(Transacciones.CreateTBPersonas);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Transacciones.DropTablePersonas);

    }
}
