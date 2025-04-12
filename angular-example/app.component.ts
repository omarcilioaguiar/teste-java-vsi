import { Component } from '@angular/core';
// Para usar [(ngModel)], precisaríamos importar FormsModule em AppModule
// import { FormsModule } from '@angular/forms'; // Não incluído aqui por simplicidade

@Component({
  selector: 'app-root', // Seletor padrão do componente raiz
  templateUrl: './app.component.html', // Aponta para o arquivo HTML
  // styleUrls: ['./app.component.css'] // Arquivo de estilo (opcional)
})
export class AppComponent {
  // Propriedade para One-way Data Binding (Interpolação)
  mensagem: string = 'Olá do Componente!';

  // Propriedade para Property Binding
  mensagemInicial: string = 'Valor inicial';

  // Propriedade para Two-way Data Binding (ngModel)
  textoTwoWay: string = 'Texto inicial Two-Way';

  // Método chamado por Event Binding (evento 'input')
  onInputDigitado(valor: string): void {
    console.log('Evento input detectado! Valor digitado:', valor);
    // Poderia atualizar outra propriedade aqui, se necessário
  }

  // Propriedade que poderia ser passada para um componente filho via @Input
  valorParaFilho: string = 'Dados do Pai';

  constructor() {
    // Código que executa quando o componente é inicializado
    console.log('AppComponent inicializado.');
  }
}

// NOTA: Este é apenas o arquivo do *componente*. Um projeto Angular completo
// precisaria de um módulo (app.module.ts), um arquivo principal (main.ts),
// um index.html, e configurações de build (angular.json, tsconfig.json, etc.).
// Este snippet foca em demonstrar a sintaxe e conceitos básicos pedidos na questão,
// refletindo uma experiência básica com o framework.