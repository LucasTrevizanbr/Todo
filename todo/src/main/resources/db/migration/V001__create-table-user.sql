create table user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    conclusion_points_goal INT,
    email varchar(130),
    name varchar(200),
    nick_name varchar(30),
    password varchar(200),
    photo_link varchar(300),
    registration_date DATETIME(6)
) engine=InnoDB