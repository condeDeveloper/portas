package br.com.conde.portas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("A varredura de todos os jogos")
class VarreduraTest {

    @Test
    @DisplayName("tres portas, uma aberta: ficar da 1/3 e trocar da 2/3, exatos")
    void oClassico() {
        Varredura.Resultado r = new Varredura(3, 1, Apresentador.SABE).medir();

        assertThat(r.ficando()).isEqualTo(Fracao.de(1, 3));
        assertThat(r.trocando()).isEqualTo(Fracao.de(2, 3));
        assertThat(r.empatam()).isFalse();
    }

    @Test
    @DisplayName("as duas estrategias cobrem todos os jogos em que houve oferta")
    void asDuasSomamAOferta() {
        for (int portas = 3; portas <= 8; portas++) {
            for (Apresentador quem : Apresentador.values()) {
                Varredura.Resultado r = new Varredura(portas, 1, quem).medir();

                assertThat(r.ficando().mais(r.trocando()).maiorQue(r.ofereceu()))
                        .as("com %d portas e apresentador %s", portas, quem)
                        .isFalse();
            }
        }
    }

    @Test
    @DisplayName("o que foi oferecido mais o que foi revelado fecha o total de jogos")
    void ofertaMaisRevelacaoFechaUm() {
        for (int portas = 3; portas <= 8; portas++) {
            Varredura.Resultado sabe = new Varredura(portas, 1, Apresentador.SABE).medir();
            Varredura.Resultado ignora = new Varredura(portas, 1, Apresentador.IGNORA).medir();

            assertThat(sabe.ofereceu().mais(sabe.revelou())).isEqualTo(Fracao.UM);
            assertThat(ignora.ofereceu().mais(ignora.revelou())).isEqualTo(Fracao.UM);
        }
    }

    @ParameterizedTest(name = "com {0} portas, ficar da sempre 1/{0}")
    @ValueSource(ints = {3, 4, 5, 6, 7, 8, 9, 10})
    @DisplayName("ficar na primeira escolha nao depende de nada do que o apresentador faz")
    void ficarEhSempreAPrimeiraChance(int portas) {
        for (int abertas = 1; abertas <= portas - 2; abertas++) {
            Varredura.Resultado r = new Varredura(portas, abertas, Apresentador.SABE).medir();

            assertThat(r.ficando()).isEqualTo(Fracao.de(1, portas));
        }
    }

    @Test
    @DisplayName("com o apresentador que sabe, trocar sempre ganha de ficar")
    void trocarGanhaSempre() {
        for (int portas = 3; portas <= 10; portas++) {
            for (int abertas = 1; abertas <= portas - 2; abertas++) {
                Varredura.Resultado r = new Varredura(portas, abertas, Apresentador.SABE).medir();

                assertThat(r.trocando().maiorQue(r.ficando()))
                        .as("%d portas, %d abertas", portas, abertas)
                        .isTrue();
            }
        }
    }

    @Test
    @DisplayName("quanto mais portas ele abre, maior a vantagem de trocar")
    void aVantagemCresceComAsAberturas() {
        Fracao anterior = Fracao.ZERO;
        for (int abertas = 1; abertas <= 8; abertas++) {
            Varredura.Resultado r = new Varredura(10, abertas, Apresentador.SABE).medir();

            assertThat(r.trocando().maiorQue(anterior))
                    .as("abrindo %d portas", abertas)
                    .isTrue();
            anterior = r.trocando();
        }
        assertThat(anterior).isEqualTo(Fracao.de(9, 10));
    }

    @Test
    @DisplayName("com todas menos uma abertas, trocar da o complemento exato de ficar")
    void comUmaSoFechadaFechaEmUm() {
        for (int portas = 3; portas <= 10; portas++) {
            Varredura varredura = new Varredura(portas, portas - 2, Apresentador.SABE);
            Varredura.Resultado r = varredura.medir();

            assertThat(varredura.fechadasRestantes()).isEqualTo(1);
            assertThat(r.ficando().mais(r.trocando())).isEqualTo(Fracao.UM);
        }
    }

    @Test
    @DisplayName("quem sabe onde esta o premio nunca o revela")
    void quemSabeNuncaRevela() {
        for (int portas = 3; portas <= 8; portas++) {
            assertThat(new Varredura(portas, 1, Apresentador.SABE).medir().revelou())
                    .isEqualTo(Fracao.ZERO);
        }
    }

    @Test
    @DisplayName("sobram portas - 1 - abertas para trocar")
    void contaDasFechadas() {
        assertThat(new Varredura(3, 1, Apresentador.SABE).fechadasRestantes()).isEqualTo(1);
        assertThat(new Varredura(10, 1, Apresentador.SABE).fechadasRestantes()).isEqualTo(8);
        assertThat(new Varredura(10, 8, Apresentador.SABE).fechadasRestantes()).isEqualTo(1);
    }

    @Test
    @DisplayName("com menos de tres portas nao ha jogo")
    void poucasPortas() {
        assertThatThrownBy(() -> new Varredura(2, 1, Apresentador.SABE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tres portas");
    }

    @Test
    @DisplayName("ele precisa deixar pelo menos uma porta fechada para trocar")
    void aberturasDemais() {
        assertThatThrownBy(() -> new Varredura(3, 2, Apresentador.SABE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("de 1 a 1");
        assertThatThrownBy(() -> new Varredura(10, 0, Apresentador.SABE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
