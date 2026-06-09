console.log("\n--- CLIENTE JAVASCRIPT CONSUMINDO O WEB SERVICE ---");

fetch("http://localhost:8080/atletas")
    .then(res => res.json())
    .then(atletas => {
        atletas.forEach(a => {
            console.log(`-> Jogador: ${a.nome} | Camisa: ${a.numero} | Posição: ${a.posicao}`);
        });
    })
    .catch(err => console.error("Erro na API:", err));