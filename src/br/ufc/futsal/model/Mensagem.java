package br.ufc.futsal.model;

import java.io.Serializable;

/**
 * Representa uma mensagem de requisição/resposta no protocolo RMI.
 * Encapsula: tipo (request/reply), ID, referência de objeto, ID do método e argumentos em JSON.
 * Essa estrutura demonstra um protocolo explícito de comunicação entre cliente e servidor.
 */
public class Mensagem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int messageType; // 0 = Request, 1 = Reply
    private int requestId;
    private String objectReference;
    private int methodId;
    private byte[] arguments; // representação externa (JSON)

    public Mensagem() {}

    public Mensagem(int messageType, int requestId, String objectReference, int methodId, byte[] arguments) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.objectReference = objectReference;
        this.methodId = methodId;
        this.arguments = arguments;
    }

    public int getMessageType() { return messageType; }
    public void setMessageType(int messageType) { this.messageType = messageType; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getObjectReference() { return objectReference; }
    public void setObjectReference(String objectReference) { this.objectReference = objectReference; }

    public int getMethodId() { return methodId; }
    public void setMethodId(int methodId) { this.methodId = methodId; }

    public byte[] getArguments() { return arguments; }
    public void setArguments(byte[] arguments) { this.arguments = arguments; }
}
