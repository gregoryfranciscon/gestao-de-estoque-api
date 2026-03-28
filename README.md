# Controle de Estoque

Sistema de gestao de estoque com back-end em Spring Boot e front-end em HTML, CSS e JavaScript puro.

Hoje o projeto roda como uma aplicacao unica no ambiente local:
- o Spring Boot serve a API REST
- o Spring Boot tambem serve os arquivos do front
- os dados ficam persistidos em H2 local em arquivo

## Tecnologias

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 Database
- Maven Wrapper
- HTML, CSS e JavaScript
- JUnit 5 / Spring Boot Test / MockMvc

## Estrutura do projeto

```text
controleestoque/
|-- frontend/
|   |-- index.html
|   `-- src/
|       |-- css/
|       |-- js/
|       `-- pages/
|-- src/
|   |-- main/
|   |   |-- java/com/gregory/controleestoque/
|   |   |   |-- config/
|   |   |   |-- controller/
|   |   |   |-- dto/
|   |   |   |-- exception/
|   |   |   |-- model/
|   |   |   |-- repository/
|   |   |   `-- service/
|   |   `-- resources/
|   `-- test/
|       |-- java/com/gregory/controleestoque/
|       `-- resources/
|-- start-dev.bat
|-- start-dev.ps1
`-- pom.xml
```

## Funcionalidades atuais

- Dashboard com resumo geral
- Cadastro, edicao, listagem e exclusao de fornecedores
- Cadastro, edicao, listagem, filtro e exclusao de produtos
- Registro e listagem de movimentacoes de estoque
- Regra de entrada e saida com atualizacao de estoque
- Indicadores de estoque baixo
- Front integrado aos endpoints da API
- Persistencia local dos dados em H2
- Suite automatizada de testes de integracao

## Como subir o projeto

### Requisitos

- Java 17 instalado

### Jeito mais simples no Windows

Na pasta raiz do projeto, execute:

```powershell
.\start-dev.bat
```

Esse atalho abre uma janela e sobe a aplicacao.

Depois acesse:

```text
http://localhost:8080
```

### Rodando direto pelo Maven Wrapper

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

## Persistencia local

O banco utilizado em desenvolvimento e um H2 em arquivo local.

- caminho configurado: `./data/controleestoque`
- JDBC URL: `jdbc:h2:file:./data/controleestoque;AUTO_SERVER=TRUE`

Isso significa que os dados nao somem quando a aplicacao e reiniciada.

Os arquivos do banco local foram configurados no `.gitignore`, entao nao devem ser versionados.

## Front-end

O front fica em `frontend/` e e servido pelo proprio Spring Boot com:

```properties
spring.web.resources.static-locations=classpath:/static/,file:./frontend/
```

Entradas principais:

- `http://localhost:8080/`
- `http://localhost:8080/index.html`
- `http://localhost:8080/src/pages/dashboard.html`
- `http://localhost:8080/src/pages/produtos.html`
- `http://localhost:8080/src/pages/fornecedores.html`
- `http://localhost:8080/src/pages/movimentacoes.html`

## H2 Console

Com a aplicacao rodando:

```text
http://localhost:8080/h2-console
```

Use:

- JDBC URL: `jdbc:h2:file:./data/controleestoque;AUTO_SERVER=TRUE`
- User: `sa`
- Password: em branco

## Endpoints principais

### Sistema

- `GET /api/health`

### Dashboard

- `GET /api/dashboard/resumo`
- `GET /api/dashboard/estoque-baixo`
- `GET /api/dashboard/ultimas-movimentacoes?limit=5`

### Fornecedores

- `GET /api/fornecedores`
- `GET /api/fornecedores/{id}`
- `POST /api/fornecedores`
- `PUT /api/fornecedores/{id}`
- `DELETE /api/fornecedores/{id}`

### Produtos

- `GET /api/produtos`
- `GET /api/produtos?nome=logitech`
- `GET /api/produtos/{id}`
- `GET /api/produtos/estoque-baixo`
- `POST /api/produtos`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}`

### Movimentacoes

- `GET /api/movimentacoes`
- `GET /api/movimentacoes?produtoId=1`
- `GET /api/movimentacoes?tipo=ENTRADA`
- `GET /api/movimentacoes/{id}`
- `POST /api/movimentacoes`

## Exemplos de payload

### Criar fornecedor

```json
{
  "nome": "Fornecedor ABC",
  "telefone": "(11) 99999-9999",
  "email": "contato@abc.com"
}
```

### Criar produto

```json
{
  "nome": "Mouse Logitech MX",
  "categoria": "Perifericos",
  "preco": 250.00,
  "estoqueAtual": 10,
  "estoqueMinimo": 3,
  "fornecedorId": 1
}
```

### Criar movimentacao

```json
{
  "produtoId": 1,
  "tipo": "ENTRADA",
  "quantidade": 5,
  "dataHora": "2026-03-28T18:00:00",
  "observacao": "Reposicao"
}
```

## Testes automatizados

A suite atual cobre:

- carga do contexto Spring
- health check
- entrega do front pelo Spring Boot
- dashboard
- CRUD e validacoes de fornecedores
- CRUD, filtros, estoque baixo e validacoes de produtos
- movimentacoes, filtros e regra de estoque

Para executar:

```powershell
.\mvnw.cmd test
```

Na ultima validacao local, a suite executou com:

```text
20 testes, 0 falhas, 0 errors
```

## Observacoes

- O projeto usa seed inicial para popular o ambiente vazio.
- O front foi integrado preservando o layout existente.
- O banco de desenvolvimento e local e persistente.
- Os warnings do Mockito/ByteBuddy no JDK nao impedem o build hoje, mas podem merecer ajuste futuro.

## Autores

- Gregory
- Bruno

## Licenca

MIT. Veja [LICENSE](LICENSE).
