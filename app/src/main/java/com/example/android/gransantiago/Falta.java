package com.example.android.gransantiago;

public class Falta
{
	private String pfalta;
	private String pcubre;
    private String asignatura;
    private String nfalta;
    private String ncubre;
    private String sesion;

    public Falta(String falta, String cubre, String asig, String nfalta,String ncubre){
		pfalta = falta;
		pcubre = cubre;
        asignatura=asig;
        nfalta=nfalta;
        ncubre=ncubre;
	}
    public Falta(){
        pfalta = "nadie";
        pcubre = "nadie";
        asignatura="ninguna";
        nfalta="nadie";
        ncubre="nadie";
        sesion="0";
    }
    public String getSesion(){
        return sesion;
    }
	public String getProfesorCubre(){
		return pcubre;
	}
    public String getNombrefalta(){
        return nfalta;
    }
    public String getNombreCubre(){
        return ncubre;
    }

    public String getProfesorFalta(){
		return pfalta ;
	}

    public String getAsignatura(){
        return asignatura;
    }
    public void setProfesorCubre(String profesor){
        pcubre=profesor;
    }

    public void setProfesorFalta(String profesor){
        pfalta=profesor;return;
    }

    public void setAsignatura(String asig){
        asignatura=asig;
    }
    public void setSesion(String ses){
        sesion=ses;
    }

}