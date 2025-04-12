# Questão 8: Testes para Funcionalidade de Registro de Usuário

Análise da estratégia de testes para a funcionalidade de registro de usuário descrita.

**Funcionalidade:** Tela para inserir, deletar ou atualizar informações de usuário (nome, email, endereço, telefone). Nome e email são obrigatórios. Email deve ser único. Apenas Admins podem deletar usuários.

## 1. Tipos de Testes e Cenários

Uma estratégia de testes abrangente combinaria diferentes tipos de testes para cobrir a funcionalidade em vários níveis:

1.  **Testes Unitários:**
    * **Foco:** Isolar e testar pequenas unidades de código (métodos individuais, classes) no backend.
    * **Onde:** Classes de Serviço (ex: `UsuarioService`), validadores customizados, componentes de utilidade.
    * **Cenários a Testar:**
        * Validação de formato de email (método `isValidEmail(String email)`).
        * Lógica de verificação de campos obrigatórios (nome, email).
        * Lógica de hash de senha (se aplicável, não mencionado mas comum).
        * Métodos do serviço que chamam o repositório (usando Mocks para o repositório):
            * `createUser()`: Mockar repositório para simular sucesso, falha por email duplicado, falha genérica de DB.
            * `updateUser()`: Mockar repositório.
            * `deleteUser()`: Mockar repositório.
            * `findByEmail()`: Mockar repositório para retornar usuário existente ou vazio.

2.  **Testes de Integração:**
    * **Foco:** Testar a interação entre diferentes componentes/camadas do backend, incluindo a interação real com o banco de dados (ou um banco de dados de teste).
    * **Onde:** Testar a camada de Controller/API interagindo com a camada de Serviço e esta com a camada de Repositório/Persistência.
    * **Cenários a Testar:**
        * **Criação:**
            * Criar usuário com dados válidos -> Verificar se o usuário foi persistido corretamente no DB.
            * Tentar criar usuário com email já existente -> Verificar se a exceção/erro esperado é lançado/retornado (ex: erro HTTP 409 Conflict) e se o usuário *não* foi criado.
            * Tentar criar usuário sem campos obrigatórios -> Verificar erro de validação (ex: erro HTTP 400 Bad Request).
        * **Atualização:**
            * Atualizar dados de usuário existente (endereço, telefone) -> Verificar se os dados foram atualizados no DB.
            * Tentar atualizar email para um que já existe (pertencente a *outro* usuário) -> Verificar erro de duplicação.
            * Tentar atualizar dados de usuário inexistente -> Verificar erro (ex: 404 Not Found).
        * **Deleção:**
            * Deletar usuário (como Admin) -> Verificar se o usuário foi removido do DB.
            * Tentar deletar usuário (como não-Admin) -> Verificar erro de permissão (ex: 403 Forbidden).
            * Tentar deletar usuário inexistente -> Verificar erro (ex: 404 Not Found).
        * **Leitura/Busca:** (Implícito na funcionalidade, útil para verificações)
            * Buscar usuário por email existente/inexistente.

3.  **Testes End-to-End (E2E):**
    * **Foco:** Simular o fluxo completo do usuário através da interface gráfica (navegador).
    * **Onde:** Usar ferramentas como Cypress, Selenium, Playwright para interagir com a tela de registro.
    * **Cenários a Testar:**
        * Abrir a tela de registro.
        * Preencher o formulário com dados válidos e submeter -> Verificar mensagem de sucesso na UI e/ou se o usuário aparece em uma lista (se aplicável).
        * Preencher o formulário sem campos obrigatórios e submeter -> Verificar se mensagens de erro aparecem nos campos corretos.
        * Preencher com email inválido -> Verificar erro no campo email.
        * Tentar registrar com email já existente -> Verificar mensagem de erro apropriada.
        * (Como Admin) Navegar até um usuário, clicar em deletar, confirmar -> Verificar se o usuário some da UI.
        * (Como não-Admin) Verificar se o botão/opção de deletar está desabilitado ou ausente, ou se ao tentar a ação, uma mensagem de erro é exibida.

## 2. Exemplos de Edge Cases e Como Lidar

Edge cases testam os limites e condições incomuns da funcionalidade.

* **Nomes/Emails/Endereços Muito Longos:**
    * *Teste:* Inserir dados que excedam o limite esperado ou o tamanho do campo no banco de dados.
    * *Tratamento Esperado:* A aplicação deve validar o comprimento máximo (idealmente na UI e no backend) e retornar um erro claro. O banco de dados também pode rejeitar (gerando um erro no backend).
* **Caracteres Especiais:**
    * *Teste:* Inserir nomes, emails (parte local), endereços com apóstrofos (`'`), hífens (`-`), acentos, espaços múltiplos, caracteres não-latinos, emojis, ou até mesmo tags HTML/Script (`<script>alert('XSS')</script>`).
    * *Tratamento Esperado:* Validações devem permitir caracteres válidos para cada campo (ex: permitir acentos em nomes, mas não `<script>`). O backend deve sanitizar entradas (especialmente as exibidas na UI) para prevenir XSS. Emails devem seguir o padrão RFC, mas ter cuidado com unicode se suportado. Queries devem usar Prepared Statements para prevenir SQL Injection.
* **Campos Opcionais Vazios vs. Nulos:**
    * *Teste:* Enviar endereço/telefone como string vazia (`""`) ou como `null` (se a API permitir JSON null).
    * *Tratamento Esperado:* A aplicação deve lidar consistentemente com ambos os casos, geralmente tratando-os como "não fornecido".
* **Email Case-Insensitive?**
    * *Teste:* Registrar `user@example.com`. Tentar registrar `User@example.com`.
    * *Tratamento Esperado:* Geralmente, emails são considerados case-insensitive na parte do domínio, mas potencialmente case-sensitive na parte local (antes do @), embora na prática muitos sistemas tratem tudo como case-insensitive. A aplicação deve definir uma regra (usualmente case-insensitive para verificação de unicidade) e aplicá-la consistentemente (ex: armazenar sempre em lowercase).
* **Concorrência:**
    * *Teste:* (Difícil em testes manuais/E2E, mais para testes de integração/carga) Duas requisições tentam registrar o MESMO email exatamente ao mesmo tempo.
    * *Tratamento Esperado:* A constraint `UNIQUE` no banco de dados deve garantir que apenas uma das operações tenha sucesso. A aplicação deve tratar a exceção de violação de constraint retornando um erro apropriado (ex: 409 Conflict) para a requisição que falhou.
* **Atualização do Próprio Email:**
    * *Teste:* Usuário A tenta atualizar seu email para um email que já pertence ao Usuário B.
    * *Tratamento Esperado:* A validação de unicidade deve impedir a operação e retornar um erro.

## 3. Exemplo de Teste Case (Código/Pseudocódigo)

**Exemplo de Teste de Integração (usando JUnit 5 e AssertJ, simulando um teste com Spring Boot e um banco de dados de teste como H2):**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional; // Para rollback

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Usa um perfil de teste (ex: application-test.properties com H2)
class UserRegistrationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository; // Para verificações diretas no DB (opcional)

    // DTO para representar o payload da requisição
    static class UserRegistrationRequest {
        public String name;
        public String email;
        public String address;
        public String phone;
        // Construtor, getters/setters omitidos por brevidade
    }

    @Test
    @Transactional // Garante rollback após o teste, mantendo o DB limpo
    void testRegisterUser_WithDuplicateEmail_ShouldReturnConflict() {
        // Arrange: Garantir que um usuário com o email já existe
        User existingUser = new User("Existing User", "duplicate@example.com", "Addr 1", "111");
        userRepository.save(existingUser); // Salva diretamente no DB de teste

        // Preparar a nova requisição com o mesmo email
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.name = "New User";
        request.email = "duplicate@example.com"; // Email duplicado
        request.address = "Addr 2";
        request.phone = "222";

        // Act: Chamar o endpoint de registro da API
        ResponseEntity<String> response = restTemplate.postForEntity("/api/users/register", request, String.class);

        // Assert: Verificar o resultado
        // 1. Status HTTP esperado: 409 Conflict
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        // 2. (Opcional) Verificar a mensagem de erro no corpo da resposta
        assertThat(response.getBody()).containsIgnoringCase("Email já cadastrado");

        // 3. Verificar se o novo usuário NÃO foi salvo no DB
        long count = userRepository.countByEmail("duplicate@example.com");
        assertThat(count).isEqualTo(1); // Deve haver apenas o usuário original
    }

     @Test
    @Transactional
    void testRegisterUser_WithValidData_ShouldReturnCreated() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.name = "Valid User";
        request.email = "valid@example.com";
        request.address = "Valid Address";
        request.phone = "123456789";

        // Act
        ResponseEntity<User> response = restTemplate.postForEntity("/api/users/register", request, User.class); // Supondo que retorna o usuário criado

        // Assert
        // 1. Status HTTP: 201 Created
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 2. Corpo da resposta contém os dados do usuário (sem a senha, se houver)
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("valid@example.com");
        assertThat(response.getBody().getName()).isEqualTo("Valid User");
        // Verificar ID gerado, etc.

        // 3. Verificar se foi salvo no DB
         assertThat(userRepository.findByEmail("valid@example.com")).isPresent();
    }

     @Test
    void testDeleteUser_AsNonAdmin_ShouldReturnForbidden() {
         // Arrange: Criar um usuário para deletar e obter token de não-admin
         // ... (setup do usuário e obtenção de token JWT/sessão de não-admin)
         long userIdToDelete = /* ... obter ID do usuário criado ... */;
         // HttpHeaders headers = new HttpHeaders();
         // headers.setBearerAuth("non-admin-token");
         // HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // Act: Tentar chamar o endpoint DELETE (simulação)
         // ResponseEntity<Void> response = restTemplate.exchange(
         //      "/api/users/{id}", HttpMethod.DELETE, requestEntity, Void.class, userIdToDelete);

        // Assert: (Simulado, pois o setup é complexo)
         // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
          System.out.println("Simulando teste: DELETE /api/users/{id} como não-admin -> Esperado 403 Forbidden");
          assertThat(HttpStatus.FORBIDDEN.value()).isEqualTo(403); // Placeholder assertion
    }

    // Outros testes para validação de campos obrigatórios, atualização, etc.
}

// Classe User (Entidade JPA - Exemplo)
// @Entity
class User {
    // @Id @GeneratedValue Long id;
    String name; String email; String address; String phone;
    // Construtores, Getters, Setters...
     public User(String name, String email, String address, String phone) { /*...*/ }
     public String getEmail() { return email; }
     public String getName() { return name; }
}

// Interface UserRepository (Spring Data JPA - Exemplo)
// interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<User, Long> {
//     java.util.Optional<User> findByEmail(String email);
//     long countByEmail(String email);
// }
interface UserRepository { // Mock para compilar o exemplo
    User save(User u);
    java.util.Optional<User> findByEmail(String e);
    long countByEmail(String e);
}