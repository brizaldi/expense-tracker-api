CREATE TABLE et_users(
    user_id INTEGER PRIMARY KEY NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    email VARCHAR(30) NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE et_categories(
    category_id INTEGER PRIMARY KEY NOT NULL,
    user_id INTEGER NOT NULL,
    title VARCHAR(20) NOT NULL,
    description VARCHAR(50) NOT NULL
);

ALTER TABLE et_categories ADD CONSTRAINT cat_users_fk
FOREIGN KEY (user_id) REFERENCES et_users(user_id);

CREATE TABLE et_transactions(
    transaction_id INTEGER PRIMARY KEY NOT NULL,
    category_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    note VARCHAR(50) NOT NULL,
    transaction_date BIGINT NOT NULL
);

ALTER TABLE et_transactions ADD CONSTRAINT trans_cat_fk
FOREIGN KEY (category_id) REFERENCES et_categories(category_id);
ALTER TABLE et_transactions ADD CONSTRAINT trans_user_fk
FOREIGN KEY (user_id) REFERENCES et_users(user_id);

CREATE SEQUENCE et_users_seq INCREMENT 1 START 1;
CREATE SEQUENCE et_categories_seq INCREMENT 1 START 1;
CREATE SEQUENCE et_transactions_seq INCREMENT 1 START 1000;