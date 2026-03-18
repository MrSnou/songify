ALTER TABLE song
    ADD uuid UUID DEFAULT gen_random_uuid() NOT NULL UNIQUE;