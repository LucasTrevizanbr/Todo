create table goal (

  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  creation_date DATETIME(6),
  expected_finalization_date DATETIME(6),
  real_finalization_date DATETIME(6),
  retaken_date DATETIME(6),
  stop_date DATETIME(6),
  difficulty VARCHAR(30),
  objective VARCHAR(200),
  points INTEGER,
  status VARCHAR(30),
  user_id bigint,
  CONSTRAINT owner_of_goal FOREIGN KEY (user_id) REFERENCES user(id)

) engine=InnoDB