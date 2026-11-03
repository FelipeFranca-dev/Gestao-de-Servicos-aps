CREATE DATABASE IF NOT EXISTS aps_servicos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE aps_servicos;

CREATE TABLE IF NOT EXISTS clientes (
                                        id    INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        nome  VARCHAR(120) NOT NULL
    );

CREATE TABLE IF NOT EXISTS dados_servicos (
                                              id           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                              tipo         VARCHAR(80)  NOT NULL,
    endereco     VARCHAR(200) NOT NULL,
    `data`       DATE         NOT NULL,
    `status`     VARCHAR(30)  NOT NULL,
    observacoes  TEXT
    );

CREATE TABLE IF NOT EXISTS clientes_servicos (
                                                 id_clientes  INT NOT NULL,
                                                 id_servicos  INT NOT NULL,
                                                 PRIMARY KEY (id_servicos),
    FOREIGN KEY (id_clientes) REFERENCES clientes(id),
    FOREIGN KEY (id_servicos) REFERENCES dados_servicos(id) ON DELETE CASCADE
    );