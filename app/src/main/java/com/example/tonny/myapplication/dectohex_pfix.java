package com.example.tonny.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class dectohex_pfix extends Activity {
    //Graficos
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private Bitmap bmp;

    public class GameView extends SurfaceView {
        public GameView(Context context) {
            super(context);
            gameLoopThread = new GameLoopThread(this);
            holder = getHolder();
            holder.addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    boolean retry = true;
                    gameLoopThread.setRunning(false);
                    while (retry) {
                        try {
                            gameLoopThread.join();
                            retry = false;
                        } catch (InterruptedException e) {
                        }
                    }
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    gameLoopThread.setRunning(true);
                    gameLoopThread.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                                           int width, int height) {
                }
            });
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        }

        @Override
        protected void onDraw(Canvas canvas) {//DIBUJAR EN PANTALLA

        }
    }


    /////////////////////////////////////////////////////
    //###################################################
    /////////////////////////////////////////////////////
    //Declaracion de Widgets
    EditText nDecimal1;
    EditText nBitsFracc1;
    RadioGroup rbSignSel;
    RadioButton rbSigned;
    RadioButton rbUnsigned;
    TextView Rbinario;
    TextView Rhexa;
    Button ClearBtn;
    Button CalcBtn;
    Button backBtn;
    Button nextBtn;
    Button playBtn;
    TextView PasoTXT;

    //Objetos de calculo de animacion
    List<frame> divisiones;

    List<frame> multiplicaciones;
    frame Complemento;

    //Variables del programa
    float Entrada; //Entrada del edit text
    int parteEntera; //Parte entera de Entrada
    int bitsFraccion; //Numero de bits para fraccion (del edit text)
    String parteEnteraBits; //numero binario de parte entera
    int numBitsParteEntera; //Numero de bits del numero binario
    float parteFaccionaria;
    boolean signo; //1 - negativo / 0 . positivo //Signo del número

    String binarioCompleto;


    //Variables Graficas
    private Canvas canvas;
    boolean animando = false;
    int bloque = 0; //0 parte entera : 1 parte decimal : 2 signo
    int paso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dectohex_pfix);

        //Seteando los widgets
        nDecimal1   = (EditText)    findViewById(R.id.nDecimal1);
        nBitsFracc1 = (EditText)    findViewById(R.id.nBitsFracc1);
        rbSignSel   = (RadioGroup)  findViewById(R.id.rbSignSel);
        rbSigned    = (RadioButton) findViewById(R.id.rbSigned);
        rbUnsigned  = (RadioButton) findViewById(R.id.rbUnsigned);
        Rbinario    = (TextView)    findViewById(R.id.Rbinario);
        Rhexa       = (TextView)    findViewById(R.id.Rhexa);
        ClearBtn    = (Button)      findViewById(R.id.ClearBtn);
        CalcBtn     = (Button)      findViewById(R.id.CalcBtn);
        backBtn     = (Button)      findViewById(R.id.backBtn);
        nextBtn     = (Button)      findViewById(R.id.nextBtn);
        playBtn     = (Button)      findViewById(R.id.playBtn);
        PasoTXT     = (TextView)    findViewById(R.id.PasoTXT);

        //Seteando graficos
//        LinearLayout layout = (LinearLayout) findViewById(R.id.lienzo);
//        View mView = new View(this);
//        layout.addView(mView, new LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));

       //Seteando listeners de botones
        ClearBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                nDecimal1.setText("");
                nBitsFracc1.setText("");
                playBtn.setText(">");
                Rbinario.setText("0.0");
                Rhexa.setText("0.0");
                rbSigned.toggle();
                animando = false;
            }
        });

        //Play button
        playBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(!animando) {
                    playBtn.setText("||");
                    animando = true;
                }
                else {
                    playBtn.setText(">");
                    animando = false;
                }
            }
        });

        CalcBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                calcular();
            }
        });

        //para obtener el ancho de la pantalla en pixeles
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
    }

    public void calcular(){
        if (!validar()) //Validando la entrada
            return;


        //Construyendo frames de divisiones
        ///////////////////////////////////
        int pE = parteEntera; // Parte entera
        String resultComp = "";
        divisiones = null;
        divisiones = new ArrayList<frame>();
        while(true){
            if(pE==0){
                System.out.println("Tonny: Numero de objetos en la lista: " + divisiones.size());
                System.out.println("Tonny: Numero binario construido: " + resultComp);
                System.out.println("Tonny: Datos de los objetos: ");

                for(int i=0; i<divisiones.size();i++){
                    divFrame o = (divFrame) divisiones.get(i);
                    System.out.println("Tonny: Objeto  " + i + ": ");
                    System.out.println("\tTonny: Resultado: " + o.Resultado);
                    System.out.println("\tTonny: Dividendo: " + o.divid);
                    System.out.println("\tTonny: nLetras: " + o.nletras);
                    System.out.println("\tTonny: cociente: " + o.cociente);
                }
                break;
            }
            resultComp = (pE%2) + resultComp;
            divFrame f = new divFrame(resultComp,pE);
            divisiones.add((frame) f);
            pE /= 2;
        }
        /////////////////////////////////////////
        /////////////////////////////////////////

        //Calculando frames de divisiones
        /////////////////////////////////
        multiplicaciones = null; //Borrando objeto
        multiplicaciones = new ArrayList<frame>();
        String resultComppF = "";

        float pF = parteFaccionaria;//Parte fraccionaria en memoria

        for(int i=0; i<bitsFraccion; i++) {
            float pF2 = pF;
            pF *= 2;

            System.out.println("Tonny: Multiplicacion: " + pF2 + "\t" + pF);

            if (pF < 1.0) {
                resultComppF += "0";
            } else {
                resultComppF += "1";
                pF -= 1.0;
            }

            mulFrame f = new mulFrame(resultComppF, pF2);
            multiplicaciones.add((frame) f);
        }
        /////////////////////////////////
        /////////////////////////////////

        //Para imprimir resultado o generar frames para el complemento
        if(rbUnsigned.isChecked()){
            Rbinario.setText(resultComp + "." + resultComppF);
            Rhexa.setText("0x" + Integer.toHexString(Integer.parseInt( resultComp+resultComppF,2)).toUpperCase());
        }
        else {
            if (signo) {
                String RC = resultComp + resultComppF;
                System.out.println("Tonny: Resultado completo: " + RC);
                String CA1 = "";

                for (int i = 0; i < RC.length(); i++) {
                    if (RC.charAt(i) == '0')
                        CA1 += '1';
                    else
                        CA1 += '0';
                }

                System.out.println("Tonny: Complemento: " + CA1);

                int itemp = Integer.parseInt(CA1, 2);
                itemp += 1;
                String RC2 = Integer.toBinaryString(itemp);
                RC = String.valueOf(RC2);

                //Reajustar el numero de bits
                for (int i = 0; i < CA1.length() - RC2.length(); i++)
                    RC = '0' + RC;

                System.out.println("Tonny: Complemento +1: " + RC);

                RC = '1' + RC;

                System.out.println("Tonny: +Bit signo: " + RC);

                Rbinario.setText(String.format("%s.%s", RC.substring(0, RC.length() - bitsFraccion), RC.substring(RC.length() - 1 - bitsFraccion, RC.length() - 1)));
                Rhexa.setText("0x" + Integer.toHexString(Integer.parseInt(RC, 2)).toUpperCase());
            }
            else{
                Rbinario.setText("0" + resultComp + "." + resultComppF);
                Rhexa.setText("0x" + Integer.toHexString(Integer.parseInt("0" + resultComp + resultComppF,2)).toUpperCase());
            }
        }
    }

    //Metodo para validacion y obtención de datos
    public boolean validar(){
        //Validando conversion de float
        try{
            Entrada = Float.valueOf(nDecimal1.getText().toString());
        }
        catch(IllegalArgumentException e) {
            Toast.makeText(this, "Número decimal invalido. ¿Más de un punto?", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Extrayendo parte entera
        parteEntera = (int) Entrada;
        if (rbSigned.isChecked()){
            signo = (Entrada<0);
        }
        else{
            signo = false;
            //Si tiene signo el numero se avisa que se toma como positivo
            if(parteEntera<0){
                Toast.makeText(this, "El número introducido tiene signo. Se tomará como positivo.", Toast.LENGTH_SHORT).show();
            }
        }

        //Extrayendo parte entera como positiva
        parteEntera = (parteEntera<0) ? parteEntera*-1 : parteEntera;

        //Convirtiendo parte entera a String de bits
        parteEnteraBits = Integer.toBinaryString(parteEntera);
        numBitsParteEntera = parteEnteraBits.length();


        System.out.print("Tonny: Binario de parte entera: ");
        System.out.println(parteEnteraBits);
        System.out.print("Tonny: Numero de bits: ");
        System.out.println(Integer.valueOf(numBitsParteEntera).toString());

        //Validando numero de bits para fraccion
        try{
            bitsFraccion =  Integer.valueOf(nBitsFracc1.getText().toString());
        }
        catch(IllegalArgumentException e) {
            Toast.makeText(this, "Número invalido de bits. ¿Caracter inválido?", Toast.LENGTH_SHORT).show();
            nBitsFracc1.setText("");
            return false;
        }

        //Validando cantidad de bits
        if (bitsFraccion>63){
            Toast.makeText(this,"Son demaciados bits para la parte fraccionaria. Aplicación limitada a 64 bits.", Toast.LENGTH_LONG).show();
            return false;
        }

        int bits = bitsFraccion+numBitsParteEntera+((signo) ? 1 : 0);

        if(bits>64){
            Toast.makeText(this,"Son demaciados bits para el número completo. Parte entera muy grande o muchos bits para parte fraccionaria. El resultado está limitado a 64 bits de salida. " + bits + " bits totales requeridos.", Toast.LENGTH_LONG).show();
            return false;
        }

        parteFaccionaria = Entrada % 1;
        System.out.println("Tonny: Parte fraccionaria " + parteFaccionaria);


        return true;
    }

    void printT (String m){
        System.out.print("Tonny: " + m);
    }
}


