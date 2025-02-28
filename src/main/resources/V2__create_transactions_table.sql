CREATE TABLE IF NOT EXISTS bletzzieco_transactions (
    uuid CHAR(36) NOT NULL PRIMARY KEY,
    payer CHAR(36) NOT NULL,
    payee CHAR(36) NOT NULL,
    amount DOUBLE NOT NULL,
    status VARCHAR(255) NOT NULL
);