# Challenge Itaú

# Credit-Card-Transaction-Services

É um micro serviço desenvolvido para ilustrar um exemplo de uma das milhares funcionalidades que a aplicação legado pode fornecer se for quebrada em serviços.
    
Este serviço é responsação por receber transações de cartão de credito, tanto via mensageria(fila porque não tinha como implantar o topico do Kafka) quanto RESTFull(endpoint), onde será feito o cadastro dessa transação na nossa base.
    
Foi disponibilizado endpoints para consultas por id da transação, pelo usuario, pelo meio de pagamento do usuario, pelo dia, e tambem entre datas de inicio e fim que deseja pesquisar.
    
Isso ajuda os clientes nas analises de fraude, por exemplo o BackOffice pode bater nesses endpoints para fazer uma analise melhor das transações daque cliente. 
    
O outro serviço que foi citado na solução abaixo (serviço que analisaria alguma transação ou usuario ou meio de pagamento atraves de um alerta vindo de um topico tambem) poderia fazer essas consultas facilmente, desacoplado de tudo.

    
### Installation

Requires to run
[Java 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
[Docker](https://docs.docker.com/docker-for-windows/install/)



Install the dependencies and devDependencies and start the server.

```sh
$ docker-compose build
$ docker-compose up
```

Após executar os comandos temos a aplicação em container, junto com o rabbitmq e mongodb.

A documentaço da aplicação consta nessa url 
(http://localhost:8082/swagger-ui.html#/)

Além disso, se quiser testar localmente tem uma collection do Postman salva no repositorio, só importar no postman (Challenge-Itau.postman_collection.json).

## Perguntas

## 1. Qual é a sua estratégia para modernizar esse legado?

 Dado o problema, eu pensei na seguinte solução:
 
 ![itau (1)](https://user-images.githubusercontent.com/34173316/81080121-c6e84100-8ec6-11ea-9c76-dcb3b18f39b7.png)

Devido ao grande volume de transações que o Itaú deve conter hoje, implementaria um apache Kafka logo após o mainframe, pois ele é um serviço de streaming e mensagens bem distribuído, suportando assim transformar e mover um grande volume de mensagens no dia, deixando assim tudo mais escalável, confiável e tolerante a falhas.

Como mostra o desenho a seguir, ele tem um grande leque para leitura e escrita de dados com serviços externos, como alguns bancos de dados(SQL e NoSQL), serviços de pesquisas(ElasticSearch) e filas. Mais um ponto positivos, poderiamos nos conectar em outros serviços de uma forma mais rapido, e sem dependencia do mainframe para pegar alguns dados necessarios.

![kafka](https://user-images.githubusercontent.com/34173316/81080509-2cd4c880-8ec7-11ea-84de-140d841af8eb.png)


O Kafka Connect é quem tem esse papel, ele é uma estrutura que possibilita a gente se conectar a serviços externos e importar ou exportar dados para o nosso cluster Kafka, de uma forma confiável e bem escalável.
Temos também o Kafka Stream, que é uma biblioteca bem leve para processamentos de dados ou fluxos, citei esse cara pois imagino que o Itaú tem muito legado em cobol ou algo do tipo, que ainda não consegue transmitir dados conhecidos em aplicações mais recentes, tipo o JSON. O Kafka Stream pode por exemplo pegar esse binário posicional e transformar em Avro ( que é o que o Kafka utiliza como padrão para serializar dados).Também pode ser utilizado para qualquer outro tipo de processamento de fluxo ai no Itaú.
Outro que foi citado no desenho, é o Schema Registry que nos ajuda a garantir que os contratos “implicitos” dos dados que são gravados no topico (AVRO) seja cumprido fazendo verificações de compatibiliades.
E por fim, o KSQL, para fazer queries e monitorações em topicos existentes e gerar um novo dado para um novo topico, por exemplo um alarme de  uma possível fraude em determinada transação.

-  Com todo nosso serviço de streaming ok, teremos nossos topicos de acordos com nossos requisitos(transação de cartão de credito, transação de transferencia bancaria, alarmes, e etc…)
Então a partir daí quebrar todos aqueles monolitos em micro serviços, o porque disso?
    • conseguimos escalar partes do sistemas sobrecarregados.
    • podemos definir outros tipos de tecnologias em partes dos sistemas sendo mais especifica para aquele contexto
    • podemos isolar o serviço em caso de falhas
    • permitir implementação de novas funcionalidades com mais rapidez

No exemplo temos serviços específicos por funcionalidade e domínio (por não saber a quantidade de requisições que o serviço só por funcionalidade teria).
Pensei em um orquestrador de Container (Kubernetes) na Nuvem (GKE, EKS) para gerenciar todas nossas aplicações que estariam containizadas com Docker e implantadas no Kubernetes. Esse orquestradores nos trazem mais simplicidades para automações e escalabilidades de aplicações. Economizando tempo em montar toda uma infra com balanceadores, services discovery e etc.

Como citado na solução, teríamos um serviço (credit-card-transaction) que seria um consumidor de um tópico de transações de cartões de crédito, ele iria pegar essas informações da transação e salvar no seu banco de dados. Além disso iria expor um endpoint para consulta dessas transações, onde o Backoffice poderia bater nesse endpoint para trazer transações de um usuário específico, por exemplo, e fazer uma melhor análise dela. E também de acesso do nosso outro serviço (credit-card-rules-analise), que também seria um consumidor de um outro tópico só que de alertas de fraudes de cartão de crédito, por exemplo, e partir desse alerta executaria alguma regra específica, buscando as transações daquele cliente no nosso outro serviço. E também podendo expor endpoints para acessos do BackOffice, saber quais análises já foram feitas dado aquele alarme, ou se eles quiserem re-executar alguma outra regra de análise. 

OBS: está sendo utilizado somente um banco de dados para facilitar na implementação do teste, mas o ideal seria cada serviço ter o seu, descartando o unico ponto de falha(que nao foi implementado por falta de tempo).
Isso é só um exemplo de micro serviços que podemos ter dado toda a problemática, podemos ter N aplicações orquestradas pelo Kubernetes.

Mas resumidamente, teremos uma infra baseada a eventos, onde teremos N micro serviços consumindo essas mensagens e fazendo o tratamento especifico para cada um, e tambem teríamos todos esses dados salvos em uma base específica de cada domínio. Possibilitando também que o Backoffice tenha acesso a todos esses dados para uma melhor análise nos endpoints expostos por cada serviço.


### 2. Escolha uma funcionalidade prioritária para modernizar.

Escolhi a de transações de cartões de crédito.


### 3. Quais os critérios você adotou para priorizar a funcionalidade a ser modernizada?

Para fazer qualquer tipo de análise, precisamos ter essas transações conosco, então escolhi essa funcionalidade para o pontapé inicial, pois tambem poderiamos já liberar todas transações para o nossos clientes(Back Office, etc), ao invés dele acessar um legado para ter as transações, agora ele teria um micro serviços muito mais eficiente e rápido para consultar suas transações.

### 4. Quais seriam os requisitos imprescindíveis para essa funcionalidade?

temos alguns requisitos importante, teríamos que ter o topico no kafka ja funcionando ou que o legado nos enviasse essa transação de alguma forma.
Precisamos de uma infra para hospedar esses containers.

### 5. Como será a convivência desta funcionalidade modernizada com o legado? (Visão técnica)

O legado poderia agora se conectar ao Kafka e agora então ser um produtor para os tópicos de transações, se não for possível, poderia nos disponibilizar essa mensagem de qualquer forma que o Kafka saiba se conectar e buscá-la.

Estaríamos já tirando uma funcionalidade desse mainframe.

### 6.Quais serão os fatores críticos para o sucesso da modernização dessa funcionalidade?facilitar a implementação de novas features.

Não teríamos mais um único lugar que fizesse tudo, teríamos vários serviços menores, específicos cada um com sua função, facilitando assim a criação de uma nova feature em cada serviço, com entregas separadas, com menor tempo de build e deploy, e menor intervalo de entregas em produção. Agilizando assim uma nova entrega de uma nova funcionalidade em produção.

### 7.Quais seriam as linguagem e tecnologias envolvidas na solução e por quê?

Além das citadas acima, em termos de serviços, teríamos uma aplicação java implementada com seu leque do Spring Framework.

Java é uma das tecnologias mais utilizadas no mercado, tem um grande número de frameworks que facilitam a vida do desenvolvedor, podendo ser rodada em qualquer sistema operacional.

Como citado, tem um número grande de frameworks, e um deles é o Spring Framework que vamos utilizar nesse cenário.

Spring Framework facilita bastante o desenvolvimento, temos maior legibilidade do código e menos implementações, o que facilita na manutenção.

- spring boot: facilita bastante na configuração do projeto, sendo assim seu projeto está no ar muito mais rápido com um server já pré-configurado.
- spring mvc: para controle das camadas de model, view e controller.
- spring data: facil acesso a repositórios com queries mais simplificadas, rodando o hibernate e jpa por baixo, facilita bastante a conexão com o banco
- rabbitMq: nossa parte de mensageria em tópicos( foi escolhido o tópico para simular o Kafka, porque podemos ter diversos clientes querendo receber a notificação, sem que haja concorrência e perda de mensagem )
- docker/docker-compose: todos os serviços estão containizados e configurado em uma única network, facilitando assim a configuração e comunicação entre os serviços(simulando o kubernetes)

### 8.Como você irá armazenar esses dados (tecnologias e modelos)?

Será implementado em um modelo não-relacional, utilizando o MongoDB como a tecnologia adotada, por ter uma maior escalabilidade e flexibilidade.

### 9.Como a performance será otimizada de um jeito que ficará fácil de escalar?

Teremos micro serviços bem menores, com menos responsabilidades, e processando só que é responsabilidade dele naquela “máquina”, gerando menos concorrência e diminuindo o número de processamento.

### 10.Dado que é um projeto importantíssimo para a organização, como você faria para acelerar o desenvolvimento, entregando valor para o cliente em menor tempo?

Como será tudo implementado como micro serviços, estamos desacoplados agora, então podemos criar um micro serviço que mais importa para o cliente, conseguimos fazer isso sem depender de uma infra única.

E pensando na metodologia ágil, podemos fazer entregas de valores para cada Sprint, assim o pessoal de negócio deixa de esperar uma única entrega que contemple tudo, e passa agora a ter pequenas funcionalidades com um tempo de entrega mais rápido, já se consegue ver valor no negocio.
