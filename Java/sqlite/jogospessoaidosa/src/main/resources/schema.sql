-- Tabela Equipe
CREATE TABLE IF NOT EXISTS equipe (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    rpa TEXT NOT NULL
);

-- Tabela Representante
CREATE TABLE IF NOT EXISTS representante (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    equipe_id INTEGER,
    FOREIGN KEY (equipe_id) REFERENCES equipe(id) ON DELETE CASCADE
);

-- Tabela Telefone
CREATE TABLE IF NOT EXISTS telefone (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    dd TEXT NOT NULL,
    numero TEXT NOT NULL,
    representante_id INTEGER,
    FOREIGN KEY (representante_id) REFERENCES representante(id) ON DELETE CASCADE
);

-- Tabela Doenca
CREATE TABLE IF NOT EXISTS doenca (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT,
    descricao TEXT
);

-- Tabela Pessoa
CREATE TABLE IF NOT EXISTS pessoa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT,
    idade INTEGER,
    cpf TEXT,
    rg TEXT,
    data_nascimento INTEGER,
    data_nascimento_formatada TEXT,
    idoso BOOLEAN,
    avaliacaoMedica BOOLEAN
);

-- Tabela Usuario
CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    palavra_magica TEXT UNIQUE NOT NULL,
    senha TEXT NOT NULL
);

--  JOINs
CREATE TABLE IF NOT EXISTS pessoa_doencas (
    pessoa_id INTEGER NOT NULL,
    doenca_id INTEGER NOT NULL,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
    FOREIGN KEY (doenca_id) REFERENCES doenca(id),
    PRIMARY KEY (pessoa_id, doenca_id)
);

CREATE TABLE IF NOT EXISTS pessoa_doencas (
    pessoa_id INTEGER NOT NULL,
    doenca_id INTEGER NOT NULL,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
    FOREIGN KEY (doenca_id) REFERENCES doenca(id),
    PRIMARY KEY (pessoa_id, doenca_id)
);

CREATE TABLE IF NOT EXISTS pessoa_participantes (
    pessoa_id INTEGER NOT NULL,
    participante_id INTEGER NOT NULL,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
    FOREIGN KEY (participante_id) REFERENCES participante(id),
    PRIMARY KEY (pessoa_id, participante_id)
);

CREATE TABLE IF NOT EXISTS pessoa_equipes (
    pessoa_id INTEGER,
    equipe_id INTEGER,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
    FOREIGN KEY (equipe_id) REFERENCES equipe(id)
);

CREATE TABLE IF NOT EXISTS equipe_participantes (
    equipe_id INTEGER NOT NULL,
    pessoa_id INTEGER NOT NULL,
    PRIMARY KEY (equipe_id, pessoa_id),
    FOREIGN KEY (equipe_id) REFERENCES equipe(id) ON DELETE CASCADE,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS equipe_pessoa (
    equipe_id INTEGER NOT NULL,
    pessoa_id INTEGER NOT NULL,
    PRIMARY KEY (equipe_id, pessoa_id),
    FOREIGN KEY (equipe_id) REFERENCES equipe(id) ON DELETE CASCADE,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
);
