package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.service.AtletaOutputStream;
import java.io.*;
import java.net.*;

public class ClienteAtleta {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            // 1. EMPACOTAR a mensagem de request
            Atleta atleta = new Atleta("Falcao", 12, "Ala");
            Atleta[] lista = {atleta};
            
            AtletaOutputStream output = new AtletaOutputStream(lista, 1, socket.getOutputStream());
            output.enviarDados();
            System.out.println("Request enviado...");

            // 2. DESEMPACOTAR a mensagem de reply do servidor
            DataInputStream replyInput = new DataInputStream(socket.getInputStream());
            String resposta = replyInput.readUTF();
            System.out.println("Resposta do Servidor: " + resposta);

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}