package br.ufc.futsal.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteTime extends Remote {
    String getNome() throws RemoteException;
    String getCidade() throws RemoteException;
}
