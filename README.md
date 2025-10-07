# Sistema Hospitalar - Tech Challenge Fase 3: Segurança e Microsserviços

## 🎯 Objetivo do Projeto

O objetivo deste projeto, desenvolvido para o Tech Challenge da POS Tech FIAP, é construir um **backend modular e seguro** para um sistema de gestão hospitalar. O foco está na implementação de **segurança robusta (Spring Security/JWT)**, **consultas flexíveis (GraphQL)** e **comunicação assíncrona confiável (RabbitMQ)**, garantindo um sistema escalável e com boas práticas de desenvolvimento.

## 💻 Arquitetura e Microsserviços

A solução está dividida em microsserviços e componentes de infraestrutura definidos no `docker-compose.yml`:

| Serviço/Componente | Módulo | Porta | Tecnologia/Função |
| :--- | :--- | :--- | :--- |
| `sishospitalar-agendamento` | `sishospitalar` | `8080` | Backend Principal. Implementa autenticação/autorização (Spring Security/JWT), expõe a API GraphQL, gerencia o agendamento de consultas e publica eventos no RabbitMQ. |
| `sishospitalar-notificacoes` | `sishospitalar-notificacoes` | `8081` | Microsserviço Consumidor. Escuta eventos de agendamento (RabbitMQ) e simula o envio de lembretes/notificações aos pacientes. |
| `db` | `postgres:15-alpine` | `5432` | Banco de dados PostgreSQL para persistência dos dados de usuários e consultas. |
| `rabbitmq` | `rabbitmq:3-management` | `5672/15672` | Broker de Mensagens (RabbitMQ) para comunicação assíncrona entre os microsserviços. |

## 🛠️ Tecnologias Principais

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Segurança:** Spring Security e JWT (JSON Web Tokens)
* **API de Dados:** GraphQL (para consultas flexíveis)
* **Comunicação Assíncrona:** RabbitMQ
* **Persistência:** Spring Data JPA + PostgreSQL
* **Containerização:** Docker e Docker Compose

## 🚀 Como Executar o Projeto (Docker Compose)

O projeto é configurado para ser executado de forma completa utilizando Docker Compose.

### Pré-requisitos

* Docker e Docker Compose instalados.
* Uma ferramenta para testar a API (como o Postman, utilizando o arquivo de Collection fornecido).

### Passos de Execução

1.  **Clone o Repositório** (Assumindo que o código-fonte está organizado com os dois módulos (`sishospitalar` e `sishospitalar-notificacoes`) e o `docker-compose.yml` na raiz).

2.  **Inicie os Containers:**
    Navegue até o diretório raiz do projeto (onde está o `docker-compose.yml`) e execute o comando:

    ```bash
    docker-compose up --build
    ```

    Este comando irá:
    * Construir as imagens Docker dos serviços `agendamento` e `notificacoes`.
    * Criar e inicializar os containers `db` (PostgreSQL) e `rabbitmq`.
    * Inicializar o serviço `agendamento` (porta **8080**).
    * Inicializar o serviço `notificacoes` (porta **8081**), que se conectará ao RabbitMQ e começará a escutar mensagens.

3.  **Verifique a Inicialização:**
    Aguarde até que os logs indiquem que ambos os serviços Spring Boot foram iniciados com sucesso. O serviço de agendamento estará acessível em `http://localhost:8080`.

## 🧪 Teste de Funcionalidades (Postman Collection)

O arquivo **`Fiap Sistema Hospitalar FASE 3.postman_collection.json`** contém todos os testes para validar os requisitos funcionais e de segurança do sistema.

Ou acesse https://documenter.getpostman.com/view/19364354/2sB3QJNAtj

### Configuração do Postman

1.  **Importe a Collection:** Importe o arquivo JSON fornecido para o seu Postman.
2.  **Configure a Variável de Ambiente:**
    * Crie um novo **Environment** (Ambiente) no Postman.
    * Defina a variável `baseUrl` com o valor inicial: `http://localhost:8080`.
    * **Nota:** As variáveis de ID de usuário (`medicoId`, `pacienteId`, etc.) e os tokens JWT (`tokenMedico`, `tokenPaciente`, etc.) serão definidas automaticamente pelos scripts de teste nas requisições de Registro e Login.

### Fluxo de Teste Recomendado

Siga a ordem dentro de cada pasta de perfil (`01 - Médico`, `02 - Enfermeiro`, `03 - Paciente`):

1.  Execute o `Registro` e, em seguida, o `Login` para salvar os tokens JWT.
2.  Utilize as consultas e mutações GraphQL para testar as regras de negócio e de autorização de cada perfil.

## 🔒 Regras de Autorização (Spring Security)

A lógica de segurança implementada no `ConsultaController` garante que:

| Funcionalidade | Endpoint | Permissão Exigida |
| :--- | :--- | :--- |
| **Login/Registro** | `/auth/**` | Público (`permitAll`) |
| **Criação/Edição/Cancelamento** | `criarConsulta`, `atualizarConsulta`, `cancelarConsulta` | `MEDICO` ou `ENFERMEIRO` |
| **Listar Consultas Futuras (Geral)** | `consultasFuturas` | `MEDICO` ou `ENFERMEIRO` |
| **Listar Consultas por Paciente** | `consultasPorPaciente(pacienteId)` | **Regra Complexa:** `MÉDICO`/`ENFERMEIRO` podem buscar qualquer `pacienteId`. `PACIENTE` só pode buscar o seu próprio ID. |

## 🔗 Comunicação Assíncrona (RabbitMQ)

A comunicação entre o `sishospitalar-agendamento` (Produtor) e o `sishospitalar-notificacoes` (Consumidor) ocorre via RabbitMQ, utilizando um `TopicExchange` chamado `consulta-exchange`.

**Fluxo de Mensagens:**

1.  O `AgendamentoService` publica mensagens no `consulta-exchange` com as chaves de roteamento (`routing keys`): `nova-consulta`, `atualizar-consulta` ou `cancelar-consulta`.
2.  O `NotificacaoService` consome as mensagens nas filas ligadas a essas chaves (`filaNovaConsulta`, `filaAtualizarConsulta`, `filaCancelarConsulta`), simulando o envio de e-mails para o paciente.


**Autores**

* José Franklin Miranda Gomes Leite RA 361614
* Vitor Henrique dos Santos  RA 361617
