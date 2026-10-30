-- Rode este script uma vez no MySQL antes de usar o DaoSql.
-- Ex.: mysql -u root -p < banco/schema.sql

CREATE DATABASE IF NOT EXISTS gestao_servicos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestao_servicos;

CREATE TABLE IF NOT EXISTS servicos (
    id           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cliente      VARCHAR(120) NOT NULL,
    tipo         VARCHAR(80)  NOT NULL,
    endereco     VARCHAR(200) NOT NULL,
    data_servico DATE         NOT NULL,
    status       VARCHAR(30)  NOT NULL,
    observacoes  TEXT
);
