package br.com.conde.portas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * O caso de cem portas, que e como a historia costuma ser contada. Ele so e
 * convincente por causa das NOVENTA E OITO aberturas, nao das cem portas.
 */
@DisplayName("Cem portas")
class CemPortasTest {

    @Test
    @DisplayName("com uma porta aberta, trocar vale quase nada: 1,01x")
    void umaAberta() {
        Varredura.Resultado r = new Varredura(100, 1, Apresentador.SABE).medir();

        assertThat(r.ficando()).isEqualTo(Fracao.de(1, 100));
        assertThat(r.trocando()).isEqualTo(Fracao.de(99, 9800));
        assertThat(r.vantagemDaTroca()).isEqualTo(1.01, within(0.005));
    }

    @Test
    @DisplayName("a conta nao estoura: os pesos dos dez mil jogos fecham em um")
    void osPesosFecham() {
        Varredura.Resultado r = new Varredura(100, 1, Apresentador.SABE).medir();

        assertThat(r.ofereceu()).isEqualTo(Fracao.UM);
        assertThat(r.revelou()).isEqualTo(Fracao.ZERO);
    }
}
