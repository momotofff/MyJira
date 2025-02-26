-- Create enumerable types
CREATE TYPE UserRole AS ENUM ('Admin', 'User', 'Viewer');
CREATE TYPE TaskStatus AS ENUM ('Pending', 'Active', 'Resolved', 'Closed');
CREATE TYPE TaskPriority AS ENUM ('Low', 'Medium', 'High', 'Critical');

-- Create table 'users'
CREATE TABLE Users (
    id          BIGSERIAL       PRIMARY KEY,
    userName    TEXT            NOT NULL UNIQUE,
    email       TEXT            NOT NULL UNIQUE,
    role        UserRole        NOT NULL DEFAULT 'User'
);

-- Create table 'Tasks'
CREATE TABLE Tasks (
    id          BIGSERIAL       PRIMARY KEY,
    title       TEXT            NOT NULL,
    description TEXT,
    status      TaskStatus      NOT NULL DEFAULT 'Pending',
    priority    TaskPriority    NOT NULL,
    author      BIGINT          NOT NULL REFERENCES Users(id),
    assignee    BIGINT          REFERENCES Users(id)
);

