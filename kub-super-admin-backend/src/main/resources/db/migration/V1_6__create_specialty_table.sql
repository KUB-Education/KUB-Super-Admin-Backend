CREATE TABLE IF NOT EXISTS public.specialty
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    study_field_id bigserial NOT NULL,
    code character varying(32) COLLATE pg_catalog."default" NOT NULL,
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,

    CONSTRAINT specialty_pkey PRIMARY KEY (id),
    CONSTRAINT specialty_code_unique UNIQUE (code),
    CONSTRAINT code_check CHECK (code::text <> ''::text) NOT VALID, -- not empty
    CONSTRAINT name_check CHECK (name::text <> ''::text) NOT VALID, -- not empty
    CONSTRAINT study_field_id_fkey FOREIGN KEY (study_field_id) -- foreign key
        REFERENCES public.study_field (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;