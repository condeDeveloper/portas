# portas

O problema das três portas (Monty Hall) generalizado, em Java 21: o prêmio está
atrás de uma porta, você escolhe uma, o apresentador abre outras e pergunta se
você quer trocar.

Nada aqui é sorteado. O programa **enumera todos os jogos possíveis** (onde está
o prêmio × qual porta você escolheu × quais portas ele abriu), cada um com o
peso exato dele, e soma em **fração**: `2/3`, não `0,6666667`. Com ponto
flutuante dá para dizer que duas estratégias empatam até a décima quinta casa;
com fração, empate é empate.

```
mvn -B -ntp verify
mvn -B -ntp exec:java -Dexec.args="portas"   # (ou apresentador, abertas)
```

## Três portas, uma aberta

Apresentador clássico: ele sabe onde está o prêmio e abre **uma** porta vazia.

| portas | ficando | trocando | troca ganha | em decimal |
|---:|---:|---:|---:|---:|
| **3** | **1/3** | **2/3** | **2,00×** | 66,67% |
| 4 | 1/4 | 3/8 | 1,50× | 37,50% |
| 5 | 1/5 | 4/15 | 1,33× | 26,67% |
| 6 | 1/6 | 5/24 | 1,25× | 20,83% |
| 7 | 1/7 | 6/35 | 1,20× | 17,14% |
| 8 | 1/8 | 7/48 | 1,17× | 14,58% |
| 9 | 1/9 | 8/63 | 1,14× | 12,70% |
| 10 | 1/10 | 9/80 | 1,13× | 11,25% |

Com três portas, trocar ganha o dobro. Com dez portas e **uma** aberta, trocar
ganha 11,25%: **menos** que ficar com três portas, e a vantagem sobre ficar cai
de 2,00× para 1,13×. O motivo está na última coluna da tabela de baixo: sobram
oito portas fechadas, e a chance que estava nas outras nove se divide entre elas.

## O apresentador é o problema inteiro

Mesmas três portas, mesmo prêmio, mesma escolha, mesma porta aberta na sua
frente. A única coisa que muda é **o que ele sabe e como ele decide**:

| apresentador | ficando | trocando | ofereceu | revelou |
|---|---:|---:|---:|---:|
| sabe onde está | 1/3 | **2/3** | 1 | 0 |
| abre ao acaso | 1/3 | 1/3 | 2/3 | 1/3 |
| só oferece se você acertou | 1/3 | **0** | 1/3 | 0 |

As colunas `ficando` e `trocando` são a chance sobre **todos** os jogos. Dentro
dos jogos em que houve oferta, a conta fica assim:

| apresentador | ficar | trocar |
|---|---:|---:|
| sabe onde está | 33,33% | **66,67%** |
| abre ao acaso | 50,00% | 50,00% |
| só oferece se você acertou | **100,00%** | 0,00% |

O que abre ao acaso revela o prêmio em **1/3** dos jogos e acaba com a brincadeira
ali. Nos 2/3 que sobram, a cena é idêntica à do apresentador que sabe: uma porta
vazia aberta, duas fechadas, a pergunta feita. E a resposta certa é outra: **tanto
faz**. A porta vazia que apareceu por sorte não carrega informação nenhuma; a que
ele escolheu a dedo carrega.

O terceiro é pior: contra ele, trocar perde **sempre**, não às vezes.

## Quantas portas ele abre

Dez portas, apresentador que sabe, abrindo `k` vazias:

| abertas | sobram fechadas | ficando | trocando | troca ganha |
|---:|---:|---:|---:|---:|
| 1 | 8 | 1/10 | 9/80 | 1,13× |
| 2 | 7 | 1/10 | 9/70 | 1,29× |
| 3 | 6 | 1/10 | 3/20 | 1,50× |
| 4 | 5 | 1/10 | 9/50 | 1,80× |
| 5 | 4 | 1/10 | 9/40 | 2,25× |
| 6 | 3 | 1/10 | 3/10 | 3,00× |
| 7 | 2 | 1/10 | 9/20 | 4,50× |
| **8** | **1** | **1/10** | **9/10** | **9,00×** |

Ficar é sempre 1/10: a sua primeira escolha foi feita antes de ele abrir qualquer
coisa, e nada do que ele faz depois mexe nela. O que muda é para onde vai a
chance que ele tira das portas abertas, e ela vai inteira para as fechadas que
sobram. Com oito abertas sobra **uma**, e ela fica com 9/10.

É essa a versão que costumam contar como "imagine cem portas": ela só funciona
porque o apresentador abre **todas menos uma**. Com cem portas e uma aberta só, a
vantagem de trocar é de 1,01×.

## O que as medidas me corrigiram

1. **"A vantagem de trocar não muda com o número de portas abertas; o que muda é
   a chance absoluta."** Escrevi isso no rodapé da tabela e a própria tabela me
   desmentiu: a vantagem vai de **1,13× a 9,00×** com as mesmas dez portas. O
   teste `aVantagemNaoEhConstante` prende as duas pontas.
2. **"Quanto mais portas, mais óbvio que trocar é melhor."** Com dez portas e uma
   aberta, trocar dá 11,25% e a vantagem é de 1,13×: **mais fraca** que no jogo
   de três portas, não mais forte. O que torna o caso de cem portas convincente
   não é a quantidade de portas, é a quantidade de **aberturas**.
3. **"Se ele abriu ao acaso mas por sorte não revelou o prêmio, caiu no caso
   clássico."** Não cai: dá 50/50. A cena é a mesma e a resposta é outra, porque
   a resposta nunca esteve na cena — está no procedimento.
4. **"Para este tamanho de problema, `double` basta."** Somar 1/10 dez vezes em
   `double` dá 0,9999999999999999. Com os pesos não fechando em 1, "as duas
   estratégias empatam" vira "empatam quase", e o caso do apresentador que abre
   ao acaso é justamente um empate exato. Daí a classe `Fracao`.

## Como está organizado

- `Fracao`: fração exata, sempre reduzida, com comparação que não passa por
  `double`.
- `Apresentador`: os três procedimentos. É o único eixo que muda a resposta.
- `Varredura`: enumera prêmio × escolha × aberturas com o peso exato de cada
  jogo e devolve `ficando`, `trocando`, `ofereceu` e `revelou`.
- `Medidor`: as três tabelas acima, rodando no CI nos três sistemas.

55 testes.
