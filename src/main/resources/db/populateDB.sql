DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);
INSERT INTO meals
(user_id, datetime, description, calories) VALUES
  (100000, TIMESTAMP '2016-05-30 10:00:38', 'Завтрак', 610),
  (100000, TIMESTAMP '2015-05-30 15:36:38', 'Обед', 510),
  (100000, TIMESTAMP '2015-05-30 20:36:38', 'Ужин', 1000),
  (100000, TIMESTAMP '2015-05-31 10:36:38', 'Завтрак', 510),
  (100000, TIMESTAMP '2015-05-31 15:36:38', 'salo', 1000),
  (100001, TIMESTAMP '2015-06-1 15:36:38', 'Админ ланч', 510),
  (100001, TIMESTAMP '2015-06-1 20:10:08', 'Админ ужин', 1510);
