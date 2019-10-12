ALTER TABLE VOTE
  ALTER COLUMN create_date SET DEFAULT CURRENT_TIMESTAMP;

-- Users
INSERT INTO USER (user_id, password, username, active)
VALUES
  (1, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'user', 1);

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

-- Votes
INSERT INTO VOTE (vote_id, user_id, title, body, create_date)
VALUES (1, 1, '投票项1',
        '"投票项1"',
        {ts '2016-10-19 11:10:13.247'});

-- Items
INSERT INTO ITEM (vote_id, user_id, body, create_date,rules,agree_rule,agree_max,agree_min)
VALUES (1, 1,
        '"第一轮投票"',
        CURRENT_TIMESTAMP(),
        '1',
        '32',
        10,
        1);