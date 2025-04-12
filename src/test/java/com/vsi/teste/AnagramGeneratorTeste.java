package com.vsi.teste;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe AnagramGenerator.
 */
class AnagramGeneratorTest {

    private AnagramGenerator generator;

    @BeforeEach
    void setUp() {
        // Instancia um novo gerador antes de cada teste
        generator = new AnagramGenerator();
    }

    @Test
    @DisplayName("Teste com 3 letras distintas (abc)")
    void testGerarAnagramas_TresLetrasDistintas() {
        List<String> expected = Arrays.asList("abc", "acb", "bac", "bca", "cab", "cba");
        List<String> actual = generator.gerarAnagramas("abc");
        // Garante que ambas as listas estão ordenadas para comparação
        Collections.sort(actual);
        assertEquals(expected.size(), actual.size(), "Número de anagramas deve ser 6");
        assertEquals(expected, actual, "Os anagramas gerados devem corresponder ao esperado");
    }

    @Test
    @DisplayName("Teste com 1 letra (a)")
    void testGerarAnagramas_UmaLetra() {
        List<String> expected = Collections.singletonList("a");
        List<String> actual = generator.gerarAnagramas("a");
        assertEquals(expected.size(), actual.size(), "Número de anagramas deve ser 1");
        assertEquals(expected, actual, "O anagrama gerado deve ser a própria letra");
    }

    @Test
    @DisplayName("Teste com 2 letras distintas (xy)")
    void testGerarAnagramas_DuasLetrasDistintas() {
        List<String> expected = Arrays.asList("xy", "yx");
        List<String> actual = generator.gerarAnagramas("xy");
        // Garante que ambas as listas estão ordenadas para comparação
        Collections.sort(actual);
        assertEquals(expected.size(), actual.size(), "Número de anagramas deve ser 2");
        assertEquals(expected, actual, "Os anagramas gerados devem corresponder ao esperado");
    }


    // --- Testes de Casos de Borda e Inválidos ---

    @Test
    @DisplayName("Teste com entrada nula deve lançar IllegalArgumentException")
    void testGerarAnagramas_EntradaNula() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> generator.gerarAnagramas(null),
                "Esperado IllegalArgumentException para entrada nula"
        );
        assertTrue(thrown.getMessage().contains("nula ou vazia"));
    }

    @Test
    @DisplayName("Teste com entrada vazia deve lançar IllegalArgumentException")
    void testGerarAnagramas_EntradaVazia() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> generator.gerarAnagramas(""),
                "Esperado IllegalArgumentException para entrada vazia"
        );
        assertTrue(thrown.getMessage().contains("nula ou vazia"));
    }

    @Test
    @DisplayName("Teste com entrada contendo não-letras deve lançar IllegalArgumentException")
    void testGerarAnagramas_NaoLetras() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> generator.gerarAnagramas("a1c"),
                "Esperado IllegalArgumentException para entrada com não-letras"
        );
        assertTrue(thrown.getMessage().contains("apenas letras"));
    }

    @Test
    @DisplayName("Teste com entrada contendo letras repetidas deve lançar IllegalArgumentException")
    void testGerarAnagramas_LetrasRepetidas() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> generator.gerarAnagramas("aab"),
                "Esperado IllegalArgumentException para entrada com letras repetidas"
        );
        assertTrue(thrown.getMessage().contains("distintas"));
    }

    @Test
    @DisplayName("Teste com entrada contendo letras repetidas (case insensitive) deve lançar IllegalArgumentException")
    void testGerarAnagramas_LetrasRepetidasCaseInsensitive() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> generator.gerarAnagramas("aAb"), // 'a' e 'A' são consideradas repetidas
                "Esperado IllegalArgumentException para entrada com letras repetidas (case insensitive)"
        );
        assertTrue(thrown.getMessage().contains("distintas"));
    }
}