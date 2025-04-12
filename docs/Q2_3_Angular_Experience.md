# Questão 2.3: Experiência com Angular

**Possuo experiência básica com Angular.** Consigo realizar tarefas fundamentais como:

* Criação e configuração de componentes.
* Utilização de data binding (one-way e two-way com `ngModel`).
* Comunicação simples entre componentes (usando `@Input` e `@Output`).
* Criação e injeção de serviços básicos para encapsular lógica ou chamadas HTTP simples.
* Configuração de rotas básicas.

Ainda estou em processo de aprofundamento nos recursos mais avançados do framework, como gerenciamento de estado complexo (NgRx/NGXS), RxJS avançado, otimizações de performance e funcionalidades mais específicas do ecossistema Angular.

## Core Features e Casos de Uso do Angular

Angular é um framework robusto baseado em TypeScript, mantido pelo Google, para construção de aplicações web, mobile e desktop. Seus principais recursos incluem:

* **Arquitetura Baseada em Componentes:** A UI é construída como uma árvore de componentes reutilizáveis e encapsulados.
* **TypeScript:** Utiliza TypeScript, adicionando tipagem estática e recursos modernos ao JavaScript, o que melhora a manutenibilidade e escalabilidade.
* **Data Binding:** Sincronização de dados entre a lógica do componente (classe TypeScript) e a view (template HTML).
    * *One-way:* Interpolação (`{{ }}`), Property Binding (`[]`).
    * *Event Binding:* `()`.
    * *Two-way:* `[()]` (geralmente com `ngModel`).
* **Injeção de Dependência (DI):** Um mecanismo poderoso para fornecer instâncias de serviços e outras dependências aos componentes, promovendo desacoplamento e testabilidade.
* **Módulos (NgModules):** Organizam a aplicação em blocos coesos de funcionalidade, gerenciando componentes, diretivas, pipes e serviços.
* **Roteamento (`@angular/router`):** Permite a criação de Single Page Applications (SPAs) com navegação entre diferentes views sem recarregar a página inteira.
* **Serviços:** Classes usadas para encapsular lógica de negócio, acesso a dados ou outras funcionalidades que podem ser compartilhadas entre componentes.
* **RxJS:** Utiliza extensivamente a biblioteca Reactive Extensions for JavaScript para lidar com programação assíncrona e eventos de forma reativa.
* **Angular CLI:** Ferramenta de linha de comando essencial para criar, gerenciar, testar e construir projetos Angular.

**Casos de Uso Comuns:**

* **Single Page Applications (SPAs):** Aplicações web complexas que oferecem experiência de usuário fluida, semelhante a aplicativos desktop.
* **Aplicações Empresariais (Enterprise):** Projetos de grande escala que se beneficiam da estrutura opinativa, tipagem e escalabilidade do Angular.
* **Progressive Web Apps (PWAs):** Aplicações web que podem ser instaladas e funcionar offline.
* **Aplicações Multiplataforma:** Com frameworks como Ionic ou NativeScript, o código Angular pode ser usado para construir aplicativos móveis nativos.

## Exemplo Prático (Básico)

Um cenário simples onde usei Angular foi para criar um pequeno formulário de cadastro. Usei:

1.  **Componente:** Para encapsular o HTML do formulário e a lógica de manipulação dos dados.
2.  **Data Binding:** `ngModel` (two-way binding) para vincular os campos do formulário (`<input>`) a propriedades na classe do componente.
3.  **Event Binding:** `(ngSubmit)` no formulário para chamar um método na classe do componente quando o formulário fosse enviado.
4.  **Serviço:** Um serviço simples injetado no componente para lidar com o envio dos dados (neste caso, apenas exibindo no console, mas poderia ser uma chamada HTTP).

## Snippet de Código (Demonstração de Conceitos Básicos)

Os arquivos `angular-example/app.component.ts` e `angular-example/app.component.html` contêm um exemplo **muito básico** que ilustra:

* Interpolação: Exibindo um valor da classe no HTML.
* Property Binding: Definindo o valor de um input a partir da classe.
* Event Binding: Chamando um método da classe quando o usuário digita em um input.
* Two-way Binding (`ngModel`): Sincronizando um input com uma propriedade da classe.

Este snippet serve para demonstrar a compreensão da sintaxe e dos mecanismos fundamentais do data binding no Angular, alinhado com a experiência básica mencionada. Veja também o `angular-example/README.md` para mais detalhes sobre o exemplo.