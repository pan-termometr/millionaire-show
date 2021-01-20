CREATE TABLE `question`
(
    `id`       bigint NOT NULL AUTO_INCREMENT,
    `a`        varchar(255) DEFAULT NULL,
    `b`        varchar(255) DEFAULT NULL,
    `c`        varchar(255) DEFAULT NULL,
    `d`        varchar(255) DEFAULT NULL,
    `answer`   char(1)      DEFAULT NULL,
    `level`    int    NOT NULL,
    `question` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
