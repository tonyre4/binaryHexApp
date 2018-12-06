package com.example.tonny.myapplication;

class frame {
    public String Resultado;
}


class divFrame extends frame{

    public int divid;
    public int nletras;
    public int cociente;

    public divFrame(String R, int d){
        Resultado = String.valueOf(R);
        divid = d;
        nletras = String.valueOf(divid).length();
        cociente = divid/2;
    }
}

class mulFrame extends  frame{

    public float real,doublereal;
    public int nletras;

    public mulFrame(String R, float r){
        Resultado = R;
        if (r>=0)
            real = r;
        else
            real *= -1;
        doublereal = real*2;
        nletras = String.valueOf(Resultado).length();
    }

}

class ca2Frame extends frame{
    public String CA2;
    public String PosNbin;
    public String CA1;
    public int nletras;
    public int operacion; // 0 - Presentacion del numero

    public ca2Frame(int op, String R, String PosN){

        Resultado = R;
        operacion = op;
        PosNbin = PosN;
        nletras = String.valueOf(Resultado).length();
    }

}