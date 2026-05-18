package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Time;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteTimeImpl extends UnicastRemoteObject implements RemoteTime {
    private static final long serialVersionUID = 1L;
    private Time time;

    public RemoteTimeImpl(Time time) throws RemoteException {
        super();
        this.time = time;
    }

    @Override
    public String getNome() throws RemoteException {
        return time.getNome();
    }

    @Override
    public String getCidade() throws RemoteException {
        return time.getCidade();
    }
}
