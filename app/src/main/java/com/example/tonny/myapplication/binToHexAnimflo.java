package com.example.tonny.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class binToHexAnimflo extends SurfaceView implements
		SurfaceHolder.Callback {

	//Objetos de calculo de animacion
	List<frame> divisiones;
	List<frame> multiplicaciones;
	int foto = 0;
	boolean animate = false;
	boolean ready = false;
	final int filas = 6;
	int totalframes = 0;
	int missingframes = 0;

	List<String[]> binEnt;  //resultado(binario)|dividendo(entero)|cociente(entero)|residuo(entero)
	List<String[]> binFrac; //resultado(binario)|entrada(float)|doble(float)|salida(entero)
	List<String[]> binExp;  //resultado(binario)|dividendo(entero)|cociente(entero)|residuo(entero)

	boolean calcExpOnce;

	String formatoBits;


	String Decimal;
	String bitsFracc;
	String finalR;
	String finalRHex;
	boolean signo;
	boolean consigno;

	private MySurfaceThread2 thread;
	private Paint paint;// = new Paint(Paint.ANTI_ALIAS_FLAG);



	private Bitmap bmp;
	private SurfaceHolder holder;
	private MySurfaceThread gameLoopThread;



	public void StopThread () {
		thread.setRunning(false);
	}


	//@Override
	//protected void onDraw(Canvas canvas) {
	public void draw(Canvas canvas) {
		super.draw(canvas);

		//Pintar fondo
		paint.setColor(Color.LTGRAY);
		canvas.drawPaint(paint);
		if (!ready) //Si los datos no estan listos no dibujar nada
			return;

		if(animate){ //Si la animacion esta encendida aumentar el frame
			//setFPS(0.4);
			if(missingframes==45){
				nextFrame();
				missingframes=0;
			}
			else{
				missingframes++;
			}
		}

		int an = getWidth();//OBTENER ANCHO
		int al = getHeight();//OBTENER ALTO

		int anMarg = (int) (an*0.01);

		//Tamaño de fuente
		int rowsize = al/filas;
		int fontsize = (int) (rowsize*0.8);
		paint.setTextSize(fontsize);
		paint.setColor(Color.BLACK);

		//Dibujar frame de entradas
		if (foto == 0){
			canvas.drawText("Entradas: ", anMarg, 1*rowsize,paint);
			canvas.drawText("Número: " + Decimal,50+anMarg,3*rowsize, paint);
			canvas.drawText("Formato: " + formatoBits + " bits",50+anMarg,4*rowsize, paint);
			if(signo){
				paint.setColor(Color.RED);
				canvas.drawText("Negativo",50+anMarg,5*rowsize, paint);
			}
			else{
				paint.setColor(Color.BLUE);
				canvas.drawText("Positivo",50+anMarg,5*rowsize, paint);
			}
			return;
		}

		if (foto==1){
			canvas.drawText("MSB del resultado: ", anMarg, 1*rowsize,paint);

			if(signo){
				paint.setColor(Color.RED);
				canvas.drawText("1",an/2,3*rowsize, paint);
				canvas.drawText("Negativo",50+anMarg,5*rowsize, paint);
			}
			else{
				paint.setColor(Color.BLUE);
				canvas.drawText("0",an/2,3*rowsize, paint);
				canvas.drawText("Positivo",50+anMarg,5*rowsize, paint);
			}
			return;
		}

		int foto2 = foto-2;
		if(foto2<binEnt.size()){
            //Dibujar Constantes
            String[] o = binEnt.get(foto2);
			//System.out.println(o[0] + "/" + o[1] + "/" + o[2] + "/" + o[3]);
            paint.setTextSize(fontsize/2);
            paint.setColor(Color.BLACK);
            canvas.drawText("Calculo de número decimal a binario:" , anMarg , rowsize/2 ,paint);
            canvas.drawText("Paso " + (foto2+1) + ":" , anMarg, rowsize+5, paint);

            //Dibujar resultado
            String t = "Resultado actual: ";
            canvas.drawText(t, anMarg, rowsize*6, paint);
            float z = paint.measureText(t);
			paint.setColor(Color.RED);
			canvas.drawText( "" + o[3], anMarg+z, rowsize*6, paint);

            if (!(o[0].equals(""))){
				paint.setColor(Color.BLACK);
				z += paint.measureText(o[3]);
                canvas.drawText( o[0], anMarg+z, rowsize*6, paint);
            }


            //Dibujando division
            int tempf = fontsize;
            int diag = 60;
            paint.setTextSize(tempf);
            paint.setColor(Color.BLACK);
            z = paint.measureText("2");
            canvas.drawText("2", anMarg, rowsize*4,paint);
            canvas.drawLine(anMarg+z,rowsize*4,anMarg+z+diag,rowsize*4-(tempf), paint);
            float z2 = paint.measureText("2" + o[1]);
            canvas.drawLine(anMarg+diag+z, rowsize*4-(tempf) , anMarg+20+z+z2,rowsize*4-(tempf), paint);
            canvas.drawText("" + o[1], anMarg+diag+z, rowsize*4, paint);
            canvas.drawText("" + o[2], anMarg+z+((int)(diag*1.3)), rowsize*4-(tempf)-40, paint);

            float z3 = z2 + paint.measureText("00");
            if(o[3].equals("1")){
                paint.setColor(Color.RED);
                canvas.drawText("Impar", anMarg+z+z3, rowsize*4, paint);
            }
            else{
                paint.setColor(Color.BLUE);
                canvas.drawText("Par", anMarg+z+z3, rowsize*4, paint);
            }
            canvas.drawText("" + o[3], anMarg+z2, rowsize*5, paint);

            if(foto2 == binEnt.size()-1){
                paint.setTextSize(tempf/2);
                paint.setColor(Color.DKGRAY);
                canvas.drawText("Último Paso", anMarg+z+z3, rowsize*2, paint);
            }

            return;
        }

        foto2 -= binEnt.size();

        //Dibujar frame de multiplicaciones
        if(foto2<binFrac.size()){
            //Dibujar Constantes
            String[] o = binFrac.get(foto2);
            paint.setTextSize(fontsize/2);
            paint.setColor(Color.BLACK);
            canvas.drawText("Cálculo de parte fraccionaria: " , anMarg , rowsize/2 ,paint);
            canvas.drawText("Paso " + (foto2+1)  + ": " , anMarg, rowsize+5, paint);

            //Dibujar resultado
            String t = "Resultado actual: ";
            canvas.drawText(t, anMarg, rowsize*6, paint);
            float uc = paint.measureText(o[3]);
            float z = paint.measureText(t);
			paint.setTextSize(getMaxFont(o[0]+o[3], fontsize,an-anMarg-(int)(z),paint));
			paint.setColor(Color.RED);
			canvas.drawText(o[0]+o[3], anMarg + z, rowsize * 6, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText(o[0], anMarg + z, rowsize * 6, paint);


            //Dibujando Multiplicación
            int tempf = fontsize;
            paint.setTextSize(tempf);
            paint.setColor(Color.BLACK);
            z = paint.measureText(o[1]);
            //Imprime entrada
            canvas.drawText(o[1], anMarg, rowsize*3,paint);
            float z2 = paint.measureText("x 2");
            canvas.drawText("x 2" , anMarg-z2+z, rowsize*4,paint);
            canvas.drawLine(anMarg, rowsize*4+20 , anMarg+z,rowsize*4+20, paint);
            //Imprime "doble"
			canvas.drawText(o[2], anMarg, rowsize*5, paint);
			paint.setColor(Color.RED);
            canvas.drawText(o[3], anMarg, rowsize*5, paint);
            paint.setColor(Color.BLACK);


            if(o[3].equals("1")){
                float z4 = paint.measureText(o[2]);
                paint.setTextSize(fontsize/2);
                canvas.drawText("" + "-1 en el siguiente" , anMarg+uc+uc+z4, rowsize*5, paint);
            }

            if(foto2 == binFrac.size()-1){
                paint.setTextSize(tempf/2);
                paint.setColor(Color.DKGRAY);
                canvas.drawText("Último Paso", anMarg+z, rowsize*2, paint);
            }

            return;
        }


		foto2 -= binFrac.size();

		if(foto2==0){
			paint.setTextSize(fontsize/2);
			paint.setColor(Color.BLACK);
			canvas.drawText("Resumen: " , anMarg , rowsize/2 ,paint);
			canvas.drawText("Parte entera: ", anMarg, (rowsize*1), paint);
			canvas.drawText("Parte fraccion:", anMarg, (rowsize*3), paint);
			canvas.drawText("Mantisa: ", anMarg, (rowsize*5), paint);
			paint.setTextSize(getMaxFont(binEnt.get(binEnt.size()-1)[3] +  binEnt.get(binEnt.size()-1)[0] + binFrac.get(binFrac.size()-1)[0] +  binFrac.get(binFrac.size()-1)[3], fontsize,an-anMarg,paint));
			canvas.drawText( /*binEnt.get(binEnt.size()-1)[3] + */ binEnt.get(binEnt.size()-1)[0] + binFrac.get(binFrac.size()-1)[0] +  binFrac.get(binFrac.size()-1)[3] , anMarg, (int)(rowsize*6), paint);
			paint.setTextSize(fontsize);
			paint.setColor(Color.GREEN);
			canvas.drawText(binEnt.get(binEnt.size()-1)[3] +  binEnt.get(binEnt.size()-1)[0], anMarg, (rowsize*2), paint);
			paint.setTextSize(getMaxFont(binFrac.get(binFrac.size()-1)[0] +  binFrac.get(binFrac.size()-1)[3], fontsize,an-anMarg,paint));
			paint.setColor(Color.YELLOW);
			canvas.drawText(binFrac.get(binFrac.size()-1)[0] +  binFrac.get(binFrac.size()-1)[3], anMarg, (rowsize*4), paint);
			float v = paint.measureText(binEnt.get(binEnt.size()-1)[3] +  binEnt.get(binEnt.size()-1)[0]);
			paint.setTextSize(fontsize/2);
			paint.setColor(Color.BLACK);
			canvas.drawText("Se recorren " + (binEnt.get(binEnt.size()-1)[0].length()) + " lugares el punto. (" + (binEnt.get(binEnt.size()-1)[0].length()+1) + "bits - 1)" , (anMarg*7)+v, (rowsize*1), paint);
			return;
		}

		foto2 -= 1;

		if(foto2==0) {
			paint.setTextSize(fontsize / 2);
			paint.setColor(Color.BLACK);
			canvas.drawText("Calcular exponente: ", anMarg, rowsize / 2, paint);
			paint.setTextSize(fontsize);
			canvas.drawText("127", anMarg, rowsize*2, paint);
			canvas.drawText("+ " + (binEnt.get(binEnt.size()-1)[0].length())  , anMarg, rowsize*3, paint);
			canvas.drawText("" + (binEnt.get(binEnt.size()-1)[0].length()+127)  , anMarg, rowsize*4, paint);
			float v = paint.measureText("+ " + (binEnt.get(binEnt.size()-1)[0].length()) );
			canvas.drawLine(anMarg, rowsize*3+20 , anMarg+v,rowsize*3+20, paint);
			calcExpOnce = false;
			return;
		}

		foto2 -= 1;

		if(!calcExpOnce){
			calcExp((binEnt.get(binEnt.size()-1)[0].length()+127));
			calcExpOnce = true;
		}

		if(foto2<binExp.size()){
			//Dibujar Constantes
			String[] o = binExp.get(foto2);
			//System.out.println(o[0] + "/" + o[1] + "/" + o[2] + "/" + o[3]);
			paint.setTextSize(fontsize/2);
			paint.setColor(Color.BLACK);
			canvas.drawText("Calculo de número decimal a binario (exponente):" , anMarg , rowsize/2 ,paint);
			canvas.drawText("Paso " + (foto2+1) + ":" , anMarg, rowsize+5, paint);

			//Dibujar resultado
			String t = "Resultado actual: ";
			canvas.drawText(t, anMarg, rowsize*6, paint);
			float z = paint.measureText(t);
			paint.setColor(Color.RED);
			canvas.drawText( "" + o[3], anMarg+z, rowsize*6, paint);

			if (!(o[0].equals(""))){
				paint.setColor(Color.BLACK);
				z += paint.measureText(o[3]);
				canvas.drawText( o[0], anMarg+z, rowsize*6, paint);
			}


			//Dibujando division
			int tempf = fontsize;
			int diag = 60;
			paint.setTextSize(tempf);
			paint.setColor(Color.BLACK);
			z = paint.measureText("2");
			canvas.drawText("2", anMarg, rowsize*4,paint);
			canvas.drawLine(anMarg+z,rowsize*4,anMarg+z+diag,rowsize*4-(tempf), paint);
			float z2 = paint.measureText("2" + o[1]);
			canvas.drawLine(anMarg+diag+z, rowsize*4-(tempf) , anMarg+20+z+z2,rowsize*4-(tempf), paint);
			canvas.drawText("" + o[1], anMarg+diag+z, rowsize*4, paint);
			canvas.drawText("" + o[2], anMarg+z+((int)(diag*1.3)), rowsize*4-(tempf)-40, paint);

			float z3 = z2 + paint.measureText("00");
			if(o[3].equals("1")){
				paint.setColor(Color.RED);
				canvas.drawText("Impar", anMarg+z+z3, rowsize*4, paint);
			}
			else{
				paint.setColor(Color.BLUE);
				canvas.drawText("Par", anMarg+z+z3, rowsize*4, paint);
			}
			canvas.drawText("" + o[3], anMarg+z2, rowsize*5, paint);

			if(foto2 == binEnt.size()-1){
				paint.setTextSize(tempf/2);
				paint.setColor(Color.DKGRAY);
				canvas.drawText("Último Paso", anMarg+z+z3, rowsize*2, paint);
			}

			return;
		}

		foto2 -= binExp.size();

		//Generando string de exponente
		String exponente = binExp.get(binExp.size()-1)[3] + binExp.get(binExp.size()-1)[0];
		for (int i = 0; i< ((formatoBits.equals("64"))? 11:8)-exponente.length() ;i++){
			exponente = "0" + exponente;
		}

		if(foto2==0){
			paint.setTextSize(fontsize/2);
			paint.setColor(Color.BLACK);
			canvas.drawText("Resultados: " , anMarg , rowsize/2 ,paint);
			canvas.drawText("Signo: ", anMarg, (rowsize*1), paint);
			canvas.drawText("Exponente:", anMarg, (rowsize*3), paint);
			canvas.drawText("Mantisa: ", anMarg, (rowsize*5), paint);
			paint.setTextSize(getMaxFont(binEnt.get(binEnt.size()-1)[3] +  binEnt.get(binEnt.size()-1)[0] + binFrac.get(binFrac.size()-1)[0] +  binFrac.get(binFrac.size()-1)[3], fontsize,an-anMarg,paint));
			//Mantisa
			canvas.drawText( /*binEnt.get(binEnt.size()-1)[3] + */ binEnt.get(binEnt.size()-1)[0] + binFrac.get(binFrac.size()-1)[0] +  binFrac.get(binFrac.size()-1)[3] , anMarg, (int)(rowsize*6), paint);
			if(signo){
				paint.setColor(Color.RED);
				canvas.drawText("1: Negativo", anMarg,2*rowsize, paint);
			}
			else{
				paint.setColor(Color.BLUE);
				canvas.drawText("0: Positivo", anMarg,2*rowsize, paint);
			}
			paint.setColor(Color.BLACK);
			canvas.drawText(exponente + "  (" + ((formatoBits.equals("64"))?"11":"8") +" bits)", anMarg, (rowsize*4), paint);
			return;
		}

		foto2 -= 1;

		String completo = (signo) ? "1" : "0";
		completo+= exponente;
		completo+= binEnt.get(binEnt.size()-1)[0] + binFrac.get(binFrac.size()-1)[0] +  binFrac.get(binFrac.size()-1)[3];

		//totalframes=0;
		if(foto2 == 0){
			paint.setTextSize(fontsize / 2);
			canvas.drawText("Conversion a hexadecimal: ", anMarg, rowsize / 2, paint);
			canvas.drawText("Cuartetos de bits: ", anMarg, rowsize*2, paint);
			canvas.drawText("Número hexadecimal: ", anMarg, rowsize*4, paint);

			paint.setTextSize(getMaxFont(completo,fontsize,an-anMarg,paint));
			float m = 0;
			float m2 = 0;
			for(int i=0 ; i<=completo.length()/4;i++){
				paint.setColor(getColor(i));
				int index = completo.length()-((i+1)*4);
				int index2 = completo.length()-(i*4);
				index = (index<0) ? 0 : index;
				m += paint.measureText(completo.substring(index, index2));
				String h = Integer.toHexString(Integer.parseInt(completo.substring(index, index2),2)).toUpperCase();
				m2 += paint.measureText(h);
				canvas.drawText(completo.substring(index, index2), an-anMarg-m, rowsize*3 , paint);
				canvas.drawText(h, an-anMarg-m2, rowsize*5, paint);
			}
			paint.setTextSize(fontsize / 2);
			paint.setColor(Color.BLACK);
			m2 += paint.measureText("0x");
			canvas.drawText("0x", an-anMarg-m2, rowsize*5, paint);
			if(totalframes<foto)
			    totalframes = foto;
			return;
		}

	}

	public int getColor(int i){
	    i %= 6;
	    switch (i){
            case 0:
                return Color.YELLOW;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.CYAN;
            case 3:
                return Color.RED;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.MAGENTA;
        }
        return Color.BLACK;
    }

	public void clearDiv(){
		divisiones = null;
		divisiones = new ArrayList<frame>();
	}

	public int getDivSize(){
	    return divisiones.size();
    }

    public divFrame getDivObj(int i){
	    return (divFrame) divisiones.get(i);
    }

    public void addDiv(String r, int p){
        divFrame f = new divFrame(r, p);
        divisiones.add((frame) f);
    }

	public void clearMul(){
		multiplicaciones = null;
		multiplicaciones = new ArrayList<frame>();
	}

	public int getMulSize(){
		return multiplicaciones.size();
	}

	public divFrame getMulObj(int i){
		return (divFrame) multiplicaciones.get(i);
	}

	public void addMul(String r, float p){
		mulFrame f = new mulFrame(r, p);
		multiplicaciones.add((frame) f);
	}

	public void setAnimation(boolean b){
		animate = b;
	}

	public void setRdy(){
		ready = true;
		foto = 0;
		totalframes = 0;
	}

	public void setData(String Entrada, String bits, boolean sign){
		Decimal = Entrada;
		formatoBits = bits;
		signo = sign;
	}

	//Datos de conversion parte entera a binario
	public void addFistStep(String resultado, String dividendo, String cociente, String residuo){//resultado(binario)|dividendo(entero)|cociente(entero)|residuo(entero)
		String[] s = {resultado,dividendo,cociente,residuo};
		binEnt.add(s);
	}
	public void deleteFirstStep(){
		binEnt = null;
		binEnt = new ArrayList<String[]>();
	}

	//Datos de conversion parte entera a binario
	public void addthirdStep(String resultado, String dividendo, String cociente, String residuo){//resultado(binario)|dividendo(entero)|cociente(entero)|residuo(entero)
		String[] s = {resultado,dividendo,cociente,residuo};
		binExp.add(s);
	}
	public void deletethirdStep(){
		binExp = null;
		binExp = new ArrayList<String[]>();
	}


	//Datos de conversion parte fraccionaria a binario
	public void addSecondStep(String resultado, String entrada, String doble){//resultado(binario)|entrada(float)|doble(float)|salida(entero)
		String[] s = {resultado,entrada,doble,String.valueOf(doble.charAt(0))};
		binFrac.add(s);
	}
	public void deleteSecondStep(){
		binFrac = null;
		binFrac = new ArrayList<String[]>();
	}

	public int getMaxFont(String input, int fsize, int ancho, Paint p){
		int f = fsize;
		p.setTextSize(f);
		float z = p.measureText(input);
		while(z>ancho){
			f -= 1;
			p.setTextSize(f);
			z = p.measureText(input);
		}
		return f;
	}


	public void setFPS(double f){
		thread.setFPS(f);
	}

	public void backFrame(){
	    if(!ready) return;
		if(foto==0){

		}
		else{
			foto--;
		}
	}

	public void nextFrame(){
		System.out.println("Foto: " + foto);
		System.out.println("Totales: " + totalframes);
	    if(!ready) return;
		//int totalPasos = getDivSize()+getMulSize();
        if (totalframes == 0){
            foto++;
            return;
        }
		if(foto==totalframes){

		}
		else {
			foto++;
		}
	}

	private void calcExp(int pE){
		String resultComp = "";
		deletethirdStep();
		while(true){
			if(pE==0){
				System.out.println("Tonny: Numero de objetos en la lista: " + binExp.size());
				System.out.println("Tonny: Numero binario construido: " + resultComp);
				System.out.println("Tonny: Datos de los objetos: ");

				for(int i=0; i<binExp.size();i++){
					String[] o = binExp.get(i);
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
			addthirdStep(resultComp, "" + pE, "" + pE2, "" + res);
			resultComp = res + resultComp;
			pE = pE2;
		}
	}


	public binToHexAnimflo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//this.setBackgroundColor(Color.RED);
		init();

	}

	public binToHexAnimflo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//this.setBackgroundColor(Color.RED);
		init();

	}

	public binToHexAnimflo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		//this.setBackgroundColor(Color.RED);
		init();

	}

	private void init() {
		getHolder().addCallback(this);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		setFocusable(true); // make sure we get key events

		//paint.setStyle(Paint.Style.STROKE); // Da el efecto de CIRCULOS CONCENTRICOS
		paint.setStrokeWidth(3);
		paint.setColor(Color.WHITE);

		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);


	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
							   int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread = new MySurfaceThread2(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
}
