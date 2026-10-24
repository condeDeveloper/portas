package br.com.conde.portas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Prende os numeros que estao escritos no README. Se uma conta mudar, um destes
 * testes quebra e o texto tem que ser reescrito junto.
 */
@DisplayName("Os numeros do README")
class MedidasTest {

    @ParameterizedTest(name = "{0} portas, 1 aberta")
    @CsvSource({
            " 3, 1,  3, 2,  3",
            " 4, 1,  4, 3,  8",
            " 5, 1,  5, 4, 15",
            " 6, 1,  6, 5, 24",
            " 7, 1,  7, 6, 35",
            " 8, 1,  8, 7, 48",
            " 9, 1,  9, 8, 63",
            "10, 1, 10, 9, 80",
    })
    @DisplayName("tabela PORTAS")
    void tabelaPortas(int portas, long ficaCima, long ficaBaixo, long trocaCima, long trocaBaixo) {
        Varredura.Resultado r = new Varredura(portas, 1, Apresentador.SABE).medir();

        assertThat(r.ficando()).isEqualTo(Fracao.de(ficaCima, ficaBaixo));
        assertThat(r.trocando()).isEqualTo(Fracao.de(trocaCima, trocaBaixo));
    }

    @ParameterizedTest(name = "10 portas, {0} abertas")
    @CsvSource({
            "1, 9, 80, 1.13",
            "2, 9, 70, 1.29",
            "3, 3, 20, 1.50",
            "4, 9, 50, 1.80",
            "5, 9, 40, 2.25",
            "6, 3, 10, 3.00",
            "7, 9, 20, 4.50",
            "8, 9, 10, 9.00",
    })
    @DisplayName("tabela ABERTAS")
    void tabelaAbertas(int abertas, long cima, long baixo, double vantagem) {
        Varredura.Resultado r = new Varredura(10, abertas, Apresentador.SABE).medir();

        assertThat(r.trocando()).isEqualTo(Fracao.de(cima, baixo));
        assertThat(r.vantagemDaTroca()).isEqualTo(vantagem, within(0.005));
    }

    @Test
    @DisplayName("tabela APRESENTADOR, linha por linha")
    void tabelaApresentador() {
        Varredura.Resultado sabe = new Varredura(3, 1, Apresentador.SABE).medir();
        assertThat(sabe.ficando()).hasToString("1/3");
        assertThat(sabe.trocando()).hasToString("2/3");
        assertThat(sabe.ofereceu()).hasToString("1");
        assertThat(sabe.revelou()).hasToString("0");

        Varredura.Resultado ignora = new Varredura(3, 1, Apresentador.IGNORA).medir();
        assertThat(ignora.ficando()).hasToString("1/3");
        assertThat(ignora.trocando()).hasToString("1/3");
        assertThat(ignora.ofereceu()).hasToString("2/3");
        assertThat(ignora.revelou()).hasToString("1/3");

        Varredura.Resultado malvado = new Varredura(3, 1, Apresentador.MALVADO).medir();
        assertThat(malvado.ficando()).hasToString("1/3");
        assertThat(malvado.trocando()).hasToString("0");
        assertThat(malvado.ofereceu()).hasToString("1/3");
        assertThat(malvado.revelou()).hasToString("0");
    }

    @Test
    @DisplayName("com 10 portas e 1 aberta, trocar da MENOS que com 3 portas")
    void dezPortasUmaAbertaDaMenosQueTres() {
        Fracao tres = new Varredura(3, 1, Apresentador.SABE).medir().trocando();
        Fracao dez = new Varredura(10, 1, Apresentador.SABE).medir().trocando();

        assertThat(tres).isEqualTo(Fracao.de(2, 3));
        assertThat(dez).isEqualTo(Fracao.de(9, 80));
        assertThat(tres.maiorQue(dez)).isTrue();
    }

    @Test
    @DisplayName("o erro que a medida corrigiu: a vantagem da troca VAI de 1,13x a 9,00x")
    void aVantagemNaoEhConstante() {
        double comUma = new Varredura(10, 1, Apresentador.SABE).medir().vantagemDaTroca();
        double comOito = new Varredura(10, 8, Apresentador.SABE).medir().vantagemDaTroca();

        assertThat(comUma).isEqualTo(1.125, within(0.0005));
        assertThat(comOito).isEqualTo(9.0, within(0.0005));
        assertThat(comOito).isGreaterThan(comUma);
    }

    @Test
    @DisplayName("as tabelas rodam inteiras sem estourar")
    void asTabelasRodam() {
        Medidor.portas();
        Medidor.apresentador();
        Medidor.abertas();
    }
}
