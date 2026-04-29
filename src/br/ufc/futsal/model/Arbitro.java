package br.ufc.futsal.model;

public class Arbitro extends Pessoa {
    private String categoria;

    public Arbitro(String nome, String categoria) {
        super(nome);
        this.categoria = categoria; //Ex: FIFA, Nacional, Regional
    }

    public String getCategoria() {
        return categoria;
    }

}
