### 1. Create database structure

1. Firstly, run a docker image for MySQL database.
```shell
docker run --name mysql105 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=12345678 -d mysql:latest
docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=12345678 -e POSTGRES_DB=customer_info postgres:latest```

2. Create the following database structure

```mysql
CREATE DATABASE customer_info;
USE customer_info;

create table customer (
    id varchar(255) not null, 
    first_name varchar(255), 
    last_name varchar(255), 
    username varchar(100), 
    cpf varchar(14), 
    primary key (id)
);

INSERT INTO `customer_info`.`customer` (`id`, `first_name`, `last_name`, `username`, `cpf`) VALUES (1, 'Francisco', 'Medeiros', 'fthiagomedeiros', '000.000.000-11');
INSERT INTO `customer_info`.`customer` (`id`, `first_name`, `last_name`, `username`, `cpf`) VALUES (2, 'Assis', 'Medeiros', 'assismedeiros', '000.000.000-22');
```