-- Tabel voor rollen (student, docent, beheerder)
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabel voor gebruikers
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id INT NOT NULL REFERENCES roles(role_id) ON DELETE CASCADE,
    token VARCHAR(255)
);

-- Invoegen van rollen
INSERT INTO roles (role_name) VALUES ('student');
INSERT INTO roles (role_name) VALUES ('teacher');
INSERT INTO roles (role_name) VALUES ('admin');

-- Invoegen van een standaard acount voor elke rol
INSERT INTO users (email, password_hash, role_id) VALUES ('admin@gmail.com', 'password', 3);
INSERT INTO users (email, password_hash, role_id) VALUES ('docent@gmail.com', 'password', 2);
INSERT INTO users (email, password_hash, role_id) VALUES ('student@gmail.com', 'password', 1);

-- Tabel voor crebo's (kwalificatiedossiers)
CREATE TABLE crebos (
    crebo_number VARCHAR(50) PRIMARY KEY,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voorbeeld crebo
INSERT INTO     crebos (crebo_number, description)
                VALUES ('12345', 'Voorbeeld kwalificatiedossier');

-- Tabel voor studenten
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    student_number VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    tussenvoegsel VARCHAR(50),
    last_name VARCHAR(255) NOT NULL,
    klas VARCHAR(50),
    cohort VARCHAR(4), -- bijv. '2023'
    crebo_number VARCHAR(50) NOT NULL REFERENCES crebos(crebo_number),
    geboortedatum DATE,
    photo BYTEA,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voorbeeld student
INSERT INTO     students (user_id, student_number, first_name, last_name, crebo_number)
                VALUES (3, '123456', 'Jan', 'Jansen', '12345');

-- Tabel voor docenten
CREATE TABLE teachers (
    teacher_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    first_name VARCHAR(255) NOT NULL,
    tussenvoegsel VARCHAR(50),
    last_name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor kerntaken
CREATE TABLE kerntaken (
    kerntaak_id SERIAL PRIMARY KEY,
    crebo_number VARCHAR(50) NOT NULL REFERENCES crebos(crebo_number),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor werkprocessen
CREATE TABLE werkprocessen (
    werkproces_id SERIAL PRIMARY KEY,
    kerntaak_id INT NOT NULL REFERENCES kerntaken(kerntaak_id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor fasen
CREATE TABLE fases (
    fase_id SERIAL PRIMARY KEY,
    fase_name VARCHAR(50) NOT NULL UNIQUE, -- bijv. 'oriëntatiefase', 'developmentfase', 'expertfase'
    description TEXT
);

-- Tabel voor criteria
CREATE TABLE criteria (
    criteria_id SERIAL PRIMARY KEY,
    werkproces_id INT NOT NULL REFERENCES werkprocessen(werkproces_id),
    description TEXT NOT NULL,
    fase_id INT NOT NULL REFERENCES fases(fase_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor periodes
CREATE TABLE periods (
    period INT PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description TEXT
);

-- Insert voorbeeld periode
INSERT INTO     periods (period, start_date, end_date, description)
                VALUES (1, '2023-09-01', '2023-12-31', 'Eerste periode');

-- Tabel voor projecten
CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    period INT NOT NULL REFERENCES periods(period),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voorbeeld project
INSERT INTO     projects (student_id, name, description, period)
                VALUES (1, 'Project 1', 'Dit is een voorbeeld project', 1);

-- Tabel voor verantwoordingen
CREATE TABLE verantwoordingen (
    verantwoording_id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    criteria_id INT NOT NULL REFERENCES criteria(criteria_id),
    project_id INT NOT NULL REFERENCES projects(project_id) ON DELETE CASCADE,
    verantwoording_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, criteria_id, project_id)
);

-- Tabel voor beoordelingen
CREATE TABLE beoordelingen (
    beoordeling_id SERIAL PRIMARY KEY,
    po_id INT NOT NULL REFERENCES teachers(teacher_id) ON DELETE SET NULL,
    student_id INT NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    criteria_id INT NOT NULL REFERENCES criteria(criteria_id),
    verantwoording_id INT REFERENCES verantwoordingen(verantwoording_id),
    score INT CHECK (score BETWEEN 0 AND 6),
    feedback TEXT,
    beoordeling_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Tabel voor notities
CREATE TABLE notities (
    notitie_id SERIAL PRIMARY KEY,
    teacher_id INT NOT NULL REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    student_id INT NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor student-docent koppelingen
CREATE TABLE student_teacher_assignments (
     assignment_id SERIAL PRIMARY KEY,
     student_id INT NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
     teacher_id INT NOT NULL REFERENCES teachers(teacher_id) ON DELETE CASCADE,
     role VARCHAR(50) NOT NULL,
     period INT NOT NULL REFERENCES periods(period),
     start_date DATE,
     end_date DATE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     UNIQUE (student_id, teacher_id, role, period)
);

-- Optionele tabel voor rubrics (indien nodig)
CREATE TABLE rubrics (
    rubric_id SERIAL PRIMARY KEY,
    crebo_number VARCHAR(50) NOT NULL REFERENCES crebos(crebo_number),
    fase_id INT NOT NULL REFERENCES fases(fase_id),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (crebo_number, fase_id)
);