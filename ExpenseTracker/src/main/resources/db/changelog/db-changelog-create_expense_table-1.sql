--liquibase formatted sql

--changeset Anica:1

CREATE TABLE IF NOT EXISTS public.expense
(
    id uuid NOT NULL,
    amount double precision NOT NULL,
    creation_time timestamp without time zone,
    description character varying(255) COLLATE pg_catalog."default",
    expense_group_id uuid,
    user_id uuid,
    CONSTRAINT expense_pkey PRIMARY KEY (id),
    CONSTRAINT fkekyts7i8w5cam119wj1itdom2 FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fknela9oeq8fkibex6xs93qtuft FOREIGN KEY (expense_group_id)
    REFERENCES public.expense_group (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.expense
    OWNER to postgres;

-- rollback DROP TABLE IF EXISTS public.expense