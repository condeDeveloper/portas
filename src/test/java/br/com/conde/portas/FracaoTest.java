package br.com.conde.portas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("A fracao exata")
class FracaoTest {

    @Test
    @DisplayName("ela se reduz sozinha, entao duas fracoes iguais sao o mesmo objeto")
    void reduzSozinha() {
        assertThat(Fracao.de(2, 4)).isEqualTo(Fracao.de(1, 2));
        assertThat(Fracao.de(6, 3)).isEqualTo(Fracao.de(2, 1));
        assertThat(Fracao.de(100, 300)).isEqualTo(Fracao.de(1, 3));
    }

    @Test
    @DisplayName("o sinal fica sempre em cima")
    void oSinalFicaEmCima() {
        assertThat(Fracao.de(1, -2)).isEqualTo(Fracao.de(-1, 2));
        assertThat(Fracao.de(-1, -2)).isEqualTo(Fracao.de(1, 2));
    }

    @Test
    @DisplayName("um terco mais um terco mais um terco e exatamente um")
    void tresTercosDaUm() {
        Fracao terco = Fracao.de(1, 3);

        assertThat(terco.mais(terco).mais(terco)).isEqualTo(Fracao.UM);
    }

    @Test
    @DisplayName("dez portas de 1/10 somam um na fracao, mas NAO somam um em double")
    void oMesmoEmDoubleNaoFecha() {
        double emDouble = 0;
        Fracao emFracao = Fracao.ZERO;
        for (int porta = 0; porta < 10; porta++) {
            emDouble += 1.0 / 10;
            emFracao = emFracao.mais(Fracao.de(1, 10));
        }

        assertThat(emDouble).isNotEqualTo(1.0);     // da 0.9999999999999999
        assertThat(emFracao).isEqualTo(Fracao.UM);
    }

    @Test
    @DisplayName("multiplicar e dividir funcionam como na escola")
    void contasBasicas() {
        assertThat(Fracao.de(2, 3).vezes(Fracao.de(3, 4))).isEqualTo(Fracao.de(1, 2));
        assertThat(Fracao.de(1, 2).dividido(3)).isEqualTo(Fracao.de(1, 6));
        assertThat(Fracao.de(1, 2).mais(Fracao.de(1, 3))).isEqualTo(Fracao.de(5, 6));
    }

    @Test
    @DisplayName("a comparacao nao passa por double")
    void comparaSemDouble() {
        assertThat(Fracao.de(2, 3).maiorQue(Fracao.de(1, 3))).isTrue();
        assertThat(Fracao.de(9, 80).maiorQue(Fracao.de(1, 10))).isTrue();
        assertThat(Fracao.de(1, 10).maiorQue(Fracao.de(9, 80))).isFalse();
    }

    @Test
    @DisplayName("o texto sai como se escreve: 2/3, e 1 quando e inteiro")
    void textoLegivel() {
        assertThat(Fracao.de(2, 3)).hasToString("2/3");
        assertThat(Fracao.de(4, 4)).hasToString("1");
        assertThat(Fracao.ZERO).hasToString("0");
    }

    @Test
    @DisplayName("fracao com zero embaixo nao existe")
    void zeroEmbaixo() {
        assertThatThrownBy(() -> Fracao.de(1, 0)).isInstanceOf(IllegalArgumentException.class);
    }
}
