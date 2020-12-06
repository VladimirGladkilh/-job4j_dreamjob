CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT
);

Alter TABLE candidate add column photoId int;


CREATE TABLE photo (
   id SERIAL PRIMARY KEY,
   path TEXT
);