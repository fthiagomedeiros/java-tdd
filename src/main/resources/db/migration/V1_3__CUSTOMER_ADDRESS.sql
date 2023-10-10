CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS "customer_info".CUSTOMER_ADDRESS (
    ID                UUID DEFAULT uuid_generate_v4(),
    STREET            VARCHAR(256),
    CUSTOMER_UUID     UUID REFERENCES CUSTOMERS
    );

--KEYS
ALTER TABLE "customer_info".CUSTOMER_ADDRESS ADD CONSTRAINT PK_CUSTOMERS_ADDRESS PRIMARY KEY (ID);