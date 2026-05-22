package br.ufc.futsal.service;

import br.ufc.futsal.model.Time;

public class PlacarService {
    private final Time timeA;
    private final Time timeB;
    private int golsTimeA = 0;
    private int golsTimeB = 0;

    public PlacarService(Time timeA, Time timeB) {
        this.timeA = timeA;
        this.timeB = timeB;
    }

    public void registrarGol(Time time){
        if (time == null) {
            return;
        }

        if (timeA != null && timeA.getNome().equalsIgnoreCase(time.getNome())){
            golsTimeA++;
        } else if (timeB != null && timeB.getNome().equalsIgnoreCase(time.getNome())) {
            golsTimeB++;
        }
    }

    public String consultarPlacar(){
        String nomeA = timeA != null ? timeA.getNome() : "Time A";
        String nomeB = timeB != null ? timeB.getNome() : "Time B";
        return "Placar: " + nomeA + " " + golsTimeA + " X " + golsTimeB + " " + nomeB;
    }
}
