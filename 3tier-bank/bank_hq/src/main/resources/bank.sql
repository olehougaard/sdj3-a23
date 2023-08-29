DROP SCHEMA IF EXISTS bank CASCADE;
CREATE SCHEMA bank;
SET SCHEMA 'bank';

CREATE DOMAIN Currency CHAR(3) CHECK (VALUE = UPPER(VALUE));
CREATE DOMAIN MoneyAmount DECIMAL(12, 2);
CREATE DOMAIN RegNumber DECIMAL(4) CHECK (VALUE > 0);
CREATE DOMAIN AccountNumber DECIMAL(10) CHECK (VALUE > 0);

CREATE TABLE Exchange_Rates(
  from_currency Currency,
  to_currency Currency,
  rate DECIMAL(9,4) NOT NULL CHECK(rate > 0),
  PRIMARY KEY(from_currency, to_currency));
  
CREATE TABLE Branch(
  reg_number RegNumber,
  address VARCHAR(100),
  PRIMARY KEY(reg_number));
  
CREATE TABLE Customer(
  cpr CHAR(10),
  name VARCHAR(100) NOT NULL,
  address VARCHAR(100) NOT NULL,
  PRIMARY KEY(cpr));

CREATE SEQUENCE acc_number_seq INCREMENT 1 START WITH 1;

CREATE TABLE Account(
  account_number AccountNumber DEFAULT (nextval('acc_number_seq')),
  reg_number RegNumber,
  customer CHAR(10) NOT NULL,
  balance MoneyAmount NOT NULL DEFAULT 0,
  currency Currency NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (reg_number, account_number),
  FOREIGN KEY (reg_number) REFERENCES Branch(reg_number),
  FOREIGN KEY (customer) REFERENCES Customer(cpr));
  
CREATE TABLE Transaction(
  transaction_id SERIAL,
  amount MoneyAmount NOT NULL,
  currency Currency NOT NULL,
  transaction_type VARCHAR(10) NOT NULL CHECK(transaction_type IN ('Deposit', 'Withdrawal', 'Transfer')),
  transaction_text VARCHAR(200) NOT NULL,
  primary_reg_number RegNumber NOT NULL,
  primary_account_number AccountNumber NOT NULL,
  secondary_reg_number RegNumber,
  secondary_account_number AccountNumber,
  PRIMARY KEY(transaction_id),
  FOREIGN KEY (primary_reg_number, primary_account_number) REFERENCES Account(reg_number, account_number),
  FOREIGN KEY (secondary_reg_number, secondary_account_number) REFERENCES Account(reg_number, account_number),
  CHECK((secondary_reg_number IS NULL) = (secondary_account_number IS NULL)));

INSERT INTO Exchange_Rates(from_currency, to_currency, rate) VALUES
  ('USD', 'EUR', 0.9415),
  ('USD', 'DKK', 7.0023),
  ('DKK', 'EUR', 0.1428),
  ('DKK', 'USD', 0.1345),
  ('EUR', 'USD', 1.0619),
  ('EUR', 'DKK', 7.4370);
  
INSERT INTO Customer VALUES
  ('1234567890', 'Me', 'My address'),
  ('1122334455', 'Other guy', 'Other address');

INSERT INTO Branch VALUES
  (4711, 'Banegaardsgade 2'),
  (1122, '461 Ocean Boulevard');
