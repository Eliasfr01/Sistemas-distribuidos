package br.ufc.futsal.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI {
    public static void main(String[] args) {
        try {
            FutsalServiceImpl serv = new FutsalServiceImpl();

            Registry reg;
            try {
                // tenta criar o registry local; se já existir, reutiliza o atual
                reg = LocateRegistry.createRegistry(1099);
                System.out.println("Registry RMI criado na porta 1099");
            } catch (Exception registryError) {
                reg = LocateRegistry.getRegistry(1099);
                System.out.println("Registry RMI já estava ativo; usando o existente na porta 1099");
            }

            reg.rebind("FutsalService", serv);

            System.out.println("Servidor RMI do futsal pronto e registrado como 'FutsalService'");
            System.out.println("Use o cliente em outra execução/aba da IDE para testar as chamadas remotas.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
