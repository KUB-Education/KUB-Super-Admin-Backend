CREATE TABLE IF NOT EXISTS public.room
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    location character varying(256) COLLATE pg_catalog."default" NOT NULL,
    capacity smallint NOT NULL,

    CONSTRAINT room_pkey PRIMARY KEY (id),
    CONSTRAINT location_check CHECK (location::text <> ''::text) NOT VALID, -- not empty
    CONSTRAINT capacity_check CHECK (capacity >= 0)
    )

TABLESPACE pg_default;