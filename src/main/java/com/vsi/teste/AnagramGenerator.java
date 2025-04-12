package com.vsi.teste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utilitário para gerar todos os anagramas possíveis de um grupo de letras distintas.
 *
 * @see <a href="file:../doc-files/DSE%20-%20Test%20-%20Java%20Developer.docx.pdf">Questão 1 do Teste</a>
 */
public class AnagramGenerator {

    // Pattern para validar se a entrada contém apenas letras (ASCII)
    private static final Pattern LETTERS_ONLY_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    /**
     * Gera todos os anagramas possíveis para uma string de letras distintas.
     *
     * @param letras Uma string contendo apenas letras distintas.
     * @return Uma lista de strings, onde cada string é um anagrama único das letras de entrada.
     * @throws IllegalArgumentException Se a entrada for nula, vazia, contiver caracteres não-letras
     * ou letras repetidas.
     */
    public List<String> gerarAnagramas(String letras) {
        validarEntrada(letras);

        Set<String> anagramasSet = new HashSet<>(); // Usa Set para garantir unicidade automaticamente
        gerarPermutacoes("", letras, anagramasSet);

        List<String> anagramasList = new ArrayList<>(anagramasSet);
        Collections.sort(anagramasList); // Ordena para consistência da saída
        return anagramasList;
    }

    /**
     * Método auxiliar recursivo para gerar as permutações.
     *
     * A lógica é:
     * 1. Caso Base: Se não há mais letras restantes (sufixo está vazio), a permutação atual (prefixo) está completa. Adiciona ao conjunto.
     * 2. Passo Recursivo: Para cada letra no sufixo:
     * a. Fixa a letra atual, adicionando-a ao prefixo.
     * b. Cria um novo sufixo removendo a letra atual.
     * c. Chama recursivamente a função com o novo prefixo e novo sufixo.
     *
     * @param prefixo     A parte do anagrama já construída.
     * @param sufixo      As letras restantes a serem permutadas.
     * @param resultSet   O conjunto onde os anagramas completos são armazenados.
     */
    private void gerarPermutacoes(String prefixo, String sufixo, Set<String> resultSet) {
        int n = sufixo.length();
        if (n == 0) {
            // Caso base: não há mais letras para permutar, adiciona o anagrama formado
            resultSet.add(prefixo);
        } else {
            // Para cada letra no sufixo
            for (int i = 0; i < n; i++) {
                // Constrói o próximo prefixo adicionando o caractere atual (sufixo.charAt(i))
                String novoPrefixo = prefixo + sufixo.charAt(i);

                // Constrói o próximo sufixo removendo o caractere atual
                // Pega a parte antes do caractere + a parte depois do caractere
                String novoSufixo = sufixo.substring(0, i) + sufixo.substring(i + 1, n);

                // Chama recursivamente com os novos prefixo e sufixo
                gerarPermutacoes(novoPrefixo, novoSufixo, resultSet);
            }
        }
    }

    /**
     * Valida a string de entrada.
     *
     * @param letras A string a ser validada.
     * @throws IllegalArgumentException Se a validação falhar.
     */
    private void validarEntrada(String letras) {
        // 1. Validação básica: não nulo e não vazio
        if (letras == null || letras.isEmpty()) {
            throw new IllegalArgumentException("A entrada não pode ser nula ou vazia.");
        }

        // 2. Validação de conteúdo: apenas letras
        if (!LETTERS_ONLY_PATTERN.matcher(letras).matches()) {
            throw new IllegalArgumentException("A entrada deve conter apenas letras (a-z, A-Z).");
        }

        // 3. Validação de distinção: verifica se há letras repetidas
        Set<Character> caracteresUnicos = new HashSet<>();
        for (char c : letras.toCharArray()) {
            if (!caracteresUnicos.add(Character.toLowerCase(c))) { // Compara ignorando case para 'a' e 'A'
                throw new IllegalArgumentException("A entrada deve conter apenas letras distintas. Letra repetida: " + c);
            }
        }
    }

    // --- Exemplo de uso (Opcional) ---
    // public static void main(String[] args) {
    //     AnagramGenerator generator = new AnagramGenerator();
    //     try {
    //         List<String> anagramas = generator.gerarAnagramas("abc");
    //         System.out.println("Anagramas de 'abc': " + anagramas); // Output: [abc, acb, bac, bca, cab, cba]

    //         List<String> anagramas2 = generator.gerarAnagramas("a");
    //         System.out.println("Anagramas de 'a': " + anagramas2); // Output: [a]

    //         // Exemplo de erro: Letra repetida
    //         // List<String> anagramas3 = generator.gerarAnagramas("aab");
    //         // System.out.println("Anagramas de 'aab': " + anagramas3);

    //         // Exemplo de erro: Vazio
    //         // List<String> anagramas4 = generator.gerarAnagramas("");
    //         // System.out.println("Anagramas de '': " + anagramas4);

    //          // Exemplo de erro: Não letra
    //         // List<String> anagramas5 = generator.gerarAnagramas("a1");
    //         // System.out.println("Anagramas de 'a1': " + anagramas5);


    //     } catch (IllegalArgumentException e) {
    //         System.err.println("Erro: " + e.getMessage());
    //     }
    // }
}