INSERT INTO users (
    nom,
    email,
    password,
    role,
    actif,
    created_at,
    updated_at
)
VALUES (
    'Admin User',
    'admin@university.ma',
    '$2a$12$Huabfcu1AdMoId8IrVrNQuMa65dp3vCEaVOGf.lW01CU9DMpLEctG', -- bcrypt(Admin@1234)
    'ADMIN',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);