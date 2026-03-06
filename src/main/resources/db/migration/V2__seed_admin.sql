-- Administrateur initial (password: Admin@1234)
-- Hash BCrypt généré avec strength=12
INSERT INTO users (nom, email, password, role, actif)
VALUES (
    'Super Admin',
    'admin@university.ma',
    '$2a$12$mCCHi2rGilU4xisYFu1IhuLZhOHCqOpzqp1C8ErYKhTpKbjqXhL/m',
    'ADMIN',
    TRUE
)
ON CONFLICT (email) DO NOTHING;