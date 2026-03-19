# Auth Service API

<div align="center">

![Java](https://img.shields.io/badge/Java-17-blue?style=flat)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-336791?style=flat)

![Maven](https://img.shields.io/badge/Maven-Build-orange?style=flat)

Um serviço robusto de autenticação e autorização construído com **Spring Boot 3.5.7**, implementando **JWT (JSON Web Tokens)** para comunicação segura entre aplicações.

</div>

---

## 🎯 Objetivo

Este projeto foi desenvolvido para estudo e prática de autenticação e autorização em aplicações backend.

## 📋 Sobre o Projeto

Auth Service é uma API REST completa desenvolvida para gerenciar autenticação e autorização de usuários. O projeto implementa boas práticas de segurança moderna, incluindo:

- ✅ Autenticação com JWT (Access Token + Refresh Token)
- ✅ Criptografia de senhas com BCrypt
- ✅ Controle de acesso baseado em roles (RBAC)
- ✅ Documentação automática com Swagger/OpenAPI
- ✅ Validação robusta de dados com Spring Validation
- ✅ Tratamento centralizado de exceções
- ✅ Integração com PostgreSQL
- ✅ Containerização com Docker e Docker Compose
- ✅ Filtros de segurança customizados para JWT

---

## 🛠️ Stack Tecnológico

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **Spring Boot** | 3.5.7 | Framework principal |
| **Spring Security** | 3.5.7 | Autenticação e autorização |
| **Spring Data JPA** | 3.5.7 | Acesso a dados |
| **JWT (JJWT)** | 0.11.5 | Tokens de autenticação |
| **PostgreSQL** | 14 | Banco de dados relacional |
| **Swagger/OpenAPI** | 2.4.0+ | Documentação da API |
| **Docker** | Latest | Containerização |
| **Java** | 17 | Linguagem de programação |
| **Maven** | 4.0.0 | Gerenciador de dependências |
| **Lombok** | 1.18+ | Redução de boilerplate |

---

## 🚀 Como Começar

### Pré-requisitos

- **Java 17+** instalado
- **Maven 3.8+**
- **Docker** e **Docker Compose**
- **PostgreSQL 14** (ou usar o Docker Compose)

### Instalação Local

#### 1. Clone o repositório
```bash
git clone <seu-repositorio>
cd auth-service
```

#### 2. Configure as variáveis de ambiente

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Auth
spring.datasource.username=admin
spring.datasource.password=admin
jwt.secret=sua-chave-secreta-muito-segura
```

#### 3. Inicie o banco de dados com Docker Compose

```bash
docker-compose up -d
```

#### 4. Compile e execute a aplicação

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

---


## 📚 Documentação da API

### Swagger/OpenAPI

Acesse a documentação interativa da API:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Endpoints Principais

#### 1. **Registro de Usuário**
```http
POST /auth/register
Content-Type: application/json

{
  "username": "joao_silva",
  "email": "joao@email.com",
  "password": "senha_segura_123"
}
```

**Resposta (201 Created)**
```json
{
  "message": "Usuário registrado com sucesso!"
}
```

---

#### 2. **Login**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "password": "senha_segura_123"
}
```

**Resposta (202 Accepted)**
```json
{
  "message": "Usuario Logado com sucesso!",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Headers Retornados:**
- `Set-Cookie`: `refresh_token=...; HttpOnly; Secure; SameSite=Strict`

---

#### 3. **Refresh Token**
```http
POST /auth/refresh
Cookie: refresh_token=...
```

**Resposta (200 OK)**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

#### 4. **Obter Perfil do Usuário (Requer Admin)**
```http
GET /auth/users/{id}/profile
Authorization: Bearer {accessToken}
```

**Resposta (200 OK)**
```json
{
  "id": 1,
  "username": "joao_silva",
  "email": "joao@email.com",
  "role": "A"
}
```

---

## 🔐 Segurança

### Autenticação JWT

O projeto implementa um sistema de dois tokens:

- **Access Token**: Curta duração (10 minutos) - Usado nas requisições
- **Refresh Token**: Longa duração (30 dias) - Armazenado em cookie HttpOnly

### Configuração JWT

Edite em `application.properties`:

```properties
jwt.secret=sua-chave-muito-segura-minimo-32-caracteres
jwt.access.expiration-ms=600000      # 10 minutos
jwt.refresh.expiration-ms=2592000000 # 30 dias
```

### Controle de Acesso (RBAC)

Roles disponíveis:
- **A** (Admin): Acesso completo
- **U** (User): Acesso limitado

---

## 🏗️ Estrutura do Projeto

```
src/main/java/com/api/auth_service/
├── controller/              # Controladores REST
│   ├── LoginController.java
│   ├── RegisterController.java
│   ├── RefreshController.java
│   └── UsersController.java
├── service/                 # Lógica de negócio
│   └── RefreshTokenService.java
├── security/                # Autenticação e JWT
│   ├── JwtUtil.java
│   └── JwtFilter.java
├── model/                   # Entidades JPA
│   ├── UserModel.java
│   └── RefreshToken.java
├── repositories/            # Acesso a dados (Spring Data JPA)
│   ├── UserRepository.java
│   └── RefreshTokenRepository.java
├── dto/                     # Data Transfer Objects
│   ├── LoginRequestDTO.java
│   ├── LoginResponseDTO.java
│   ├── UserRequestDTO.java
│   └── ProfileResponseDTO.java
├── exception/               # Exceções customizadas
│   ├── GlobalExceptionHandler.java
│   ├── UserNotFoundException.java
│   ├── TokenExpiredException.java
│   └── UnathorizedAccessExeption.java
├── config/                  # Configurações
│   ├── SecurityConfig.java
│   └── OpenAPIConfig.java
└── AuthServiceApplication.java
```

---

## 💾 Banco de Dados

### Schema Principal

**Tabela: users**
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(1) NOT NULL DEFAULT 'U'
);
```

**Tabela: refresh_tokens**
```sql
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    token TEXT NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL
);
```

---

## 🧪 Testes

Execute os testes unitários:

```bash
mvn test
```

Execute com cobertura de código:

```bash
mvn test jacoco:report
```

---

## 📦 Docker

### Build da imagem

```bash
docker build -t auth-service:latest .
```

### Executar com Docker Compose

```bash
docker-compose up
```

---

## 🔍 Exemplo de Fluxo Completo

### 1. Registrar novo usuário
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria_santos",
    "email": "maria@example.com",
    "password": "SenhaForte123!"
  }'
```

### 2. Fazer login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@example.com",
    "password": "SenhaForte123!"
  }' \
  -c cookies.txt
```

### 3. Usar o token nas requisições
```bash
curl -X GET http://localhost:8080/auth/users/1/profile \
  -H "Authorization: Bearer <seu-access-token>"
```

### 4. Renovar o token (quando expirar)
```bash
curl -X POST http://localhost:8080/auth/refresh \
  -b cookies.txt
```

---

## ⚙️ Configurações Principais

### application.properties

```properties
# Aplicação
spring.application.name=auth-service
server.port=8080

# Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/Auth
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=paocomovopaocomovopaocomovopaocomovo
jwt.access.expiration-ms=600000       # 10 minutos
jwt.refresh.expiration-ms=2592000000  # 30 dias
```

---

## 🐛 Tratamento de Exceções

O projeto implementa tratamento centralizado de erros:

| Exceção | Status HTTP | Descrição |
|---------|-------------|-----------|
| `UserNotFoundException` | 404 | Usuário não encontrado |
| `TokenExpiredException` | 401 | Token expirado |
| `TokenNotFoundException` | 401 | Token não fornecido |
| `UnathorizedAccessExeption` | 403 | Acesso não autorizado |
| `IncorrectEmailOrPassordException` | 401 | Credenciais inválidas |

---

## 🤝 Contribuindo

Este projeto é um portfólio educacional. Para melhorias:

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -am 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

## 👨‍💻 Autor

Desenvolvido como projeto de estágio/portfólio.

---

## 📞 Contato & Suporte

Para dúvidas e sugestões, abra uma issue no repositório ou entre em contato.

---

## 🎯 Próximas Melhorias Planejadas

- [ ] Implementar OAuth2/OpenID Connect
- [ ] Autenticação com 2FA (Two-Factor Authentication)
- [ ] Integração com serviço de email para confirmação de conta
- [ ] Rate limiting nos endpoints
- [ ] Audit logging de ações
- [ ] WebSocket para notificações em tempo real

---

**Última atualização:** Março 2026
=======
Mensagens de erro padronizadas
>>>>>>> bc7ace94953a8a125c5270c7d089a0a8f684d0bd
