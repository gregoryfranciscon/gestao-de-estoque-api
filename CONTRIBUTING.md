# Contribuindo

Obrigado pelo interesse em contribuir com este projeto.

## Diretrizes gerais

- mantenha o foco em melhorias objetivas e alinhadas ao escopo do sistema
- preserve a separacao por camadas da aplicacao
- evite misturar mudancas estruturais, visuais e de regra de negocio no mesmo PR
- prefira codigo simples, legivel e facil de manter
- atualize a documentacao quando a mudanca afetar uso, setup ou comportamento

## Fluxo sugerido

1. Faça um fork ou crie uma branch a partir da principal.
2. Implemente a mudanca com commits claros.
3. Rode os testes automatizados.
4. Revise os arquivos alterados.
5. Abra um Pull Request explicando contexto, objetivo e impacto.

## Padroes esperados

- Java 17
- Spring Boot
- Maven Wrapper
- HTML, CSS e JavaScript puro no front
- identacao com 4 espacos no back-end
- organizacao por responsabilidade dentro de `com.gregory.controleestoque`

## Comandos uteis

Subir localmente:

```powershell
.\start-dev.bat
```

Rodar testes:

```powershell
.\mvnw.cmd test
```

## Antes de enviar uma contribuicao

- confirme que o projeto sobe em `http://localhost:8080`
- confirme que `.\mvnw.cmd test` passa
- evite incluir arquivos locais de IDE, banco ou logs
- revise README e demais docs se necessario
