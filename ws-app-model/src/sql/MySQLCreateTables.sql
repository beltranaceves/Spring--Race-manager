-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
DROP TABLE Race;
DROP TABLE Inscription;

CREATE TABLE Race (
    raceId BIGINT NOT NULL AUTO_INCREMENT,
    city VARCHAR(120) COLLATE latin1_bin NOT NULL,
    raceDescription VARCHAR(360) COLLATE latin1_bin NOT NULL,
    inscriptionPrice DOUBLE NOT NULL,
    maxParticipants INT NOT NULL,
    creationDate DATETIME NOT NULL,
    numberOfInscribed INT NOT NULL,
    CONSTRAINT RacePK PRIMARY KEY (raceId)
) ENGINE = InnoDB;

CREATE TABLE Inscription (
    inscriptionId BIGINT NOT NULL AUTO_INCREMENT,
    userEmail VARCHAR(255) COLLATE latin1_bin NOT NULL,
    raceId BIGINT NOT NULL,
    creditCardNumber VARCHAR(16) NOT NULL,
    dorsalNumber INT NOT NULL,
    inscriptionDate DATETIME NOT NULL,
    collected TINYINT NOT NULL,
    CONSTRAINT InscriptionPK PRIMARY KEY (inscriptionId),
    CONSTRAINT InscriptionRaceFK FOREIGN KEY (raceId) REFERENCES Inscription(raceId) ON DELETE CASCADE
) ENGINE = InnoDB;
