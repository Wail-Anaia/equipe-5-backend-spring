-- Enum type pour les rôles
CREATE TYPE user_role AS ENUM ('ADMIN', 'ENCADRANT', 'ETUDIANT');

-- Table utilisateurs
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    nom         VARCHAR(100)        NOT NULL,
    email       VARCHAR(255)        NOT NULL UNIQUE,
    password    VARCHAR(255)        NOT NULL,
    role        user_role           NOT NULL,
    actif       BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- Table audit_logs
CREATE TABLE audit_logs (
    id          BIGSERIAL PRIMARY KEY,
    action      VARCHAR(50)         NOT NULL,
    user_id     BIGINT,
    performed_by BIGINT,
    details     TEXT,
    created_at  TIMESTAMP           NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_audit_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_audit_performer FOREIGN KEY (performed_by)
        REFERENCES users(id) ON DELETE SET NULL
);

-- Index pour performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_audit_action ON audit_logs(action);
CREATE INDEX idx_audit_created ON audit_logs(created_at);