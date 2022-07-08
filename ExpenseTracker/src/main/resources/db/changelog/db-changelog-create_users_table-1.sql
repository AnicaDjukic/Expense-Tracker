--liquibase formatted sql

--changeset Anica:1

CREATE TABLE IF NOT EXISTS public.users
(
    id uuid NOT NULL,
    password character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    role_id bigint,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT fk4qu1gr772nnf6ve5af002rwya FOREIGN KEY (role_id)
    REFERENCES public.role (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

-- rollback DROP TABLE IF EXISTS public.users