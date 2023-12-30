CREATE TABLE transactions
(
   id UUID NOT NULL,
   description VARCHAR (255),
   amount DECIMAL(19,4),
   type VARCHAR (255),
   owner VARCHAR (255),
   category VARCHAR (255),
   PRIMARY KEY (id)
);