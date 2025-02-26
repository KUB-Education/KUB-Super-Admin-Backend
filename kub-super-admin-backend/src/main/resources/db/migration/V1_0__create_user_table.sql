CREATE TABLE IF NOT EXISTS public."user"
(
    id bigserial, -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    email character varying(64) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(32) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(32) COLLATE pg_catalog."default" NOT NULL,
    middle_name character varying(32) COLLATE pg_catalog."default",
    password_hashed character varying(64) COLLATE pg_catalog."default",
    status character varying(32) COLLATE pg_catalog."default" NOT NULL,
    temporary_password_expiration timestamp(6) without time zone,
    temporary_password_hashed character varying(64) COLLATE pg_catalog."default",
    
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT email_check CHECK (email::text <> ''::text) NOT VALID,
    CONSTRAINT first_name_check CHECK (first_name::text <> ''::text) NOT VALID,
    CONSTRAINT last_name_check CHECK (last_name::text <> ''::text) NOT VALID,
    CONSTRAINT middle_name_check CHECK (middle_name::text <> ''::text) NOT VALID,
    CONSTRAINT password_hashed_check CHECK (password_hashed::text <> ''::text) NOT VALID,
    CONSTRAINT temporary_password_hashed_check CHECK (temporary_password_hashed::text <> ''::text) NOT VALID,
    CONSTRAINT user_status_check CHECK (status::text = ANY (ARRAY['EMAIL_SENDING_FAILURE'::character varying, 'ACTIVATION_PENDING'::character varying, 'ACTIVATION_EXPIRED'::character varying, 'ACTIVATED'::character varying, 'RECOVERY_PENDING'::character varying]::text[]))
    )

TABLESPACE pg_default;