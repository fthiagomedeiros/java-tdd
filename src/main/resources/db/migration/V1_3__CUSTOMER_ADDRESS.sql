CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS "customer_info".CUSTOMER_ADDRESS (
    ID                UUID DEFAULT uuid_generate_v4() REFERENCES CUSTOMERS,
    STREET            VARCHAR(256)
    );