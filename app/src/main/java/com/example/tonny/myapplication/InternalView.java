package com.example.tonny.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Stack;

public class InternalView extends View {
    public int widthScreen = 0; //ancho de la pantalla


    String Log=""; // this variable contains each step with its \n
	private Paint signo = new Paint(); //linea de la raiz cuadrada (pa=signo)
    private Paint resultados = new Paint(); //resultados (pat=exponente)
	public InternalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	//métodos aregados para la clase
	public InternalView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public InternalView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}


    @Override
	protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        String periodos[] = this.Log.split("\n");
        int acum = 0;
        //Algoritmo de c�lculo y dibujado
        int tamPeriodos = 0;

        //Propiedades del PAINT (tamaño de texto y color)
        //int textSize = 30;
        int textSize = widthScreen / 16;
        int numberOfW = 1;
        //se le da estilo al texto que se va a dibujar en el canvas
        resultados.setTextSize(textSize); //el tamaño del texto de acuerdo a la dimensión de la pantalla
        resultados.setColor(Color.BLACK); //el color del texto
        while(tamPeriodos < periodos.length){
            String periodo = periodos[tamPeriodos];

            //tomar la linea de mayor tamaño para definir tamaño de canvas
            if(periodo.length() > numberOfW){
                numberOfW = periodo.length();
            }

            //se dibuja en el canvas los pasos
            canvas.drawText(periodo, 0, textSize * tamPeriodos + textSize, resultados);
            acum = textSize *tamPeriodos+textSize; //la altura se adapta de acuerdo al número de líneas
            tamPeriodos++; //variable para salir del ciclo

        }

        //Definir tamaño de canvas
        setLayoutParams(new LinearLayout.LayoutParams(numberOfW * textSize, acum + textSize));

        //android.util.Log.d("ACUM::", ""+acum);
    }

    //mostrar los mensajes adecuados durante el desarrollo de la aplicación
    private void toast(String mensaje){
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    public void obtenerNumero(String dec, int bits, String tipo) {
	    //se ejecutan los métodos de acuerdo a la opción seleccionada
	    if(tipo.equals("puntofijo")){
            FixedPointToHex x = new FixedPointToHex(dec, bits);
            x.perform();
            this.Log = x.Log;
        }else if(tipo.equals("puntoFlotante")){
            FloatPointToHex x = new FloatPointToHex(dec, bits);
            x.perform();
            this.Log = x.Log;
        }
    }

}
