package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Mensagem;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProtocolService extends Remote {
    // Recebe uma Mensagem (request) e retorna uma Mensagem (reply)
    Mensagem doOperation(Mensagem request) throws RemoteException;
}
