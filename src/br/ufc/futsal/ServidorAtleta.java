package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.service.AtletaInputStream;
import java.io.*;
import java.net.*;

public class ServidorAtleta {
    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor de Futsal aguardando atleta...");

            while (true) { // Fica ligado para sempre
                try (Socket conexao = servidor.accept()) {
                    // 1. DESEMPACOTAR a requisição (Request) do cliente
                    AtletaInputStream input = new AtletaInputStream(conexao.getInputStream());
                    Atleta recebido = input.lerAtleta();
                    
                    System.out.println("Recebido: " + recebido.getNome());

                    // 2. EMPACOTAR a resposta (Reply) para o cliente
                    DataOutputStream reply = new DataOutputStream(conexao.getOutputStream());
                    reply.writeUTF("Atleta " + recebido.getNome() + " cadastrado com sucesso!");
                    reply.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}