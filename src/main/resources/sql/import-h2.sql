ALTER TABLE VOTE
  ALTER COLUMN create_date SET DEFAULT CURRENT_TIMESTAMP;

-- Users
INSERT INTO USER (user_id, password, username, active)
VALUES
  (1, '$2a$10$kYdU1bOG50gufWRhAjaSLu5/EnATu6gYzxZcDsiAcltuBBfRmTowe', 'user', 1);

-- Roles
INSERT INTO ROLE (role_id, role)
VALUES (1, 'ROLE_ADMIN');
INSERT INTO ROLE (role_id, role)
VALUES (2, 'ROLE_USER');

-- User Roles
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (1, 1);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (1, 2);
