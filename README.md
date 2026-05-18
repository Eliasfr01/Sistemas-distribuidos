Sistema de Gerenciamento de Campeonatos de Futsal

Este projeto consiste em um sistema distribuído para a gestão e consulta de campeonatos de futsal, desenvolvido para a disciplina de Sistemas Distribuídos.

Implementação RMI (adicionada):

- `br.ufc.futsal.rmi.ServidorRMI` — inicia o serviço RMI e registra `FutsalService`.
- `br.ufc.futsal.rmi.ClienteRMI` — cliente de teste que invoca métodos remotos.

Como compilar e executar (Windows PowerShell):

```
# compilar
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName } ; javac -d out $files

# em uma aba: iniciar servidor
java -cp out br.ufc.futsal.rmi.ServidorRMI

# em outra aba: executar cliente de teste
java -cp out br.ufc.futsal.rmi.ClienteRMI
```

Notas:
- Entidades principais (`Atleta`, `Time`, `Resultados`, `Arbitro`, `Pessoa`) agora implementam `Serializable` para passagem por valor.
- `RemoteTime` e `RemoteTimeImpl` demonstram passagem por referência (objeto remoto passado ao servidor).

