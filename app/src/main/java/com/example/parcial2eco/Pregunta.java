package com.example.parcial2eco;

public class Pregunta {

    private String pregunta;
    private int puntaje,promedio,totalPersonas;

    public Pregunta(String pregunta, int puntaje, int promedio,int totalPersonas) {
        this.pregunta = pregunta;
        this.puntaje = puntaje;
        this.promedio = promedio;
        this.totalPersonas=totalPersonas;
    }

    public Pregunta(){

    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }


    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getPromedio() {
        return promedio;
    }

    public void setPromedio(int promedio) {
        this.promedio = promedio;
    }

    public int getTotalPersonas() {
        return totalPersonas;
    }

    public void setTotalPersonas(int totalPersonas) {
        this.totalPersonas = totalPersonas;
    }
}

