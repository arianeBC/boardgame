CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

CREATE TABLE boardgames (
                            id       LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
                            name    VARCHAR(128) NOT NULL,
                            level   INT NOT NULL,
                            minPlayers INT NOT NULL,
                            maxPlayers VARCHAR(50) NOT NULL,
                            gameType VARCHAR(50) NOT NULL
);

CREATE TABLE reviews (
                         id       LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         gameId   LONG NOT NULL,
                         text     VARCHAR(1024) NOT NULL UNIQUE
);


ALTER TABLE reviews
    ADD CONSTRAINT game_review_fk FOREIGN KEY (gameId)
        REFERENCES boardgames (id);

INSERT INTO boardgames (name, level, minPlayers, maxPlayers, gameType)
VALUES ('Splendor', 3, 2, '4', 'Strategy Game');

INSERT INTO boardgames (name, level, minPlayers, maxPlayers, gameType)
VALUES ('Clue', 2, 1, '6', 'Strategy Game');

INSERT INTO boardgames (name, level, minPlayers, maxPlayers, gameType)
VALUES ('Linkee', 1, 2, '+', 'Trivia Game');

INSERT INTO reviews (gameId, text)
VALUES (1, 'A great strategy game. The one who collects 15 points first wins. Calculation skill is required.');

INSERT INTO reviews (gameId, text)
VALUES (1, 'Collecting gemstones makes me feel like a wealthy merchant. Highly recommend!');

INSERT INTO reviews (gameId, text)
VALUES (2, 'A detective game to guess the criminal, weapon, and place of the crime scene. It is more fun with more than 3 players.');
