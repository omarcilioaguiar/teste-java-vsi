# Questão 7: Funcionalidade de Gerenciamento de Plantas (CRUD)

Análise dos requisitos e respostas para a funcionalidade de gerenciamento de Plantas no sistema XYZ (Fase 1).

## 1. Caso de Uso / User Story

Aqui estão exemplos tanto no formato de Caso de Uso (mais tradicional) quanto User Story (mais ágil).

**Opção A: User Story**

* **Título:** Gerenciar Cadastro de Plantas
* **Como um:** Administrador do Sistema XYZ
* **Eu quero:** Criar, visualizar, atualizar e deletar registros de Plantas no sistema
* **Para que:** Os dados das plantas estejam corretos e disponíveis para uso na segunda fase do projeto e para garantir a integridade das informações de plantas.

* **Critérios de Aceitação:**
    * [Criar] Deve ser possível inserir uma nova Planta fornecendo um Código (numérico, único, obrigatório) e uma Descrição (alfanumérica, opcional, máx. 10 caracteres).
    * [Criar] O sistema DEVE impedir a criação de uma Planta com um Código que já exista.
    * [Visualizar/Pesquisar] Deve ser possível pesquisar/listar as Plantas cadastradas (critérios de pesquisa a definir, mas pelo menos por Código).
    * [Atualizar] Deve ser possível atualizar a Descrição de uma Planta existente. (Assumindo que o Código não pode ser atualizado após a criação).
    * [Deletar] Apenas usuários com perfil "Admin" podem deletar uma Planta existente.
    * [Deletar] Usuários não-Admin NÃO devem ter a opção de deletar ou, se tentarem, devem receber uma mensagem de erro de permissão.
    * [Validação] O campo Código deve aceitar apenas números.
    * [Validação] O campo Descrição deve aceitar caracteres alfanuméricos e ter no máximo 10 caracteres.

**Opção B: Caso de Uso (Resumido)**

* **Nome do Caso de Uso:** Manter Cadastro de Plantas
* **Atores:** Administrador, Usuário Básico (implícito, para C R U)
* **Descrição:** Permite aos usuários autorizados criar, pesquisar, visualizar, atualizar e deletar informações de Plantas no sistema XYZ.
* **Pré-condições:** O usuário deve estar autenticado no sistema XYZ.
* **Fluxo Principal (Exemplo - Criação):**
    1.  Usuário (Admin ou Básico) seleciona a opção para criar uma nova Planta.
    2.  Sistema apresenta o formulário de cadastro com os campos Código e Descrição.
    3.  Usuário preenche o Código (numérico) e opcionalmente a Descrição (alfanumérica, <= 10 chars).
    4.  Usuário submete o formulário.
    5.  Sistema valida os dados (formato, obrigatoriedade). (Exceção 1)
    6.  Sistema verifica se o Código já existe no banco de dados. (Exceção 2)
    7.  Sistema persiste os dados da nova Planta no banco de dados.
    8.  Sistema exibe uma mensagem de sucesso para o usuário.
* **Fluxos Alternativos / Exceções:**
    * **Exceção 1 (Validação Falhou):** Sistema exibe mensagens de erro indicando os campos inválidos e não persiste os dados. Usuário pode corrigir e tentar novamente.
    * **Exceção 2 (Código Duplicado):** Sistema exibe mensagem informando que o código já está em uso e não persiste os dados.
    * **Fluxo de Deleção (Iniciado por Admin):** Admin seleciona uma planta e a opção deletar. Sistema confirma a ação. Sistema remove o registro do banco. Sistema informa sucesso.
    * **Fluxo de Deleção (Tentativa por Usuário Básico):** Usuário básico tenta deletar. Sistema impede a ação e exibe mensagem de permissão negada.

## 2. Regras de Negócio e Premissas Relevantes

**Regras de Negócio Explícitas (do requisito):**

1.  **Código da Planta:**
    * Deve ser numérico.
    * É obrigatório.
    * Deve ser único em todo o sistema.
2.  **Descrição da Planta:**
    * Deve ser alfanumérica.
    * Tem um tamanho máximo de 10 caracteres.
    * É opcional.
3.  **Deleção de Planta:**
    * Apenas usuários com perfil "Admin" podem realizar a operação de exclusão.

**Premissas Assumidas (para construir a solução):**

1.  **Perfil de Usuário:** Existe um sistema de autenticação e autorização que define perfis de usuário, incluindo um perfil "Admin".
2.  **Operações CRUD:** Assume-se que usuários não-Admin (se existirem outros perfis com acesso a esta funcionalidade) podem realizar as operações de Criar, Ler (Pesquisar/Visualizar) e Atualizar Plantas, mas não Deletar.
3.  **Atualização de Código:** Assume-se que o Código da Planta, uma vez criado, não pode ser alterado (é uma chave primária ou chave de negócio imutável). Apenas a Descrição pode ser atualizada. Se o código pudesse ser alterado, a validação de unicidade na atualização seria necessária.
4.  **Persistência:** Os dados das plantas serão armazenados em um banco de dados relacional ou similar.
5.  **Interface:** Haverá uma interface de usuário (tela) ou API para interagir com essas funcionalidades.
6.  **Feedback ao Usuário:** O sistema fornecerá feedback claro ao usuário sobre o sucesso ou falha das operações e sobre erros de validação ou permissão.

## 3. Validações e Medidas de Segurança

**Validações:**

* **Validação de Tipo e Formato:**
    * *Código:* Verificar se a entrada contém apenas dígitos (`[0-9]+`).
    * *Descrição:* Verificar se a entrada contém apenas caracteres alfanuméricos (`[a-zA-Z0-9]*`) e se o comprimento é `<= 10`.
* **Validação de Obrigatoriedade:**
    * *Código:* Verificar se o campo não está vazio ou nulo.
* **Validação de Unicidade:**
    * *Código:* Antes de **criar** uma nova planta (ou **atualizar** o código, se fosse permitido), consultar o banco de dados para garantir que não existe outra planta com o mesmo código. Esta verificação deve ser feita de forma atômica (ex: usando constraints UNIQUE no DB e tratando a exceção, ou com bloqueio pessimista/otimista se necessário em cenários de alta concorrência).
* **Validação de Negócio:**
    * *Descrição:* Embora opcional, pode haver regras adicionais (não especificadas) sobre o conteúdo permitido.

**Medidas de Segurança:**

1.  **Autenticação:** Garantir que apenas usuários logados possam acessar a funcionalidade.
2.  **Autorização (Controle de Acesso):**
    * Implementar verificação de permissão **no backend** antes de executar a operação de `DELETE`. Não confiar apenas em esconder o botão de delete na interface para não-admins. Verificar o perfil/role do usuário que fez a requisição.
    * Verificar também as permissões para `CREATE`, `READ`, `UPDATE` conforme definido para os diferentes perfis de usuário.
3.  **Input Sanitization (Defesa em Profundidade):** Embora as validações de formato já restrinjam a entrada, como boa prática, especialmente se a Descrição for exibida em algum lugar, sanitizar a entrada no backend para prevenir ataques como Cross-Site Scripting (XSS), removendo ou escapando caracteres potencialmente perigosos (ex: `<script>`).
4.  **Prevenção de Mass Assignment (se aplicável a APIs):** Se for uma API REST, garantir que apenas os campos permitidos (Descrição) possam ser atualizados. Não permitir que um usuário mal-intencionado envie outros campos (como o Código ou algum campo interno) no payload de atualização. Usar DTOs (Data Transfer Objects) específicos para cada operação.
5.  **Logs de Auditoria:** Registrar quem criou, atualizou ou deletou qual planta e quando. Isso é importante para rastreabilidade e segurança.

## 4. Sugestões de Testes (Incluindo Edge Cases)

Os testes devem cobrir as funcionalidades (CRUD), validações e segurança.

**Tipos de Testes:**

* **Testes Unitários:** Testar a lógica de validação em isolamento (ex: método que valida se o código é numérico, método que valida o tamanho da descrição). Testar a lógica de negócio nos Services/Controllers (mockando o repositório).
* **Testes de Integração:** Testar a interação entre a camada de serviço/controller e a camada de persistência (banco de dados). Verificar se a unicidade do código está funcionando no nível do DB, se as operações CRUD refletem corretamente no DB. Testar a lógica de autorização.
* **Testes de API (se aplicável):** Testar os endpoints da API REST/GraphQL, verificando os códigos de status HTTP, payloads de requisição/resposta, validações e segurança (autenticação/autorização).
* **Testes End-to-End (E2E):** Simular a interação do usuário na interface gráfica (se houver), cobrindo os fluxos principais de CRUD e validação do ponto de vista do usuário.

**Cenários de Teste (Exemplos):**

* **Happy Path:**
    * Criar planta com código e descrição válidos.
    * Criar planta apenas com código válido (descrição vazia/nula).
    * Pesquisar planta por código existente.
    * Atualizar descrição de planta existente.
    * Deletar planta (como Admin).
* **Validações:**
    * Tentar criar planta com código vazio/nulo.
    * Tentar criar planta com código não-numérico ("ABC", "12A").
    * Tentar criar planta com descrição > 10 caracteres.
    * Tentar criar planta com descrição contendo caracteres não alfanuméricos (se a regra for estrita).
* **Unicidade:**
    * Tentar criar planta com código que já existe.
* **Segurança (Autorização):**
    * Tentar deletar planta (como usuário não-Admin) -> Esperar falha/erro de permissão.
    * Tentar acessar a funcionalidade sem estar logado -> Esperar redirecionamento para login ou erro de não autorizado.
* **Edge Cases:**
    * *Código:* Tentar criar planta com código `0`, código muito grande (verificar limites do tipo de dado no DB), código negativo (se permitido/não permitido pela regra de ser "numérico").
    * *Descrição:* Tentar criar/atualizar com descrição exatamente com 10 caracteres. Tentar criar/atualizar com descrição contendo apenas espaços (se permitido/não permitido). Tentar criar/atualizar com caracteres alfanuméricos de diferentes alfabetos ou unicode (se aplicável).
    * *Deleção:* Tentar deletar uma planta que não existe mais.
    * *Concorrência:* (Mais complexo, geralmente em testes de integração/carga) Dois usuários tentam criar uma planta com o mesmo código simultaneamente.