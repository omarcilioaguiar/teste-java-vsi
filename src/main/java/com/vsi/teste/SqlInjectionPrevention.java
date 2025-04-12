package com.vsi.teste;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Exemplos de código para demonstrar a prevenção de SQL Injection.
 * NOTA: Este código é apenas para demonstração. Em uma aplicação real,
 * use um pool de conexões e trate exceções de forma mais robusta.
 * Assume-se a existência de um banco de dados e uma tabela 'usuarios'
 * com colunas 'id', 'nome', 'email'.
 *
 * @see ../../../../../docs/Q2_4_SQL_Injection.md Explicação detalhada.
 */
public class SqlInjectionPrevention {

    // --- Dados de Conexão (Exemplo - NÃO FAÇA ISSO EM PRODUÇÃO!) ---
    private static final String DB_URL = "jdbc:h2:mem:testdb"; // Exemplo com H2 em memória
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    // --- Método VULNERÁVEL (NÃO USE!) ---

    /**
     * Exemplo de método VULNERÁVEL a SQL Injection usando concatenação de Strings.
     * @param connection Conexão com o banco.
     * @param email Email fornecido pelo usuário (potencialmente malicioso).
     * @throws SQLException Se ocorrer erro no banco.
     */
    public void buscarUsuarioPorEmail_Vulneravel(Connection connection, String email) throws SQLException {
        // NUNCA construa queries concatenando input do usuário diretamente!
        String sql = "SELECT id, nome, email FROM usuarios WHERE email = '" + email + "'";
        System.out.println("Executando (Vulnerável): " + sql);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                System.out.println("Usuário encontrado (Vulnerável): ID=" + resultSet.getInt("id")
                        + ", Nome=" + resultSet.getString("nome")
                        + ", Email=" + resultSet.getString("email"));
            }
        }
        // Se 'email' for algo como: ' OR '1'='1
        // A query se torna: SELECT id, nome, email FROM usuarios WHERE email = '' OR '1'='1'
        // E retorna TODOS os usuários!
    }

    // --- Método SEGURO usando PreparedStatement ---

    /**
     * Exemplo de método SEGURO usando PreparedStatement (Parameterized Queries).
     * @param connection Conexão com o banco.
     * @param email Email fornecido pelo usuário.
     * @throws SQLException Se ocorrer erro no banco.
     */
    public void buscarUsuarioPorEmail_Seguro(Connection connection, String email) throws SQLException {
        // Query parametrizada com placeholder (?)
        String sql = "SELECT id, nome, email FROM usuarios WHERE email = ?";
        System.out.println("Preparando (Seguro): " + sql);

        // Usa try-with-resources para garantir que PreparedStatement e ResultSet sejam fechados
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Define o valor do parâmetro (placeholder '?') de forma segura
            // O driver JDBC trata o valor para evitar injeção
            preparedStatement.setString(1, email);

            System.out.println("Executando PreparedStatement com parâmetro: " + email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("Usuário encontrado (Seguro): ID=" + resultSet.getInt("id")
                            + ", Nome=" + resultSet.getString("nome")
                            + ", Email=" + resultSet.getString("email"));
                }
            }
        }
        // Se 'email' for ' OR '1'='1', o PreparedStatement tratará como uma string literal
        // e buscará por um email que seja exatamente "' OR '1'='1'", provavelmente não encontrando nada.
    }

    // --- Método SEGURO usando ORM (Conceitual - JPA/Hibernate) ---

    /**
     * Exemplo CONCEITUAL de como seria a busca usando JPA/Hibernate.
     * A implementação real dependeria das suas entidades e repositórios.
     *
     * // 1. Definição da Entidade (Exemplo)
     * @Entity
     * public class Usuario {
     * @Id @GeneratedValue
     * private Long id;
     * private String nome;
     * private String email;
     * // getters/setters...
     * }
     *
     * // 2. Definição do Repositório (Exemplo com Spring Data JPA)
     * public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
     * // O Spring Data JPA gera a query segura baseada no nome do método
     * Optional<Usuario> findByEmail(String email);
     *
     * // Ou usando JPQL (Java Persistence Query Language) que também é seguro contra injection
     * // se usar parâmetros nomeados ou posicionais
     * @Query("SELECT u FROM Usuario u WHERE u.email = :emailParam")
     * Optional<Usuario> buscarPorEmailComQuery(@Param("emailParam") String email);
     * }
     *
     * // 3. Uso no Serviço (Exemplo)
     * @Service
     * public class UsuarioService {
     * @Autowired
     * private UsuarioRepository usuarioRepository;
     *
     * public Optional<Usuario> buscarUsuarioPorEmail_ORM(String email) {
     * System.out.println("Buscando usuário com email (ORM): " + email);
     * // O ORM (Hibernate/JPA) gera a query SQL parametrizada por baixo dos panos
     * Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
     * if(usuario.isPresent()) {
     * System.out.println("Usuário encontrado (ORM): " + usuario.get());
     * } else {
     * System.out.println("Usuário não encontrado (ORM).");
     * }
     * return usuario;
     * }
     * }
     */
    public void buscarUsuarioPorEmail_ORM_Placeholder(String email) {
        System.out.println("--- Exemplo Conceitual com ORM (JPA/Hibernate) ---");
        System.out.println("Framework ORM seria responsável por gerar PreparedStatement internamente.");
        System.out.println("Chamada típica no código: usuarioRepository.findByEmail(\"" + email + "\");");
        System.out.println("Isto é inerentemente seguro contra SQL Injection se o ORM for usado corretamente.");
        System.out.println("--- Fim do Exemplo Conceitual ---");
    }


    // --- Método principal para demonstração ---
    // public static void main(String[] args) {
    //     SqlInjectionPrevention demo = new SqlInjectionPrevention();

    //     try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
    //         System.out.println("Conexão com H2 estabelecida.");

    //         // Criar tabela e inserir dados de teste
    //         try (Statement stmt = conn.createStatement()) {
    //             stmt.execute("DROP TABLE IF EXISTS usuarios;");
    //             stmt.execute("CREATE TABLE usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(255), email VARCHAR(255) UNIQUE);");
    //             stmt.execute("INSERT INTO usuarios (nome, email) VALUES ('Alice', 'alice@example.com');");
    //             stmt.execute("INSERT INTO usuarios (nome, email) VALUES ('Bob', 'bob@example.com');");
    //             System.out.println("Tabela 'usuarios' criada e populada.");
    //         }

    //         System.out.println("\n--- Testando Busca Vulnerável ---");
    //         String emailNormal = "alice@example.com";
    //         String emailMalicioso = "' OR '1'='1"; // Input malicioso clássico

    //         System.out.println("\nBuscando email normal (Vulnerável): " + emailNormal);
    //         demo.buscarUsuarioPorEmail_Vulneravel(conn, emailNormal);

    //         System.out.println("\nBuscando com input malicioso (Vulnerável): " + emailMalicioso);
    //         try {
    //              demo.buscarUsuarioPorEmail_Vulneravel(conn, emailMalicioso); // !! Deve retornar todos !!
    //         } catch(Exception e) {
    //             System.err.println("Erro esperado ou inesperado na busca vulnerável: " + e.getMessage());
    //         }


    //         System.out.println("\n--- Testando Busca Segura (PreparedStatement) ---");
    //         System.out.println("\nBuscando email normal (Seguro): " + emailNormal);
    //         demo.buscarUsuarioPorEmail_Seguro(conn, emailNormal);

    //         System.out.println("\nBuscando com input malicioso (Seguro): " + emailMalicioso);
    //         demo.buscarUsuarioPorEmail_Seguro(conn, emailMalicioso); // !! Não deve retornar nada !!

    //         System.out.println("\n--- Testando Busca Segura (ORM - Conceitual) ---");
    //         demo.buscarUsuarioPorEmail_ORM_Placeholder(emailNormal);
    //         demo.buscarUsuarioPorEmail_ORM_Placeholder(emailMalicioso);


    //     } catch (SQLException e) {
    //         System.err.println("Erro de Banco de Dados: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    // }
}