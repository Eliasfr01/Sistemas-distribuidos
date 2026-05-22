package br.ufc.futsal.model;

import java.io.Serializable;

/**
 * Encapsula o resultado de uma partida de futsal.
 * É transmitido por valor do servidor para o cliente.
 */
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
                + golsB + " " + timeB.getNome());
    }

    public Time getTimeA() { return timeA; }
    public Time getTimeB() { return timeB; }
    public int getGolsA() { return golsA; }
    public int getGolsB() { return golsB; }
}
