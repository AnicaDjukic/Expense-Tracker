--liquibase formatted sql

--change Anica:1

CREATE TABLE IF NOT EXISTS public.expense_group
(
    id uuid NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    user_id uuid,
    CONSTRAINT expense_group_pkey PRIMARY KEY (id),
    CONSTRAINT fkou6m7q9098jymaejbiox6occy FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.expense_group
    OWNER to postgres;

-- rollback DROP TABLE IF EXISTS public.expense_group