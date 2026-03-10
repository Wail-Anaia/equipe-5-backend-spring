-- ══════════════════════════════════════════════════════
-- V1 — Schéma initial : types ENUM + tables de base
-- ══════════════════════════════════════════════════════

-- Types ENUM PostgreSQL
CREATE TYPE user_role        AS ENUM ('ADMIN', 'ENCADRANT', 'ETUDIANT');
CREATE TYPE project_status   AS ENUM ('EN_ATTENTE', 'EN_COURS', 'TERMINE', 'VALIDE', 'REJETE');
CREATE TYPE document_type    AS ENUM ('PDF', 'DOCX', 'PPTX', 'XLSX', 'ZIP', 'OTHER');
CREATE TYPE livrable_status  AS ENUM ('A_RENDRE', 'SOUMIS', 'VALIDE', 'REJETE');

-- ── Table users ───────────────────────────────────────
CREATE TABLE users (
    id          BIGSERIAL       PRIMARY KEY,
    nom         VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    role        user_role       NOT NULL DEFAULT 'ETUDIANT',
    actif       BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role  ON users(role);

-- ── Table teams ───────────────────────────────────────
CREATE TABLE teams (
    id          BIGSERIAL       PRIMARY KEY,
    nom         VARCHAR(100)    NOT NULL,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- ── Table team_members ────────────────────────────────
CREATE TABLE team_members (
    team_id     BIGINT          NOT NULL REFERENCES teams(id)  ON DELETE CASCADE,
    user_id     BIGINT          NOT NULL REFERENCES users(id)  ON DELETE CASCADE,
    PRIMARY KEY (team_id, user_id)
);

-- ── Table projects ────────────────────────────────────
CREATE TABLE projects (
    id              BIGSERIAL       PRIMARY KEY,
    titre           VARCHAR(200)    NOT NULL,
    description     TEXT,
    technologies    VARCHAR(500),
    status          project_status  NOT NULL DEFAULT 'EN_ATTENTE',
    encadrant_id    BIGINT          REFERENCES users(id)   ON DELETE SET NULL,
    team_id         BIGINT          REFERENCES teams(id)   ON DELETE SET NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_projects_status       ON projects(status);
CREATE INDEX idx_projects_encadrant_id ON projects(encadrant_id);

-- ── Table documents ───────────────────────────────────
CREATE TABLE documents (
    id              BIGSERIAL       PRIMARY KEY,
    file_name       VARCHAR(255)    NOT NULL,
    file_path       VARCHAR(512)    NOT NULL,
    file_size       BIGINT,
    doc_type        document_type   NOT NULL DEFAULT 'OTHER',
    project_id      BIGINT          REFERENCES projects(id) ON DELETE CASCADE,
    student_id      BIGINT          REFERENCES users(id)    ON DELETE SET NULL,
    uploaded_at     TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_documents_project_id ON documents(project_id);
CREATE INDEX idx_documents_student_id ON documents(student_id);

-- ── Table livrables ───────────────────────────────────
CREATE TABLE livrables (
    id              BIGSERIAL       PRIMARY KEY,
    titre           VARCHAR(200)    NOT NULL,
    description     TEXT,
    date_limite     DATE,
    status          livrable_status NOT NULL DEFAULT 'A_RENDRE',
    project_id      BIGINT          NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- ── Table audit_logs ──────────────────────────────────
CREATE TABLE audit_logs (
    id              BIGSERIAL       PRIMARY KEY,
    action          VARCHAR(50)     NOT NULL,
    user_id         BIGINT,
    performed_by    BIGINT,
    details         TEXT,
    ip_address      VARCHAR(45),
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_logs_action     ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at DESC);