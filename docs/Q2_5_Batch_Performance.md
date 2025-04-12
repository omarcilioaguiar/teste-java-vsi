# Questão 2.5: Diagnóstico e Melhoria de Performance de Processo Batch (DB + FTP)

Um processo batch que interage com um banco de dados (DB) e um servidor FTP pode apresentar gargalos em diversas etapas. Diagnosticar e otimizar requer uma abordagem sistemática.

## Passos para Diagnóstico e Identificação de Bottlenecks

1.  **Monitoramento e Logging Detalhado:**
    * **Instrumentação:** Adicionar logs com timestamps precisos no início e fim de cada etapa principal do processo:
        * Início/Fim da execução total.
        * Leitura de dados do DB (início da query, fim da recepção dos dados).
        * Processamento de cada lote/registro.
        * Conexão com o servidor FTP.
        * Transferência de cada arquivo (início, fim, tamanho, taxa de transferência).
        * Desconexão do FTP.
        * Atualizações no DB (se houver).
    * **Métricas:** Coletar métricas de performance: tempo de execução de queries, número de registros processados por segundo, taxa de transferência FTP (KB/s ou MB/s), uso de CPU e memória do servidor da aplicação e do DB durante a execução do batch.

2.  **Análise dos Logs e Métricas:**
    * Identificar as etapas que consomem a maior parte do tempo total de execução. É a leitura do DB? O processamento dos dados na aplicação? A transferência FTP? As atualizações no DB?
    * Verificar se há picos de uso de recursos (CPU, memória, I/O de disco, rede) nos servidores envolvidos (aplicação, DB, FTP) durante a execução.

3.  **Profiling (se necessário):**
    * Se a análise de logs indicar que o gargalo está no código da aplicação (processamento dos dados), usar um **Java Profiler** (como JProfiler, YourKit, VisualVM com sampling/profiling) para analisar o uso de CPU e memória em nível de método, identificando métodos lentos ou excesso de alocação de objetos.

4.  **Análise Específica do Banco de Dados:**
    * Usar ferramentas específicas do SGBD para analisar o plano de execução das queries lentas (ex: `EXPLAIN PLAN` em Oracle/PostgreSQL, `EXPLAIN` em MySQL, "Estimated Execution Plan" no SQL Server). Verificar se índices estão sendo usados corretamente, se há full table scans desnecessários, etc.
    * Monitorar o DB durante a execução do batch para identificar esperas (locks, I/O, etc.). Ferramentas como AWR (Oracle), `pg_stat_activity` (PostgreSQL), Performance Monitor (SQL Server) podem ajudar.

5.  **Análise Específica do FTP:**
    * Verificar a latência e a largura de banda da rede entre o servidor da aplicação e o servidor FTP (`ping`, `traceroute`, `iperf`).
    * Analisar os logs do servidor FTP (se disponíveis) para verificar tempos de resposta e possíveis erros.
    * Testar a velocidade de transferência manualmente com um cliente FTP para comparar com a performance da aplicação.

## Técnicas de Otimização

Com base nos gargalos identificados:

1.  **Otimização de Consultas ao Banco de Dados:**
    * **Indexação:** Criar ou ajustar índices nas colunas usadas nas cláusulas `WHERE`, `JOIN` e `ORDER BY` das queries de leitura/atualização.
    * **Reescrita de Queries:** Simplificar queries complexas, evitar `SELECT *` (trazer apenas colunas necessárias), otimizar JOINs, usar subqueries ou CTEs eficientemente.
    * **Batching de Leitura/Escrita:**
        * *Leitura:* Se estiver lendo muitos dados, buscar em lotes (chunks) menores em vez de carregar tudo na memória de uma vez (usando `LIMIT`/`OFFSET`, `Workspace size` do JDBC, ou paginação do ORM).
        * *Escrita:* Se estiver atualizando/inserindo muitos registros, usar batch updates do JDBC (`addBatch()`, `executeBatch()`) ou mecanismos de bulk insert do ORM/SGBD para reduzir o overhead de comunicação com o DB.
    * **Nível de Isolamento:** Verificar se o nível de isolamento da transação é apropriado. Um nível muito alto pode causar mais locks e reduzir a concorrência.

2.  **Melhora na Execução da Lógica:**
    * **Algoritmos:** Revisar os algoritmos de processamento de dados. Há loops ineficientes? Cálculos redundantes?
    * **Gerenciamento de Memória:** Evitar criar objetos desnecessários dentro de loops. Reutilizar objetos quando possível. Usar tipos primitivos em vez de wrappers se a nulidade não for um problema.
    * **Processamento Paralelo/Assíncrono:** Se as tarefas de processamento de registros ou transferência de arquivos forem independentes, considerar paralelizar o trabalho usando `ExecutorService`, `CompletableFuture`, ou frameworks como Spring Batch que têm suporte a processamento paralelo/multithreaded. *Cuidado:* Isso pode aumentar a carga no DB ou no servidor FTP, exigindo ajuste fino.

3.  **Otimização da Transferência FTP:**
    * **Bibliotecas Eficientes:** Garantir o uso de bibliotecas FTP/SFTP Java eficientes e atualizadas (ex: Apache Commons Net, JSch).
    * **Tamanho do Buffer:** Ajustar o tamanho do buffer de transferência na biblioteca FTP pode impactar a performance.
    * **Conexões Persistentes:** Se transferindo múltiplos arquivos pequenos, tentar reutilizar a conexão FTP em vez de conectar/desconectar para cada arquivo (se a biblioteca e o servidor suportarem).
    * **Transferência Paralela:** Se transferindo múltiplos arquivos e o servidor FTP/rede suportarem, transferir arquivos em paralelo usando múltiplas threads/conexões.
    * **Compressão:** Se a largura de banda for o gargalo e os arquivos forem compressíveis, comprimir os dados antes de transferir e descomprimir no destino (requer acordo entre as partes).
    * **Protocolo:** Considerar usar SFTP (SSH File Transfer Protocol) ou FTPS (FTP over SSL/TLS) em vez de FTP simples, pois além de mais seguros, podem ter implementações mais eficientes em alguns casos. Verificar se o gargalo não é a criptografia em si.

## Ferramentas e Técnicas de Análise

* **Logging Frameworks:** Logback, Log4j2 (com Mapped Diagnostic Context - MDC para rastrear fluxos).
* **APM (Application Performance Management):** Ferramentas como Dynatrace, Datadog, New Relic, AppDynamics (monitoramento contínuo, identificação de gargalos, análise de queries).
* **Java Profilers:** JProfiler, YourKit, VisualVM (análise detalhada de CPU, memória, threads).
* **Ferramentas de Banco de Dados:** `EXPLAIN PLAN`/`EXPLAIN`, SQL Developer/pgAdmin/MySQL Workbench (planos de execução), AWR/Statspack (Oracle), `pg_stat_activity` (PostgreSQL), Performance Dashboard Reports (SQL Server).
* **Ferramentas de Rede:** `ping`, `traceroute`, `iperf`, Wireshark (análise de latência, banda, pacotes).
* **Benchmarking:** Criar testes isolados para medir a performance de partes específicas (só a query, só o processamento, só o FTP) com volumes de dados controlados.

A chave é medir, identificar o maior gargalo, otimizar esse ponto, e repetir o processo até que a performance atinja o nível desejado ou aceitável.