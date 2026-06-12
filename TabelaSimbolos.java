import java.util.ArrayList;
import java.util.List;

// Armazena os lexemas encontrados durante a análise que serão usados
// nas fases seguintes da compilação (análise sintática e semântica).
// São inseridos aqui: identificadores, constantes inteiras e literais.
// Palavras reservadas e operadores não entram, pois seus códigos já os identificam.
public class TabelaSimbolos {

    // Categorias possíveis de um símbolo na tabela
    public enum TipoSimbolo {
        IDENTIFICADOR,
        CONST_INTEIRA,
        CONST_LITERAL
    }

    // Cada entrada da tabela guarda o lexema, seu tipo e sua posição
    public static class Simbolo {
        private final String      lexema;
        private final TipoSimbolo tipo;
        private final int         posicao;

        public Simbolo(String lexema, TipoSimbolo tipo, int posicao) {
            this.lexema  = lexema;
            this.tipo    = tipo;
            this.posicao = posicao;
        }

        public String      getLexema()  { return lexema;  }
        public TipoSimbolo getTipo()    { return tipo;    }
        public int         getPosicao() { return posicao; }
    }

    private final List<Simbolo> tabela = new ArrayList<>();

    // Busca linear pelo lexema. Retorna a posição se encontrado, -1 caso contrário.
    public int buscar(String lexema) {
        for (Simbolo s : tabela) {
            if (s.getLexema().equals(lexema)) {
                return s.getPosicao();
            }
        }
        return -1;
    }

    // Insere o lexema se ainda não existir, evitando duplicatas.
    // Sempre retorna a posição, seja ela nova ou já existente.
    public int inserir(String lexema, TipoSimbolo tipo) {
        int pos = buscar(lexema);
        if (pos != -1) return pos;
        pos = tabela.size();
        tabela.add(new Simbolo(lexema, tipo, pos));
        return pos;
    }

    // Exibe a tabela de símbolos completa ao final da análise
    public void imprimir() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║              TABELA DE SÍMBOLOS                     ║");
        System.out.println("╠═════╦══════════════════════════════╦════════════════╣");
        System.out.printf( "║ %-3s ║ %-28s ║ %-14s ║%n", "Pos", "Lexema", "Tipo");
        System.out.println("╠═════╬══════════════════════════════╬════════════════╣");
        for (Simbolo s : tabela) {
            System.out.printf("║ %-3d ║ %-28s ║ %-14s ║%n",
                    s.getPosicao(), s.getLexema(), s.getTipo());
        }
        System.out.println("╚═════╩══════════════════════════════╩════════════════╝");
        System.out.println("Total de símbolos: " + tabela.size());
    }

    public int tamanho() { return tabela.size(); }
}
