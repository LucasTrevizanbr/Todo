create table task (
   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   completed BOOLEAN NOT NULL,
   description VARCHAR(300),
   goal_id BIGINT,
   CONSTRAINT owner_of_task FOREIGN KEY (goal_id) REFERENCES goal(id)
) engine=InnoDB