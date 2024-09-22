CREATE TYPE UserType AS ENUM ('Admin', 'User');

CREATE TABLE Users (
  id        BIGSERIAL   PRIMARY KEY,
  userName  TEXT        NOT NULL,
  firstName TEXT        NOT NULL,
  lastName  TEXT        NOT NULL,
  email     TEXT        NOT NULL,
  password  TEXT        NOT NULL,
  phone     TEXT,
  userType  UserType    NOT NULL DEFAULT 'User'
);

