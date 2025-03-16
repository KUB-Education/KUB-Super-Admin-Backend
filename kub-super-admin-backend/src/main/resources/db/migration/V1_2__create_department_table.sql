CREATE TABLE IF NOT EXISTS public.department
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,

    CONSTRAINT department_pkey PRIMARY KEY (id),
    CONSTRAINT name_check CHECK (name::text <> ''::text) NOT VALID
    )

TABLESPACE pg_default;