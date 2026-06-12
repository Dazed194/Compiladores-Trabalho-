import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// Ponto de entrada do programa. Simula o papel do analisador sintático:
// chama proximoToken() em loop e exibe cada token reconhecido até chegar ao EOF.
// Aceita um arquivo externo como argumento ou usa o programa de exemplo embutido.
public class Main {

    // Programa Portugol de exemplo usado quando nenhum arquivo é passado
    private static final String EXEMPLO_EMBUTIDO =
        "inicio\n" +
        "  inteiro:a;\n" +
        "  imprima (\"digite um valor para a:\");\n" +
        "  leia(a);\n" +
        "  se a = 5\n" +
        "  entao\n" +
        "    escreva (\"igual a 5\");\n" +
        "  senao\n" +
        "    escreva (\"diferente de 5\");\n" +
        "  fim_se\n" +
        "fim\n";

    public static void main(String[] args) throws IOException {

        // Carrega o código-fonte: arquivo externo ou exemplo embutido
        String fonte;
        if (args.length > 0) {
            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            fonte = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("Analisando arquivo: " + args[0]);
        } else {
            fonte = EXEMPLO_EMBUTIDO;
            System.out.println("Analisando programa de exemplo embutido.");
        }

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          ANALISADOR LÉXICO — PORTUGOL (UniCEUB 2026)                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Exibe o código-fonte numerado por linha para facilitar a conferência
        System.out.println("── Código-Fonte ──────────────────────────────────────────────────────");
        String[] linhas = fonte.split("\n", -1);
        for (int i = 0; i < linhas.length; i++) {
            System.out.printf("%3d │ %s%n", i + 1, linhas[i]);
        }
        System.out.println();

        // Instancia a tabela de símbolos e o analisador léxico
        TabelaSimbolos tabela    = new TabelaSimbolos();
        AnalisadorLexico lexico  = new AnalisadorLexico(fonte, tabela);

        System.out.println("── Sequência de Tokens ───────────────────────────────────────────────");
        System.out.printf("%-6s  %-4s  %-16s  %-28s  %-9s%n",
                "Linha", "Cód.", "Nome", "Lexema", "PosTabela");
        System.out.println("─".repeat(70));

        Token token;
        int totalTokens = 0;

        // Loop principal: solicita tokens ao léxico até receber EOF (código 999)
        do {
            token = lexico.proximoToken();

            String posStr = (token.getPosTabela() >= 0)
                    ? String.valueOf(token.getPosTabela())
                    : "-";

            System.out.printf("  %-4d  %-4d  %-16s  %-28s  %-9s%n",
                    token.getLinha(),
                    token.getCodigo(),
                    token.nomeToken(),
                    "'" + token.getLexema() + "'",
                    posStr);

            // Sinaliza erro léxico sem interromper a análise
            if (token.getCodigo() == Token.ERRO) {
                System.err.printf("[ERRO LÉXICO] Linha %d: caractere inesperado '%s'%n",
                        token.getLinha(), token.getLexema());
            }

            totalTokens++;

        } while (token.getCodigo() != Token.EOF);

        System.out.println("─".repeat(70));
        System.out.printf("Total de tokens reconhecidos: %d%n", totalTokens);

        // Ao final, imprime a tabela de símbolos construída durante a análise
        tabela.imprimir();
    }
}
