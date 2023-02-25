package com.example.pm011ptarea32023;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pm011ptarea32023.configuracion.SQLiteConexion;
import com.example.pm011ptarea32023.transacciones.Personas;
import com.example.pm011ptarea32023.transacciones.Transacciones;

import java.util.ArrayList;

public class listacombo extends AppCompatActivity {
    SQLiteConexion conexion;
    Spinner combopersonas;
    EditText txtid, txtnombres, txtapellidos, txtedad, txtcorreo, txtdireccion;
    ArrayList<Personas> listapersonas;
    ArrayList<String> Arreglopersonas;
    Button btnactualizar,btneliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacombo);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        combopersonas = (Spinner) findViewById(R.id.combopersonas);
        txtid =(EditText) findViewById(R.id.txtcbid);
        txtnombres =(EditText) findViewById(R.id.txtcbnombres);
        txtapellidos =(EditText) findViewById(R.id.txtcbapellidos);
        txtedad =(EditText) findViewById(R.id.txtcbedad);
        txtcorreo =(EditText) findViewById(R.id.txtcbcorreo);
        txtdireccion =(EditText) findViewById(R.id.txtcbdireccion);

        btneliminar = (Button) findViewById(R.id.btneliminar);
        btnactualizar = (Button) findViewById(R.id.btnactualizar);

        ObtenerListaPersonas();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Arreglopersonas);
        combopersonas.setAdapter(adp);

        combopersonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int indice, long l) {
                txtnombres.setText(listapersonas.get(indice).getNombres());
                txtid.setText(listapersonas.get(indice).getId().toString());
                txtapellidos.setText(listapersonas.get(indice).getApellidos());
                txtcorreo.setText(listapersonas.get(indice).getCorreo());
                txtdireccion.setText(listapersonas.get(indice).getDireccion());
                txtedad.setText(listapersonas.get(indice).getEdad().toString());

                btnactualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            ActualizarContacto();
                    }

                });

                btneliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EliminarContacto();
                    }

                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void EliminarContacto() {
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            Long Resultado = Long.valueOf(db.delete(Transacciones.tablapersonas, "id=?", new String[]{txtid.getText().toString()}));

            if (Resultado > 0) {
                Toast.makeText(this, "Se eliminó el registro con éxito", Toast.LENGTH_LONG).show();
                //LLamado para refrescar pantalla despues de eliminar el contacto
                FillList();
                ObtenerListaPersonas();
                ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Arreglopersonas);
                combopersonas.setAdapter(adp);
            } else {
                Toast.makeText(this, "No se pudo eliminar el registro", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "No se pudo eliminar el dato", Toast.LENGTH_LONG).show();
        }
    }

    private void ActualizarContacto() {
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("nombres", txtnombres.getText().toString());
            valores.put("apellidos", txtapellidos.getText().toString());
            valores.put("correo", txtcorreo.getText().toString());
            valores.put("edad", txtedad.getText().toString());
            valores.put("direccion", txtdireccion.getText().toString());


            int resultado = db.update(Transacciones.tablapersonas, valores, "id=?", new String[]{txtid.getText().toString()});
            if (resultado > 0) {
                Toast.makeText(this, "Se actualizó el registro con éxito", Toast.LENGTH_LONG).show();
                //LLamado para refrescar pantalla despues de eliminar el contacto
                FillList();
                ObtenerListaPersonas();
                ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Arreglopersonas);
                combopersonas.setAdapter(adp);
            } else {
                Toast.makeText(this, "No se pudo actualizar el registro", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "No se pudo actualizar el dato", Toast.LENGTH_LONG).show();
        }
    }

    private void ObtenerListaPersonas(){
        SQLiteDatabase db = conexion.getWritableDatabase();
        Personas person = null;
        listapersonas = new ArrayList<Personas>();

        //Cursor
        Cursor cursor = db.rawQuery("SELECT * FROM personas", null);

        while (cursor.moveToNext()){
            person = new Personas();
            person.setId(cursor.getInt(0));
            person.setNombres(cursor.getString(1));
            person.setApellidos(cursor.getString(2));
            person.setEdad(cursor.getInt(3));
            person.setCorreo(cursor.getString(4));
            person.setDireccion(cursor.getString(5));

            listapersonas.add(person);
        }
        cursor.close();
        FillList();
    }
    private void FillList(){
        Arreglopersonas = new ArrayList<>();
        for (int i = 0; i < listapersonas.size(); i++){
            Arreglopersonas.add(listapersonas.get(i).getId()+ "|" +
                    listapersonas.get(i).getNombres()+ "|" +
                    listapersonas.get(i).getApellidos());

        }
    }

}