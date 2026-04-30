package br.ufc.futsal.service;

import java.net.*;

public class TorcedorMulticast implements Runnable {
    @Override
    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket(4321);
            InetAddress grupo = InetAddress.getByName("230.0.0.1");
            NetworkInterface rede = NetworkInterface.getByInetAddress(InetAddress.getByName("localhost"));

            // Entra no grupo de multicast
            socket.joinGroup(new InetSocketAddress(grupo, 4321), rede);
            System.out.println("[INFO] Ouvindo notícias da liga Futsal...");

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacote); // Trava aqui até receber aviso

                String aviso = new String(pacote.getData(), 0, pacote.getLength());
                System.out.println("\n[AVISO DA LIGA]: " + aviso);
            }
        } catch (Exception e) {
            System.err.println("Erro no Multicast: " + e.getMessage());
        }
    }
}