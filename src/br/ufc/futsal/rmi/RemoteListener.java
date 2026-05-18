package br.ufc.futsal.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteListener extends Remote {
    void notify(String message) throws RemoteException;
}
