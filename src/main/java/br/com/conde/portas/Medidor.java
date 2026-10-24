package br.com.conde.portas;

import java.util.Locale;

/**
 * As tabelas do README. Uso:
 * {@code mvn exec:java -Dexec.args="portas"} (ou {@code apresentador}, {@code abertas}).
 */
public final class Medidor {

    private Medidor() {
    }

    public static void main(String[] args) {
        String tabela = args.length > 0 ? args[0] : "tudo";
        boolean tudo = tabela.equals("tudo");
        if (tudo || tabela.equals("portas")) {
            portas();
        }
        if (tudo || tabela.equals("apresentador")) {
            apresentador();
        }
        if (tudo || tabela.equals("abertas")) {
            abertas();
        }
    }

    /** O classico, com uma porta aberta, por numero de portas. */
    public static void portas() {
        System.out.println();
        System.out.println("PORTAS  (apresentador que sabe, abrindo UMA porta vazia)");
        System.out.printf(Locale.ROOT, "%-10s %12s %12s %14s %12s%n",
                "portas", "ficando", "trocando", "troca ganha", "em decimal");
        for (int quantas = 3; quantas <= 10; quantas++) {
            Varredura.Resultado r = new Varredura(quantas, 1, Apresentador.SABE).medir();
            System.out.printf(Locale.ROOT, "%-10d %12s %12s %13.2fx %12.4f%n",
                    quantas, r.ficando(), r.trocando(), r.vantagemDaTroca(), r.trocando().valor());
        }
        System.out.println();
        System.out.println("com tres portas, trocar ganha 2/3 contra 1/3: o dobro.");
        System.out.println("com dez, a troca ganha menos que com tres em termos ABSOLUTOS,");
        System.out.println("porque sobram oito portas fechadas para dividir a chance.");
        System.out.println();
    }

    /** A mesma cena, tres apresentadores, tres respostas. */
    public static void apresentador() {
        System.out.println();
        System.out.println("APRESENTADOR  (3 portas, 1 aberta: a mesma cena, respostas diferentes)");
        System.out.printf(Locale.ROOT, "%-24s %10s %10s %12s %12s%n",
                "apresentador", "ficando", "trocando", "ofereceu", "revelou");
        for (Apresentador quem : Apresentador.values()) {
            Varredura.Resultado r = new Varredura(3, 1, quem).medir();
            System.out.printf(Locale.ROOT, "%-24s %10s %10s %12s %12s%n",
                    quem.rotulo(), r.ficando(), r.trocando(), r.ofereceu(), r.revelou());
        }
        System.out.println();
        System.out.println("as colunas ficando e trocando sao a chance SOBRE TODOS OS JOGOS.");
        System.out.println("dentro dos jogos em que houve oferta, a conta fica assim:");
        for (Apresentador quem : Apresentador.values()) {
            Varredura.Resultado r = new Varredura(3, 1, quem).medir();
            if (r.ofereceu().cima() == 0) {
                continue;
            }
            double fica = r.ficando().valor() / r.ofereceu().valor();
            double troca = r.trocando().valor() / r.ofereceu().valor();
            System.out.printf(Locale.ROOT, "  %-24s ficar %.4f, trocar %.4f%n",
                    quem.rotulo(), fica, troca);
        }
        System.out.println();
    }

    /** Quantas portas o apresentador abre muda tudo. */
    public static void abertas() {
        System.out.println();
        System.out.println("ABERTAS  (10 portas, apresentador que sabe, abrindo k vazias)");
        System.out.printf(Locale.ROOT, "%-10s %14s %12s %12s %14s%n",
                "abertas", "sobram fechadas", "ficando", "trocando", "troca ganha");
        for (int k = 1; k <= 8; k++) {
            Varredura varredura = new Varredura(10, k, Apresentador.SABE);
            Varredura.Resultado r = varredura.medir();
            System.out.printf(Locale.ROOT, "%-10d %14d %12s %12s %13.2fx%n",
                    k, varredura.fechadasRestantes(), r.ficando(), r.trocando(),
                    r.vantagemDaTroca());
        }
        System.out.println();
        System.out.println("quanto mais portas o apresentador abre, mais a chance se");
        System.out.println("concentra na unica porta que sobra: com 8 das 10 abertas,");
        System.out.println("trocar da 9/10, que e nove vezes o que ficar da.");
        System.out.println();
    }
}
