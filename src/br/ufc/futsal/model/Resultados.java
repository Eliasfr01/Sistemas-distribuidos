package br.ufc.futsal.model;

public class Resultados {
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
}
