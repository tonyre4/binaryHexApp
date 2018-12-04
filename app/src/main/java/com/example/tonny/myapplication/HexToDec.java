package com.example.tonny.myapplication;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class HexToDec extends AppCompatActivity{

    Button punto_fijo, punto_flotante; //conocer el tipo de conversión
    EditText cantidad; //obtener la cantidad
    InternalView2 view; //enlace para dibujar en el canvas
    Spinner bits; //número de bits seleccionados
    Context CX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hextodec);

        //se realiza el enlace con los elementos del xml
        punto_fijo = (Button) findViewById(R.id.punto_fijo);
        punto_flotante = (Button) findViewById(R.id.punto_flotante);
        cantidad = (EditText) findViewById(R.id.numero);
        view = (InternalView2) findViewById(R.id.view);
        bits = (Spinner) findViewById(R.id.bits);

        CX = this;
        //para obtener el ancho de la pantalla en pixeles
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        view.widthScreen = size.x;

        punto_fijo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    //Se obtiene el número
                    String numero = cantidad.getText().toString();
                    //Se manda llamar el método que desencadena el procedimiento
                    view.obtenerNumero(numero, Integer.parseInt(bits.getSelectedItem().toString()));
                    Toast.makeText(CX, "Conversion realizada", Toast.LENGTH_LONG).show();
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "No valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        punto_flotante.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    //Se obtiene el número
                    String numero = cantidad.getText().toString();
                    if(numero.contains(".")){
                        //en caso de punto flotante el número ingresado no debe contener un punto
                        Toast.makeText(getApplicationContext(), "No valido", Toast.LENGTH_SHORT).show();
                    }else{
                        //Se manda llamar el método que desencadena el procedimiento
                        view.obtenerNumeroFlotante(numero,Integer.parseInt(bits.getSelectedItem().toString()));
                        Toast.makeText(CX, "Conversion realizada", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "No valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
