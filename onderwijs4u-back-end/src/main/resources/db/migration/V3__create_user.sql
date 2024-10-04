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
                VALUES ('12345', 'Voorbeeld kwalificatiedossier'),
                       ('CREBO12345', 'Voorbeeld kwalificatiedossier voor kerntaken');

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

-- Insert voor kerntaken
INSERT INTO kerntaken (kerntaakid, crebonumber, name, description)
VALUES
    (1, 'CREBO12345', 'B1-K1', 'Realiseert software'),
    (2, 'CREBO12345', 'B1-K2', 'Voert ICT-projecten uit');


-- Tabel voor werkprocessen
CREATE TABLE werkprocessen (
    werkprocesid SERIAL PRIMARY KEY,
    kerntaakid INT NOT NULL REFERENCES kerntaken(kerntaakid),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voor werkprocessen
INSERT INTO werkprocessen (werkprocesid, kerntaakid, name, description)
VALUES
    (1, 1, 'B1-K1-W1', 'Stemt opdracht af, plant werkzaamheden en bewaakt de voortgang'),
    (2, 1, 'B1-K1-W2', 'Maakt een technisch ontwerp voor software'),
    (3, 1, 'B1-K1-W3', 'Realiseert (onderdelen van) software'),
    (4, 1, 'B1-K1-W4', 'Test software en doet verbetervoorstellen'),
    (5, 2, 'B1-K2-W1', 'Werkt samen in een projectteam'),
    (6, 2, 'B1-K2-W2', 'Presenteert het opgeleverde werk'),
    (7, 2, 'B1-K2-W3', 'Evalueert de samenwerking');

-- -- Insert voorbeeld werkproces
-- INSERT INTO     werkprocessen (kerntaakid, name, description)
--                 VALUES (1, 'Werkt samen in project team', 'De student werkt samen in een project team');

-- Tabel voor fasen
CREATE TABLE fases (
    faseid SERIAL PRIMARY KEY,
    fasename VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- Insert fases
INSERT INTO fases (fasename, description) VALUES ('Oriëntatiefase', 'In deze fase oriënteert de student zich op het project');
INSERT INTO fases (fasename, description) VALUES ('Developmentfase', 'In deze fase werkt de student aan het project');
INSERT INTO fases (fasename, description) VALUES ('Expertfase', 'In deze fase is de student een expert op het gebied van het project');

-- Tabel voor criteria
CREATE TABLE criteria (
    criteriaid SERIAL PRIMARY KEY,
    werkprocesid INT NOT NULL REFERENCES werkprocessen(werkprocesid),
    description TEXT NOT NULL,
    faseid INT NOT NULL REFERENCES fases(faseid),
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert voor criteria
INSERT INTO criteria (criteriaid, werkprocesid, description, faseid)
VALUES
    (1, 1, 'Heeft de werkzaamheden effectief afgestemd met de opdrachtgever', 1),
    (2, 2, 'Heeft een gedetailleerd en technisch correct ontwerp gemaakt', 2),
    (3, 3, 'Heeft de software correct geïmplementeerd volgens het ontwerp', 2),
    (4, 4, 'Heeft de software effectief getest en verbetervoorstellen gedaan', 3),
    (5, 5, 'Heeft constructief samengewerkt binnen het projectteam', 1),
    (6, 6, 'Heeft het opgeleverde werk effectief gepresenteerd aan de opdrachtgever', 2),
    (7, 7, 'Heeft de samenwerking geëvalueerd en feedback gegeven', 3);

-- Tabel voor periodes
CREATE TABLE periods (
    period INT PRIMARY KEY,
    startdate DATE NOT NULL,
    enddate DATE NOT NULL,
    description TEXT
);

-- Insert voorbeeld periode
INSERT INTO     periods (period, startdate, enddate, description)
                VALUES
                (1, '2023-09-01', '2023-12-31', 'Eerste periode'),
                (2, '2024-01-01', '2024-04-30', 'Tweede periode'),
                (3, '2024-05-01', '2024-08-31', 'Derde periode'),
                (4, '2024-09-01', '2024-12-31', 'Vierde periode'),
                (5, '2025-01-01', '2025-04-30', 'Vijfde periode'),
                (6, '2025-05-01', '2025-08-31', 'Zesde periode');


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
                VALUES (1, 'Project: havik-oog', 'In dit project programmeer je een arduino om een verkeerslicht te simuleren.', 1),
                (1, 'Project: spinnen-oog', 'Dit project maak je een html/css website', 2),
                (1, 'Project: berg', 'Dit project maak je een micro front-end.', 3);

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