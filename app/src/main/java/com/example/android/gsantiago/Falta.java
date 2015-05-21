package com.example.android.gsantiago;

public class Falta
{
	private String pfalta;
	private String pcubre;
    private String asignatura;

	public Falta(String falta, String cubre, String asig){
		pfalta = falta;
		pcubre = cubre;
        asignatura=asig;
	}
    public Falta(){
        pfalta = "nadie";
        pcubre = "nadie";
        asignatura="ninguna";
    }
	public String getProfesorCubre(){
		return pcubre;
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

}
