package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Arbitro;
import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Resultados;
import br.ufc.futsal.model.Time;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class FutsalServiceImpl extends UnicastRemoteObject implements FutsalServiceRemote {
    private static final long serialVersionUID = 1L;

    private final List<Atleta> atletas = new ArrayList<>();
    private final List<Time> times = new ArrayList<>();

    protected FutsalServiceImpl() throws RemoteException {
        super();
        // exemplo: adicionar um árbitro e um time inicial
    }

    @Override
    public synchronized String registerAtleta(Atleta atleta) throws RemoteException {
        atletas.add(atleta);
        return "Atleta " + atleta.getNome() + " registrado com sucesso.";
    }

    @Override
    public synchronized Atleta[] listAtletas() throws RemoteException {
        return atletas.toArray(new Atleta[0]);
    }

    @Override
    public synchronized String registerTime(RemoteTime time) throws RemoteException {
        try {
            Time t = new Time(time.getNome(), time.getCidade());
            times.add(t);
            return "Time " + t.getNome() + " registrado (por referência)";
        } catch (RemoteException e) {
            throw e;
        }
    }

    @Override
    public Resultados computeResult(Time timeA, Time timeB, int golsA, int golsB) throws RemoteException {
        Resultados r = new Resultados(timeA, timeB, golsA, golsB);
        return r; // retornado por valor
    }

    @Override
    public Arbitro getArbitroInfo(String nome) throws RemoteException {
        // retorna um árbitro fictício para exemplo
        return new Arbitro(nome, "Nacional");
    }
}
