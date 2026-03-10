-- ══════════════════════════════════════════════════════
-- V2 — Données initiales : compte admin par défaut
-- Password : Admin@1234  (BCrypt strength=12)
-- ══════════════════════════════════════════════════════
INSERT INTO users (nom, email, password, role, actif)
VALUES (
    'Wail Anaia',
    'admin@university.ma',
    '$2a$12$Huabfcu1AdMoId8IrVrNQuMa65dp3vCEaVOGf.lW01CU9DMpLEctG',
    'ADMIN',
    TRUE
);