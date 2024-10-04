-- Tabel voor rollen (student, docent, beheerder)
CREATE TABLE roles (
    roleid SERIAL PRIMARY KEY,
    rolename VARCHAR(50) NOT NULL UNIQUE
);

-- Tabel voor gebruikers
CREATE TABLE users (
    userid SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    passwordhash VARCHAR(255) NOT NULL,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    roleid INT NOT NULL REFERENCES roles(roleid) ON DELETE CASCADE,
    token VARCHAR(255)
);

-- Invoegen van rollen
INSERT INTO roles (rolename) VALUES ('student');
INSERT INTO roles (rolename) VALUES ('teacher');
INSERT INTO roles (rolename) VALUES ('admin');

-- Invoegen van een standaard acount voor elke rol
INSERT INTO users (email, passwordhash, roleid) VALUES ('admin@gmail.com', 'password', 3);
INSERT INTO users (email, passwordhash, roleid) VALUES ('docent@gmail.com', 'password', 2);
INSERT INTO users (email, passwordhash, roleid) VALUES ('student@gmail.com', 'password', 1);

-- Tabel voor crebo's (kwalificatiedossiers)
CREATE TABLE crebos (
    crebonumber VARCHAR(50) PRIMARY KEY,
    description TEXT,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voorbeeld crebo
INSERT INTO     crebos (crebonumber, description)
                VALUES ('12345', 'Voorbeeld kwalificatiedossier');

-- Tabel voor studenten
CREATE TABLE students (
    studentid SERIAL PRIMARY KEY,
    userid INT NOT NULL UNIQUE REFERENCES users(userid) ON DELETE CASCADE,
    studentnumber VARCHAR(50) UNIQUE NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    tussenvoegsel VARCHAR(50),
    lastname VARCHAR(255) NOT NULL,
    klas VARCHAR(50),
    cohort VARCHAR(4), -- bijv. '2023'
    crebonumber VARCHAR(50) NOT NULL REFERENCES crebos(crebonumber),
    geboortedatum DATE,
    photo BYTEA,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voorbeeld student
INSERT INTO     students (userid, studentnumber, firstname, lastname, crebonumber)
                VALUES (3, '123456', 'Jan', 'Jansen', '12345');

-- Tabel voor docenten
CREATE TABLE teachers (
    teacherid SERIAL PRIMARY KEY,
    userid INT NOT NULL UNIQUE REFERENCES users(userid) ON DELETE CASCADE,
    firstname VARCHAR(255) NOT NULL,
    tussenvoegsel VARCHAR(50),
    lastname VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor kerntaken
CREATE TABLE kerntaken (
    kerntaakid SERIAL PRIMARY KEY,
    crebonumber VARCHAR(50) NOT NULL REFERENCES crebos(crebonumber),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor werkprocessen
CREATE TABLE werkprocessen (
    werkprocesid SERIAL PRIMARY KEY,
    kerntaakid INT NOT NULL REFERENCES kerntaken(kerntaakid),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor fasen
CREATE TABLE fases (
    faseid SERIAL PRIMARY KEY,
    fasename VARCHAR(50) NOT NULL UNIQUE, -- bijv. 'oriëntatiefase', 'developmentfase', 'expertfase'
    description TEXT
);

-- Tabel voor criteria
CREATE TABLE criteria (
    criteriaid SERIAL PRIMARY KEY,
    werkprocesid INT NOT NULL REFERENCES werkprocessen(werkprocesid),
    description TEXT NOT NULL,
    faseid INT NOT NULL REFERENCES fases(faseid),
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor periodes
CREATE TABLE periods (
    period INT PRIMARY KEY,
    startdate DATE NOT NULL,
    enddate DATE NOT NULL,
    description TEXT
);

-- Insert voorbeeld periode
INSERT INTO     periods (period, startdate, enddate, description)
                VALUES (1, '2023-09-01', '2023-12-31', 'Eerste periode');

-- Tabel voor projecten
CREATE TABLE projects (
    projectid SERIAL PRIMARY KEY,
    studentid INT NOT NULL REFERENCES students(studentid) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    period INT NOT NULL REFERENCES periods(period),
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voorbeeld project
INSERT INTO     projects (studentid, name, description, period)
                VALUES (1, 'Project 1', 'Dit is een voorbeeld project', 1);

-- Tabel voor verantwoordingen
CREATE TABLE verantwoordingen (
    verantwoordingid SERIAL PRIMARY KEY,
    studentid INT NOT NULL REFERENCES students(studentid) ON DELETE CASCADE,
    criteriaid INT NOT NULL REFERENCES criteria(criteriaid),
    projectid INT NOT NULL REFERENCES projects(projectid) ON DELETE CASCADE,
    verantwoordingtext TEXT NOT NULL,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (studentid, criteriaid, projectid)
);

-- Tabel voor beoordelingen
CREATE TABLE beoordelingen (
    beoordelingid SERIAL PRIMARY KEY,
    poid INT NOT NULL REFERENCES teachers(teacherid) ON DELETE SET NULL,
    studentid INT NOT NULL REFERENCES students(studentid) ON DELETE CASCADE,
    criteriaid INT NOT NULL REFERENCES criteria(criteriaid),
    verantwoordingid INT REFERENCES verantwoordingen(verantwoordingid),
    score INT CHECK (score BETWEEN 0 AND 6),
    feedback TEXT,
    beoordelingdate DATE NOT NULL DEFAULT CURRENT_DATE,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Tabel voor notities
CREATE TABLE notities (
    notitieid SERIAL PRIMARY KEY,
    teacherid INT NOT NULL REFERENCES teachers(teacherid) ON DELETE CASCADE,
    studentid INT NOT NULL REFERENCES students(studentid) ON DELETE CASCADE,
    text TEXT NOT NULL,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel voor student-docent koppelingen
CREATE TABLE studentteacherassignments (
     assignmentid SERIAL PRIMARY KEY,
     studentid INT NOT NULL REFERENCES students(studentid) ON DELETE CASCADE,
     teacherid INT NOT NULL REFERENCES teachers(teacherid) ON DELETE CASCADE,
     role VARCHAR(50) NOT NULL,
     period INT NOT NULL REFERENCES periods(period),
     startdate DATE,
     enddate DATE,
     createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     UNIQUE (studentid, teacherid, role, period)
);

-- Optionele tabel voor rubrics (indien nodig)
CREATE TABLE rubrics (
    rubricid SERIAL PRIMARY KEY,
    crebonumber VARCHAR(50) NOT NULL REFERENCES crebos(crebonumber),
    faseid INT NOT NULL REFERENCES fases(faseid),
    description TEXT,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (crebonumber, faseid)
);