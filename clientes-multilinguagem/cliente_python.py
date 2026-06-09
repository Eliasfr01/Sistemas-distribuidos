import urllib.request
import json

print("\n--- CLIENTE PYTHON CONSUMINDO O WEB SERVICE ---")
try:
    url = "http://localhost:8080/atletas"
    response = urllib.request.urlopen(url)
    dados = response.read().decode('utf-8')
    atletas = json.loads(dados)

    for a in atletas:
        print(f"-> Jogador: {a['nome']} | Camisa: {a['numero']} | Posição: {a['posicao']}")
except Exception as e:
    print("Erro ao conectar na API:", e)