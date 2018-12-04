/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.tonny.myapplication;

import java.text.DecimalFormat;

/**
 *
 * @author qpalzm
 */
public class IntToBin {

    protected String number;
    public String[] binary;
    public String binary_string;
    int numBits;
    String Log = "";

    public void set_int(String toSet, int numBits){
        this.binary = new String[numBits];
        for(int i = 0; i < numBits; i++)
            this.binary[i]="0";
        this.numBits = numBits;
        this.number = toSet;
    }

    public String reverse(){
        //reverse
        this.Log += "--Colocar binario al reves--"+ "\n";
        this.binary_string="";
        for(int i = this.binary.length - 1; i > -1; i--)
            binary_string += this.binary[i];
        //this.binary_string = this.binary_string.substring(this.binary_string.indexOf("1"), this.binary_string.length());
        //this.binary_string =
        this.Log += "--Conversion finalizada: "+ this.binary_string+ "\n";
        this.Log += "#################"+ "\n";
        return this.binary_string;
    }

    public String perform(){
        int i = 0;
        Double ax = Double.parseDouble(this.number);
        if(ax == 0.0){
            this.binary_string = "0";
            return this.binary_string;
        }

        this.Log += "#####CONVERTIR NUMERO ENTERO " + (new DecimalFormat("#").format(ax)) + " A BINARIO######"+ "\n";
        this.Log += "Division por dos -> Modulo"+ "\n";
        while(ax.intValue() != 0  && i < this.numBits){
            this.binary[i] = ""+ ax.intValue()%2;
            this.Log += ax.intValue() +" / 2 = "+ ax.intValue()/2 + " -> " + ax.intValue()%2 + "\n";
            ax = Double.parseDouble(""+ax.intValue() / 2);
            i++;
        }
        return reverse();
    }

    public String performToFraction(){
        Double num = Double.parseDouble("."+this.number);
        int i = 0;
        this.binary_string="";
        if(this.number.equals("") || this.number.equals("0"))
            num = 0.0;
        this.Log += "#####CONVERTIR NUMERO FRACCIONARIO " + num + " A BINARIO######"+ "\n";
        this.Log += "Multiplicacion por dos -> Parte entera "+ "\n";
        if(this.number.equals("") || this.number.equals("0")){
            this.binary_string="0";
        }else{
            while(i < this.numBits && num != 0.0){
                this.Log += num + " x " + "2 = "+ (num*2) + " -> " + ( (num*2) >= 1 ? "1" : "0")+"\n";
                num *=2;
                if(num >= 1){
                    this.binary_string+="1";
                    num -= 1;
                }else
                    this.binary_string+="0";
                i++;
            }
        }

        this.Log += "#### Resultado en binario: " + this.binary_string+" ####"+"\n";
        return this.binary_string;
    }



}

