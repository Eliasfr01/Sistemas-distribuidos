package br.ufc.futsal;

import br.ufc.futsal.service.TorcedorMulticast;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteTorcedor {
    public static void main(String[] args) {
        // 1. Inicia a thread para ouvir avisos UDP (Multicast)
        new Thread(new TorcedorMulticast()).start();

        // 2. Conexão Unicast (TCP) para Votação
        try (Socket socket = new Socket("localhost", 5000);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            // Processo de Login
            System.out.println("Servidor: " + entrada.readUTF());
            saida.writeUTF(scanner.nextLine());

            // Processo de Votação
            System.out.println("Servidor: " + entrada.readUTF());
            saida.writeInt(scanner.nextInt());

            System.out.println("Voto enviado! Aguarde o encerramento da liga.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}