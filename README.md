🛡️ Auth Service — Spring Boot | JWT | Refresh Token | Roles | Swagger

Sistema de autenticação completo desenvolvido com Java + Spring Boot, incluindo:

✔️ Login e Registro
✔️ JWT Access Token
✔️ Refresh Token
✔️ Controle de Permissões (Roles)
✔️ Filtros de Segurança personalizados
✔️ Tratamento de erros centralizado
✔️ DTOs
✔️ Documentação com Swagger
✔️ Arquitetura limpa e escalável

Este projeto foi pensado para ser um serviço de autenticação independente (Auth Server), podendo ser usado como base de estudos ou como módulo de autenticação para aplicações reais.

🚀 Tecnologias Utilizadas

Java 17+

Spring Boot 3+

Spring Web

Spring Security (manual filter-based logic)

Spring Validation

JWT (jjwt)

Swagger / OpenAPI

Lombok

Maven

📌 Funcionalidades
🔐 Autenticação

Login com email e senha

Registro de novos usuários

Tokens gerados:

Access Token (curta duração)

Refresh Token (longa duração)

🛡️ Autorização / Roles

Cada usuário possui uma role (ex: USER ou ADMIN)

Endpoints protegidos via filtro que valida JWT

🔁 Refresh Token

Endpoint dedicado para renovar o Access Token

Refresh Token armazenado no usuário

Apenas tokens válidos podem gerar novos tokens

⚙️ Validações

Validação de DTOs

Tratamento de erros via @RestControllerAdvice

Mensagens de erro padronizadas

📘 Documentação

Swagger/OpenAPI disponível em: