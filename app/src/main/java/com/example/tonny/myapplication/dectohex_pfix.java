package com.example.tonny.myapplication;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.EditText;

public class dectohex_pfix extends AppCompatActivity {

    EditText StrnDecimal;
    EditText StrbitsFracc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dectohex_pfix);

        //para obtener el ancho de la pantalla en pixeles
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

    }
}


