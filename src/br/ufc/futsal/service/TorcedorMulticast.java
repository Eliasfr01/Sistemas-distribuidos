package br.ufc.futsal.service;

import br.ufc.futsal.rmi.RemoteListener;
import br.ufc.futsal.rmi.RemoteListener;
import br.ufc.futsal.rmi.FutsalServiceRemote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TorcedorMulticast extends UnicastRemoteObject implements Runnable, RemoteListener {
    public TorcedorMulticast() throws Exception { super(); }

    @Override
    public void run() {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            FutsalServiceRemote serv = (FutsalServiceRemote) reg.lookup("FutsalService");
            serv.registerListener(this);
            System.out.println("[INFO] Registrado como listener RMI da liga Futsal...");

            // fica vivo para receber callbacks
            synchronized (this) { this.wait(); }
        } catch (Exception e) {
            System.err.println("Erro ao registrar listener RMI: " + e.getMessage());
        }
    }

    @Override
    public void notify(String message) {
        System.out.println("\n[AVISO DA LIGA]: " + message);
    }
}