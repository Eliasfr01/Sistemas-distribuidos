package br.ufc.futsal.service;

import br.ufc.futsal.rmi.FutsalServiceRemote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AdministradorService {
    public static void enviarAviso(String mensagem) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            FutsalServiceRemote serv = (FutsalServiceRemote) reg.lookup("FutsalService");
            serv.broadcast(mensagem);
            System.out.println("✓ Notificação enviada aos clientes: " + mensagem);
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}