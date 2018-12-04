/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.tonny.myapplication;

public class FloatPointToHex{

    private String Dec; //string of a number to convert
    private String DecUnsigned; //string of a number to convert
    private int Exponent; //exponent
    private String signed_unsigned_or_error;

    private String sign_string;
    private String exponent_string;
    private String mantissa_string;
    private int type;

    String Log=""; // this variable contains each step with its \n

    public FloatPointToHex(String dec, int bits) {
        this.Dec = dec;
        this.DecUnsigned = this.getOnlyNumber(dec);
        this.type = bits;
        this.signedOrUnsigned();

    }

    public FloatPointToHex(){

    }

    public void signedOrUnsigned() {
        if (this.Dec.indexOf("-") != -1) {
            if (this.Dec.indexOf("-") == 0)
                this.signed_unsigned_or_error = "-";
            else
                this.signed_unsigned_or_error = "error";
        }else
            this.signed_unsigned_or_error = "+";
    }


    public String getOnlyNumber(String dec){
        String format="";
        for(int i=0; i < dec.length(); i++ )
            if(dec.charAt(i) != '-' && dec.charAt(i)!= '+')
                format+= dec.charAt(i);
        if(format.charAt(format.length()-1) == '.')
            format += "0";
        else if(format.equals("0"))
            format = "0.0";
        return (format.charAt(0)=='.' || format.charAt(0)==' ') ? "0"+format : format;
    }

    public boolean isAllZeros(String str){
        int cont = 0;
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i)=='0')
                cont++;
        }
        if( (str.contains(".") && cont == str.length()-1) || (!str.contains(".") && cont == str.length())){
            return true;
        }
        return false;
    }


    public void perform() {

        boolean isDecSigned = false;
        if(!this.signed_unsigned_or_error.equals("error")){
            this.Log += "***Conversion real a aritmetica Punto Flotante a Hexadecimal "+((this.type==32) ? "32bits":"64bits") + "***"+ "\n";
            this.Log += "# Numero a convertir: " + ((this.signed_unsigned_or_error == "+") ? ""+ this.DecUnsigned : this.signed_unsigned_or_error + this.DecUnsigned) + "\n";
            if (this.signed_unsigned_or_error.equals("-")) {
                this.Log += "# Numero negativo #"+ "\n";
                this.exponent_string = "1";
                isDecSigned = true;
            } else if(this.signed_unsigned_or_error.equals("+")){
                this.Log += "# Numero positivo # "+ "\n";
                this.exponent_string = "0";
            }

            //verificar que no sea cero lo ingresado
            if(isAllZeros(this.DecUnsigned) && this.type==32){
                this.Log += "Resultado en Hexadecimal:"+ "\n";
                this.Log += "0x00000000";
            }else if(isAllZeros(this.DecUnsigned) && this.type == 64){
                this.Log += "Resultado en Hexadecimal:"+ "\n";
                this.Log += "0x0000000000000000";

            }else{
                String x = "";
                //Convert number (before .) to binary
                if(this.DecUnsigned.indexOf(".")!=-1)
                    x = this.DecUnsigned.substring(0 , this.DecUnsigned.indexOf("."));
                else
                    x = this.DecUnsigned;
                IntToBin number_to_bin = new IntToBin();
                number_to_bin.set_int(x, (this.type == 32) ? 23 : 52);
                number_to_bin.perform();

                String number_to_bin_string = number_to_bin.binary_string;

                this.Log += "Parte entera: " + x + " | En binario: " + number_to_bin_string + "\n";
                this.Log += "Proceso::"+ "\n";
                this.Log += number_to_bin.Log;



                //Convert decimal (after .) to binary
                String y="";
                if(this.DecUnsigned.indexOf(".")!=-1 && this.DecUnsigned.substring(this.DecUnsigned.indexOf("."), this.DecUnsigned.length()).length()-1 != 0)
                    y = this.DecUnsigned.substring(this.DecUnsigned.indexOf(".")+1 , this.DecUnsigned.length() );
                else
                    y = "0";




                IntToBin fraction_to_bin = new IntToBin();
                fraction_to_bin.set_int(y, (this.type == 32) ? 23 : 52 - this.Exponent);
                fraction_to_bin.performToFraction();

                this.Log += "Parte fraccionaria: " +y + " | En binario: " + fraction_to_bin.binary_string+ "\n";
                this.Log += "Proceso::"+ "\n";
                this.Log += fraction_to_bin.Log;



                if(Double.parseDouble(number_to_bin_string) == 0.0){
                    if(fraction_to_bin.binary_string.indexOf("1")==0){
                        this.Exponent = -1;
                    }else{
                        this.Exponent = (fraction_to_bin.binary_string.length() - fraction_to_bin.binary_string.indexOf("1")  + 1) * -1;
                    }

                    //this.Exponent = -1;
                }else{
                    this.Exponent = number_to_bin_string.length()- 1 - number_to_bin_string.indexOf("1");
                }






                IntToBin exponent_to_bin = new IntToBin();
                exponent_to_bin.set_int(""+(Exponent + ( (this.type == 32) ? 127.0 : 1023.0)),  (this.type == 32) ? 8 : 11);
                exponent_to_bin.perform();
                this.Log+= "============"+ "\n";
                this.Log += "Se recorrieron "+ this.Exponent + " espacios hasta el primer 1 en el binario de la parte entera del numero, por lo tanto; \n";
                this.Log += "Exponente = " + this.Exponent + "\n :";
                this.Log += "Exponente + "+((this.type == 32) ? 127.0 : 1023.0)+" = " + (this.Exponent + ( (this.type == 32) ? 127.0 : 1023.0) ) + "| En binario: " +exponent_to_bin.binary_string+ "\n";
                this.Log += "Proceso::"+ "\n";
                this.Log += exponent_to_bin.Log;
                this.Log += "============"+ "\n";



                this.mantissa_string =
                        number_to_bin_string.substring(number_to_bin_string.indexOf("1") + 1, number_to_bin_string.length())  +
                                "" + fraction_to_bin.binary_string;
                this.Log += "Mantisa: " + this.mantissa_string+ "\n";
                this.Log += "============"+ "\n";

                String finalBinary = this.exponent_string + exponent_to_bin.binary_string + this.mantissa_string;
                this.Log += "Tamaño de binaro 32bits: " + finalBinary.length() + "\n";

                //convert to hex

                String HEX = this.binaryToHex(finalBinary);


                this.Log += "Resultado en Hexadecimal:"+ "\n";
                this.Log += "0x"+cleanBeginWithZeros(HEX);


            }



        }else{
            this.Log += "Error! Signo en posicion incorrecta";
        }
    }

    public int fourBitsToDecimal(String bits){
        int r=0;
        if(bits.charAt(0) == '1')
            r+= 8;
        if(bits.charAt(1) == '1')
            r+= 4;
        if(bits.charAt(2) == '1')
            r+= 2;
        if(bits.charAt(3) == '1')
            r+= 1;
        return r;
    }

    public String cleanBeginWithZeros(String str){
        String clean= "";
        if(str.charAt(0)=='0' && str.charAt(1) == '.'){
            return str;
        }else{
            for(int i = 0; i < str.length(); i++){
                if(str.charAt(i) != '0')
                    return str.substring(i, str.length());
            }
        }
        return str;
    }

    public String binaryToHex(String binary){
        if(binary.length() < this.type){
            for(int i = binary.length(); i < this.type; i++)
                binary += "0";
        }
        this.Log += "%%%%%%%%%%%%%%%%%"+ "\n";
        this.Log +="Representacion en binario de 32bits:"+ "\n";
        this.Log += "Signo / Exponente / Mantisa"+ "\n";
        this.Log += binary.charAt(0)  + " / " + binary.substring(1, 9) + " / " + binary.substring(9, binary.length())+ "\n";
        this.Log += binary+ "\n";
        this.Log += "%%%%%%%%%%%%%%%%%"+ "\n";
        this.Log += "#####PROCESO CONVERSION A HEXADECIMAL#####"+ "\n";
        this.Log += "VALOR DECIMAL DE OCTETO -> NUMERO EN HEXADECIMAL"+ "\n";
        String hex="";
        for(int i = 3 ; i < this.type; i += 4){
            int dec = this.fourBitsToDecimal(binary.substring(i-3 , i+1));
            this.Log += dec + " -> ";
            switch(dec){
                case 0:
                    hex+= "0";
                    break;
                case 1:
                    hex+= "1";
                    break;
                case 2:
                    hex+= "2";
                    break;
                case 3:
                    hex+= "3";
                    break;
                case 4:
                    hex+= "4";
                    break;
                case 5:
                    hex+= "5";
                    break;
                case 6:
                    hex+= "6";
                    break;
                case 7:
                    hex+= "7";
                    break;
                case 8:
                    hex+= "8";
                    break;
                case 9:
                    hex+= "9";
                    break;
                case 10:
                    hex+= "a";
                    break;
                case 11:
                    hex+= "b";
                    break;
                case 12:
                    hex+= "c";
                    break;
                case 13:
                    hex+= "d";
                    break;
                case 14:
                    hex+= "e";
                    break;
                case 15:
                    hex+= "f";
                    break;
            }


            this.Log +=  hex.charAt(hex.length()-1) +"\n";

        }
        this.Log += "#####Termina conversion a hexadecimal#####"+ "\n";
        return hex;

    }

}
