package com.example.tonny.myapplication;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class dectohex_pflo extends Activity {

    //Declaracion de Widgets
    EditText nDecimal1;
    RadioGroup rbBitSel;
    RadioButton rb32b;
    RadioButton rb64b;
    TextView Rhex;
    Button ClearBtn;
    Button CalcBtn;
    Button backBtn;
    Button nextBtn;
    Button playBtn;
    binToHexAnimflo SV1;

//    EditText nDecimal1   = (EditText)    findViewById(R.id.nDecimal1);
//    EditText nBitsFracc1 = (EditText)    findViewById(R.id.nBitsFracc1);
//    RadioGroup rbSignSel   = (RadioGroup)  findViewById(R.id.rbSignSel);
//    RadioButton rbSigned    = (RadioButton) findViewById(R.id.rbSigned);
//    RadioButton rbUnsigned  = (RadioButton) findViewById(R.id.rbUnsigned);
//    TextView Rbinario    = (TextView)    findViewById(R.id.Rbinario);
//    TextView Rhexa       = (TextView)    findViewById(R.id.Rhexa);
//    Button ClearBtn    = (Button)      findViewById(R.id.ClearBtn);
//    Button CalcBtn     = (Button)      findViewById(R.id.CalcBtn);
//    Button backBtn     = (Button)      findViewById(R.id.backBtn);
//    Button nextBtn     = (Button)      findViewById(R.id.nextBtn);
//    Button playBtn     = (Button)      findViewById(R.id.playBtn);
//    TextView PasoTXT     = (TextView)    findViewById(R.id.PasoTXT);
//    MySurfaceView SV1      = (MySurfaceView) findViewById(R.id.lienzo);


    //Variables del programa
    float Entrada; //Entrada del edit text
    int parteEntera;
    String parteEnteraString; //numero binario de parte entera
    int numBitsParteEntera; //Numero de bits del numero binario
    float parteFaccionaria;
    boolean signo; //1 - negativo / 0 . positivo //Signo del número
    boolean bitsFormato; // false 32 bits, true 64 bits

    String binarioCompleto;


    //Variables Graficas
    private Canvas canvas;
    boolean animando = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dectohex_pflo);

        //Seteando los widgets
        nDecimal1   = (EditText)    findViewById(R.id.numeroDecFlo);
        rbBitSel    = (RadioGroup)  findViewById(R.id.bitSel);
        rb32b       = (RadioButton) findViewById(R.id.b32);
        rb64b       = (RadioButton) findViewById(R.id.b64);
        Rhex        = (TextView)    findViewById(R.id.result2);
        ClearBtn    = (Button)      findViewById(R.id.ClearBtn2);
        CalcBtn     = (Button)      findViewById(R.id.CalcBtn2);
        backBtn     = (Button)      findViewById(R.id.backBtn2);
        nextBtn     = (Button)      findViewById(R.id.nextBtn2);
        playBtn     = (Button)      findViewById(R.id.playBtn2);
        SV1         = (binToHexAnimflo) findViewById(R.id.lienzo2);

    }

    public void resetOrClear(View v) {  //LISTENER1
        nDecimal1.setText("");
        playBtn.setText(">");
        Rhex.setText("0x0");
        rb32b.toggle();
        animando = false;
    }

    public void playAnimation(View v){ //LISTENER2
        if(!animando) {
            playBtn.setText("||");
            animando = true;
            SV1.setAnimation(animando);
        }
        else {
            playBtn.setText(">");
            animando = false;
            SV1.setAnimation(animando);
        }
    }

    public void calc(View v){
        calcular();
    }

    public void backFrame(View v){
        SV1.backFrame();
    }

    public void nextFrame(View v){
        SV1.nextFrame();
    }


    public void calcular(){  //LISTENER3
        if (!validar()) //Validando la entrada
            return;

        //Datos de parte entera
        ///////////////////////////////////
        int pE = parteEntera; // Parte entera
        String resultComp = "";
        SV1.deleteFirstStep();
        while(true){
            if(pE==0){
                System.out.println("Tonny: Numero de objetos en la lista: " + SV1.binEnt.size());
                System.out.println("Tonny: Numero binario construido: " + resultComp);
                System.out.println("Tonny: Datos de los objetos: ");

                for(int i=0; i<SV1.binEnt.size();i++){
                    String[] o = SV1.binEnt.get(i);
                    System.out.println("Tonny: Dato  " + i + ": ");
                    System.out.println("\tTonny: Resultado: " + o[0]);
                    System.out.println("\tTonny: Dividendo: " + o[1]);
                    System.out.println("\tTonny: cociente: "  + o[2]);
                    System.out.println("\tTonny: residuo: "   + o[3]);
                }
                break;
            }
            int res = pE % 2;
            int pE2 = pE / 2;
            SV1.addFistStep(resultComp, "" + pE, "" + pE2, "" + res);
            resultComp = res + resultComp;
            pE = pE2;
        }

        //Datos de parte fraccionaria
        /////////////////////////////
        int bitsFraction = (rb32b.isChecked()) ? 23 : 52;
        bitsFraction -= SV1.binEnt.size();
        SV1.deleteSecondStep();
        String resultComp2 = "";

        System.out.println("\nInit parte fraccionaria...");

        float pF = parteFaccionaria;
        for(int i=0 ; i<bitsFraction ; i++){
            float dpF = pF*2;
            if(i==0){

            }
            else{
                resultComp2 = resultComp2 + SV1.binFrac.get(i-1)[3];
            }


            SV1.addSecondStep(resultComp2, "" + pF , "" + dpF);

            pF = (dpF<1) ? dpF: dpF-1;
            String[] o = SV1.binFrac.get(i);

            System.out.println("\tTonny: Digito: " + (i+1));
            System.out.println("\tTonny: Resultado: " + o[0]);
            System.out.println("\tTonny: Entrada: " + o[1]);
            System.out.println("\tTonny: Doble: " + o[2]);
            System.out.println("\tTonny: Bit: " + o[3] + "\n");
        }




        //Configuraciones del thread
        SV1.setFPS(30); //Animacion rapida
        SV1.setData(nDecimal1.getText().toString(), (rb32b.isChecked()) ? "32":"64" , (Entrada<0)); //Seteando resultados y entradas
        SV1.setRdy(); //Activar graficos
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
        if (rb32b.isChecked()){
            bitsFormato = false;
        }
        else{
            bitsFormato = true;
        }

        //Extrayendo parte entera como positiva
        parteEntera = (parteEntera<0) ? parteEntera*-1 : parteEntera;

        //Convirtiendo parte entera a String de bits
        parteEnteraString = Integer.toBinaryString(parteEntera);
        numBitsParteEntera = parteEnteraString.length();


        System.out.print("Tonny: Binario de parte entera: ");
        System.out.println(parteEnteraString);
        System.out.print("Tonny: Numero de bits: ");
        System.out.println(Integer.valueOf(numBitsParteEntera).toString());

        //Calculando parte fraccionaria
        parteFaccionaria = Entrada % 1;
        parteFaccionaria = (parteFaccionaria<0) ? parteFaccionaria*-1 : parteFaccionaria;
        System.out.println("Tonny: Parte fraccionaria " + parteFaccionaria);
        return true;
    }

    @Override
    protected void onPause () {
        super.onPause();
        SV1.StopThread (); // Es para evitar que truene "deteniendo el hilo"
    }


}


