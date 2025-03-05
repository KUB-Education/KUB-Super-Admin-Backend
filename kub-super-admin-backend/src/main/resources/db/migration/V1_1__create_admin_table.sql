CREATE TABLE IF NOT EXISTS public.admin
(
    id bigserial,  -- implicitly adds NOT NULL, creates sequence, assigns DEFAULT according to sequence
    user_id bigserial,
    
    CONSTRAINT admin_pkey PRIMARY KEY (id),
    CONSTRAINT user_id_unique UNIQUE (user_id),
    CONSTRAINT "user_id_fkey" FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;