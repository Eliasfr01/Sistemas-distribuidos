package br.ufc.futsal.model;

import java.io.Serializable;

public class Atleta extends Pessoa implements Serializable{
    private int numeroCamisa;
    private String posicao;

    public Atleta(String nome, int numeroCamisa, String posicao) {
        super(nome);
        this.numeroCamisa = numeroCamisa;
        this.posicao = posicao;
    }

    public int getNumeroCamisa() {
        return numeroCamisa;
    }
    public String getPosicao(){
        return posicao;
    }
}
