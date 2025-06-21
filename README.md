# com-ng-billiging-bank

## Pré requisitos

* Java 17
* Maven 3.8.6+ (Opcional)

## Instalação

* Clonar o repositório
* Executar o comando

```bash
    mvn clean package ou ./mvnw clean package
    java -jar target/bank-0.0.1-SNAPSHOT.jar
```

## Testes unitários

* Executar o comando

```bash
    mvn test
```

## Testes de mutação

* Executar o comando

```bash
  mvn org.pitest:pitest-maven:mutationCoverage
```

## Documentação

Com o projeto rodando, a documentação pode ser acessada em http://localhost:8080/swagger-ui.html
