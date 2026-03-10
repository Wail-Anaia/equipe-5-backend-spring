-- ══════════════════════════════════════════════════════
-- V3 — Données de démo (encadrant + étudiants + équipe + projet)
-- ══════════════════════════════════════════════════════

-- Encadrant  (password: Encadrant@1)
INSERT INTO users (nom, email, password, role, actif) VALUES
('Prof. Karim Idrissi', 'k.idrissi@university.ma',
 '$2a$12$irZaAlEcHMUDijEv6V1PlO8QmdZWbzfFVOhpPRvCnzqIVqc9lxxme', 'ENCADRANT', TRUE);

-- Étudiants  (password: Etudiant@1)
INSERT INTO users (nom, email, password, role, actif) VALUES
('Yasmine Benhaddou', 'y.benhaddou@etu.ma',
 '$2a$12$6SeM9qLZKSWYz6ORoMasdeO6UzOv2QdOnYxXkTB8JSJoeQs4lj7hK', 'ETUDIANT', TRUE),
('Omar Zouheir', 'o.zouheir@etu.ma',
 '$2a$12$6SeM9qLZKSWYz6ORoMasdeO6UzOv2QdOnYxXkTB8JSJoeQs4lj7hK', 'ETUDIANT', TRUE),
('Sara Alami', 's.alami@etu.ma',
 '$2a$12$6SeM9qLZKSWYz6ORoMasdeO6UzOv2QdOnYxXkTB8JSJoeQs4lj7hK', 'ETUDIANT', TRUE);

-- Équipe
INSERT INTO teams (nom) VALUES ('Team Alpha');

INSERT INTO team_members (team_id, user_id)
SELECT t.id, u.id FROM teams t, users u
WHERE t.nom = 'Team Alpha' AND u.email IN ('y.benhaddou@etu.ma','o.zouheir@etu.ma');

-- Projet démo
INSERT INTO projects (titre, description, technologies, status, encadrant_id, team_id)
SELECT
    'Plateforme e-learning IA',
    'Développement d''une plateforme d''apprentissage adaptatif basée sur l''intelligence artificielle.',
    'Angular, Spring Boot, Python, TensorFlow',
    'EN_COURS',
    (SELECT id FROM users WHERE email = 'k.idrissi@university.ma'),
    (SELECT id FROM teams WHERE nom = 'Team Alpha');