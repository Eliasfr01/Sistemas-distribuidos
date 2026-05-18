package br.ufc.futsal.model;

import java.io.Serializable;

public class Pessoa implements Serializable {
    private String nome;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
