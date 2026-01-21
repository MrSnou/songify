-- CREATE EXTENSION IF NOT EXISTS "uuid-oosp"; @Depricated

ALTER TABLE album
    ADD created_on TIMESTAMP(6) WITH TIME ZONE DEFAULT now();

ALTER TABLE album
    ADD uuid UUID;
--         DEFAULT uuid_generate_v4() NOT NULL UNIQUE; @Depricated

ALTER TABLE artist
    ADD created_on TIMESTAMP(6) WITH TIME ZONE DEFAULT now();

ALTER TABLE artist
    ADD uuid UUID;
--         DEFAULT uuid_generate_v4() NOT NULL UNIQUE; @Depricated

ALTER TABLE genre
    ADD created_on TIMESTAMP(6) WITH TIME ZONE DEFAULT now();

ALTER TABLE genre
    ADD uuid UUID;
--         DEFAULT uuid_generate_v4() NOT NULL UNIQUE; @Depricated