package com.vsi.teste;

import java.util.Objects;

/**
 * Exemplo de classe que demonstra a necessidade e implementação
 * dos métodos equals() e hashCode().
 *
 * Cenário: Representar uma Pessoa, onde duas instâncias são consideradas
 * iguais se possuírem o mesmo CPF, independentemente de outros atributos ou
 * da identidade do objeto na memória.
 *
 * @see ../../../../../docs/Q2_1_Equals_HashCode.md Explicação detalhada.
 */
public class EqualsHashCodeExample {

    public static class Pessoa {
        private final String nome;
        private final String cpf; // Chave de negócio para igualdade
        private int idade;

        public Pessoa(String nome, String cpf, int idade) {
            // Validações básicas (poderiam ser mais robustas)
            if (cpf == null || cpf.trim().isEmpty()) {
                throw new IllegalArgumentException("CPF não pode ser nulo ou vazio.");
            }
            this.nome = nome;
            this.cpf = cpf.trim(); // Garante que não há espaços extras no CPF
            this.idade = idade;
        }

        public String getNome() { return nome; }
        public String getCpf() { return cpf; }
        public int getIdade() { return idade; }
        public void setIdade(int idade) { this.idade = idade; }

        /**
         * Sobrescreve o método equals para definir a igualdade lógica baseada no CPF.
         *
         * Considerações Implementadas:
         * 1. Reflexivo: x.equals(x) é true. (Garantido pela lógica)
         * 2. Simétrico: Se x.equals(y) é true, então y.equals(x) deve ser true. (Garantido pela comparação mútua de CPF)
         * 3. Transitivo: Se x.equals(y) e y.equals(z) são true, então x.equals(z) deve ser true. (Garantido pela comparação de CPF)
         * 4. Consistente: Chamadas múltiplas a x.equals(y) retornam consistentemente o mesmo valor,
         * a menos que as propriedades usadas na comparação sejam modificadas. (Garantido pois CPF é final)
         * 5. Nulo: x.equals(null) deve retornar false. (Verificado explicitamente)
         * 6. Verificação de Tipo: Compara apenas com objetos da mesma classe ou compatível. (Verificado com instanceof ou getClass())
         */
        @Override
        public boolean equals(Object o) {
            // 1. Otimização: Verifica se é a mesma referência de objeto
            if (this == o) return true;

            // 2. Verificação de Nulo e Tipo: Garante que 'o' não é nulo e é uma instância de Pessoa
            if (o == null || getClass() != o.getClass()) return false;
            // Alternativa: if (!(o instanceof Pessoa)) return false; (permite subclasses, mas pode violar simetria se a subclasse sobrescrever equals de forma diferente)

            // 3. Casting: Converte 'o' para o tipo Pessoa para acessar seus campos
            Pessoa pessoa = (Pessoa) o;

            // 4. Comparação de Campos Relevantes: Compara o campo chave (CPF)
            // Usa Objects.equals para tratar corretamente CPFs nulos (embora nosso construtor previna isso)
            return Objects.equals(cpf, pessoa.cpf);
        }

        /**
         * Sobrescreve o método hashCode para alinhar com a implementação de equals().
         *
         * Contrato hashCode():
         * 1. Consistência Interna: Múltiplas chamadas a hashCode() no mesmo objeto devem retornar o mesmo
         * inteiro, desde que nenhuma informação usada em equals() seja modificada. (Garantido pois CPF é final)
         * 2. Consistência com Equals: Se dois objetos são iguais de acordo com equals(), então chamar hashCode()
         * em cada um deles DEVE produzir o mesmo resultado inteiro. (Garantido usando o CPF no cálculo)
         * 3. Colisões: Se dois objetos são desiguais de acordo com equals(), não é *obrigatório* que hashCode()
         * produza resultados distintos. No entanto, gerar códigos diferentes para objetos diferentes melhora
         * o desempenho de tabelas hash (como HashMap, HashSet).
         *
         * Implementação: Usa Objects.hash() para calcular o hash code baseado nos campos usados em equals() (neste caso, apenas CPF).
         */
        @Override
        public int hashCode() {
            // Calcula o hash code baseado APENAS nos campos usados no método equals()
            return Objects.hash(cpf);
            // Se mais campos fossem usados em equals(), seriam incluídos aqui:
            // return Objects.hash(cpf, outroCampo, maisUmCampo);
        }

        @Override
        public String toString() {
            return "Pessoa{" +
                    "nome='" + nome + '\'' +
                    ", cpf='" + cpf + '\'' +
                    ", idade=" + idade +
                    '}';
        }
    }

    // --- Exemplo de Uso (Opcional) ---
    // public static void main(String[] args) {
    //     Pessoa p1 = new Pessoa("Alice", "123.456.789-00", 30);
    //     Pessoa p2 = new Pessoa("Alice Silva", "123.456.789-00", 35); // Mesmo CPF, nome e idade diferentes
    //     Pessoa p3 = new Pessoa("Bob", "987.654.321-99", 40);
    //     Pessoa p4 = new Pessoa("Alice", "123.456.789-00", 30); // Exatamente os mesmos dados de p1

    //     System.out.println("p1: " + p1);
    //     System.out.println("p2: " + p2);
    //     System.out.println("p3: " + p3);
    //     System.out.println("p4: " + p4);
    //     System.out.println("--- Comparando Objetos ---");

    //     // Comparação de Referência vs. Lógica (equals)
    //     System.out.println("p1 == p4 ? " + (p1 == p4)); // false (objetos diferentes na memória)
    //     System.out.println("p1.equals(p4) ? " + p1.equals(p4)); // true (mesmo CPF)

    //     System.out.println("p1.equals(p2) ? " + p1.equals(p2)); // true (mesmo CPF, apesar de outros dados diferentes)
    //     System.out.println("p1.equals(p3) ? " + p1.equals(p3)); // false (CPFs diferentes)
    //     System.out.println("p1.equals(null) ? " + p1.equals(null)); // false

    //     System.out.println("--- Comparando Hash Codes ---");
    //     // HashCodes devem ser iguais para objetos 'equals'
    //     System.out.println("p1.hashCode(): " + p1.hashCode());
    //     System.out.println("p2.hashCode(): " + p2.hashCode());
    //     System.out.println("p3.hashCode(): " + p3.hashCode());
    //     System.out.println("p4.hashCode(): " + p4.hashCode());

    //     System.out.println("p1.hashCode() == p2.hashCode() ? " + (p1.hashCode() == p2.hashCode())); // true
    //     System.out.println("p1.hashCode() == p4.hashCode() ? " + (p1.hashCode() == p4.hashCode())); // true
    //     System.out.println("p1.hashCode() == p3.hashCode() ? " + (p1.hashCode() == p3.hashCode())); // false (geralmente, colisões são raras)

    //      // Exemplo de uso em Coleções baseadas em Hash (HashSet)
    //      java.util.Set<Pessoa> pessoasSet = new java.util.HashSet<>();
    //      pessoasSet.add(p1);
    //      pessoasSet.add(p2); // Não será adicionado, pois p2.equals(p1) é true
    //      pessoasSet.add(p3);
    //      System.out.println("Set de Pessoas (tamanho esperado 2): " + pessoasSet.size()); // Output: 2
    //      System.out.println("Conteúdo do Set: " + pessoasSet);
    // }
}