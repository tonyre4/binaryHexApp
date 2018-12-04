package com.example.tonny.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Stack;

public class InternalView2 extends View {
    public int widthScreen = 0; //contrar el ancho de la pantalla

    private String Hex; //string del número a convertir
    private String cadenaBinaria; //cadena binaria resultante del hexadecimal
    private String signed_unsigned_or_error; //saber el signo de la cadena

    String Log=""; //la variable contiene cada paso del procedimiento

    private Paint resultados = new Paint(); //estilo de los resultados

    public InternalView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //Metodos generados por la clase
    public InternalView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public InternalView2(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }

    //método que se encarga de mostrar el procedimiento en el canvas
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        String periodos[] = this.Log.split("\n"); //saber cuantos pasos son
        int acum = 0; //para llevar el control de la altura
        int tamPeriodos = 0; //variable utilizada para salir del ciclo

        int textSize = widthScreen/16; //el tamaño del texto va de acuerdo al tamaño de la pantalla
        int numberOfW = 1;

        resultados.setTextSize(textSize); //se asigna el tamaño del texto a la variable de estilo
        resultados.setColor(Color.BLACK); //se asigna el color del texto a la variable de estilo

        //ciclo para poner todos los pasos en el canvas
        while(tamPeriodos < periodos.length){
            String periodo = periodos[tamPeriodos];

            if(periodo.length() > numberOfW){
                numberOfW = periodo.length(); //controla el ancho del canvas
            }
            //cada paso se agrega al canvas
            canvas.drawText(periodo, 0, textSize *tamPeriodos+ textSize, resultados);

            acum = textSize *tamPeriodos+textSize; //la altura va aumentando de acuerdo al número de líneas
            tamPeriodos++; //se aumenta la variable para salir del ciclo
        }

        //se agrega el ancho y la altura para modificar el layout
        setLayoutParams(new LinearLayout.LayoutParams(numberOfW * textSize, acum + textSize));
    }

    //mostrar los mensajes correspondientes
    private void toast(String mensaje){
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    //obtiene el número cuando es la conversión de punto fijo
    public void obtenerNumero(String hexa, int bits) {
        this.Log = ""; //se reinicia la variable que guarda los pasos
        this.Hex = hexa; //se obtiene la cadena
        this.signedOrUnsigned();
    }

    //se obtiene el número cuando es la conversión de punto flotante
    public void obtenerNumeroFlotante(String hexa, int bits) {
        this.Log = ""; //se reinicia la variable que guarda los pasos
        this.Hex = hexa; //se obtiene la cadena
        descomponerBinario(this.Hex, bits); //se ejecuta el método para convertir el hexadecimal a binario
    }

    //descomponer hexadecimal a binario
    public void descomponerBinario(String hexa, int bits){
        this.Log += "***Conversion real a aritmetica - Hexadecimal a Decimal - Punto flotante***"+ "\n";
        this.Log += "# Numero a convertir: " + this.Hex+ "\n";
        this.Log += "# Descomponer hexadecimal a Binario #" + "\n";
        String binario = "";
        int start = 0;
        if(hexa.contains("-")){
            start = 1;
        }
        for(int i=start;i<hexa.length();i++){
            String descomponer = "";
            if(hexa.charAt(i)=='0'){
                binario += "0000";
                descomponer = "0000";
            }else if(hexa.charAt(i)=='1'){
                binario += "0001";
                descomponer = "0001";
            } else if (hexa.charAt(i) == '2'){
                binario += "0010";
                descomponer = "0010";
            }else if(hexa.charAt(i)=='3'){
                binario += "0011";
                descomponer = "0011";
            }else if(hexa.charAt(i)=='4'){
                binario += "0100";
                descomponer = "0100";
            }else if(hexa.charAt(i)=='5'){
                binario += "0101";
                descomponer = "0101";
            } else if (hexa.charAt(i) == '6'){
                binario += "0110";
                descomponer = "0110";
            }else if(hexa.charAt(i)=='7'){
                binario += "0111";
                descomponer = "0111";
            }else if(hexa.charAt(i)=='8'){
                binario += "1000";
                descomponer = "1000";
            }else if(hexa.charAt(i)=='9'){
                binario += "1001";
                descomponer = "1001";
            }else if (hexa.charAt(i) == 'a' || hexa.charAt(i)=='A'){
                binario += "1010";
                descomponer = "1010";
            }else if(hexa.charAt(i) == 'b' || hexa.charAt(i)=='B'){
                binario += "1011";
                descomponer = "1011";
            }else if (hexa.charAt(i) == 'c' || hexa.charAt(i)=='C'){
                binario += "1100";
                descomponer = "1100";
            }else if(hexa.charAt(i) == 'd' || hexa.charAt(i)=='D'){
                binario += "1101";
                descomponer = "1101";
            }else if (hexa.charAt(i) == 'e' || hexa.charAt(i)=='E'){
                binario += "1110";
                descomponer = "1110";
            }else if(hexa.charAt(i) == 'f' || hexa.charAt(i)=='F'){
                binario += "1111";
                descomponer = "1111";
            }

            this.Log += hexa.charAt(i) + " -> " + descomponer + "\n";
        }
        //se ejecuta el método para agregar los ceros que hagan falta
        agregarCeros(binario, bits);
    }

    //a la cadena binaria se le agregan los ceros faltantes de acuerdo a sus bits, 32 o 64
    public void agregarCeros(String cadena, int bits){
        if(bits==32){
            for(int i=cadena.length()-1;i<31;i++){
                cadena+="0";
            }
        }else if(bits==64){
            for(int i=cadena.length()-1;i<63;i++){
                cadena+="0";
            }
        }
        this.cadenaBinaria = cadena;
        this.Log += "# Completar bits #" + "\n";
        this.Log += this.cadenaBinaria + "\n";
        realizarConversion(bits); //se ejecuta el método para realizar la conversión
    }

    //aqui se encuentran todos los pasos para realizar la conversión de hexadecimal a decimal
    //de punto flotante
    public void realizarConversion(int bits){
        boolean signo = false;
        //el primer bit de la cadena nos da si el número es positivo o negativo
        if(this.cadenaBinaria.charAt(0)=='1'){
            this.Log += this.cadenaBinaria.charAt(0) + " -> Negativo" + "\n";
            signo = true;
        }else{
            this.Log += this.cadenaBinaria.charAt(0) + " -> Positivo" + "\n";
        }
        //EXPONENTE
        int exponente = 0;
        String mantisa = "";
        //para el caso de 32 bits, 8 bits son necesarios para calcular el exponente
        if(bits==32){
            String ex = this.cadenaBinaria.substring(1,9); //se obtienen los 8 bits
            for(int i=ex.length()-1, e=0;i>=0;i--,e++){
                if(ex.charAt(i)=='1'){
                    exponente += (int) Math.pow(2, e); //se calcula el resultado del exponente
                }
            }
            this.Log += "# Obtener exponente #" + "\n";
            this.Log += ex + " -> " + exponente + "\n";
            this.Log += " ## Realizar resta ##" + "\n";
            int exponenteAux = exponente - 127; //se realiza la resta para saber donde poner el punto
            this.Log += exponente + " - 127 = " + exponenteAux + "\n";
            exponente = exponenteAux;
            //se obtiene la mantisa
            mantisa = this.cadenaBinaria.substring(9,this.cadenaBinaria.length());
        }else if(bits==64){
            String ex = this.cadenaBinaria.substring(1,12); //se obtiene la parte del exponente, 11 bits
            for(int i=ex.length()-1, e=0;i>=0;i--,e++){
                if(ex.charAt(i)=='1'){
                    exponente += (int) Math.pow(2, e); //se calcula el resultado del exponente
                }
            }
            this.Log += "# Obtener exponente #" + "\n";
            this.Log += ex + " -> " + exponente + "\n";
            this.Log += " ## Realizar resta ##" + "\n";
            int exponenteAux = exponente - 1023; //se realiza la resta para saber donde poner el punto
            this.Log += exponente + " - 1023 = " + exponenteAux + "\n";
            exponente = exponenteAux;
            //se obtiene la mantisa
            mantisa = this.cadenaBinaria.substring(12,this.cadenaBinaria.length());
        }
        this.Log += "# Mantisa #" + "\n";
        this.Log += mantisa + "\n";

        String parteEntera = ""; //parte entera
        String parteDecimal = ""; //parte decimal

        if(exponente!=128 && exponente!=1024){
            //casos especiales del exponente
            if(exponente<0 && exponente!=-127 && exponente!=-1023){
                parteEntera = "1"; //parte entera
                parteDecimal = mantisa.substring(0,mantisa.length()); //parte decimal
            }else if(exponente>0){
                parteEntera = "1"+mantisa.substring(0,exponente); //parte entera
                parteDecimal = mantisa.substring(exponente,mantisa.length()); //parte decimal
            }else if(exponente==-127 || exponente==-1023) {
                parteEntera = "0"; //parte entera
                parteDecimal = mantisa.substring(0, mantisa.length()); //parte decimal
            }
            this.Log += "# Parte Entera #" + "\n";

            //se calcula la parte entera
            double sumaEntero = 0;
            for(int s=parteEntera.length()-1, e=0;s>=0;s--,e++){
                String caracter = parteEntera.charAt(s)+" -> ";
                String exp = "2^"+e+" = ";

                double valExp = Math.pow(2,e);
                double valCarac = 0;

                if(parteEntera.charAt(s)=='1'){
                    valCarac = valExp;
                }

                sumaEntero += valCarac;
                this.Log += caracter + exp + valCarac + "\n";
            }

            this.Log += parteEntera + " -> " + sumaEntero + "\n";

            this.Log += "# Parte Decimal #" + "\n";

            //se calcula la parte decimal
            double sumaDecimal = 0;
            for(int s=0, e=-1;s<parteDecimal.length();s++,e--){
                String caracter = parteDecimal.charAt(s)+" -> ";
                String exp = "2^"+e+" = ";

                double valExp = Math.pow(2,e);
                double valCarac = 0;

                if(parteDecimal.charAt(s)=='1'){
                    valCarac = valExp;
                }

                sumaDecimal += valCarac;
                this.Log += caracter + exp + valCarac + "\n";
            }

            this.Log += parteDecimal + " -> " + sumaDecimal + "\n";

            this.Log += "\n" + "### RESULTADO ###" + "\n";
            double res = sumaEntero + sumaDecimal;
            if(exponente<0 || exponente==127 || exponente==1023){
                res =  res * (Math.pow(2,exponente));
            }

            if(signo){
                res = res * -1; //si el bit de signo era uno el resultado es multiplicado por -1
            }
            this.Log += res + "\n"; //se agrega el resultado
        }else if(exponente==128 || exponente==1024){
            this.Log += "\n"+"***INFINITY***"+"\n";
        }else{
            this.Log += "\n"+"***NAN***"+"\n";
        }

        invalidate();
    }


    public void signedOrUnsigned() {
        if (this.Hex.indexOf("-") != -1) {
            if (this.Hex.indexOf("-") == 0)
                this.signed_unsigned_or_error = "-";
            else
                this.signed_unsigned_or_error = "error";
        }else {
            this.signed_unsigned_or_error = "+";
        }

        perform(); //se manda llamar el método para realizar el procedimiento
    }


    public void perform() {
        boolean isDecSigned = false;
        if(!this.signed_unsigned_or_error.equals("error")){
            this.Log += "***Conversion real a aritmetica - Hexadecimal a Decimal ***"+ "\n";
            this.Log += "# Numero a convertir: " + this.Hex+ "\n";
            if (this.signed_unsigned_or_error.equals("-")) {
                this.Log += "# Numero negativo #"+ "\n";
                isDecSigned = true; //saber si es un número negativo
            } else if(this.signed_unsigned_or_error.equals("+"))
                this.Log += "# Numero positivo # "+ "\n";
            String x = "";

            this.Log += "# Conversión de los caracteres "+ "\n";

            int encontrarPunto = this.Hex.indexOf("."); //saber si es un número con punto

            double sumaResultado = 0;
            double sumaResultado2 = 0;
            int hasta, hastaP2 = 0;
            String cadena, cadena2 = "";
            //obtener la parte entera
            if(encontrarPunto == -1){
                hasta = this.Hex.length()-1;
                cadena = this.Hex;

                //cada digito se multiplica por 16 elavado a la posición en la que se encuentre
                //y la posición empieza en 0 y se va incrementando
                for(int s=hasta, e=0;s>=0;s--,e++){
                    String caracter = cadena.charAt(s)+" -> ";
                    String exp = "16^"+e+" -> ";

                    double valExp = Math.pow(16,e);
                    double valCarac = 0;

                    if(this.Hex.charAt(s)=='A' || this.Hex.charAt(s)=='a'){
                        valCarac = valExp * 10;
                    }else if(this.Hex.charAt(s)=='B' || this.Hex.charAt(s)=='b'){
                        valCarac = valExp * 11;
                    }else if(this.Hex.charAt(s)=='C' || this.Hex.charAt(s)=='c'){
                        valCarac = valExp * 12;
                    }else if(this.Hex.charAt(s)=='D' || this.Hex.charAt(s)=='d'){
                        valCarac = valExp * 13;
                    }else if(this.Hex.charAt(s)=='E' || this.Hex.charAt(s)=='e'){
                        valCarac = valExp * 14;
                    }else if(this.Hex.charAt(s)=='F' || this.Hex.charAt(s)=='f'){
                        valCarac = valExp * 15;
                    }else{
                        String auxC = ""+this.Hex.charAt(s);
                        valCarac = valExp * Double.parseDouble(auxC);
                    }
                    sumaResultado += valCarac;
                    this.Log += caracter + exp + valExp + " x " + this.Hex.charAt(s) + " = " + valCarac + "\n";
                    if(isDecSigned && s==1)
                        break;
                }
            }else{
                hastaP2 = this.Hex.length();
                this.Log += "# Parte del punto a la izquierda "+ "\n";

                //si se encontró punto se calcula la parte decimal
                ////cada digito se multiplica por 16 elavado a la posición en la que se encuentre
                //y la posición empieza en -1 y se va decrementando
                for(int s=encontrarPunto-1, e=0;s>=0;s--,e++){
                    String caracter = this.Hex.charAt(s)+" -> ";
                    String exp = "16^"+e+" -> ";

                    double valExp = Math.pow(16,e);
                    double valCarac = 0;

                    if(this.Hex.charAt(s)=='A' || this.Hex.charAt(s)=='a'){
                        valCarac = valExp * 10;
                    }else if(this.Hex.charAt(s)=='B' || this.Hex.charAt(s)=='b'){
                        valCarac = valExp * 11;
                    }else if(this.Hex.charAt(s)=='C' || this.Hex.charAt(s)=='c'){
                        valCarac = valExp * 12;
                    }else if(this.Hex.charAt(s)=='D' || this.Hex.charAt(s)=='d'){
                        valCarac = valExp * 13;
                    }else if(this.Hex.charAt(s)=='E' || this.Hex.charAt(s)=='e'){
                        valCarac = valExp * 14;
                    }else if(this.Hex.charAt(s)=='F' || this.Hex.charAt(s)=='f'){
                        valCarac = valExp * 15;
                    }else{
                        String auxC = ""+this.Hex.charAt(s);
                        valCarac = valExp * Double.parseDouble(auxC);
                    }
                    sumaResultado += valCarac;
                    this.Log += caracter + exp + valExp + " x " + this.Hex.charAt(s) + " = " + valCarac + "\n";
                    if(isDecSigned && s==1)
                        break;
                }

            }

            //si el numero ingresado es entero, es decir que no se encontró punto se ejecuta esta forma
            //el digito se multiplica por 16 elevado a la posicion en la que se encuentre
            //donde posición empieza en 0 y se va incrementando
            if(encontrarPunto != -1){
                this.Log += "# Parte del punto a la derecha "+ "\n";
                for(int s=encontrarPunto+1, e=-1;s<hastaP2;s++,e--){
                    String caracter = this.Hex.charAt(s)+" -> ";
                    String exp = "16^"+e+" -> ";

                    double valExp = Math.pow(16,e);
                    double valCarac = 0;

                    if(this.Hex.charAt(s)=='A' || this.Hex.charAt(s)=='a'){
                        valCarac = valExp * 10;
                    }else if(this.Hex.charAt(s)=='B' || this.Hex.charAt(s)=='b'){
                        valCarac = valExp * 11;
                    }else if(this.Hex.charAt(s)=='C' || this.Hex.charAt(s)=='c'){
                        valCarac = valExp * 12;
                    }else if(this.Hex.charAt(s)=='D' || this.Hex.charAt(s)=='d'){
                        valCarac = valExp * 13;
                    }else if(this.Hex.charAt(s)=='E' || this.Hex.charAt(s)=='e'){
                        valCarac = valExp * 14;
                    }else if(this.Hex.charAt(s)=='F' || this.Hex.charAt(s)=='f'){
                        valCarac = valExp * 15;
                    }else{
                        String auxC = ""+this.Hex.charAt(s);
                        valCarac = valExp * Double.parseDouble(auxC);
                    }
                    sumaResultado2 += valCarac;
                    this.Log += caracter + exp + valExp + " x " + this.Hex.charAt(s) + " = " + valCarac + "\n";
                }
            }
            //si se habia un signo negativo se realiza la multiplicación por menos 1
            if(isDecSigned){
                sumaResultado = (sumaResultado+sumaResultado2) * -1;
            }else{
                sumaResultado = sumaResultado + sumaResultado2;
            }
            this.Log += "RESULTADO: " + sumaResultado + "\n";

        }else{
            this.Log += "Error! Signo en posicion incorrecta";
        }
        invalidate();
    }

}