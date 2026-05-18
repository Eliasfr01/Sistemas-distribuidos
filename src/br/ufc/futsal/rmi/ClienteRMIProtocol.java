package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Mensagem;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMIProtocol {
    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            ProtocolService serv = (ProtocolService) reg.lookup("FutsalService");

            // Exemplo: registrar atleta via Mensagem com argumentos em JSON
            String atletaJson = "{\"nome\":\"Falcao\",\"numeroCamisa\":12,\"posicao\":\"Ala\"}";
            Mensagem req = new Mensagem(0, 1, "FutsalService", 1, atletaJson.getBytes("UTF-8"));
            Mensagem rep = serv.doOperation(req);
            System.out.println("Reply: " + new String(rep.getArguments(), "UTF-8"));

            // Exemplo: listar atletas
            Mensagem req2 = new Mensagem(0,2,"FutsalService",2,null);
            Mensagem rep2 = serv.doOperation(req2);
            System.out.println("Atletas JSON: " + new String(rep2.getArguments(), "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
