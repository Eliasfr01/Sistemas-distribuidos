package br.ufc.futsal;

import br.ufc.futsal.service.TorcedorMulticast;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteTorcedor {
    public static void main(String[] args) {
        // 1. Inicia a thread para ouvir avisos via RMI
        try {
            TorcedorMulticast listener = new TorcedorMulticast();
            new Thread(listener).start();

            // 2. Votação via RMI
            java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
            br.ufc.futsal.rmi.FutsalServiceRemote serv = (br.ufc.futsal.rmi.FutsalServiceRemote) reg.lookup("FutsalService");
            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                System.out.println("Digite seu login:");
                String login = scanner.nextLine();
                System.out.println("Vote no craque: 1-Falcao, 2-Ricardinho");
                int voto = scanner.nextInt();
                serv.vote(login, voto);
                System.out.println("Voto enviado via RMI! Aguarde o encerramento da liga.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}