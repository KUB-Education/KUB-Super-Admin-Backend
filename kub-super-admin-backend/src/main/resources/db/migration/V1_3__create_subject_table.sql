CREATE TABLE IF NOT EXISTS public.subject
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,
    type character varying(64) COLLATE pg_catalog."default" NOT NULL,

    CONSTRAINT subject_pkey PRIMARY KEY (id),
    CONSTRAINT name_check CHECK (name::text <> ''::text) NOT VALID, -- not empty
    CONSTRAINT type_check CHECK (type::text = ANY (ARRAY['LECTURE'::character varying,
     'LABORATORY'::character varying, 'SEMINAR'::character varying, 'EXAM'::character varying,
     'CREDIT'::character varying, 'RESUBMISSION'::character varying, 'COMMISSION'::character varying]::text[]))
    )

TABLESPACE pg_default;