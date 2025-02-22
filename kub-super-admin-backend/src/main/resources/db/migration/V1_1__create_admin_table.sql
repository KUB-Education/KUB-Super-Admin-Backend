CREATE TABLE IF NOT EXISTS public.admin
(
    user_id uuid NOT NULL DEFAULT gen_random_uuid(),
    id uuid NOT NULL,
    CONSTRAINT admin_pkey PRIMARY KEY (user_id),
    CONSTRAINT "user_id_fkey" FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;