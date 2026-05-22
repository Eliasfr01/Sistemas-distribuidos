# Sistema de Gerenciamento de Campeonatos de Futsal

Projeto de Sistemas Distribuídos para cadastro e consulta de dados de futsal usando **RMI**.

## Fluxo recomendado

O caminho principal hoje é este:

1. `br.ufc.futsal.rmi.ServidorRMI` — sobe o registry RMI e registra o serviço `FutsalService`.
2. `br.ufc.futsal.rmi.ClienteRMI` — cliente principal com menu interativo para cadastrar, listar e consultar dados.
3. `br.ufc.futsal.rmi.ClienteRMIProtocol` — menu interativo baseado em `Mensagem` para testar o protocolo JSON.
4. `br.ufc.futsal.ClienteTorcedor` — votação livre do craque da partida e recebimento de avisos.

## Funcionalidades principais

- cadastro de atletas
- listagem de atletas cadastrados
- cadastro de times
- listagem de times cadastrados
- cálculo/consulta de resultados
- consulta de árbitro
- envio de avisos para torcedores conectados
- registro de votos de torcedores

## Organização do código

- `src/br/ufc/futsal/model` → entidades do domínio (`Atleta`, `Time`, `Resultados`, `Arbitro`, `Pessoa`, `Mensagem`)
- `src/br/ufc/futsal/rmi` → comunicação remota, protocolo e servidor RMI
- `src/br/ufc/futsal/service` → serviços de apoio e classes auxiliares
- `src/br/ufc/futsal` → clientes e consoles de teste

## Como executar no Windows PowerShell

### Compilar

```powershell
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
```

### Executar pela IDE

Se você estiver usando IntelliJ IDEA, Eclipse ou NetBeans:

1. Abra o projeto pela pasta raiz `trabalhoSD`.
2. Marque `src` como diretório de fontes, se a IDE não fizer isso automaticamente.
3. Execute primeiro `br.ufc.futsal.rmi.ServidorRMI`.
4. Em seguida execute `br.ufc.futsal.rmi.ClienteRMI` ou `br.ufc.futsal.rmi.ClienteRMIProtocol`.

### Executar pelo terminal

```powershell
java -cp out br.ufc.futsal.rmi.ServidorRMI
java -cp out br.ufc.futsal.rmi.ClienteRMI
```

## Observações importantes

- As entidades principais implementam `Serializable`, então podem ser enviadas por valor no RMI.
- `RemoteTime` e `RemoteTimeImpl` mostram passagem por referência com objeto remoto.
- O servidor mantém os dados em memória durante a execução; as informações agora são digitadas pelo usuário nos clientes.
- As classes `FutsalService`, `ServidorAtleta` e outros pontos antigos foram mantidos apenas como referência/legado; o fluxo atual é o RMI.

## Documentação adicional

Este projeto inclui vários documentos de suporte para facilitar o uso e a compreensão:

- **`GUIA_EXECUCAO.md`** — como executar o sistema pela IDE ou terminal
- **`ESTRUTURA_FUNCIONALIDADES.md`** — mapa detalhado das funcionalidades e organização do código
- **`DICAS_APRESENTACAO.md`** — roteiro pronto para apresentação de 5 minutos
- **`ROTEIRO_VISUAL.md`** — roteiro visual com o que mostrar em cada slide
- **`CHECKLIST_TESTES.md`** — passos para validar tudo antes da apresentação

## Comece por aqui

Se é sua primeira vez com este projeto:

1. Leia `GUIA_EXECUCAO.md` para aprender como rodá-lo
2. Execute `ServidorRMI` e depois `ClienteRMI` para usar o menu interativo
3. Leia `ESTRUTURA_FUNCIONALIDADES.md` para entender o código
4. Se vai apresentar, use `DICAS_APRESENTACAO.md` e `ROTEIRO_VISUAL.md`

