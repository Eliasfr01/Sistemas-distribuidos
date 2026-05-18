package br.ufc.futsal.model;

import java.io.Serializable;

public class Resultados implements Serializable {
    private Time timeA;
    private Time timeB;
    private int golsA;
    private int golsB;

    public Resultados(Time timeA, Time timeB, int golsA, int golsB) {
        this.timeA = timeA;
        this.timeB = timeB;
        this.golsA = golsA;
        this.golsB = golsB;
    }

    public void exibirResultados(){
        System.out.println("Fim de jogo! Placar final: "
                + timeA.getNome() + " " + golsA + " X "
                + golsB + " " + timeA.getNome());
    }

    public Time getTimeA() { return timeA; }
    public Time getTimeB() { return timeB; }
    public int getGolsA() { return golsA; }
    public int getGolsB() { return golsB; }
}
