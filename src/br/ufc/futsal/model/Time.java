package br.ufc.futsal.model;

import java.io.Serializable;

public class Time implements Serializable {
    private String nome;
    private String cidade;

    public Time(String nome, String cidade) {
        this.nome = nome;
        this.cidade = cidade;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }
}
