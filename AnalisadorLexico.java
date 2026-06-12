import java.util.HashMap;
import java.util.Map;

// Núcleo do trabalho: implementa o Autômato Finito Determinístico (AFDD).
// Lê o código-fonte caractere a caractere e retorna um token por chamada.
// O analisador sintático (Main) chama proximoToken() repetidamente até receber EOF.
public class AnalisadorLexico {

    // Tabela de palavras reservadas: mapeia o lexema para o código do token.
    // Inclui variantes com acento para maior robustez.
    private static final Map<String, Integer> PALAVRAS_RESERVADAS = new HashMap<>();

    static {
        PALAVRAS_RESERVADAS.put("inicio",   Token.INICIO);
        PALAVRAS_RESERVADAS.put("fim",      Token.FIM);
        PALAVRAS_RESERVADAS.put("se",       Token.SE);
        PALAVRAS_RESERVADAS.put("entao",    Token.ENTAO);
        PALAVRAS_RESERVADAS.put("então",    Token.ENTAO);
        PALAVRAS_RESERVADAS.put("senao",    Token.SENAO);
        PALAVRAS_RESERVADAS.put("senão",    Token.SENAO);
        PALAVRAS_RESERVADAS.put("fim_se",   Token.FIM_SE);
        PALAVRAS_RESERVADAS.put("para",     Token.PARA);
        PALAVRAS_RESERVADAS.put("ate",      Token.ATE);
        PALAVRAS_RESERVADAS.put("até",      Token.ATE);
        PALAVRAS_RESERVADAS.put("passo",    Token.PASSO);
        PALAVRAS_RESERVADAS.put("fim_para", Token.FIM_PARA);
        PALAVRAS_RESERVADAS.put("inteiro",  Token.INTEIRO);
        PALAVRAS_RESERVADAS.put("leia",     Token.LEIA);
        PALAVRAS_RESERVADAS.put("imprima",  Token.IMPRIMA);
        PALAVRAS_RESERVADAS.put("escreva",  Token.ESCREVA);
        PALAVRAS_RESERVADAS.put("e",        Token.E);
        PALAVRAS_RESERVADAS.put("ou",       Token.OU);
        PALAVRAS_RESERVADAS.put("nao",      Token.NAO);
        PALAVRAS_RESERVADAS.put("não",      Token.NAO);
    }

    // Estado interno do autômato: posição atual no texto, linha e tabela de símbolos
    private final String         fonte;
    private       int            pos;
    private       int            linha;
    private final TabelaSimbolos tabela;

    public AnalisadorLexico(String fonte, TabelaSimbolos tabela) {
        this.fonte  = fonte;
        this.tabela = tabela;
        this.pos    = 0;
        this.linha  = 1;
    }

    // Método principal do AFDD: parte do estado inicial a cada chamada,
    // consome caracteres e retorna o próximo token reconhecido.
    public Token proximoToken() {
        ignorarEspacosEComentarios();

        if (fimArquivo()) {
            return new Token(Token.EOF, "EOF", -1, linha);
        }

        char c = peek();

        if (Character.isLetter(c) || c == '_' || ehAcentuado(c)) {
            return lerIdentificadorOuReservada();
        }

        if (Character.isDigit(c)) {
            return lerConstanteInteira();
        }

        if (c == '"') {
            return lerConstanteLiteral();
        }

        // Para operadores e delimitadores, consome o caractere e decide o token
        avancar();
        int linhaAtual = linha;

        switch (c) {
            case '+': return new Token(Token.OP_MAIS,     "+",  -1, linhaAtual);
            case '-': return new Token(Token.OP_MENOS,    "-",  -1, linhaAtual);
            case '*': return new Token(Token.OP_MULT,     "*",  -1, linhaAtual);
            case '/': return new Token(Token.OP_DIV,      "/",  -1, linhaAtual);
            case '(': return new Token(Token.ABRE_PAR,    "(",  -1, linhaAtual);
            case ')': return new Token(Token.FECHA_PAR,   ")",  -1, linhaAtual);
            case ';': return new Token(Token.PONTO_VIRG,  ";",  -1, linhaAtual);
            case ':': return new Token(Token.DOIS_PONTOS, ":",  -1, linhaAtual);
            case '=': return new Token(Token.OP_IGUAL,    "=",  -1, linhaAtual);

            case '>':
                // Lookahead de 1: verifica se é '>=' ou apenas '>'
                if (!fimArquivo() && peek() == '=') {
                    avancar();
                    return new Token(Token.OP_MAIOR_IGUAL, ">=", -1, linhaAtual);
                }
                return new Token(Token.OP_MAIOR, ">", -1, linhaAtual);

            case '<':
                // Lookahead de 1: verifica se é '<=', '<>', '<-' ou apenas '<'
                if (!fimArquivo()) {
                    char next = peek();
                    if (next == '=') { avancar(); return new Token(Token.OP_MENOR_IGUAL, "<=", -1, linhaAtual); }
                    if (next == '>') { avancar(); return new Token(Token.OP_DIFERENTE,   "<>", -1, linhaAtual); }
                    if (next == '-') { avancar(); return new Token(Token.OP_ATRIB,       "<-", -1, linhaAtual); }
                }
                return new Token(Token.OP_MENOR, "<", -1, linhaAtual);

            default:
                return new Token(Token.ERRO, String.valueOf(c), -1, linhaAtual);
        }
    }

    // Descarta espaços, tabulações, quebras de linha e comentários de linha (//)
    private void ignorarEspacosEComentarios() {
        while (!fimArquivo()) {
            char c = peek();

            if (c == '\n') {
                avancar();
                linha++;
            } else if (Character.isWhitespace(c)) {
                avancar();
            } else if (c == '/' && pos + 1 < fonte.length() && fonte.charAt(pos + 1) == '/') {
                while (!fimArquivo() && peek() != '\n') avancar();
            } else {
                break;
            }
        }
    }

    // Lê uma sequência de letras/dígitos e verifica se é palavra reservada ou identificador.
    // Se for palavra reservada, não insere na tabela de símbolos.
    private Token lerIdentificadorOuReservada() {
        int linhaAtual = linha;
        StringBuilder sb = new StringBuilder();

        while (!fimArquivo()) {
            char c = peek();
            if (Character.isLetterOrDigit(c) || c == '_' || ehAcentuado(c)) {
                sb.append(avancar());
            } else {
                break;
            }
        }

        String lexema = sb.toString();
        int cod = codigoPalavraReservada(lexema);

        if (cod != -1) {
            return new Token(cod, lexema, -1, linhaAtual);
        }

        int posTs = tabela.inserir(lexema, TabelaSimbolos.TipoSimbolo.IDENTIFICADOR);
        return new Token(Token.IDENTIFICADOR, lexema, posTs, linhaAtual);
    }

    // Lê dígitos consecutivos e forma uma constante inteira
    private Token lerConstanteInteira() {
        int linhaAtual = linha;
        StringBuilder sb = new StringBuilder();

        while (!fimArquivo() && Character.isDigit(peek())) {
            sb.append(avancar());
        }

        String lexema = sb.toString();
        int posTs = tabela.inserir(lexema, TabelaSimbolos.TipoSimbolo.CONST_INTEIRA);
        return new Token(Token.CONST_INTEIRA, lexema, posTs, linhaAtual);
    }

    // Lê tudo entre aspas duplas e forma uma constante literal (string)
    private Token lerConstanteLiteral() {
        int linhaAtual = linha;
        StringBuilder sb = new StringBuilder();
        avancar(); // consome a aspas de abertura

        while (!fimArquivo() && peek() != '"') {
            char c = avancar();
            if (c == '\n') linha++;
            sb.append(c);
        }

        if (!fimArquivo()) avancar(); // consome a aspas de fechamento

        String lexema = "\"" + sb.toString() + "\"";
        int posTs = tabela.inserir(lexema, TabelaSimbolos.TipoSimbolo.CONST_LITERAL);
        return new Token(Token.CONST_LITERAL, lexema, posTs, linhaAtual);
    }

    // Consulta o HashMap de palavras reservadas (sem diferenciar maiúsculas/minúsculas)
    private int codigoPalavraReservada(String lexema) {
        Integer codigo = PALAVRAS_RESERVADAS.get(lexema.toLowerCase());
        return (codigo != null) ? codigo : -1;
    }

    private boolean fimArquivo() { return pos >= fonte.length(); }

    // Lookahead: olha o próximo caractere sem consumir
    private char peek() { return fonte.charAt(pos); }

    // Consome e retorna o próximo caractere
    private char avancar() { return fonte.charAt(pos++); }

    // Verifica se o caractere é uma letra acentuada do português
    private boolean ehAcentuado(char c) {
        String acentuados = "áéíóúàèìòùâêîôûãõäëïöüçÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕÄËÏÖÜÇ";
        return acentuados.indexOf(c) >= 0;
    }
}
