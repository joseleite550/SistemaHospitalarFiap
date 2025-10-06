--senha 123456
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Dr. Teste', 'medico@fiap.com', '$2a$10$tJ9fP.n.v.rY1f8gZ8n2S.m4xN8qL2Fp.8hT.0g2P0g8K7D.g6D7t', 'MEDICO'),
('Enf. Teste', 'enfermeiro@fiap.com', '$2a$10$tJ9fP.n.v.rY1f8gZ8n2S.m4xN8qL2Fp.8hT.0g2P0g8K7D.g6D7t', 'ENFERMEIRO'),
('Paciente Teste', 'paciente@fiap.com', '$2a$10$tJ9fP.n.v.rY1f8gZ8n2S.m4xN8qL2Fp.8hT.0g2P0g8K7D.g6D7t', 'PACIENTE');

INSERT INTO consultas (data_hora, medico_id, paciente_id, observacoes) VALUES
('2028-11-10 14:00:00', 1, 3, 'Primeira consulta bara base de teste');

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL
);
CREATE TABLE consultas (
    id BIGSERIAL PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL,
    medico_id BIGINT NOT NULL,
    enfermeiro_id BIGINT,
    paciente_id BIGINT NOT NULL,
    observacoes VARCHAR(500),
    FOREIGN KEY (medico_id) REFERENCES usuarios(id),
    FOREIGN KEY (enfermeiro_id) REFERENCES usuarios(id),
    FOREIGN KEY (paciente_id) REFERENCES usuarios(id)
);