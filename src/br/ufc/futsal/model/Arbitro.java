package br.ufc.futsal.model;

import java.io.Serializable;

public class Arbitro extends Pessoa implements Serializable {
    private String categoria;

    public Arbitro(String nome, String categoria) {
        super(nome);
        this.categoria = categoria; //Ex: FIFA, Nacional, Regional
    }

    public String getCategoria() {
        return categoria;
    }

}
