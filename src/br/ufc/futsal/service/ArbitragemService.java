package br.ufc.futsal.service;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Time;

public class ArbitragemService {
    private int cartoes = 0;
    private int faltas = 0;

    public boolean aplicarCartao(Atleta atleta){
        if (atleta == null) return false;
        System.out.println("Aplicando cartão amarelo para: " + atleta.getNome());
        cartoes++;
        return false;
    }

    public void marcarFalta(String timeFaltoso) {
        if (timeFaltoso == null || timeFaltoso.isEmpty()) return;
        System.out.println("Falta cometida pelo: " + timeFaltoso);
        faltas++;
    }

    public int getTotalCartoes() {
        return cartoes;
    }

    public int getTotalFaltas() {
        return faltas;
    }
}
