# Sistema de Gestão de Estoque - Frontend

Estrutura base de frontend em HTML, CSS e JavaScript puro (sem frameworks), organizada para facilitar manutenção, crescimento do projeto e integração com API REST no backend.

## Organização de pastas

- `src/assets`: arquivos estáticos (imagens, ícones, fontes).
- `src/css`: estilos globais, variáveis de design e estilos por página.
- `src/js/services`: camada de comunicação com API (requisições HTTP).
- `src/js/modules`: lógica por domínio/tela (produtos, fornecedores, movimentações, dashboard).
- `src/js/utils`: funções utilitárias reutilizáveis.
- `src/pages`: páginas HTML separadas por tela.
- `src/components`: componentes HTML reutilizáveis (header, sidebar, tabela).

## Árvore de diretórios

```text
frontend/
├── .gitignore
├── index.html
├── README.md
└── src/
    ├── assets/
    │   ├── fonts/
    │   ├── icons/
    │   └── images/
    ├── components/
    │   ├── header.html
    │   ├── sidebar.html
    │   └── tabela.html
    ├── css/
    │   ├── dashboard.css
    │   ├── fornecedores.css
    │   ├── global.css
    │   ├── movimentacoes.css
    │   ├── produtos.css
    │   └── variables.css
    ├── js/
    │   ├── main.js
    │   ├── modules/
    │   │   ├── dashboard.js
    │   │   ├── fornecedores.js
    │   │   ├── movimentacoes.js
    │   │   └── produtos.js
    │   ├── services/
    │   │   └── api.js
    │   └── utils/
    │       └── helpers.js
    └── pages/
        ├── dashboard.html
        ├── fornecedores.html
        ├── movimentacoes.html
        └── produtos.html
```
