# Frontend

Front-end do sistema de controle de estoque feito em HTML, CSS e JavaScript puro.

O front esta integrado com a API Spring Boot e, no fluxo principal do projeto, e servido pelo proprio back-end.

## Estrutura

```text
frontend/
|-- index.html
`-- src/
    |-- components/
    |-- css/
    |-- js/
    `-- pages/
```

## Pastas principais

- `src/css`: estilos globais e estilos por pagina
- `src/js/services`: camada de acesso a API
- `src/js/modules`: logica por tela
- `src/js/utils`: utilitarios reutilizaveis
- `src/pages`: paginas HTML
- `src/components`: fragmentos HTML reutilizaveis

## Como usar no projeto

Suba a aplicacao pela raiz:

```powershell
.\start-dev.bat
```

Depois acesse:

```text
http://localhost:8080
```

## Observacao

O arquivo `dev-server.cjs` foi mantido apenas como utilitario opcional para desenvolvimento isolado do front. O fluxo oficial do projeto usa o Spring Boot servindo estes arquivos.
