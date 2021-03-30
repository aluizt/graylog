# Graylog

Exemplo de como utilizar o Graylog para centralizar os logs de diferentes aplicações.


### Pré-requisitos
* Java >= 8
* Gradle
* Docker
* Git



Para utilizar o Graylog, vamos criar containers através do docker com imagens do Graylog, ElasticSearch e MongoDB.

Poderiamos criar um arquivo docker-compose, mas para ficar mais claro vamos subir os containers individualmente.     


#### MongoDB
```  
sudo docker run --name mongo -d mongo:4.2
``` 

#### ElasticSearch
```   
sudo docker run --name elasticsearch -e "http.host=0.0.0.0" -e "discovery.type=single-node" -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" -d docker.elastic.co/elasticsearch/elasticsearch-oss:7.10.2
```   

#### Graylog
``` 
sudo docker run --name graylog --link mongo --link elasticsearch -p 9000:9000 -p 12201:12201/udp -p 1514:1514 -p 5555:5555 -e GRAYLOG_HTTP_EXTERNAL_URI="http://127.0.0.1:9000/" -d graylog/graylog:4.0
``` 

Criamos um container do Graylog fazendo um link para o MongoDB e para o Elastic, também foi disponibilizado as portas que serão utilizadas.


Após subir o Graylog, vamos acessar pelo navegador o endereço http://127.0.0.1:9000


![image](https://user-images.githubusercontent.com/41808527/113050218-f822cd00-917a-11eb-907c-3ac54cfc6069.png)


Na aba System / Inputs, selecione GELF UDP e click Launch new Input. Aparecera uma aba com informações básicas, click em Save


![image](https://user-images.githubusercontent.com/41808527/113050557-59e33700-917b-11eb-885b-89562c7f9bab.png)


Pronto, seu Input GELF UDP esta pronto, será através dele que você recebera os logs no Graylog.

### Spring Boot

Adicione a dependencia abaixo no arquivo build.gradle de seu projeto

``` 
implementation group: 'de.siegmar', name: 'logback-gelf', version: '3.0.0'
```  

Na pasta resources adicione o arquivo abaixo.

#### Logback.xml
```  
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOG_PATH" value="./logs"/>

    <include resource="org/springframework/boot/logging/logback/base.xml" />

    <appender name="GELF UDP APPENDER" class="de.siegmar.logbackgelf.GelfUdpAppender">
        <graylogHost>127.0.0.1</graylogHost>
        <graylogPort>12201</graylogPort>
        <maxChunkSize>508</maxChunkSize>
        <useCompression>true</useCompression>
    </appender>

    <appender name="SAVE-TO-FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/log.log</file>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </Pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/archived/error.%d{yyyy-MM-dd}.%i.log
            </fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
    </appender>

    <root level="info">
        <appender-ref ref="GELF UDP APPENDER" />
        <appender-ref ref="SAVE-TO-FILE"/>
    </root>

</configuration>


```

Neste arquivo configuramos o envio para o Graylog, assim como salvar em um arquivo local os logs que forem gerados pela aplicação.

Ao rodar a aplicação os logs do start do Spring já estarão visíveis no Graylog.

![image](https://user-images.githubusercontent.com/41808527/113052192-37eab400-917d-11eb-9464-762bce28e6d8.png)


![image](https://user-images.githubusercontent.com/41808527/113052348-67012580-917d-11eb-8bba-2de4e0f77be6.png)


Para teste, foi criado dois endpoints, teste em seu navegado http://127.0.0.1:8080/log/info para gerar um simples log que será enviado ao Graylog

![image](https://user-images.githubusercontent.com/41808527/113052630-c0695480-917d-11eb-9ce0-8964606fce1b.png)


![image](https://user-images.githubusercontent.com/41808527/113052741-e1ca4080-917d-11eb-99cb-3f0e2a50973d.png)

Esta é uma forma bem básica de utilizar alguns dos recursos do Graylog, diversas outras formas de utilização pode ser encontrada em https://docs.graylog.org/en/4.0/



