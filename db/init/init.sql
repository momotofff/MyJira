-- Создание типа 'UserType'
CREATE TYPE UserType AS ENUM ('Admin', 'User');

-- Создание таблицы 'users'
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('admin', 'editor', 'viewer')),
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Создание таблицы 'tasks'
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) CHECK (status IN ('pending', 'in_progress', 'completed')),
    priority VARCHAR(20) CHECK (priority IN ('high', 'medium', 'low')),
    author VARCHAR(50),
    assignee VARCHAR(50)
);

