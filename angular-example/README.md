# 🅰️ Exemplo Angular Básico (Questão 2.3)

Este diretório contém snippets de código para um componente Angular (`app.component.ts` e `app.component.html`) que demonstram conceitos básicos solicitados na questão 2.3 do teste, como:

* **Data Binding:**
    * Interpolação (`{{ propriedade }}`)
    * Property Binding (`[propriedadeHTML]="propriedadeTS"`)
    * Event Binding (`(eventoHTML)="metodoTS($event)"`)
    * Two-way Binding (`[(ngModel)]="propriedadeTS"`) - *Nota: Requer `FormsModule`*
* **Estrutura de Componente:** Demonstra a estrutura básica de um arquivo TypeScript de componente (`@Component` decorator, propriedades, métodos).

## ⚠️ Importante

* **Simplicidade:** Este exemplo é intencionalmente **muito básico** para refletir a minha experiência com Angular (que apesar de pouca experiência com a linguagem, consigo fazer coisas básicas).
* **Não é uma Aplicação Funcional Completa:** Estes são apenas os arquivos de *um* componente. Para rodar este código, você precisaria:
    1.  Ter o [Node.js e o Angular CLI](https://angular.io/guide/setup-local) instalados.
    2.  Criar um novo projeto Angular: `ng new meu-projeto-angular`
    3.  Substituir o conteúdo de `src/app/app.component.ts` e `src/app/app.component.html` pelos arquivos deste diretório.
    4.  **Para o `[(ngModel)]` funcionar:** Importar `FormsModule` em `src/app/app.module.ts`:
        ```typescript
        import { BrowserModule } from '@angular/platform-browser';
        import { NgModule } from '@angular/core';
        import { FormsModule } from '@angular/forms'; // <--- Importar aqui

        import { AppComponent } from './app.component';

        @NgModule({
          declarations: [
            AppComponent
          ],
          imports: [
            BrowserModule,
            FormsModule // <--- Adicionar aqui
          ],
          providers: [],
          bootstrap: [AppComponent]
        })
        export class AppModule { }
        ```
    5.  Rodar a aplicação: `ng serve -o`

O objetivo principal aqui é **mostrar o código e a compreensão dos conceitos básicos**, não fornecer uma aplicação completa e executável diretamente destes arquivos isolados.