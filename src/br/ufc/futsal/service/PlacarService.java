package br.ufc.futsal.service;

import br.ufc.futsal.model.Time;

public class PlacarService {
    private int golsTimeA = 0;
    private int golsTimeB = 0;

    public void registrarGol(Time time){
        if(time.equals("A")){
            golsTimeA++;
        } else {
            golsTimeB++;
        }
    }

    public String consultarPlacar(){
        return "Placar: Time A " + golsTimeA + " X " + golsTimeB + " Time B ";
    }
}
