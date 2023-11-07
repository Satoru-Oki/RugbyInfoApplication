DROP TABLE IF EXISTS rugby_players;

CREATE TABLE rugby_players (
  id VARCHAR(30) NOT NULL,
  nationality VARCHAR(30) NOT NULL,
  name VARCHAR(50) NOT NULL,
  height int(5),
  weight int(5),
  rugby_position VARCHAR(5),
  PRIMARY KEY(id)
);
