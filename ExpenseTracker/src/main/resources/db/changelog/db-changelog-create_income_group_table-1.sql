--liquibase formatted sql

--changeset Anica:1

CREATE TABLE IF NOT EXISTS public.income_group
(
    id uuid NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    user_id uuid,
    CONSTRAINT income_group_pkey PRIMARY KEY (id),
    CONSTRAINT fkh05q73et3ij14d1s84ycjr1y0 FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.income_group
    OWNER to postgres;

-- rollback DROP TABLE IF EXISTS public.income_group