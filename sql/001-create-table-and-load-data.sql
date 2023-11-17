DROP TABLE IF EXISTS rugby_players_world_cup;

CREATE TABLE rugby_players_world_cup (
  id VARCHAR(30) NOT NULL,
  nationality VARCHAR(30) NOT NULL,
  name VARCHAR(50) NOT NULL,
  height INT(5),
  weight INT(5),
  rugby_position VARCHAR(50),
  PRIMARY KEY(id)
);
