#  VSI - Teste de Desenvolvedor Java ☕

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![SQL](https://img.shields.io/badge/SQL-025E8C?style=for-the-badge&logo=sql&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)

Este repositório contém as respostas para o teste de avaliação para Desenvolvedor Java da VSI, conforme o documento `DSE - Test - Java Developer.docx.pdf`.

## 📝 Estrutura do Projeto

O projeto está organizado da seguinte forma:

* `📁 src/main/java/`: Contém o código-fonte Java das soluções.
    * `☕ AnagramGenerator.java`: Solução para a Questão 1 (Gerador de Anagramas).
    * `☕ EqualsHashCodeExample.java`: Código de exemplo para a Questão 2.1 (`equals`/`hashCode`).
    * `☕ decoupling/`: Código de exemplo para a Questão 2.2 (Design Pattern - Adapter).
    * `☕ SqlInjectionPrevention.java`: Exemplos de código para a Questão 2.4 (Prevenção de SQL Injection).
* `📁 src/test/java/`: Contém os testes unitários para o código Java.
    * `🧪 AnagramGeneratorTest.java`: Testes JUnit para a Questão 1.
* `📁 sql/`: Contém as queries SQL.
    * `💾 queries.sql`: Queries SQL para a Questão 6.
* `📁 angular-example/`: Contém um exemplo básico de Angular.
    * `🅰️ *.ts/*.html`: Arquivos do componente Angular para a Questão 2.3.
    * `📄 README.md`: Instruções específicas para visualizar o exemplo Angular.
* `📁 docs/`: Contém as respostas textuais e análises.
    * `📄 Q2_*.md`: Respostas detalhadas para as sub-questões da Questão 2.
    * `📄 Q7_Plants_CRUD.md`: Análise e respostas para a Questão 7.
    * `📄 Q8_User_Registration_Testing.md`: Análise e respostas para a Questão 8.
* `📄 README.md`: Este arquivo principal.

## 🚀 Como Executar e Testar

### 1. Código Java (Questão 1 - Anagramas)

O código Java utiliza Maven como gerenciador de dependências (especificamente para JUnit 5).

* **Compilar:**
    ```bash
    mvn compile
    ```
* **Executar Testes Unitários:**
    ```bash
    mvn test
    ```
  Ou execute a classe `AnagramGeneratorTest.java` diretamente pela sua IDE (Eclipse, IntelliJ IDEA, VS Code com Java Extension Pack).

### 2. Queries SQL (Questão 6)

As queries estão no arquivo `sql/queries.sql`.

* **Execução:**
    1.  Você precisará de um Sistema de Gerenciamento de Banco de Dados (SGBD) que suporte SQL padrão (como PostgreSQL, MySQL, SQL Server, H2, etc.).
    2.  Crie as tabelas `Salesperson`, `Customer` e `Orders` no seu SGBD.
    3.  Insira os dados de exemplo fornecidos no PDF (Página 3).
    4.  Execute as queries do arquivo `queries.sql` usando um cliente SQL (DBeaver, pgAdmin, MySQL Workbench, etc.) conectado ao seu banco de dados.

### 3. Exemplo Angular (Questão 2.3)

O exemplo é propositalmente simples para refletir a experiência básica declarada.

* **Visualização:**
    * Navegue até o diretório `angular-example/`.
    * Siga as instruções no arquivo `angular-example/README.md`. Geralmente, envolve instalar as dependências (`npm install`) e servir a aplicação (`ng serve`), mas como é um exemplo muito básico, pode ser apenas para leitura do código ou adaptação em um projeto Angular existente.

### 4. Respostas Textuais (Questões 2, 7, 8)

As respostas detalhadas para as questões conceituais e de análise estão nos arquivos `.md` dentro do diretório `docs/`. Basta abri-los com um leitor de Markdown ou visualizá-los diretamente no GitHub.

---