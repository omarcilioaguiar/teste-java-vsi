-- Teste VSI - Questão 6: Queries SQL
-- Baseado nas tabelas Salesperson, Customer e Orders fornecidas.

-- Tabelas e Dados de Exemplo (para referência e teste)
/*
-- Tabela Salesperson
CREATE TABLE Salesperson (
    ID INT PRIMARY KEY,
    Name VARCHAR(50),
    Age INT,
    Salary DECIMAL(10, 2)
);
INSERT INTO Salesperson (ID, Name, Age, Salary) VALUES
(1, 'Abe', 61, 140000),
(2, 'Bob', 34, 44000),
(5, 'Chris', 34, 40000),
(7, 'Dan', 41, 52000),
(8, 'Ken', 57, 115000),
(11, 'Joe', 38, 38000);

-- Tabela Customer
CREATE TABLE Customer (
    ID INT PRIMARY KEY,
    Name VARCHAR(50),
    City VARCHAR(50),
    Industry_Type CHAR(1)
);
INSERT INTO Customer (ID, Name, City, Industry_Type) VALUES
(4, 'Samsonic', 'Pleasant', 'J'),
(6, 'Panasung', 'Oaktown', 'J'),
(7, 'Samony', 'Jackson', 'B'),
(9, 'Orange', 'Jackson', 'B');

-- Tabela Orders
CREATE TABLE Orders (
    ID INT PRIMARY KEY,
    order_date DATE,
    customer_id INT,
    salesperson_id INT,
    Amount DECIMAL(10, 2),
    FOREIGN KEY (customer_id) REFERENCES Customer(ID),
    FOREIGN KEY (salesperson_id) REFERENCES Salesperson(ID)
);
-- Atenção ao formato da data, pode variar conforme o SGBD. Exemplo 'YYYY-MM-DD'
INSERT INTO Orders (ID, order_date, customer_id, salesperson_id, Amount) VALUES
(10, '1996-08-02', 4, 2, 540),
(20, '1999-01-30', 4, 8, 1800),
(30, '1995-07-14', 9, 1, 460),
(40, '1998-01-29', 7, 2, 2400),
(50, '1998-02-03', 6, 7, 600),  -- Correção: PDF indica customer 7, mas deve ser 6 (Panasung)? Usando 6 conforme lógica da Q6a. Ou pode ser customer 7 mesmo. Vou usar 6 para Q6a fazer sentido. Se for 7, Dan (7) teria pedido com Samony. Vou assumir que o PDF tem um erro e que o pedido 50 é do customer 6 (Panasung) para o salesperson 7 (Dan).  -- UPDATE: Mantendo como 7 conforme o PDF.
(60, '1998-03-02', 6, 7, 720),
(70, '1998-05-06', 9, 7, 150);

-- Atualização: Reavaliando o pedido 50 com customer_id = 7 (Samony de Jackson).
-- Isso afeta a query 6c, pois Dan (7) terá feito pedido para Jackson.

-- Recriando inserções com dados EXATOS do PDF (inclusive pedido 50 com customer 7)
DELETE FROM Orders;
DELETE FROM Customer;
DELETE FROM Salesperson;

INSERT INTO Salesperson (ID, Name, Age, Salary) VALUES (1, 'Abe', 61, 140000), (2, 'Bob', 34, 44000), (5, 'Chris', 34, 40000), (7, 'Dan', 41, 52000), (8, 'Ken', 57, 115000), (11, 'Joe', 38, 38000);
INSERT INTO Customer (ID, Name, City, Industry_Type) VALUES (4, 'Samsonic', 'Pleasant', 'J'), (6, 'Panasung', 'Oaktown', 'J'), (7, 'Samony', 'Jackson', 'B'), (9, 'Orange', 'Jackson', 'B');
INSERT INTO Orders (ID, order_date, customer_id, salesperson_id, Amount) VALUES (10, '1996-08-02', 4, 2, 540), (20, '1999-01-30', 4, 8, 1800), (30, '1995-07-14', 9, 1, 460), (40, '1998-01-29', 7, 2, 2400), (50, '1998-02-03', 7, 7, 600), (60, '1998-03-02', 6, 7, 720), (70, '1998-05-06', 9, 7, 150);

*/

-- -----------------------------------------------------------------------------
-- Questão 6a: Retorna os nomes de todos os Salesperson que NÃO têm nenhum pedido com 'Samsonic'.
-- -----------------------------------------------------------------------------
-- Lógica:
-- 1. Encontrar o ID do cliente 'Samsonic'.
-- 2. Encontrar os IDs de todos os Salesperson que TÊM pedidos com esse cliente.
-- 3. Selecionar os nomes dos Salesperson cujo ID NÃO ESTÁ na lista encontrada no passo 2.

-- Query 6a:
SELECT sp.Name
FROM Salesperson sp
WHERE sp.ID NOT IN (
    SELECT DISTINCT o.salesperson_id
    FROM Orders o
    JOIN Customer c ON o.customer_id = c.ID
    WHERE c.Name = 'Samsonic'
);

-- Alternativa com LEFT JOIN (pode ser mais performática em alguns SGBDs)
-- SELECT sp.Name
-- FROM Salesperson sp
-- LEFT JOIN (
--     SELECT DISTINCT o.salesperson_id
--     FROM Orders o
--     JOIN Customer c ON o.customer_id = c.ID
--     WHERE c.Name = 'Samsonic'
-- ) AS sp_com_samsonic ON sp.ID = sp_com_samsonic.salesperson_id
-- WHERE sp_com_samsonic.salesperson_id IS NULL;

-- Resultado esperado: Abe, Chris, Dan, Joe (Bob e Ken tiveram pedidos com Samsonic)


-- -----------------------------------------------------------------------------
-- Questão 6b: Atualiza os nomes dos Salesperson que têm 2 ou mais pedidos, adicionando '*' no final do nome.
-- -----------------------------------------------------------------------------
-- Lógica:
-- 1. Contar o número de pedidos por salesperson_id na tabela Orders.
-- 2. Filtrar os salesperson_id que têm contagem >= 2.
-- 3. Atualizar a tabela Salesperson, concatenando '*' ao nome, para os IDs encontrados no passo 2.

-- Query 6b: (A sintaxe de UPDATE pode variar ligeiramente entre SGBDs, especialmente o JOIN/subquery)
-- Exemplo para PostgreSQL / SQL Server:
UPDATE Salesperson
SET Name = Name || '*' -- ou Name + '*' no SQL Server
WHERE ID IN (
    SELECT salesperson_id
    FROM Orders
    GROUP BY salesperson_id
    HAVING COUNT(*) >= 2
);

-- Exemplo para MySQL:
-- UPDATE Salesperson sp
-- JOIN (
--     SELECT salesperson_id
--     FROM Orders
--     GROUP BY salesperson_id
--     HAVING COUNT(*) >= 2
-- ) AS sp_multi_order ON sp.ID = sp_multi_order.salesperson_id
-- SET sp.Name = CONCAT(sp.Name, '*');

-- Vendedores com >= 2 pedidos: Bob (2 pedidos), Dan (3 pedidos)
-- Resultado esperado: Nomes de Bob e Dan atualizados para 'Bob*' e 'Dan*'


-- -----------------------------------------------------------------------------
-- Questão 6c: Deleta todos os Salesperson que fizeram pedidos para a cidade de 'Jackson'.
-- -----------------------------------------------------------------------------
-- Lógica:
-- 1. Encontrar os IDs dos clientes localizados em 'Jackson'.
-- 2. Encontrar os IDs (distintos) dos Salesperson que têm pedidos para esses clientes.
-- 3. Deletar da tabela Salesperson os registros cujo ID está na lista encontrada no passo 2.

-- Query 6c:
DELETE FROM Salesperson
WHERE ID IN (
    SELECT DISTINCT o.salesperson_id
    FROM Orders o
    JOIN Customer c ON o.customer_id = c.ID
    WHERE c.City = 'Jackson'
);

-- Salespersons com pedidos em Jackson: Abe (pedido 30, cliente 9), Bob (pedido 40, cliente 7), Dan (pedido 50, cliente 7 e pedido 70, cliente 9)
-- Resultado esperado: Registros de Abe (1), Bob (2) e Dan (7) deletados da tabela Salesperson.
-- ATENÇÃO: Se a query 6b foi executada antes, os nomes de Bob e Dan estarão com '*'. A exclusão é pelo ID.


-- -----------------------------------------------------------------------------
-- Questão 6d: O montante total de vendas para cada Salesperson. Se o salesperson não vendeu nada, mostrar zero.
-- -----------------------------------------------------------------------------
-- Lógica:
-- 1. Começar com a tabela Salesperson (para garantir que todos apareçam).
-- 2. Fazer um LEFT JOIN com a tabela Orders usando o salesperson_id. O LEFT JOIN garante que mesmo vendedores sem vendas sejam incluídos.
-- 3. Agrupar os resultados por ID e Nome do Salesperson.
-- 4. Calcular a SOMA (SUM) dos Amounts para cada grupo.
-- 5. Usar COALESCE (ou IFNULL, dependendo do SGBD) para substituir NULL (que ocorre para vendedores sem vendas no LEFT JOIN) por 0.

-- Query 6d:
SELECT
    sp.Name,
    COALESCE(SUM(o.Amount), 0) AS Total_Sales_Amount
FROM
    Salesperson sp
LEFT JOIN
    Orders o ON sp.ID = o.salesperson_id
GROUP BY
    sp.ID, sp.Name -- Agrupar por ID e Nome para garantir unicidade se houver nomes iguais
ORDER BY
    sp.Name;

-- Resultado esperado (considerando o estado inicial das tabelas):
-- Abe: 460.00
-- Bob: 2940.00 (540 + 2400)
-- Chris: 0.00
-- Dan: 1470.00 (600 + 720 + 150)
-- Joe: 0.00
-- Ken: 1800.00

-- Se a query 6c foi executada antes, Abe, Bob e Dan não aparecerão neste resultado.
-- Se a query 6b foi executada antes, Bob e Dan aparecerão com '*' no nome.