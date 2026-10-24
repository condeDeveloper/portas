package br.com.conde.portas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A mesma cena, tres procedimentos. Estes testes existem para mostrar que a
 * resposta do problema nao esta nas portas: esta no que o apresentador sabe.
 */
@DisplayName("O apresentador e quem decide a resposta")
class ApresentadorTest {

    @Test
    @DisplayName("quem sabe onde esta: trocar da 2/3, o dobro de ficar")
    void sabe() {
        Varredura.Resultado r = new Varredura(3, 1, Apresentador.SABE).medir();

        assertThat(r.ficando()).isEqualTo(Fracao.de(1, 3));
        assertThat(r.trocando()).isEqualTo(Fracao.de(2, 3));
        assertThat(r.ofereceu()).isEqualTo(Fracao.UM);
        assertThat(r.revelou()).isEqualTo(Fracao.ZERO);
        assertThat(r.vantagemDaTroca()).isEqualTo(2.0);
    }

    @Test
    @DisplayName("quem abre ao acaso: dentro das ofertas tanto faz, e ele estraga 1/3 dos jogos")
    void ignora() {
        Varredura.Resultado r = new Varredura(3, 1, Apresentador.IGNORA).medir();

        assertThat(r.ficando()).isEqualTo(Fracao.de(1, 3));
        assertThat(r.trocando()).isEqualTo(Fracao.de(1, 3));
        assertThat(r.empatam()).isTrue();
        assertThat(r.ofereceu()).isEqualTo(Fracao.de(2, 3));
        assertThat(r.revelou()).isEqualTo(Fracao.de(1, 3));
    }

    @Test
    @DisplayName("dentro dos jogos em que houve oferta, o que ignora da meio a meio")
    void ignoraDentroDaOferta() {
        Varredura.Resultado r = new Varredura(3, 1, Apresentador.IGNORA).medir();

        assertThat(r.ficando().valor() / r.ofereceu().valor()).isEqualTo(0.5);
        assertThat(r.trocando().valor() / r.ofereceu().valor()).isEqualTo(0.5);
    }

    @Test
    @DisplayName("quem so oferece se o jogador acertou: trocar perde sempre, nao as vezes")
    void malvado() {
        Varredura.Resultado r = new Varredura(3, 1, Apresentador.MALVADO).medir();

        assertThat(r.ficando()).isEqualTo(Fracao.de(1, 3));
        assertThat(r.trocando()).isEqualTo(Fracao.ZERO);
        assertThat(r.ofereceu()).isEqualTo(Fracao.de(1, 3));
        assertThat(r.revelou()).isEqualTo(Fracao.ZERO);
    }

    @Test
    @DisplayName("a cena e a mesma nos tres: mesmas portas, mesma oferta, respostas diferentes")
    void aMesmaCenaTresRespostas() {
        Fracao sabe = new Varredura(3, 1, Apresentador.SABE).medir().trocando();
        Fracao ignora = new Varredura(3, 1, Apresentador.IGNORA).medir().trocando();
        Fracao malvado = new Varredura(3, 1, Apresentador.MALVADO).medir().trocando();

        assertThat(sabe).isNotEqualTo(ignora);
        assertThat(ignora).isNotEqualTo(malvado);
        assertThat(sabe.maiorQue(ignora)).isTrue();
        assertThat(ignora.maiorQue(malvado)).isTrue();
    }

    @Test
    @DisplayName("ficar da 1/3 nos tres: so a troca sente o procedimento")
    void ficarNaoSenteOProcedimento() {
        for (Apresentador quem : Apresentador.values()) {
            assertThat(new Varredura(3, 1, quem).medir().ficando())
                    .as("apresentador %s", quem)
                    .isEqualTo(Fracao.de(1, 3));
        }
    }

    @Test
    @DisplayName("cada apresentador tem um rotulo para a tabela")
    void rotulos() {
        assertThat(Apresentador.SABE.rotulo()).isEqualTo("sabe onde esta");
        assertThat(Apresentador.IGNORA.rotulo()).isEqualTo("abre ao acaso");
        assertThat(Apresentador.MALVADO.rotulo()).isEqualTo("so oferece se acertou");
    }
}
