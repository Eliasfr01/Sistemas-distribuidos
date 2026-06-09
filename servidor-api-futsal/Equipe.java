import java.util.List;
public class Equipe {
    public String nomeTime;
    public Tecnico tecnico;
    public List<Atleta> atletas; // Agregação de objetos locais
    public Equipe(String nomeTime, Tecnico tecnico, List<Atleta> atletas) {
        this.nomeTime = nomeTime;
        this.tecnico = tecnico;
        this.atletas = atletas;
    }
}