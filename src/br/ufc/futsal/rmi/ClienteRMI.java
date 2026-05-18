package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Resultados;
import br.ufc.futsal.model.Time;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMI {
    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            FutsalServiceRemote serv = (FutsalServiceRemote) reg.lookup("FutsalService");

            // Teste: registrar atleta (passagem por valor)
            Atleta a = new Atleta("Ricardinho", 10, "Ala");
            String res = serv.registerAtleta(a);
            System.out.println(res);

            // Listar atletas
            Atleta[] lista = serv.listAtletas();
            System.out.println("Atletas registrados: " + lista.length);

            // Teste: criar RemoteTime e registrar por referência
            Time t = new Time("Futsal FC", "Fortaleza");
            RemoteTimeImpl rt = new RemoteTimeImpl(t);
            System.out.println(serv.registerTime(rt));

            // Teste: calcular resultado (retorno por valor)
            Resultados r = serv.computeResult(t, t, 3, 2);
            r.exibirResultados();

            // Teste: obter árbitro
            System.out.println(serv.getArbitroInfo("João").getCategoria());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
