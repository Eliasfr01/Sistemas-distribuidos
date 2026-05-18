package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.service.AtletaOutputStream;
import java.io.*;
import java.net.*;

public class ClienteAtleta {
    public static void main(String[] args) {
        try {
            // Agora usamos RMI para registrar o atleta
            java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
            br.ufc.futsal.rmi.FutsalServiceRemote serv = (br.ufc.futsal.rmi.FutsalServiceRemote) reg.lookup("FutsalService");

            Atleta atleta = new Atleta("Falcao", 12, "Ala");
            String res = serv.registerAtleta(atleta);
            System.out.println("Resposta do Servidor (RMI): " + res);
        } catch (Exception e) {
            System.err.println("Erro no cliente RMI: " + e.getMessage());
        }
    }
}