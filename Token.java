// Representa um token: a unidade mínima reconhecida pelo analisador léxico.
// Cada token tem um código numérico que identifica sua categoria,
// o lexema (texto original), a posição na tabela de símbolos e a linha onde aparece.
public class Token {

    // Palavras reservadas da linguagem Portugol (códigos 100-116)
    public static final int INICIO    = 100;
    public static final int FIM       = 101;
    public static final int INTEIRO   = 102;
    public static final int SE        = 103;
    public static final int ENTAO     = 104;
    public static final int SENAO     = 105;
    public static final int FIM_SE    = 106;
    public static final int PARA      = 107;
    public static final int ATE       = 108;
    public static final int PASSO     = 109;
    public static final int FIM_PARA  = 110;
    public static final int LEIA      = 111;
    public static final int IMPRIMA   = 112;
    public static final int ESCREVA   = 113;
    public static final int E         = 114;
    public static final int OU        = 115;
    public static final int NAO       = 116;

    // Identificadores e constantes (códigos 200-202)
    public static final int IDENTIFICADOR = 200;
    public static final int CONST_INTEIRA = 201;
    public static final int CONST_LITERAL = 202;

    // Operadores relacionais (códigos 300-305)
    public static final int OP_MAIOR       = 300;
    public static final int OP_MENOR       = 301;
    public static final int OP_MAIOR_IGUAL = 302;
    public static final int OP_MENOR_IGUAL = 303;
    public static final int OP_IGUAL       = 304;
    public static final int OP_DIFERENTE   = 305;

    // Operadores aritméticos (códigos 400-403)
    public static final int OP_MAIS  = 400;
    public static final int OP_MENOS = 401;
    public static final int OP_MULT  = 402;
    public static final int OP_DIV   = 403;

    // Atribuição e delimitadores (códigos 500-603)
    public static final int OP_ATRIB    = 500;
    public static final int ABRE_PAR    = 600;
    public static final int FECHA_PAR   = 601;
    public static final int PONTO_VIRG  = 602;
    public static final int DOIS_PONTOS = 603;

    // Tokens especiais
    public static final int EOF  = 999; // fim do arquivo
    public static final int ERRO = -1;  // caractere não reconhecido

    // Atributos de cada token gerado
    private final int    codigo;
    private final String lexema;
    private final int    posTabela; // posição na tabela de símbolos (-1 se não aplicável)
    private final int    linha;

    public Token(int codigo, String lexema, int posTabela, int linha) {
        this.codigo    = codigo;
        this.lexema    = lexema;
        this.posTabela = posTabela;
        this.linha     = linha;
    }

    public int    getCodigo()    { return codigo;    }
    public String getLexema()    { return lexema;    }
    public int    getPosTabela() { return posTabela; }
    public int    getLinha()     { return linha;     }

    // Converte o código numérico em um nome legível para a saída do programa
    public String nomeToken() {
        switch (codigo) {
            case INICIO:        return "INICIO";
            case FIM:           return "FIM";
            case INTEIRO:       return "INTEIRO";
            case SE:            return "SE";
            case ENTAO:         return "ENTAO";
            case SENAO:         return "SENAO";
            case FIM_SE:        return "FIM_SE";
            case PARA:          return "PARA";
            case ATE:           return "ATE";
            case PASSO:         return "PASSO";
            case FIM_PARA:      return "FIM_PARA";
            case LEIA:          return "LEIA";
            case IMPRIMA:       return "IMPRIMA";
            case ESCREVA:       return "ESCREVA";
            case E:             return "E";
            case OU:            return "OU";
            case NAO:           return "NAO";
            case IDENTIFICADOR: return "IDENTIFICADOR";
            case CONST_INTEIRA: return "CONST_INTEIRA";
            case CONST_LITERAL: return "CONST_LITERAL";
            case OP_MAIOR:      return "OP_MAIOR";
            case OP_MENOR:      return "OP_MENOR";
            case OP_MAIOR_IGUAL:return "OP_MAIOR_IGUAL";
            case OP_MENOR_IGUAL:return "OP_MENOR_IGUAL";
            case OP_IGUAL:      return "OP_IGUAL";
            case OP_DIFERENTE:  return "OP_DIFERENTE";
            case OP_MAIS:       return "OP_MAIS";
            case OP_MENOS:      return "OP_MENOS";
            case OP_MULT:       return "OP_MULT";
            case OP_DIV:        return "OP_DIV";
            case OP_ATRIB:      return "OP_ATRIB";
            case ABRE_PAR:      return "ABRE_PAR";
            case FECHA_PAR:     return "FECHA_PAR";
            case PONTO_VIRG:    return "PONTO_VIRG";
            case DOIS_PONTOS:   return "DOIS_PONTOS";
            case EOF:           return "EOF";
            case ERRO:          return "ERRO";
            default:            return "DESCONHECIDO(" + codigo + ")";
        }
    }

    @Override
    public String toString() {
        return String.format("Token{cod=%d, nome=%-16s, lexema='%s', pos=%d, linha=%d}",
                codigo, nomeToken(), lexema, posTabela, linha);
    }
}
