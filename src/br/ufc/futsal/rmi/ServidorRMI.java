package br.ufc.futsal.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI {
    public static void main(String[] args) {
        try {
            FutsalServiceImpl serv = new FutsalServiceImpl();

            // Cria o registro RMI na porta 1099
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("FutsalService", serv);

            System.out.println("Servidor RMI Futsal rodando e registrado como 'FutsalService'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
