CREATE TABLE IF NOT EXISTS public.study_field
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    code character varying(32) COLLATE pg_catalog."default" NOT NULL,
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,

    CONSTRAINT study_field_pkey PRIMARY KEY (id),
    CONSTRAINT study_field_code_unique UNIQUE (code),
    CONSTRAINT code_check CHECK (code::text <> ''::text) NOT VALID, -- not empty
    CONSTRAINT name_check CHECK (name::text <> ''::text) NOT VALID -- not empty
    )

TABLESPACE pg_default;