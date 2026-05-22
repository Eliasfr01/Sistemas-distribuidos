package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Arbitro;
import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Resultados;
import br.ufc.futsal.model.Time;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface remota do serviço de futsal.
 * Define as operações que podem ser chamadas pelos clientes distribuídos.
 */
public interface FutsalServiceRemote extends Remote {
    /**
     * Registra um novo atleta no servidor (passagem por valor).
     */
    String registerAtleta(Atleta atleta) throws RemoteException;

    /**
     * Lista todos os atletas cadastrados.
     */
    Atleta[] listAtletas() throws RemoteException;

    /**
     * Lista todos os times cadastrados.
     */
    Time[] listTimes() throws RemoteException;

    /**
     * Registra um time via objeto remoto (passagem por referência).
     */
    String registerTime(RemoteTime time) throws RemoteException;

    /**
     * Calcula e retorna o resultado de uma partida.
     */
    Resultados computeResult(Time timeA, Time timeB, int golsA, int golsB) throws RemoteException;

    /**
     * Consulta informações de um árbitro.
     */
    Arbitro getArbitroInfo(String nome, String categoria) throws RemoteException;

    /**
     * Define os jogadores que disputarão o craque do jogo.
     */
    void setCraqueCandidates(String[] nomes) throws RemoteException;

    /**
     * Lista os jogadores candidatos ao craque do jogo.
     */
    String[] listCraqueCandidates() throws RemoteException;

    /**
     * Registra um cliente como ouvinte de avisos (callback remoto).
     */
    void registerListener(RemoteListener listener) throws RemoteException;

    /**
     * Envia uma mensagem de aviso para todos os listeners registrados.
     */
    void broadcast(String message) throws RemoteException;

    /**
     * Registra um voto de torcedor.
     */
    void vote(String login, String craque) throws RemoteException;
}
