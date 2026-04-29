package br.ufc.futsal.service;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Time;

public class ArbitragemService {
    public boolean aplicarCartao(Atleta atleta){
        System.out.println("Aplicando cartão amarelo para: " + atleta.getNome());
        return false;
    }

    public void marcarFalta(String timeFaltoso) {
        System.out.println("Falta cometida pelo: " + timeFaltoso);
    }
}
