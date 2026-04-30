package br.ufc.futsal.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AdministradorService {
    public static void enviarAviso(String mensagem) {
        try {
            // No UDP usamos DatagramSocket
            DatagramSocket socket = new DatagramSocket();

            // Endereço de Multicast (IPs de 224.0.0.0 a 239.255.255.255)
            InetAddress grupo = InetAddress.getByName("230.0.0.1");
            byte[] buffer = mensagem.getBytes();

            // Criamos o "pacote" com os dados, o tamanho, o IP do grupo e a porta
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, grupo, 4321);

            socket.send(pacote);
            System.out.println("Aviso enviado via Multicast: " + mensagem);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}