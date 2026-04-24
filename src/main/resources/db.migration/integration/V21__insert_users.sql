Insert Into users (email, password, authorities, enabled)
VALUES
('Admin', '$2a$10$9G8uhJ8EHUJgenb16LVHtOS0hC/2E5hsScEY6L1ojFsXLfGZGKeqC', '{ROLE_USER, ROLE_ADMIN}', true),
('User', '$2a$10$OG.n8msfSg.nJm1oS5gZi.f9/P/6.RHk.bcYsB5u3Cf8Hgl4J81Ou', '{ROLE_USER}', true);