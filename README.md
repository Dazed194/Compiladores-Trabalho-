
# Analisador Léxico — Portugol

Trabalho prático da disciplina de Compiladores — UniCEUB 2026.

## Descrição

Implementação de um analisador léxico para a linguagem Portugol, uma linguagem didática estruturada em português. O analisador é escrito em Java e segue o algoritmo baseado em Autômato Finito Determinístico (AFDD).

O programa lê um código-fonte em Portugol, reconhece todos os tokens da linguagem e exibe a sequência de tokens junto com a tabela de símbolos gerada.

## Estrutura do Projeto

| Arquivo | Descrição |
|---|---|
| `Token.java` | Define os códigos e atributos de cada token |
| `TabelaSimbolos.java` | Armazena identificadores e constantes encontrados |
| `AnalisadorLexico.java` | Núcleo do AFDD — reconhece os tokens caractere a caractere |
| `Main.java` | Simula o analisador sintático chamando o léxico em loop |

## Como Executar


javac -encoding UTF-8 Token.java TabelaSimbolos.java AnalisadorLexico.java Main.java
java -Dfile.encoding=UTF-8 Main


Para analisar um arquivo externo:


## java -Dfile.encoding=UTF-8 Main programa.pg


## Tokens Reconhecidos

- Palavras reservadas: `inicio`, `fim`, `se`, `entao`, `senao`, `para`, `leia`, `escreva`, entre outras
- Identificadores e constantes inteiras e literais
- Operadores relacionais: `>`, `<`, `>=`, `<=`, `=`, `<>`
- Operadores aritméticos: `+`, `-`, `*`, `/`
- Atribuição: `<-`
- Delimitadores: `(`, `)`, `;`, `:`

## Autores

- João Vítor Carvalho Barbosa
- Murilo Silva Villarouca
```
