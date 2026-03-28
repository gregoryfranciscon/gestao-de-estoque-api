# Visão Geral do Projeto

## Propósito

O projeto `controleestoque` é uma API back-end em construção para gestão de estoque. A proposta é servir como base organizada para evolução incremental, com separação clara entre camadas e preparação para crescimento do domínio.

## Estado Atual

- Aplicação Spring Boot configurada e executando
- Estrutura inicial de pacotes definida
- Persistência preparada com Spring Data JPA
- Banco H2 em memória disponível para desenvolvimento local
- Sem funcionalidades de negócio implementadas nesta etapa

## Direção Arquitetural

O projeto segue uma organização simples e comum em aplicações Spring Boot:

- `controller` para exposição de endpoints
- `service` para regras de negócio
- `repository` para acesso a dados
- `model` para entidades e modelos
- `dto` para contratos de entrada e saída
- `config` para configurações da aplicação

## Evolução Esperada

As próximas etapas devem incluir a modelagem do domínio, criação dos primeiros endpoints, persistência das entidades principais e fortalecimento da cobertura de testes.
