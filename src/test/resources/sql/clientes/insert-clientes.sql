insert into USUARIOS (id, username, password, role)
values (1,"ana@gmail.com", "$2a$12$BEjol64DvTGJkdqtlisRkOSsuv4uNajZVeKAYEs/Oy0hsBqrZfNNG", "ROLE_ADMIN");
insert into USUARIOS (id, username, password, role)
values (2,"joao@gmail.com", "$2a$12$BEjol64DvTGJkdqtlisRkOSsuv4uNajZVeKAYEs/Oy0hsBqrZfNNG", "ROLE_CLIENTE");
insert into USUARIOS (id, username, password, role)
values (3,"julia@gmail.com", "$2a$12$BEjol64DvTGJkdqtlisRkOSsuv4uNajZVeKAYEs/Oy0hsBqrZfNNG", "ROLE_CLIENTE");

insert clientes (id, nome, cpf, id_usuario) values(1, "Ana Filho", "51200313070", 1);
insert clientes (id, nome, cpf, id_usuario) values(2, "Joao Gomes", "15958194011", 2);
