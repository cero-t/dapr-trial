# Dapr example
Say hello to the Dapr world with Java!

## Pre-request

Following tools should be installed.

1. Docker
2. Dapr
3. JDK (>=11)

## Usage

### 1. Hello World

Start dapr
```
cd dapr-example
dapr run --app-id hello-dapr --app-port 8080 --port 9080 ./mvnw spring-boot:run
```

Access to the dapr process
```
curl localhost:9080/v1.0/invoke/hello-dapr/method/hello
```

And it shows
```
Hello, Dapr World
```

### 2. State Management

Start dapr (if not running)
```
cd dapr-example
dapr run --app-id hello-dapr --app-port 8080 --port 9080 ./mvnw spring-boot:run
```

Store data by Dapr State Management API
```
curl -XPOST localhost:9080/v1.0/state -H "Content-type:application/json" -d '[{"key":"test-message1","value":{"name":"cero_t","joke":"cool"}}]' -i
```

And it shows
```
HTTP/1.1 201 Created
Server: fasthttp
Date: Sun, 01 Dec 2019 06:01:22 GMT
Content-Length: 0
```

Retrive data by Dapr State Management API
```
curl localhost:9080/v1.0/state/test-message1
```

And it shows
```json
{"name":"cero_t","joke":"cool"}
```

Store data by Java (Spring Boot) Application
```
curl -XPOST localhost:9080/v1.0/invoke/hello-dapr/method/state/m1 -d '{"name":"cero_t","joke":"cool!"}' -i
```

And it shows
```
HTTP/1.1 200 OK
Server: fasthttp
Date: Sun, 01 Dec 2019 06:03:37 GMT
Content-Type: application/json
Content-Length: 0
```

Retrive data by Java (Spring Boot) Application
```
curl localhost:9080/v1.0/invoke/hello-dapr/method/state/m1
```

And it shows
```json
{"name":"cero_t","joke":"cool!"}
```

### 3. Remote Service Call

Start dapr (if not running)
```
cd dapr-example
dapr run --app-id hello-dapr --app-port 8080 --port 9080 ./mvnw spring-boot:run
```

Start another dapr
```
cd dapr-remote
dapr run --app-id dapr-remote --app-port 8081 --port 9081 ./mvnw spring-boot:run
```

Call remote service
```
curl localhost:9081/v1.0/invoke/dapr-remote/method/remote
```

And it shows
```json
{"message":"Hello from remote"}
```

### 4. Publish and Subscribe Message

Start dapr (if not running)
```
cd dapr-example
dapr run --app-id hello-dapr --app-port 8080 --port 9080 ./mvnw spring-boot:run
```

Start another dapr (if not running)
```
cd dapr-remote
dapr run --app-id dapr-remote --app-port 8081 --port 9081 ./mvnw spring-boot:run
```

Publish message by Dapr publish command
```
dapr publish --topic A --payload '{"name":"cero_t", "message":"Publish test"}'
```

And it shows
```
✅  Event published successfully
```

And it shows in the console for Dapr 9080 process
```
== APP == A is called
== APP == {id=044254f6-dd76-42ed-9c8a-377ed6ca8cd2), source=hello-dapr, type=com.dapr.event.sent, specversion=0.3, datacontenttype=application/json, data={name=cero_t, message=Publish test}}
```

Publish message by Java Application
```
curl -XPOST localhost:9080/v1.0/invoke/dapr-remote/method/pub/B -d '{"name":"cero_t","joke":"cool!"}' -i
```

And it shows
```
HTTP/1.1 200 OK
Server: fasthttp
Date: Sun, 01 Dec 2019 06:15:06 GMT
Content-Type: application/json
Content-Length: 0
```

And it shows in the console for Dapr 9080 process
```
== APP == B is called
== APP == {id=7ab8d41a-d65d-4ff8-bec7-11ff9e481c50, source=dapr-remote, type=com.dapr.event.sent, specversion=0.3, datacontenttype=application/json, data={name=cero_t, joke=cool!}}
```

### 5. Distributed Tracing

Start zipkin
```
docker run -d -p 9411:9411 openzipkin/zipkin
```

Stop dapr and re-start with `--config` option
```
(Ctrl-c)
dapr run --app-id hello-dapr --app-port 8080 --port 9080 --config ./components/tracing.yaml ./mvnw spring-boot:run
```

Stop another dapr and re-start with `--config` option
```
(Ctrl-c)
dapr run --app-id dapr-remote --app-port 8081 --port 9081 --config ./components/tracing.yaml ./mvnw spring-boot:run
```

Call remote service
```
curl localhost:9081/v1.0/invoke/dapr-remote/method/remote
```

And it shows
```json
{"message":"Hello from remote"}
```

Access to the zipkin UI server and have fun!

http://localhost:9411/zipkin/
