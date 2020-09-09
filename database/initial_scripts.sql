create table users
(
    id                int unsigned auto_increment primary key,
    email             varchar(100) not null,
    login             varchar(30)  not null,
    password          varchar(255) not null,
    firstname         varchar(100) not null,
    lastname          varchar(100) not null,
    registration_date timestamp    not null,
    modification_date timestamp
);

create table recipes
(
    id                int unsigned auto_increment primary key,
    title             varchar(100)   not null,
    difficulty        int            not null,
    minutes           int            not null,
    instruction       varchar(10000) not null,
    author_id         int unsigned   not null,
    private_only      boolean        not null,
    creation_date     timestamp      not null,
    modification_date timestamp,
    CONSTRAINT `recipe_author_fk` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
);

create table ingredients
(
    id                int unsigned auto_increment primary key,
    name              varchar(100) not null,
    description       varchar(1000),
    author_id         int unsigned not null,
    creation_date     timestamp    not null,
    modification_date timestamp,
    CONSTRAINT `ingredient_author_fk` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
);

create table recipe_ingredients
(
    id            int unsigned auto_increment primary key,
    recipe_id     int unsigned not null,
    ingredient_id int unsigned not null,
    CONSTRAINT `r2i_recipe_fk` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`),
    CONSTRAINT `r2i_ingredient_fk` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`)
);