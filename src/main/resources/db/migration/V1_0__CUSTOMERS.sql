CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS CUSTOMERS (
    "ID"                UUID DEFAULT uuid_generate_v4(),
    "FIRST_NAME"        VARCHAR(256),
    "LAST_NAME"         VARCHAR(256),
    "USERNAME"          VARCHAR(256),
    "CPF"               VARCHAR(11)
    );

--KEYS
ALTER TABLE CUSTOMERS ADD CONSTRAINT PK_CUSTOMERS PRIMARY KEY ("ID");