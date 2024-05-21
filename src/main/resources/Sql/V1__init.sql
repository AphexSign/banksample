CREATE TABLE IF NOT EXISTS public."users"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    login character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    fio character varying(255),
    telephone character varying(255),
    email character varying(255),
    balance numeric(16,2),
    date_birth date,
    first_deposit numeric(16,2),
    CONSTRAINT "Users_pkey" PRIMARY KEY (id)
)