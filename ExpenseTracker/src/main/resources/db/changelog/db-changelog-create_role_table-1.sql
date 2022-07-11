--liquibase formatted sql

--changeset Anica:1

CREATE TABLE IF NOT EXISTS public.role
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT role_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.role
    OWNER to postgres;

-- rollback DROP TABLE IF EXISTS public.role