package br.ufc.futsal.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FutsalService {
    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor de Futsal rodando na porta 5000...");

            while (true) {
                // O accept() bloqueia até alguém conectar
                Socket cliente = servidor.accept();
                System.out.println("Novo torcedor conectado: " + cliente.getInetAddress());

                // Criamos uma nova Thread para este cliente
                // Assim, o servidor volta para o accept() imediatamente para esperar o próximo
                new Thread(new ManipuladorTorcedor(cliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Esta classe é o que cada "Thread" vai executar
class ManipuladorTorcedor implements Runnable {
    private Socket socket;

    public ManipuladorTorcedor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Aqui dentro você implementa o Login e a Votação (TCP Unicast)
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());

            saida.writeUTF("Bem-vindo! Digite seu login:");
            String login = entrada.readUTF();

            saida.writeUTF("Olá " + login + "! Vote no craque: 1-Falcao, 2-Ricardinho");
            int voto = entrada.readInt();

            System.out.println("Voto recebido de " + login + ": Candidato " + voto);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
