CREATE TABLE app_user (
        id INTEGER PRIMARY KEY,
        username VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        token VARCHAR(255) NOT NULL
);