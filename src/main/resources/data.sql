INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    1,
    'koko@gmail.com',
    '$2a$10$u.QbXJh664hy2UCJqbNoQ.BuIz7Oj1fkY5ivQYlX9mUsaw5H2VVxm',
    1,
    'koko',
    0,
    'http://localhost:9008/eai/api/user-management/account/user/image/koko@gmail.com.png'
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    2,
    'zale@gmail.com',
    NULL,
    NULL,
    'zale jc22',
    0,
    NULL
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    7,
    'pride1@gmail.com',
    NULL,
    1,
    'pride steven',
    0,
    NULL
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    15,
    'aly33@gmail.com',
    '$2a$10$Iw4xBtFw9lcsRRVuNFTT8.yCMu7pjJu2/QlwC7PTprBcMtyRWkFcy',
    1,
    'aly pride',
    0,
    NULL
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    24,
    'bobo@gmail.com',
    NULL,
    NULL,
    'bobo dere',
    0,
    NULL
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    48,
    'modo.mome@gmail.com',
    '$2a$10$bd8kTgJObFVd.haL7r2Uk.PWQfSV9cjytl/US9dqRcksNv2p8xvUi',
    0,
    'modo mome',
    0,
    NULL
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    50,
    'jc@gmail.com',
    '$2a$10$DhtzxM2yeSVkmBNVrdE.QOLd9v4PPlTU5PBrtBBLit.Q9VMNp8FdK',
    0,
    'jc claude',
    0,
    'http://localhost:9008/eai/api/user-management/account/user/image/jc@gmail.com.png'
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    56,
    'remy.roy@gmail.com',
    '$2a$10$Nf6VXLtGKxJqLPg/OuA6Y.EJltMZn7rgwsxQCZeOp1esebPbMti02',
    0,
    'remy roy',
    0,
    NULL
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    58,
    'bebe@gmail.com',
    '$2a$10$9wnQnXbKGxi2fnCcJ6ZVgehf/7C2H4JQ3gz.sB15Iexo1Cy/50pGW',
    0,
    'bebe dool',
    0,
    ''
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    107,
    'you@gmail.com',
    '$2a$10$E3ww9OKhQpqSgaIarDLarO3V9oTewBYQxXX.FOq0IpYXyJ4UdAuT.',
    0,
    'you er',
    0,
    NULL
  );

INSERT INTO
  app_user (id_user, email, pwd, status, name, is_mfa, image)
VALUES
  (
    110,
    'remy.roy22@gmail.com',
    '$2a$10$3FuI85X9WDrpTUIRHqzBAuTeBRY5uv.6qdfxG1zIGJFACDZpg.PJ.',
    0,
    'remy roy',
    0,
    NULL
  );

INSERT INTO
  app_role (id_role, rl_nm)
VALUES
  (1, 'USER');

INSERT INTO
  app_role (id_role, rl_nm)
VALUES
  (2, 'ADMIN');

INSERT INTO
  app_role (id_role, rl_nm)
VALUES
  (3, 'MANAGER');

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (7188861552274916044, 1, 1);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (3398862063759541914, 1, 2);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (5192795872159942636, 1, 7);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (1410603347568997020, 1, 15);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (6484363304053588176, 2, 1);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (8619605321299085671, 2, 24);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (4375328045644167825, 2, 50);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (5832590336101168978, 2, 58);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (7145059946192128490, 3, 1);

INSERT INTO
  app_user_role (id_user_role, ID_ROLE, ID_USER)
VALUES
  (7145059946192128493, 3, 56);

INSERT INTO event
(id, `type`, description)
VALUES(6, 'LOGIN_ATTEMPT', 'You tried to login');
INSERT INTO event
(id, `type`, description)
VALUES(7, 'LOGIN_ATTEMPT_FAILED', 'Login attempted failed');
INSERT INTO event
(id, `type`, description)
VALUES(9, 'LOGIN_ATTEMPT_SUCCESS', 'You login successfully');
INSERT INTO event
(id, `type`, description)
VALUES(11, 'PROFILE_UPDATE', 'Profile updated');
INSERT INTO event
(id, `type`, description)
VALUES(12, 'PROFILE_IMAGE_UPDATED', 'Profile image updated');
INSERT INTO event
(id, `type`, description)
VALUES(13, 'CODE_VERIFY_SUCCESS', 'Code verify success');