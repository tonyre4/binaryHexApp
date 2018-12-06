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

	String Decimal;
	String bitsFracc;
	String finalR;
	String finalRHex;

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
			foto++;
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
		}

		//Dibujar frame de divisiones

		if(foto>0 && foto<=divisiones.size()){
			int elem = foto-1;

		}




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
	}

	public void setResults(String D, String b, String R, String Rh){
		Decimal = D;
		bitsFracc = b;
		finalR = R;
		finalRHex = Rh;
	}

	public void setFPS(double f){
		thread.setFPS(f);
	}

	public void backFrame(){
		if(foto==0){

		}
		else{
			foto--;
		}
	}

	public void nextFrame(){
		int totalPasos = getDivSize()+getMulSize()-1;
		if(foto==totalPasos){

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
