package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Arbitro;
import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Resultados;
import br.ufc.futsal.model.Time;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FutsalServiceRemote extends Remote {
    String registerAtleta(Atleta atleta) throws RemoteException;
    Atleta[] listAtletas() throws RemoteException;
    String registerTime(RemoteTime time) throws RemoteException;
    Resultados computeResult(Time timeA, Time timeB, int golsA, int golsB) throws RemoteException;
    Arbitro getArbitroInfo(String nome) throws RemoteException;

    // Novas operações para substituir uso de sockets/multicast
    void registerListener(RemoteListener listener) throws RemoteException;
    void broadcast(String message) throws RemoteException;
    void vote(String login, int candidato) throws RemoteException;
}
