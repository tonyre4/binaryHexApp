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

public class binToHexAnim extends SurfaceView implements
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

	String Decimal;
	String bitsFracc;
	String finalR;
	String finalRHex;
	boolean signo;
	boolean consigno;

	private MySurfaceThread thread;
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

		int anMarg = (int) (an*0.001);


		//Tamaño de fuente
		int rowsize = al/filas;
		int fontsize = (int) (rowsize*0.8);
		paint.setTextSize(fontsize);
		paint.setColor(Color.BLACK);

		//Dibujar frame de entradas
		if (foto == 0){
			canvas.drawText("Entradas: ", anMarg, 1*rowsize,paint);
			canvas.drawText("Número: " + Decimal,50+anMarg,3*rowsize, paint);
			canvas.drawText("Bits p/ fraccion: " + bitsFracc,50+anMarg,4*rowsize, paint);
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

		//Dibujar frame de divisiones
		if(foto<=divisiones.size()){
			//Dibujar Constantes
			int elem = foto-1;
			divFrame o = getDivObj(elem);
			paint.setTextSize(fontsize/2);
			paint.setColor(Color.BLACK);
			canvas.drawText("Calculo de parte entera: " , anMarg , rowsize/2 ,paint);
			canvas.drawText("Paso " + foto + ": " , anMarg, rowsize+5, paint);

			//Dibujar resultado
			String t = "Resultado actual: ";
			canvas.drawText(t, anMarg, rowsize*6, paint);
			float z = paint.measureText(t);
			paint.setColor(Color.RED);
			canvas.drawText( "" + o.Resultado.charAt(0), anMarg+z, rowsize*6, paint);

			if (o.Resultado.length()!=1){
				z += paint.measureText("" + o.Resultado.charAt(0));
				paint.setColor(Color.BLACK);
				canvas.drawText( o.Resultado.substring(1,o.Resultado.length()), anMarg+z, rowsize*6, paint);
			}

			//Dibujando division
			int tempf = fontsize;
			int diag = 60;
			paint.setTextSize(tempf);
			paint.setColor(Color.BLACK);
			z = paint.measureText("2");
			canvas.drawText("2", anMarg, rowsize*4,paint);
			canvas.drawLine(anMarg+z,rowsize*4,anMarg+z+diag,rowsize*4-(tempf), paint);
			float z2 = paint.measureText("2" + o.divid);
			canvas.drawLine(anMarg+diag+z, rowsize*4-(tempf) , anMarg+20+z+z2,rowsize*4-(tempf), paint);
			canvas.drawText("" + o.divid, anMarg+diag+z, rowsize*4, paint);
			canvas.drawText("" + o.cociente, anMarg+z+((int)(diag*1.3)), rowsize*4-(tempf)-40, paint);

			float z3 = z2 + paint.measureText("00");
			if(o.Resultado.charAt(0) == '1'){
                paint.setColor(Color.RED);
			    canvas.drawText("Impar", anMarg+z+z3, rowsize*4, paint);
            }
            else{
                paint.setColor(Color.BLUE);
                canvas.drawText("Par", anMarg+z+z3, rowsize*4, paint);
            }
            canvas.drawText("" + o.Resultado.charAt(0), anMarg+z2, rowsize*5, paint);

			if(foto == divisiones.size()){
                paint.setTextSize(tempf/2);
                paint.setColor(Color.DKGRAY);
                canvas.drawText("Último Paso", anMarg+z+z3, rowsize*2, paint);
            }

            return;
		}


        //Dibujar frame de multiplicaciones
        if(foto<=divisiones.size()+multiplicaciones.size()){
            //Dibujar Constantes
            int elem = foto-divisiones.size()-1;
            mulFrame o = (mulFrame) multiplicaciones.get(elem);
            paint.setTextSize(fontsize/2);
            paint.setColor(Color.BLACK);
            canvas.drawText("Cálculo de parte fraccionaria: " , anMarg , rowsize/2 ,paint);
            canvas.drawText("Paso " + (elem+1)  + ": " , anMarg, rowsize+5, paint);

            //Dibujar resultado
            String t = "Resultado actual: ";
            canvas.drawText(t, anMarg, rowsize*6, paint);
            float uc = paint.measureText("0");
            float z = paint.measureText(t);

            if (o.Resultado.length()!=1){
                canvas.drawText( o.Resultado.substring(0,o.nletras-1), anMarg+z, rowsize*6, paint);
                z +=  paint.measureText(o.Resultado) - uc;
            }

            paint.setColor(Color.RED);
            canvas.drawText( "" + o.Resultado.charAt(o.nletras-1), anMarg+z, rowsize*6, paint);


            //Dibujando Multiplicación
            int tempf = fontsize;
            paint.setTextSize(tempf);
            paint.setColor(Color.BLACK);
            z = paint.measureText("" + o.real);
            canvas.drawText("" + o.real, anMarg, rowsize*3,paint);
            float z2 = paint.measureText("x 2");
            canvas.drawText("x 2" , anMarg-z2+z, rowsize*4,paint);
            canvas.drawLine(anMarg, rowsize*4+20 , anMarg+z,rowsize*4+20, paint);
            paint.setColor(Color.RED);
            canvas.drawText("" + String.valueOf(o.doublereal).charAt(0), anMarg, rowsize*5, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText("" + String.valueOf(o.doublereal).substring(1,(""+ o.doublereal).length()-1), anMarg+uc+uc, rowsize*5, paint);

            if(String.valueOf(o.doublereal).charAt(0) == '1'){
                float z4 = paint.measureText("" + o.doublereal);
                paint.setTextSize(fontsize/2);
                canvas.drawText("" + String.valueOf("-1 en el siguiente") , anMarg+uc+uc+z4, rowsize*5, paint);
            }

            if(multiplicaciones.size() == foto-divisiones.size()){
                paint.setTextSize(tempf/2);
                paint.setColor(Color.DKGRAY);
                canvas.drawText("Último Paso", anMarg+z, rowsize*2, paint);
            }

            return;
        }

        //Calculo de fotogramas siguientes
        int foto2 = foto - (divisiones.size()+multiplicaciones.size()+1);
        //System.out.println("Frame de complemento: " + foto2);
        //Dibujar Resultado
        String Rdiv = divisiones.get(divisiones.size()-1).Resultado ;
        String Rmul = multiplicaciones.get(multiplicaciones.size()-1).Resultado;

        if(foto2==0){
            paint.setTextSize(fontsize/2);
            paint.setColor(Color.BLACK);
            canvas.drawText("Resumen: " , anMarg , rowsize/2 ,paint);
            canvas.drawText("Parte entera: ", anMarg, (rowsize*2), paint);
            canvas.drawText("Parte fraccion:", anMarg, (rowsize*4), paint);
            paint.setTextSize(fontsize);
            paint.setColor(Color.GREEN);
            canvas.drawText("" + Rdiv , anMarg, (rowsize*3), paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText("" + Rmul , anMarg, (rowsize*5), paint);
            return;
        }

        String Rcomp = Rdiv + Rmul;
        //Dibujar complemento
		if (consigno){

		    //Cuando tiene signo negativo
		    if (signo) {
			    /*
		        1 - Presentar Complemento a 1
		        2 - Presentar Complemento a 2
		        */
                paint.setTextSize(fontsize / 2);
                paint.setColor(Color.BLACK);
                String inv = "";
                String inv2 = "";
                for (int i = 0; i < Rdiv.length(); i++) {
                    if (Rdiv.charAt(i) == '0') {
                        inv += "1";
                    } else {
                        inv += "0";
                    }
                }
                for (int i = 0; i < Rmul.length(); i++) {
                    if (Rmul.charAt(i) == '0') {
                        inv2 += "1";
                    } else {
                        inv2 += "0";
                    }
                }

                String invC = inv + inv2;
                Rcomp = invC;
                int CA2 = Integer.parseInt(invC, 2);
                CA2 += 1;
                String CA2s = Integer.toBinaryString(CA2);
                for (int i = CA2s.length(); i < invC.length(); i++) {
                    CA2s = "0" + CA2s;
                }

                switch (foto2) {
                    case 1:
                        canvas.drawText("Complemento a 1:", anMarg, rowsize / 2, paint);
                        canvas.drawText("Invertido:", anMarg, (int) (rowsize * 2.5), paint);
                        if (Rcomp.length() > 15) {
                            paint.setTextSize(fontsize / 2);
                        } else {
                            paint.setTextSize(fontsize);
                        }
                        paint.setColor(Color.GREEN);
                        canvas.drawText(Rdiv, anMarg, (int) (rowsize * 1.5), paint);
                        canvas.drawText(inv, anMarg, (int) (rowsize * 3.5), paint);
                        float v = paint.measureText(Rdiv);
                        paint.setColor(Color.YELLOW);
                        canvas.drawText(Rmul, anMarg + v, (int) (rowsize * 1.5), paint);
                        canvas.drawText(inv2, anMarg + v, (int) (rowsize * 3.5), paint);
                        return;
                    case 2:
                        canvas.drawText("Complemento a 2:", anMarg, rowsize / 2, paint);
                        if (Rcomp.length() > 15) {
                            paint.setTextSize(fontsize / 2);
                        } else {
                            paint.setTextSize(fontsize);
                        }

                        paint.setColor(Color.GREEN);
                        canvas.drawText(inv, anMarg, (int) (rowsize * 1.5), paint);
                        v = paint.measureText(inv);
                        paint.setColor(Color.YELLOW);
                        canvas.drawText(inv2, anMarg + v, (int) (rowsize * 1.5), paint);
                        float v2 = paint.measureText(inv + inv2) - paint.measureText("+ 1");
                        float v3 = paint.measureText(inv + inv2) + paint.measureText("+ 1");
                        paint.setColor(Color.BLACK);
                        canvas.drawText("+ 1", anMarg + v2, (int) (rowsize * 2.5), paint);
                        canvas.drawLine(anMarg, (int) (rowsize * 2.5), anMarg + v3, (int) (rowsize * 2.5), paint);
                        paint.setColor(Color.GREEN);
                        canvas.drawText(CA2s.substring(0, inv.length()), anMarg, (int) (rowsize * 3.5), paint);
                        v = paint.measureText(CA2s.substring(0, inv.length()));
                        paint.setColor(Color.YELLOW);
                        canvas.drawText(CA2s.substring(inv.length(), CA2s.length()), anMarg + v, (int) (rowsize * 3.5), paint);
                        return;

                    default:
                        foto2 -= 2;
                        break;
                }

            }

            switch(foto2){
                case 1:
                    paint.setTextSize(fontsize / 2);
                    canvas.drawText("Agregando signo: ", anMarg, rowsize / 2, paint);
                    if (Rcomp.length() > 15) {
                        paint.setTextSize(fontsize / 2);
                    } else {
                        paint.setTextSize(fontsize);
                    }
                    float v = paint.measureText("0");
                    if(signo){
                        paint.setColor(Color.RED);
                        canvas.drawText("1", anMarg, (int) (rowsize * 2.5), paint);
                        paint.setTextSize(fontsize);
                        canvas.drawText("Negativo", anMarg, (int) (rowsize * 4.5), paint);
                        //Rcomp = "1" + Rcomp;
                    }
                    else{
                        paint.setColor(Color.BLUE);
                        canvas.drawText("0", anMarg, (int) (rowsize * 2.5), paint);
                        paint.setTextSize(fontsize);
                        canvas.drawText("Positivo", anMarg, (int) (rowsize * 4.5), paint);
                        //Rcomp = "0" + Rcomp;
                    }

                    if (Rcomp.length() > 15) {
                        paint.setTextSize(fontsize / 2);
                    } else {
                        paint.setTextSize(fontsize);
                    }
                    paint.setColor(Color.GREEN);
                    canvas.drawText(Rcomp.substring(0, Rdiv.length()), anMarg+v, (int) (rowsize * 2.5), paint);
                    v += paint.measureText(Rcomp.substring(0, Rdiv.length()));
                    paint.setColor(Color.YELLOW);
                    canvas.drawText(Rcomp.substring(Rdiv.length(), Rcomp.length()), anMarg + v, (int) (rowsize * 2.5), paint);

                    return;
                case 2:
                    paint.setTextSize(fontsize / 2);
                    canvas.drawText("Conversion a hexadecimal: ", anMarg, rowsize / 2, paint);
                    canvas.drawText("Cuartetos de bits: ", anMarg, rowsize*2, paint);
                    canvas.drawText("Número hexadecimal: ", anMarg, rowsize*4, paint);
                    if(signo){
                        Rcomp = "1" + Rcomp;
                    }
                    else{
                        Rcomp = "0" + Rcomp;
                    }
                    if (Rcomp.length() > 15) {
                        paint.setTextSize(fontsize / 2);
                    } else {
                        paint.setTextSize(fontsize);
                    }

                    float m = 0;
                    float m2 = 0;
                    for(int i=0 ; i<=Rcomp.length()/4;i++){
                        paint.setColor(getColor(i));
                        int index = Rcomp.length()-((i+1)*4);
                        int index2 = Rcomp.length()-(i*4);
                        index = (index<0) ? 0 : index;
                        m += paint.measureText(Rcomp.substring(index, index2));
                        String h = Integer.toHexString(Integer.parseInt(Rcomp.substring(index, index2),2)).toUpperCase();
                        m2 += paint.measureText(h);
                        canvas.drawText(Rcomp.substring(index, index2), an-anMarg-m, rowsize*3 , paint);
                        canvas.drawText(h, an-anMarg-m2, rowsize*5, paint);
                    }
                    paint.setColor(Color.BLACK);
                    m2 += paint.measureText("0x");
                    canvas.drawText("0x", an-anMarg-m2, rowsize*5, paint);
                    totalframes = foto;
                    return;
            }

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

	public void setResults(String D, String b, String R, String Rh, boolean s, boolean cs){
		Decimal = D;
		bitsFracc = b;
		finalR = R;
		finalRHex = Rh;
		signo = s;
		consigno = cs;
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


	public binToHexAnim(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//this.setBackgroundColor(Color.RED);
		init();

	}

	public binToHexAnim(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//this.setBackgroundColor(Color.RED);
		init();

	}

	public binToHexAnim(Context context, AttributeSet attrs, int defStyle) {
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
		thread = new MySurfaceThread(getHolder(), this);
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
