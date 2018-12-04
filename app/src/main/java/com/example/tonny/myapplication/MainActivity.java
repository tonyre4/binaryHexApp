package com.example.tonny.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button DecToHex_PFix_Button; //conversión de decimal a hexadecimal
    Button DecToHex_PFlo_Button; //conversión de decimal a hexadecimal
    Button HexToDec_Button; //conversión de hexadecimal a decimal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DecToHex_PFix_Button = (Button) findViewById(R.id.decimal); //se enlaza este elemento con el elemento del xml
        DecToHex_PFlo_Button = (Button) findViewById(R.id.decimal2); //se enlaza este elemento con el elemento del xml
        HexToDec_Button = (Button) findViewById(R.id.hexadecimal); //se enlaza este elemento con el elemento del xml


        //Alert dialogo con la informacion de la aplicacion
        /*AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
        alertDialog2.setTitle("Información de la aplicación");
        alertDialog2.setMessage("Proyecto grupal 2: Desglose de Conversión de Real a Aritmética de Punto Fijo y Punto Flotante de 32 y 64 bits (y VICEVERSA) - Usando CANVAS \n\n- Aplicacion creada por: Jose Antonio Molina De la Fuente, Angela Judith Carrizales Perez y Gabriela Robles Rodríguez\n\n-Funcionamiento: \n Al abrir la aplicación se mostrará el menú de esta, son dos botones los cuales corresponden a la conversión que desee realizar; Decimal a Hexadecimal o Hexadecimal a Decimal.\n Seleccione la conversión que desee realizar dando tap en el boton. \nEn la interfaz de la conversión ingrese el número en el campo de texto, posteriormente seleccione la presición (32 o 64 bits) en el Spinner que se encuentra a lado derecho y finalmente de tap en la conversión que desee realizar (punto fijo o punto flotante). \n Al dar clic en cualquiera de los dos botones mencionados, se escribirá en el canvas el procedimiento de la conversión. \n Es posible realizar scroll en el Canvas en caso de que la letra no se alcance a ver en la pantalla.");
        alertDialog2.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog2.create();

        dialog.show();*/

        DecToHex_PFix_Button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //se crea un Intent que será lanzado a la clase de Decimal a hexadecimal
                Intent intent = new Intent(MainActivity.this, dectohex_pfix.class);
                //se abre la interfaz de esta clase
                startActivityForResult(intent,0);
            }
        });

        DecToHex_PFlo_Button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //se crea un Intent que será lanzado a la clase de Decimal a hexadecimal
                Intent intent = new Intent(MainActivity.this, voidClass.class);
                //se abre la interfaz de esta clase
                startActivityForResult(intent,0);
            }
        });

        HexToDec_Button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //se crea un Intent que será lanzado a la clase de Hexadecimal a decimal
                Intent intent = new Intent(MainActivity.this, voidClass.class);
                //se abre la interfaz de esta clase
                startActivityForResult(intent,0);
            }
        });
    }
}
