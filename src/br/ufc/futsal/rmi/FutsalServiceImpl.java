package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Arbitro;
import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Mensagem;
import br.ufc.futsal.model.Resultados;
import br.ufc.futsal.model.Time;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do serviço remoto de futsal.
 * Mantém listas de atletas, times e listeners.
 * Implementa tanto FutsalServiceRemote (interface de alto nível)
 * como ProtocolService (interface de protocolo com mensagens estruturadas).
 */
public class FutsalServiceImpl extends UnicastRemoteObject implements FutsalServiceRemote, ProtocolService {
    private static final long serialVersionUID = 1L;

    private final List<Atleta> atletas = new ArrayList<>();
    private final List<Time> times = new ArrayList<>();
    private final List<String> craqueCandidates = new ArrayList<>();
    private final List<RemoteListener> listeners = new ArrayList<>();

    protected FutsalServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized String registerAtleta(Atleta atleta) throws RemoteException {
        if (atleta == null) {
            return "Erro: Atleta nulo não pode ser registrado.";
        }
        atletas.add(atleta);
        return "Atleta " + atleta.getNome() + " registrado com sucesso.";
    }

    @Override
    public synchronized Atleta[] listAtletas() throws RemoteException {
        return atletas.toArray(new Atleta[0]);
    }

    @Override
    public synchronized Time[] listTimes() throws RemoteException {
        return times.toArray(new Time[0]);
    }

    @Override
    public synchronized String registerTime(RemoteTime time) throws RemoteException {
        if (time == null) {
            return "Erro: Time nulo não pode ser registrado.";
        }
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
        if (timeA == null || timeB == null) {
            throw new RemoteException("Times não podem ser nulos.");
        }
        if (golsA < 0 || golsB < 0) {
            throw new RemoteException("Gols não podem ser negativos.");
        }
        Resultados r = new Resultados(timeA, timeB, golsA, golsB);
        return r; // retornado por valor
    }

    @Override
    public Arbitro getArbitroInfo(String nome, String categoria) throws RemoteException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RemoteException("Nome do árbitro não pode ser vazio.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new RemoteException("Categoria do árbitro não pode ser vazia.");
        }
        return new Arbitro(nome, categoria);
    }

    @Override
    public synchronized void setCraqueCandidates(String[] nomes) throws RemoteException {
        craqueCandidates.clear();
        if (nomes == null) {
            return;
        }
        for (String nome : nomes) {
            if (nome == null) {
                continue;
            }
            String valor = nome.trim();
            if (!valor.isEmpty() && !craqueCandidates.contains(valor)) {
                craqueCandidates.add(valor);
            }
        }
    }

    @Override
    public synchronized String[] listCraqueCandidates() throws RemoteException {
        return craqueCandidates.toArray(new String[0]);
    }

    @Override
    public synchronized void registerListener(RemoteListener listener) throws RemoteException {
        if (listener == null) {
            throw new RemoteException("Listener não pode ser nulo.");
        }
        listeners.add(listener);
        System.out.println("Listener registrado: " + listener);
    }

    @Override
    public synchronized void broadcast(String message) throws RemoteException {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        // Notifica todos os listeners registrados (clientes) com a mensagem
        for (RemoteListener l : new ArrayList<>(listeners)) {
            try {
                l.notify(message);
            } catch (RemoteException e) {
                // remove listeners que falham
                listeners.remove(l);
            }
        }
    }

    @Override
    public synchronized void vote(String login, String craque) throws RemoteException {
        if (login == null || login.trim().isEmpty()) {
            throw new RemoteException("Login não pode ser vazio.");
        }
        if (craque == null || craque.trim().isEmpty()) {
            throw new RemoteException("Craque da partida não pode ser vazio.");
        }
        if (!craqueCandidates.isEmpty() && !craqueCandidates.contains(craque)) {
            throw new RemoteException("Craque informado não está entre os candidatos selecionados.");
        }
        System.out.println("Voto recebido de " + login + ": Craque " + craque);
    }

    /**
     * Implementação do protocolo de requisição-resposta: doOperation.
     * Este método demonstra um protocolo de aplicação explícito:
     * - Cliente monta uma Mensagem com operação e argumentos em JSON
     * - Servidor processa e retorna outra Mensagem com resultado em JSON
     * - Dispatch baseado em referência de objeto e ID de método
     */
    @Override
    public synchronized Mensagem doOperation(Mensagem request) throws RemoteException {
        // request.arguments contém a representação externa (JSON) dos argumentos
        Mensagem reply = new Mensagem();
        reply.setMessageType(1); // reply
        reply.setRequestId(request.getRequestId());
        reply.setObjectReference(request.getObjectReference());
        reply.setMethodId(request.getMethodId());

        try {
            String objRef = request.getObjectReference();
            int method = request.getMethodId();
            String argsJson = request.getArguments() == null ? null : new String(request.getArguments(), "UTF-8");

            switch (objRef) {
                case "FutsalService":
                    switch (method) {
                        case 1: // registerAtleta(json) -> json
                            Atleta a = parseAtletaFromJson(argsJson);
                            String res = registerAtleta(a);
                            String replyJson = "{\"status\":\"ok\",\"message\":\"" + escapeJson(res) + "\"}";
                            reply.setArguments(replyJson.getBytes("UTF-8"));
                            break;
                        case 2: // listAtletas() -> json array
                            Atleta[] lista = listAtletas();
                            String arr = atletasToJson(lista);
                            reply.setArguments(arr.getBytes("UTF-8"));
                            break;
                        case 3: // registerTime(json) -> json
                            // argsJson expected to contain {"nome":"..","cidade":".."}
                            Time t = parseTimeFromJson(argsJson);
                            if (t == null) {
                                throw new RemoteException("Dados do time inválidos.");
                            }
                            times.add(t);
                            String r = "{\"status\":\"ok\",\"message\":\"Time " + escapeJson(t.getNome()) + " registrado\"}";
                            reply.setArguments(r.getBytes("UTF-8"));
                            break;
                        case 4: // computeResult(json) -> json
                            // argsJson expected: {"timeA":{...},"timeB":{...},"golsA":n,"golsB":m}
                            Resultados resu = parseResultadosFromJson(argsJson);
                            if (resu == null || resu.getTimeA() == null || resu.getTimeB() == null) {
                                throw new RemoteException("Dados do resultado inválidos.");
                            }
                            String out = resultadosToJson(resu);
                            reply.setArguments(out.getBytes("UTF-8"));
                            break;
                        case 5: // getArbitroInfo(json) -> json
                            // argsJson: {"nome":"...","categoria":"..."}
                            String nome = parseNomeFromJson(argsJson);
                            String categoria = extractJsonValue(argsJson, "categoria");
                            Arbitro arb = getArbitroInfo(nome, categoria);
                            String arbJson = "{\"nome\":\"" + escapeJson(arb.getNome()) + "\",\"categoria\":\"" + escapeJson(arb.getCategoria()) + "\"}";
                            reply.setArguments(arbJson.getBytes("UTF-8"));
                            break;
                        case 6: // listTimes() -> json array
                            Time[] listaTimes = listTimes();
                            String arrTimes = timesToJson(listaTimes);
                            reply.setArguments(arrTimes.getBytes("UTF-8"));
                            break;
                        default:
                            String err = "{\"error\":\"unknown method\"}";
                            reply.setArguments(err.getBytes("UTF-8"));
                            break;
                    }
                    break;
                default:
                    String err = "{\"error\":\"unknown object\"}";
                    reply.setArguments(err.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            // Em caso de erro, retorna mensagem de erro em JSON
            String err = "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}";
            try { reply.setArguments(err.getBytes("UTF-8")); } catch (Exception ex) {}
        }

        return reply;
    }

    // --- helpers JSON simples (não robustos, apenas para demonstração) ---

    /**
     * Parse de Atleta a partir de JSON.
     * Formato esperado: {"nome":"...","numeroCamisa":n,"posicao":"..."}
     */
    private Atleta parseAtletaFromJson(String json) {
        if (json == null) return null;
        String nome = extractJsonValue(json, "nome");
        String numero = extractJsonValue(json, "numeroCamisa");
        String pos = extractJsonValue(json, "posicao");
        int num = 0; try { num = Integer.parseInt(numero); } catch (Exception e) {}
        return new Atleta(nome, num, pos);
    }

    /**
     * Parse de Time a partir de JSON.
     * Formato esperado: {"nome":"...","cidade":"..."}
     */
    private Time parseTimeFromJson(String json) {
        if (json == null) return null;
        String nome = extractJsonValue(json, "nome");
        String cidade = extractJsonValue(json, "cidade");
        return new Time(nome, cidade);
    }

    /**
     * Parse de Resultados a partir de JSON.
     * Formato esperado: {"timeA":{...},"timeB":{...},"golsA":n,"golsB":m}
     */
    private Resultados parseResultadosFromJson(String json) {
        // naive parsing
        String timeAJson = extractJsonObject(json, "timeA");
        String timeBJson = extractJsonObject(json, "timeB");
        Time timeA = parseTimeFromJson(timeAJson);
        Time timeB = parseTimeFromJson(timeBJson);
        int golsA = 0; int golsB = 0; try { golsA = Integer.parseInt(extractJsonValue(json, "golsA")); } catch(Exception e) {}
        try { golsB = Integer.parseInt(extractJsonValue(json, "golsB")); } catch(Exception e) {}
        return new Resultados(timeA, timeB, golsA, golsB);
    }

    private String parseNomeFromJson(String json) {
        return extractJsonValue(json, "nome");
    }

    /**
     * Serializa Atleta[] para JSON array.
     */
    private String atletasToJson(Atleta[] lista) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i=0;i<lista.length;i++){
            Atleta a = lista[i];
            if (a == null) {
                sb.append("null");
            } else {
                sb.append("{\"nome\":\"").append(escapeJson(a.getNome())).append("\",\"numeroCamisa\":").append(a.getNumeroCamisa()).append(",\"posicao\":\"").append(escapeJson(a.getPosicao())).append("\"}");
            }
            if (i<lista.length-1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Serializa Time[] para JSON array.
     */
    private String timesToJson(Time[] lista) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < lista.length; i++) {
            Time t = lista[i];
            if (t == null) {
                sb.append("null");
            } else {
                sb.append("{\"nome\":\"").append(escapeJson(t.getNome())).append("\",\"cidade\":\"").append(escapeJson(t.getCidade())).append("\"}");
            }
            if (i < lista.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Serializa Resultados para JSON.
     */
    private String resultadosToJson(Resultados r) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"timeA\":{");
        sb.append("\"nome\":\"").append(escapeJson(r.getTimeA().getNome())).append("\",\"cidade\":\"").append(escapeJson(r.getTimeA().getCidade())).append("\"},");
        sb.append("\"timeB\":{");
        sb.append("\"nome\":\"").append(escapeJson(r.getTimeB().getNome())).append("\",\"cidade\":\"").append(escapeJson(r.getTimeB().getCidade())).append("\"},");
        sb.append("\"golsA\":").append(r.getGolsA()).append(",\"golsB\":").append(r.getGolsB()).append("}");
        return sb.toString();
    }

    /**
     * Extrai um valor simples (string ou número) de uma estrutura JSON.
     * Método naive, apenas para demonstração.
     */
    private String extractJsonValue(String json, String key) {
        if (json == null) return null;
        String pat = "\""+key+"\"\s*:\s*";
        int i = json.indexOf("\""+key+"\"");
        if (i==-1) return null;
        int colon = json.indexOf(':', i);
        if (colon==-1) return null;
        int start = colon+1;
        // skip spaces
        while (start<json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (start<json.length() && json.charAt(start)=='\"') {
            start++;
            int end = json.indexOf('"', start);
            if (end==-1) end = json.length();
            return json.substring(start, end);
        } else {
            // number or bare
            int end = start;
            while (end<json.length() && ",}] ".indexOf(json.charAt(end))==-1) end++;
            return json.substring(start,end).trim();
        }
    }

    /**
     * Extrai um objeto JSON aninhado (ex: "timeA":{...}).
     * Método naive, apenas para demonstração.
     */
    private String extractJsonObject(String json, String key) {
        if (json == null) return null;
        int idx = json.indexOf('"'+key+'"');
        if (idx==-1) idx = json.indexOf('"'+key+'"');
        int colon = json.indexOf(':', idx);
        if (colon==-1) return null;
        int start = json.indexOf('{', colon);
        if (start==-1) return null;
        int brace = 0;
        for (int i=start;i<json.length();i++){
            if (json.charAt(i)=='{') brace++;
            else if (json.charAt(i)=='}') brace--;
            if (brace==0) return json.substring(start, i+1);
        }
        return null;
    }

    /**
     * Escapa caracteres especiais para JSON.
     */
    private String escapeJson(String s) { 
        if (s==null) return ""; 
        return s.replace("\\","\\\\").replace("\"","\\\""); 
    }

}

