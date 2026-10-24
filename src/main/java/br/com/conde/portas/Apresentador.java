package br.com.conde.portas;

/**
 * O que o apresentador sabe e como ele escolhe as portas que abre.
 *
 * <p>Esta e a unica coisa que muda a resposta do problema. As portas sao as
 * mesmas, o premio esta no mesmo lugar e o jogador faz a mesma escolha: o que
 * decide se vale a pena trocar e o <b>procedimento do apresentador</b>. Trocar
 * o procedimento e trocar de problema, mesmo que a cena pareca igual.
 */
public enum Apresentador {

    /**
     * Ele sabe onde esta o premio e abre apenas portas vazias, nunca a que o
     * jogador escolheu. E o problema classico.
     */
    SABE("sabe onde esta"),

    /**
     * Ele nao sabe de nada e abre ao acaso entre as portas que o jogador nao
     * escolheu. As vezes revela o premio e o jogo acaba ali; a pergunta so faz
     * sentido nas rodadas em que ele, por sorte, abriu portas vazias.
     */
    IGNORA("abre ao acaso"),

    /**
     * Ele sabe onde esta o premio e so oferece a troca quando o jogador JA
     * escolheu a porta certa. Quando o jogador errou, ele nao abre nada.
     */
    MALVADO("so oferece se acertou");

    private final String rotulo;

    Apresentador(String rotulo) {
        this.rotulo = rotulo;
    }

    public String rotulo() {
        return rotulo;
    }
}
