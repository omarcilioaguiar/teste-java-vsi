# Questão 2.1: Sobrescrita de `equals()` e `hashCode()`

## Cenário de Exemplo

É necessário sobrescrever o método `equals()` (e, consequentemente, `hashCode()`) em Java quando a definição padrão de igualdade (comparação de identidade de objeto - se duas referências apontam para o mesmo objeto na memória) não é suficiente. Queremos definir uma **igualdade lógica** baseada no estado (valores dos atributos) do objeto.

**Exemplo:** Considere uma classe `Produto` com atributos `codigo` (único) e `nome`. Queremos que dois objetos `Produto` sejam considerados iguais se eles tiverem o **mesmo código**, mesmo que sejam instâncias diferentes na memória e tenham nomes diferentes.

```java
// No arquivo EqualsHashCodeExample.java, usamos a classe Pessoa com CPF como chave.
// Aqui, um exemplo com Produto:
public class Produto {
    private final String codigo; // Identificador único
    private String nome;

    public Produto(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    // Getters...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Mesma instância
        if (o == null || getClass() != o.getClass()) return false; // Nulo ou classe diferente
        Produto produto = (Produto) o;
        return java.util.Objects.equals(codigo, produto.codigo); // Compara APENAS o código
    }

    @Override
    public int hashCode() {
        // Deve usar OS MESMOS campos usados em equals()
        return java.util.Objects.hash(codigo);
    }
}