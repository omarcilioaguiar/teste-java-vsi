# Questão 2.4: Prevenção de SQL Injection

SQL Injection é uma vulnerabilidade de segurança grave que ocorre quando um atacante consegue manipular queries SQL executadas por uma aplicação, geralmente inserindo código SQL malicioso através de dados de entrada não validados ou não higienizados (como campos de formulário, parâmetros de URL, etc.). Isso pode levar a acesso não autorizado, modificação ou exclusão de dados, e até mesmo comprometer o servidor de banco de dados.

## Técnicas de Prevenção

As técnicas mais eficazes e recomendadas para prevenir SQL Injection são:

1.  **Parameterized Queries (Prepared Statements):**
    * **Como funciona:** Em vez de concatenar a entrada do usuário diretamente na string SQL, placeholders (`?` em JDBC, ou parâmetros nomeados como `:param` em JPQL/outras libs) são usados na query. Os valores de entrada são então passados separadamente para o driver do banco de dados ou ORM, que os trata como dados literais, e não como código SQL executável. O SGBD compila o "plano" da query primeiro e depois apenas insere os dados nos locais designados, impedindo que eles alterem a estrutura da query.
    * **Vantagem Principal:** É a defesa mais robusta contra SQL Injection. Também pode melhorar o desempenho, pois o SGBD pode reutilizar o plano de execução da query pré-compilada.
    * **Exemplo:** Veja `buscarUsuarioPorEmail_Seguro()` em `SqlInjectionPrevention.java`.

2.  **Object-Relational Mappers (ORMs):**
    * **Como funciona:** Frameworks ORM como Hibernate (com JPA) ou jOOQ geram as queries SQL (frequentemente usando Prepared Statements por baixo dos panos) a partir de chamadas a métodos ou linguagens de consulta de objeto (JPQL, HQL, Criteria API). Se usados corretamente (passando parâmetros através dos métodos do ORM, e não construindo queries JPQL/HQL por concatenação), eles previnem SQL Injection inerentemente.
    * **Vantagem Principal:** Abstrai a escrita de SQL, aumenta a produtividade e fornece segurança por padrão na maioria das operações comuns.
    * **Exemplo:** Veja a seção conceitual `buscarUsuarioPorEmail_ORM_Placeholder()` em `SqlInjectionPrevention.java` e os comentários sobre JPA/Spring Data JPA.

3.  **Stored Procedures (com Cuidado):**
    * **Como funciona:** A lógica SQL é armazenada no banco de dados. A aplicação chama a procedure passando os dados de entrada como parâmetros. Se a stored procedure for bem escrita (não usando SQL dinâmico internamente de forma insegura), ela pode ser segura.
    * **Desvantagem:** Acopla a lógica de negócio ao banco de dados, pode dificultar a manutenção e versionamento. SQL dinâmico *dentro* da procedure ainda pode ser vulnerável se não usar parâmetros corretamente. Geralmente, Prepared Statements ou ORMs são preferíveis.

4.  **Input Validation e Sanitization (Defesa em Profundidade):**
    * **Como funciona:** Validar se a entrada do usuário corresponde ao tipo e formato esperados (ex: um ID deve ser numérico, um email deve ter formato de email). Sanitizar/Escapar caracteres especiais (como `'`, `;`, `--`) *antes* de usá-los em queries (principalmente se Prepared Statements/ORMs não forem uma opção, o que é raro e desaconselhado).
    * **Importância:** Embora Prepared Statements/ORMs sejam a defesa primária, a validação da entrada é crucial como uma camada adicional (defesa em profundidade). Ela previne não apenas SQL Injection, mas outros ataques (XSS) e garante a integridade dos dados. Escaping manual é propenso a erros e deve ser evitado em favor das técnicas anteriores.

## Exemplos de Código Seguro

Veja o arquivo `SqlInjectionPrevention.java`:

* `buscarUsuarioPorEmail_Vulneravel()`: **NÃO FAÇA ISSO!** Mostra o método inseguro com concatenação.
* `buscarUsuarioPorEmail_Seguro()`: Mostra o uso correto de `PreparedStatement` com `?`.
* `buscarUsuarioPorEmail_ORM_Placeholder()`: Descreve conceitualmente como um ORM (JPA) lidaria com a busca de forma segura.

## Medidas Adicionais de Segurança na Camada de Banco de Dados

Além de escrever código seguro na aplicação, medidas adicionais no nível do banco de dados e infraestrutura são importantes:

1.  **Princípio do Menor Privilégio:** Conceder ao usuário do banco de dados usado pela aplicação apenas as permissões estritamente necessárias para suas operações (SELECT, INSERT, UPDATE, DELETE em tabelas específicas, EXECUTE em procedures específicas). Evitar usar contas com privilégios de administrador (root, sa, admin) na aplicação.
2.  **Web Application Firewall (WAF):** Um WAF pode detectar e bloquear padrões comuns de ataques SQL Injection (e outros ataques web) antes que cheguem à aplicação. É uma camada de defesa externa útil.
3.  **Monitoramento e Logs:** Monitorar logs do banco de dados e da aplicação para detectar atividades suspeitas ou erros que possam indicar tentativas de ataque.
4.  **Atualizações de Segurança:** Manter o SGBD, o driver JDBC, o ORM e outras bibliotecas atualizadas com os últimos patches de segurança.

A combinação de Prepared Statements/ORMs no código com o princípio do menor privilégio no banco de dados forma a espinha dorsal da prevenção de SQL Injection.