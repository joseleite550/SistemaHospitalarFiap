CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL
);
CREATE TABLE consultas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_hora TIMESTAMP NOT NULL,
    medico_id BIGINT NOT NULL,
    enfermeiro_id BIGINT,
    paciente_id BIGINT NOT NULL,
    observacoes VARCHAR(500),
    FOREIGN KEY (medico_id) REFERENCES usuarios(id),
    FOREIGN KEY (enfermeiro_id) REFERENCES usuarios(id),
    FOREIGN KEY (paciente_id) REFERENCES usuarios(id)
);
