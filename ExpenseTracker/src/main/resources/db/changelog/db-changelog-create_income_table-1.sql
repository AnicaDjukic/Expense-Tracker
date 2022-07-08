--liquibase formatted sql

--changeset Anica:1

CREATE TABLE IF NOT EXISTS public.income
(
    id uuid NOT NULL,
    amount double precision NOT NULL,
    creation_time timestamp without time zone,
    description character varying(255) COLLATE pg_catalog."default",
    income_group_id uuid,
    user_id uuid,
    CONSTRAINT income_pkey PRIMARY KEY (id),
    CONSTRAINT fknuw53hk0hha02go6e3itfne7t FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fkt3jwvnmfvcxy1fbyraptwjpuu FOREIGN KEY (income_group_id)
    REFERENCES public.income_group (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.income
    OWNER to postgres;

-- rollback DROP TABLE IF EXISTS public.income