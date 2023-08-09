CREATE TABLE c##testuser.table_1 (
    id          INTEGER       PRIMARY KEY,
    description VARCHAR2(255) NOT NULL
);

CREATE SEQUENCE c##testuser.table_1_seq;

CREATE TABLE c##testuser.table_2(
     id          INTEGER       PRIMARY KEY,
     description VARCHAR2(255) NOT NULL,
     FOREIGN KEY (id)
         REFERENCES c##testuser.table_1 (id)
);

CREATE SEQUENCE c##testuser.table_2_seq;
