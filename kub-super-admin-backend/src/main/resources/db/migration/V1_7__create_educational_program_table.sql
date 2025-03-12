CREATE TABLE IF NOT EXISTS public.educational_program
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    specialty_id bigserial NOT NULL,
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,
    degree_type character varying(64) COLLATE pg_catalog."default" NOT NULL,
    study_form character varying(64) COLLATE pg_catalog."default" NOT NULL,

    CONSTRAINT educational_program_pkey PRIMARY KEY (id),
    CONSTRAINT name_check CHECK (name::text <> ''::text) NOT VALID, -- not empty
    CONSTRAINT specialty_id_fkey FOREIGN KEY (specialty_id) -- foreign key
    REFERENCES public.specialty (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT degree_type_check CHECK (degree_type::text = ANY (ARRAY['BACHELOR'::character varying,
                                        'MASTER'::character varying, 'POSTGRADUATE'::character varying]::text[])),
    CONSTRAINT study_form_check CHECK (study_form::text = ANY (ARRAY['FULL_TIME'::character varying,
                                       'PART_TIME'::character varying]::text[]))
    )

TABLESPACE pg_default;