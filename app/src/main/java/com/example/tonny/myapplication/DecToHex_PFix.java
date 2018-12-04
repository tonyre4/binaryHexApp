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

public class DecToHex_PFix extends AppCompatActivity{
    InternalView elCanvas;
    EditText cantidad;
    Button fijo;
    Button flotante;
    Spinner spinner_bits;
    Context CX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dectohex_pfix);

        elCanvas = (InternalView) findViewById(R.id.view);
        cantidad = (EditText) findViewById(R.id.numero);
        fijo = (Button) findViewById(R.id.btn_fijo);
        flotante = (Button) findViewById(R.id.btn_flotante);
        spinner_bits = (Spinner) findViewById(R.id.spinner_bits);
        CX = this;

        //para obtener el ancho de la pantalla en pixeles
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        elCanvas.widthScreen = size.x;
        fijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!cantidad.getText().toString().equals("")){

                    try {
                        //Se obtiene el n�mero
                        String numero = cantidad.getText().toString();
                        elCanvas.obtenerNumero(numero, Integer.parseInt(spinner_bits.getSelectedItem().toString()), "puntofijo");
                        Toast.makeText(CX, "Conversion realizada", Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        Toast.makeText(CX, "Numero no valido", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(CX, "Ingresa un numero", Toast.LENGTH_LONG).show();
                }

            }
        });

        flotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cantidad.getText().toString().equals("")){
                    try {
                        String numero = cantidad.getText().toString();
                        elCanvas.obtenerNumero(numero, Integer.parseInt(spinner_bits.getSelectedItem().toString()), "puntoFlotante");
                        Toast.makeText(CX, "Conversion realizada", Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        Toast.makeText(CX, "Numero no valido", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(CX, "Ingresa un numero", Toast.LENGTH_LONG).show();
                }

            }
        });
    }



}
