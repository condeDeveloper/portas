package br.com.conde.portas;

/**
 * Uma fracao exata, sempre reduzida.
 *
 * <p>O repositorio inteiro existe para dizer "dois tercos", nao "0,6666666".
 * Com ponto flutuante, duas contas que deveriam dar o mesmo numero dao numeros
 * parecidos, e aí nao da para afirmar que duas estrategias empatam: so da para
 * dizer que empatam ate a decima quinta casa. Com fracao, empate e empate.
 */
public record Fracao(long cima, long baixo) {

    public static final Fracao ZERO = new Fracao(0, 1);
    public static final Fracao UM = new Fracao(1, 1);

    public Fracao {
        if (baixo == 0) {
            throw new IllegalArgumentException("fracao com zero embaixo");
        }
        if (baixo < 0) {
            cima = -cima;
            baixo = -baixo;
        }
        long divisor = mdc(Math.abs(cima), baixo);
        if (divisor > 1) {
            cima /= divisor;
            baixo /= divisor;
        }
    }

    public static Fracao de(long cima, long baixo) {
        return new Fracao(cima, baixo);
    }

    public Fracao mais(Fracao outra) {
        return new Fracao(cima * outra.baixo + outra.cima * baixo, baixo * outra.baixo);
    }

    public Fracao vezes(Fracao outra) {
        return new Fracao(cima * outra.cima, baixo * outra.baixo);
    }

    public Fracao dividido(long numero) {
        return new Fracao(cima, baixo * numero);
    }

    public double valor() {
        return (double) cima / baixo;
    }

    public boolean maiorQue(Fracao outra) {
        return cima * outra.baixo > outra.cima * baixo;
    }

    private static long mdc(long a, long b) {
        while (b != 0) {
            long guarda = b;
            b = a % b;
            a = guarda;
        }
        return a == 0 ? 1 : a;
    }

    @Override
    public String toString() {
        return baixo == 1 ? String.valueOf(cima) : cima + "/" + baixo;
    }
}
