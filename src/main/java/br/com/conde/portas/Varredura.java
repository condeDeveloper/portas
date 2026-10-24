package br.com.conde.portas;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumera TODOS os jogos possiveis e conta, em fracao exata, o que cada
 * estrategia ganha.
 *
 * <p>Um jogo e: onde o premio esta, qual porta o jogador escolheu e quais portas
 * o apresentador abriu. As duas primeiras sao uniformes; a terceira depende do
 * {@link Apresentador}. Nada aqui e sorteado: cada combinacao entra com o peso
 * exato dela.
 *
 * <p>Com o apresentador que abre ao acaso, parte dos jogos termina com o premio
 * revelado. Esses jogos existem e sao contados a parte: a pergunta "vale a pena
 * trocar" so faz sentido nos outros, e e dai que vem a diferenca de resposta
 * entre um apresentador e outro.
 */
public final class Varredura {

    private final int portas;
    private final int abertas;
    private final Apresentador apresentador;

    public Varredura(int portas, int abertas, Apresentador apresentador) {
        if (portas < 3) {
            throw new IllegalArgumentException("com menos de tres portas nao ha o que trocar");
        }
        if (abertas < 1 || abertas > portas - 2) {
            throw new IllegalArgumentException(
                    "com " + portas + " portas da para abrir de 1 a " + (portas - 2));
        }
        this.portas = portas;
        this.abertas = abertas;
        this.apresentador = apresentador;
    }

    public int portas() {
        return portas;
    }

    public int abertas() {
        return abertas;
    }

    public Apresentador apresentador() {
        return apresentador;
    }

    /** O resultado das duas estrategias, em fracao exata. */
    public record Resultado(Fracao ficando, Fracao trocando, Fracao ofereceu, Fracao revelou) {

        /** Quantas vezes mais a troca ganha, comparada a ficar. */
        public double vantagemDaTroca() {
            return ficando.cima() == 0 ? Double.POSITIVE_INFINITY
                    : trocando.valor() / ficando.valor();
        }

        public boolean empatam() {
            return ficando.equals(trocando);
        }
    }

    public Resultado medir() {
        Fracao ficando = Fracao.ZERO;
        Fracao trocando = Fracao.ZERO;
        Fracao ofereceu = Fracao.ZERO;
        Fracao revelou = Fracao.ZERO;
        Fracao pesoDoJogo = Fracao.de(1, (long) portas * portas);

        for (int premio = 0; premio < portas; premio++) {
            for (int escolha = 0; escolha < portas; escolha++) {
                List<int[]> aberturas = aberturasPossiveis(premio, escolha);
                if (aberturas.isEmpty()) {
                    continue;                       // o apresentador nao abriu nada
                }
                Fracao peso = pesoDoJogo.dividido(aberturas.size());

                for (int[] abertura : aberturas) {
                    boolean revelouOPremio = contem(abertura, premio);
                    if (revelouOPremio) {
                        revelou = revelou.mais(peso);
                        continue;
                    }
                    ofereceu = ofereceu.mais(peso);
                    if (escolha == premio) {
                        ficando = ficando.mais(peso);
                    } else {
                        // trocar so ganha se a porta nova for a do premio; com mais de
                        // uma porta fechada restante, a chance se divide entre elas
                        trocando = trocando.mais(peso.dividido(fechadasRestantes()));
                    }
                }
            }
        }
        return new Resultado(ficando, trocando, ofereceu, revelou);
    }

    /** Quantas portas sobram para trocar, fora a escolhida e as abertas. */
    public int fechadasRestantes() {
        return portas - 1 - abertas;
    }

    /** Todas as combinacoes de portas que o apresentador pode abrir neste jogo. */
    private List<int[]> aberturasPossiveis(int premio, int escolha) {
        List<Integer> candidatas = new ArrayList<>();
        for (int porta = 0; porta < portas; porta++) {
            if (porta == escolha) {
                continue;
            }
            if (apresentador != Apresentador.IGNORA && porta == premio) {
                continue;                           // quem sabe nunca abre o premio
            }
            candidatas.add(porta);
        }
        if (apresentador == Apresentador.MALVADO && escolha != premio) {
            return List.of();                       // ele nao oferece nada
        }
        List<int[]> saida = new ArrayList<>();
        escolher(candidatas, 0, new int[abertas], 0, saida);
        return saida;
    }

    private void escolher(List<Integer> candidatas, int indice, int[] atual, int quantas,
                          List<int[]> saida) {
        if (quantas == abertas) {
            saida.add(atual.clone());
            return;
        }
        if (indice >= candidatas.size()) {
            return;
        }
        atual[quantas] = candidatas.get(indice);
        escolher(candidatas, indice + 1, atual, quantas + 1, saida);
        escolher(candidatas, indice + 1, atual, quantas, saida);
    }

    private boolean contem(int[] portasAbertas, int porta) {
        for (int aberta : portasAbertas) {
            if (aberta == porta) {
                return true;
            }
        }
        return false;
    }
}
