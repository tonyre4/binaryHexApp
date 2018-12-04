package com.example.tonny.myapplication;

public class FixedPointToHex{

    private String Dec; //string of a number to convert
    private String DecUnsigned; //string of a number to convert
    private int Exponent; //exponent
    private String signed_unsigned_or_error;

    private String sign_string;
    private String exponent_string;
    private String mantissa_string;
    private int type;

    String Log=""; // this variable contains each step with its \n

    public FixedPointToHex(String dec, int bits) {
        this.Dec = dec;
        this.DecUnsigned = this.getOnlyNumber(dec);
        this.type = bits;
        this.signedOrUnsigned();

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


    public void perform() {

        boolean isDecSigned = false;
        if(!this.signed_unsigned_or_error.equals("error")){
            this.Log += "***Conversion real a aritmetica - Decimal a Hexadecimal Punto fijo "+this.type +"bits ***"+ "\n";
            this.Log += "# Numero a convertir: " + ((this.signed_unsigned_or_error == "+") ? ""+ this.DecUnsigned : this.signed_unsigned_or_error + this.DecUnsigned) + "\n";
            if (this.signed_unsigned_or_error.equals("-")) {
                this.Log += "# Numero negativo #"+ "\n";
                isDecSigned = true;
            } else if(this.signed_unsigned_or_error.equals("+"))
                this.Log += "# Numero positivo # "+ "\n";

            FloatPointToHex f = new FloatPointToHex(); //clase de conversion para utilizar una funcion
            //verificar que no sea cero lo ingresado
            if(f.isAllZeros(this.DecUnsigned) && this.type==32){
                this.Log += "Resultado en Hexadecimal:"+ "\n";
                this.Log += "0.0";
            }else if(f.isAllZeros(this.DecUnsigned) && this.type == 64){
                this.Log += "Resultado en Hexadecimal:"+ "\n";
                this.Log += "0.0";

            }else{
                String x = "";
                //Convertir entero a binario
                if(this.DecUnsigned.indexOf(".")!=-1) //verificar si no hay punto decimal
                    x = this.DecUnsigned.substring(0 , this.DecUnsigned.indexOf(".")); //tomar solamente parte entera
                else
                    x = this.DecUnsigned; //si no hay punto, se toma todo le numero
                //Clase para convertir a binario
                IntToBin number_to_bin = new IntToBin();
                number_to_bin.set_int(x, this.type); //se envia el numero a convertir en String y el numero de bits de rango
                number_to_bin.perform(); //se realiza la conversion
                //la variable contenedora de la cadena binaria se llama binary_string y es un atributo de la clase y es publico!
                //String number_to_bin_string = number_to_bin.binary_string;

                this.Log += "Parte entera: " + x + " | En binario: " + number_to_bin.binary_string + "\n"; //se guarda en el log el proceso realizado
                this.Log += "Proceso::"+ "\n";
                this.Log += number_to_bin.Log; //se obtiene la variable Log que es String y es publica de la clase, esta almacena el proceso para la conversion a binario



                //Convertir a binario la parte decimal
                String y="";
                if(this.DecUnsigned.indexOf(".")!=-1 && this.DecUnsigned.substring(this.DecUnsigned.indexOf("."), this.DecUnsigned.length()).length()-1 != 0)
                    //se verifica si existe un punto y si la parte fraccionaria es diferente de 0 (en caso de que exista parte fraccionaria)
                    y = this.DecUnsigned.substring(this.DecUnsigned.indexOf(".")+1 , this.DecUnsigned.length() );
                else //o si no existe punto o si la parte fraccionaria esta vacia, es decir solo puso el . de adorno
                    y = "0";
                //Clase para realizar la conversion
                IntToBin fraction_to_bin = new IntToBin();
                fraction_to_bin.set_int(y, this.type); //se asigna el valor mediante el metodo .set_int y se envia el numero y el numero de rango de bits
                fraction_to_bin.performToFraction(); //realizar la operacion de fraccion a binario

                this.Log += "Parte fraccionaria: " +y + " | En binario: " + fraction_to_bin.binary_string+ "\n"; //colocar en el log el proceso...
                this.Log += "Proceso::"+ "\n";
                this.Log += fraction_to_bin.Log;

                //String finalBinary = (this.signed_unsigned_or_error== "+") ? "" : this.signed_unsigned_or_error + number_to_bin.binary_string + "." + fraction_to_bin.binary_string;
                //convert to hex
                String finalBinary = number_to_bin.binary_string + "." + fraction_to_bin.binary_string;
                String HEX = this.binaryToHex(finalBinary);
                this.Log += "Resultado en Hexadecimal:"+ "\n";
                FloatPointToHex fph = new FloatPointToHex();
                this.Log += ((this.signed_unsigned_or_error== "+") ? "" : this.signed_unsigned_or_error ) + fph.cleanBeginWithZeros(HEX); //agregar signo si incluye


            }




        }else{
            this.Log += "Error! Signo en posicion incorrecta";
        }
    }

    public String completarCeros(String binario, String tipo){
        if(binario.length() % 4 != 0){
            String added= "";
            if(tipo.equals("entero")){
                //completar para entero, se agrega a la izquierda los ceros
                for(int i = binario.length(); i%4 != 0; i++)
                    added += "0";
                binario = added + binario;
            }else {
                //completar para fraccion se agrega a la derecha
                for(int i = 1; i%4 != 0; i++)
                    added += "0";
                binario = binario + added;
            }
            this.Log += "Binario de "+tipo+" incompleto para octetos , se completo con ceros... \n";
            this.Log += "Binario de " + tipo +" completo: " + binario + "\n";
        }
        return binario;
    }


    public String binarioAHexadecimal(String binary, String tipo){
        //Instancia de la clase a utilizar, se ocupa la funcion fourBitsToDecimal
        FloatPointToHex tool = new FloatPointToHex();

        this.Log += "#####Conversion de la parte "+tipo+", representada en binario: "+binary+" a hexadecimal #####"+ "\n";
        this.Log += "VALOR DECIMAL DE OCTETO -> NUMERO EN HEXADECIMAL"+ "\n";
        String hex="";
        for(int i = 3 ; i < binary.length(); i += 4){
            int dec = tool.fourBitsToDecimal(binary.substring(i-3 , i+1));
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
        this.Log += "#####Termina conversion  de la parte "+tipo+" a hexadecimal#####"+ "\n";
        return hex;
    }


    public String binaryToHex(String binary){

        this.Log += "%%%%%%%%%%%%%%%%%"+ "\n";
        this.Log +="Representacion del numero en binario con punto fijo:"+ "\n";
        this.Log += binary+ "\n";
        this.Log += "%%%%%%%%%%%%%%%%%"+ "\n";
        this.Log += "PROCESO::"+"\n";
        //this.Log += "#####PROCESO CONVERSION A HEXADECIMAL#####"+ "\n";
        //this.Log += "VALOR DECIMAL DE OCTETO -> NUMERO EN HEXADECIMAL"+ "\n";


        String binarioDeEntero = this.completarCeros(binary.substring(0, ( (binary.indexOf(".")==-1 ) ? binary.length() : binary.indexOf(".")) ), "entero");

        String binarioDecimal = this.completarCeros(
                binary.substring( (binary.indexOf(".")==-1 ) ? binary.length()-1 : binary.indexOf(".")+1, binary.length())
                , "fraccion");

        String hexEntero = binarioAHexadecimal(binarioDeEntero, "entera");

        String hexFraccion = binarioAHexadecimal(binarioDecimal, "fraccionaria");
        return hexEntero + ((hexFraccion.equals("")) ? "" : ".") + hexFraccion;

    }

}

