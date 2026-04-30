package br.ufc.futsal.service;

import br.ufc.futsal.model.Atleta;

import java.io.IOException;
import java.io.OutputStream;

public class AtletaOutputStream extends OutputStream {
    private Atleta[] listaAtletas;
    private int quantidade;
    private OutputStream destino;

    public AtletaOutputStream(Atleta[] listaAtletas, int quantidade, OutputStream destino) {
        this.listaAtletas = listaAtletas;
        this.quantidade = quantidade;
        this.destino = destino;
    }

    public void enviarDados()  throws IOException {
        for  (int i = 0; i < quantidade; i++) {
            Atleta a =  listaAtletas[i];

            String dados = a.getNome() + "," + a.getNumeroCamisa() + "," + a.getPosicao();

            byte[] buffer = dados.getBytes();

            this.destino.write(buffer.length);
            this.destino.write(buffer);
        }
    }

    @Override
    public void write(int b) throws IOException {
        destino.write(b);
    }

}
