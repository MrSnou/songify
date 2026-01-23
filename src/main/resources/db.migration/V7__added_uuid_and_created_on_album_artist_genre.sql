-- CREATE EXTENSION IF NOT EXISTS "uuid-oosp"; @Deprecated 14/11/2023

ALTER TABLE album
    ADD created_on TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    ADD uuid UUID DEFAULT gen_random_uuid() NOT NULL UNIQUE;

ALTER TABLE artist
    ADD created_on TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    ADD uuid UUID DEFAULT gen_random_uuid() NOT NULL UNIQUE;

ALTER TABLE genre
    ADD created_on TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    ADD uuid UUID DEFAULT gen_random_uuid() NOT NULL UNIQUE;

