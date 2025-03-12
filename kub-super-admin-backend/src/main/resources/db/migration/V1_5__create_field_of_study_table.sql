CREATE TABLE IF NOT EXISTS public.field_of_study
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    code character varying(32) COLLATE pg_catalog."default" NOT NULL,
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,

    CONSTRAINT field_of_study_pkey PRIMARY KEY (id),
    CONSTRAINT field_of_study_code_unique UNIQUE (code),
    CONSTRAINT code_check CHECK (code::text <> ''::text) NOT VALID, -- not empty
    CONSTRAINT name_check CHECK (name::text <> ''::text) NOT VALID -- not empty
    )

TABLESPACE pg_default;