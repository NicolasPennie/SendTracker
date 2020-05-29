CREATE TABLE send(
  id          SERIAL PRIMARY KEY,
  name        varchar(100) NOT NULL,
  style       varchar(100),
  grade       varchar(100),
  tick_type   varchar(100),
  location    varchar(100) NOT NULL
)
