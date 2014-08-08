CREATE TABLE `repos`(
    `name` VARCHAR(255) NOT NULL,
    `owner` VARCHAR(255) NOT NULL,
    `language` VARCHAR(255) NOT NULL,
    `homepage` VARCHAR(255),
    `amount` INT NOT NULL,
    `description` TEXT,
    PRIMARY KEY (`name`, `owner`)
);

CREATE TABLE `repo_actors`(
    `name` VARCHAR(255) NOT NULL,
    `owner` VARCHAR(255) NOT NULL,
    `actor` VARCHAR(255) NOT NULL,
    `amount` INT NOT NULL,
    PRIMARY KEY (`name`, `owner`, `actor`)
);
