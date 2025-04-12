# Questão 2.2: Decoupling de Código com Design Patterns

Para desacoplar o código de uma biblioteca de terceiros que pode ser substituída no futuro, um padrão de projeto muito eficaz é o **Adapter Pattern** (Adaptador) ou o **Facade Pattern** (Fachada), frequentemente usados em conjunto com **Dependency Injection** (Injeção de Dependência).

Vamos focar no **Adapter Pattern**.

## Abordagem Escolhida: Adapter Pattern

O Adapter Pattern permite que interfaces incompatíveis trabalhem juntas. Ele atua como uma ponte entre duas interfaces, convertendo a interface de uma classe (a biblioteca de terceiros) em outra interface que o cliente espera (nossa interface interna).

**Como Aplicar:**

1.  **Definir uma Interface Interna:** Crie uma interface Java que defina as operações que *sua aplicação* precisa realizar, abstraindo a funcionalidade da biblioteca de terceiros. Ex: `PaymentGateway`.
2.  **Criar um Adaptador:** Implemente a sua interface interna (`PaymentGateway`). Dentro dessa implementação (o Adaptador), mantenha uma referência à instância da biblioteca de terceiros (`OldPaymentGateway`). Os métodos do adaptador receberão chamadas usando a assinatura da sua interface e internamente chamarão os métodos correspondentes da biblioteca de terceiros, fazendo as traduções necessárias (nomes de métodos, tipos de parâmetros, valores de retorno). Ex: `OldPaymentGatewayAdapter`.
3.  **Usar a Interface Interna:** No restante da sua aplicação, dependa **apenas** da sua interface interna (`PaymentGateway`), não do Adaptador concreto ou da biblioteca de terceiros diretamente. Use Injeção de Dependência para fornecer a instância concreta do Adaptador onde for necessário. Ex: A classe `PaymentProcessor` recebe um `PaymentGateway` no construtor.

**Diagrama Simplificado:**

+-------------------+      +--------------------------+      +---------------------+
|   Cliente         | ---> | <<Interface>>            | <--- |     Adaptador       |
| (PaymentProcessor)|      |   PaymentGateway         |      | (OldPaymentGateway  |
+-------------------+      +--------------------------+      |      Adapter)       |
| processarPagamento()     |      +----------^----------+
| estornarPagamento()      |                 |
+--------------------------+                 | Adapta
V
+---------------------+
|  Biblioteca 3rd   |
| (OldPaymentGateway) |
+---------------------+
| executeTransaction()|
| refundTransaction() |
+---------------------+

## Vantagens

1.  **Desacoplamento Forte:** O código da aplicação depende de uma abstração (interface) que você controla, não de uma implementação concreta de terceiros.
2.  **Facilidade de Substituição:** Se a biblioteca `OldPaymentGateway` precisar ser substituída por uma `NewPaymentGateway`, basta criar um novo adaptador (`NewPaymentGatewayAdapter`) que implemente a mesma interface `PaymentGateway`. O resto da aplicação não precisa ser modificado, apenas a configuração da Injeção de Dependência para usar o novo adaptador.
3.  **Isolamento de Lógica de Adaptação:** A complexidade de "traduzir" as chamadas entre sua interface e a biblioteca fica contida dentro do adaptador, mantendo o resto do código limpo.
4.  **Testabilidade:** Você pode facilmente criar mocks ou stubs da sua interface interna (`PaymentGateway`) para testar o código da aplicação isoladamente, sem depender da biblioteca real.

## Limitações

1.  **Camada Extra de Indireção:** Adiciona uma classe extra (o Adaptador), o que pode introduzir uma pequena sobrecarga de complexidade e performance (geralmente insignificante).
2.  **Manutenção do Adaptador:** O adaptador precisa ser mantido. Se a biblioteca de terceiros mudar sua API (mesmo que não seja substituída), o adaptador precisará ser atualizado.
3.  **Adaptação Parcial:** Se a nova biblioteca tiver funcionalidades fundamentalmente diferentes ou ausentes que sua interface exige, a adaptação pode se tornar complexa ou impossível sem redesenhar a interface interna.

## Snippet de Código

Veja os arquivos no pacote `com.vsi.teste.decoupling`:

* `PaymentGateway.java` (Interface Interna)
* `OldPaymentGateway.java` (Simulação da Biblioteca de Terceiros)
* `OldPaymentGatewayAdapter.java` (O Adaptador)
* `PaymentProcessor.java` (Cliente que usa a Interface)

Este conjunto demonstra como o `PaymentProcessor` interage apenas com `PaymentGateway`, enquanto o `OldPaymentGatewayAdapter` faz a "tradução" para a `OldPaymentGateway`.