# iCommerce
## Prerequisites

### Methodology

* You should be familiar with Domain Driven Design (DDD) in order to understand the structure of this project.
* On the other hand, this project follows the Hexagonal Architecture style which is the best match to DDD according to my opinion.

## Requires
* [docker-compose](https://docs.docker.com/compose/install/)
### Technical concerns

* Spring framework - Java 11
* Gradle build system
* Docker
* Flyway
* PostgreSQL database
* Elasticsearch
* Kafka
## Architecture
![Please click here if it does not show](https://drive.google.com/uc?export=view&id=1or49PaXwj6iKU5HKcpksYuXLu8XrZ2yX)
![Please click here if it does not show](https://drive.google.com/uc?export=view&id=1GsO7O4JmXc3MKpcMTrWgm_aLvy4LoFT0)

- To guarantee the data integrity when exchanging the messages via the message broker, we apply the outbox pattern so that no message would be loss in case of intermittent network, service/worker failure, ... The worker that handles this duty is named outbox.

![Please click here if it does not show](https://drive.google.com/uc?export=view&id=1e_Fg9nBq2KRkypH4iCaP9Id_W8UCqzMd)
### Modules
* icommerce:
    This module performs all products/cart/order business.
* iam:
    This module performs all Identity Access Management and buyer business.
* essync:
    This module's in charge of indexing data to Elasticsearch when receive related events.
* outbox & iamoutbox:
    Read events from outbox table and publish those events to kafka, then delete published events.
* sharedkernel:
    A module to centralize shared classes.
* dbtool:
    This module's a utility to migrate postgres and elasticsearch schema/data

## Run application
* Clone sample configuration files:
```
> cp .env.example .env
```

* Install the following services on local machine (recommended to use Docker):

```
PostgreSQL 11.5
Host: localhost
Port: 5432
Database: icommerce
User: u_icommerce
Password: 123456789
```

```
PostgreSQL 11.5
Host: localhost
Port: 4432
Database: iam
User: u_iam
Password: 123456789
```


```
Elasticsearch 7.4.2
Host: localhost
Http Port: 9200
```

```
Kafka 2.3.0
Host: localhost
Zookeeper Port: 2181
Advertised Listener Port: 9092

Zookeeper 3.5.5
Host: localhost
Port: 2181
```

* For your convenience, I provide a `./docker-compose.yml` file to provision all the above services. Please run the below command to bring them to live: 
```
> docker-compose up
```

* After all infra boot up, start all necessary services as containers (may takes several minutes in the first build). 
* Note: if you run this on MacOS, you may have encountered [a known error of network](https://github.com/docker/for-mac/issues/2716 ) . Skip to [`manual`](#manual) in the end of readme.
```
> docker-compose -f docker-compose-service.yml up
```
## APIs
Import this to postman for better experience: https://www.getpostman.com/collections/6a8a0b46d078760a5ff7

### IAM Service
#### **1. Login by Facebook** GET: /v1/tokens
First, you need to login Facebook by SDK to app: 125857037951010.

Then, put return token to request body.

Save the return token for the icommerce services. 

Sample
```
curl -L -X POST 'http://localhost:9480/v1/tokens' \
-H 'accept: */*' \
-H 'Authorization: Bearer' \
-H 'Content-Type: application/json' \
--data-raw '{
    "fbAccessToken": "EAAByd2AKICIBAAFVQuOFsLODPDCZCZA6F3epZA20pY7qgc0jZA3AG2VpVgaTZAxjaxU0uZCfs9gZBIXLMc421Kh0i2Mx535fLXSOq6AVL49Qnp4IxnoxVt46JDd0kvLDkJY4Vc4jSaY5wGZAXqiU2ZAoPu1W5a4RYC2Il3Rf799y45ZC21rhgr6lFU9ptjUewmHFXpx4cLmZCHAYrjE9J0lGAKDDoSakUVrAwBYxegIwDTHzwZDZD"
}'
```

#### **2. Login by password** GET: /v1/tokens/by-password
Sample
```
curl -L -X POST 'http://localhost:9480/v1/tokens/by-password' \
-H 'accept: */*' \
-H 'Authorization: Bearer' \
-H 'Content-Type: application/json' \
--data-raw '{
    "email": "buyer@gmail.com",
    "password": "123456789"
}'

```

### ICommerce Service
#### **1. Get products:** GET: /v1/products
* **filter** by *productName*, *productPrice*, *productBrand*, *productColor*.
* **query** by *productName*. **productName** is indexed by fulltext
* **pageSize**
* **page** start at 0
* **sort** fieldName for asc, -fieldName for desc. Support multiple sort (field1,field2).

Sample

```
curl -L -X GET 'http://localhost:9380/v1/products?filter=productBrand:FAKE,productColour:RED&page=0&pageSize=10' \
-H 'accept: */*'
```
#### **2. Get product:** GET: /v1/products/:id
Sample

```
curl -L -X GET 'http://localhost:9380/v1/products/156986747' \
-H 'accept: */*'
```

#### **3. Add products to cart:** PUT: /v1/me/carts/items
Sample

```
curl -L -X PUT 'http://localhost:9380/v1/me/carts/items' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzNjAwMDk0NjYiLCJleHAiOjE2MDc1OTA0MzN9.ot83EOzleSfgw-kduqASK6DaJQeI3-96Clc1nUKcb70v2yz3COqoXA3lJVNdYbeFLF3P7w-4bHOnPl7x9uP61w' \
--data-raw '{
    "productId": 156986747,
    "quantity": 8
}'
```

#### **4. Get my cart:** GET: /v1/me/shopping-cart
Sample

```
curl -L -X GET 'http://localhost:9380/v1/me/shopping-cart' \
-H 'accept: */*' \
-H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzNjAwMDk0NjYiLCJleHAiOjE2MDc1OTA0MzN9.ot83EOzleSfgw-kduqASK6DaJQeI3-96Clc1nUKcb70v2yz3COqoXA3lJVNdYbeFLF3P7w-4bHOnPl7x9uP61w'
```

#### **5. Create order:** POST: /v1/me/orders
```
curl -L -X POST 'http://localhost:9380/v1/me/orders' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzNjAwMDk0NjYiLCJleHAiOjE2MDc1OTA0MzN9.ot83EOzleSfgw-kduqASK6DaJQeI3-96Clc1nUKcb70v2yz3COqoXA3lJVNdYbeFLF3P7w-4bHOnPl7x9uP61w' \
--data-raw '{
    "paymentMethod": "COD",
    "street": "Tòa nhà FPT Tân Thuận, Lô 29B-31B-33B Tân Thuận",
    "region": "HCM",
    "name": "Nguyễn Hiếu Thịnh",
    "phoneNumber": "0909009135"
}'
```

#### **6. Get order:** GET: /v1/me/orders
```
curl -L -X GET 'http://localhost:9380/v1/me/orders' \
-H 'accept: */*' \
-H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzNjAwMDk0NjYiLCJleHAiOjE2MDc1OTA0MzN9.ot83EOzleSfgw-kduqASK6DaJQeI3-96Clc1nUKcb70v2yz3COqoXA3lJVNdYbeFLF3P7w-4bHOnPl7x9uP61w'
```

## Test

#### Local:

* Start necessary infrastructure.
```
> docker-compose up
```

* Copy the profile env:

```shell script
cp ./local/application-local.properties ./dbtool/src/main/resources/
cp ./local/application-local.properties ./outbox/src/main/resources/
cp ./local/application-local.properties ./essync/src/main/resources/
cp ./local/application-local.properties ./icommerce/src/main/resources/
cp ./local/application-local.properties ./iam/src/main/resources/
cp ./local/application-local.properties ./iamoutbox/src/main/resources/
```

* Run with unit/integration tests:

```shell script
$ ./gradlew clean build
```

#### CI-CD (infrastructure will be create in code):
* In order to achieve isolation between test runs in CI pipeline (Jenkins), the test suite is set up to start the dependent services (MySQL, ...) in the separate Docker instances. After the build process is done (whether successful or not), the Docker instances will be stopped & removed, leaving their images in the local Docker registry. This configuration is recommended for the CI process only. For daily development tasks please use your persistent instances to get the fast feedback cycle. Should you want to have a taste of how it works on your local machine then issue the following command:

```shell script
$ ./gradlew clean build -Pit.pipelineEnv=true
```

#### Verify: 

If everything goes fine you can get the Test Coverage Report by opening the `./build/reports/jacoco/jacocoMergedReport/html/index.html` file in your favorite browser.

If you have any connection issue, wait for few seconds for other services to be started

### <a name="manual"></a>Manual start the ICommerce and Iam service
Requires: Java 11 (openjdk@1.11.0)

Recommend: use [jabba](https://github.com/shyiko/jabba) to switch jdk

* Using Gradle task:
1. Profile env
```shell script
cp ./local/application-local.properties ./dbtool/src/main/resources/
cp ./local/application-local.properties ./outbox/src/main/resources/
cp ./local/application-local.properties ./essync/src/main/resources/
cp ./local/application-local.properties ./icommerce/src/main/resources/
cp ./local/application-local.properties ./iam/src/main/resources/
cp ./local/application-local.properties ./iamoutbox/src/main/resources/
```

2. Migrate schema/data for PostgreSQL database and keep the index data in Elasticsearch synchronized with PostgreSQL:

```shell script
$ ./gradlew :dbtool:bootRun -Pargs="fC fM eC eM eI --index=*"
```
3. Start services (in multiple terminal):

```shell script
./gradlew :outbox:bootRun
./gradlew :essync:bootRun
./gradlew :icommerce:bootRun
./gradlew :iam:bootRun
./gradlew :iamoutbox:bootRun
``` 

#### Other ways:

* Using IntelliJ IDEA (recommended for daily development tasks):

Import this project as `Gradle` type & run the `./icommerce/icommerce/src/main/java/vn/icommerce/icommerce/ICommerceApp.java` class.

* Using Java command line (used after build):

```shell script
$ java -jar ./outbox/build/libs/outbox-1.0.0.jar
$ java -jar ./essync/build/libs/essync-1.0.0.jar
$ java -jar ./icommerce/build/libs/icommerce-1.0.0.jar
$ java -jar ./iam/build/libs/iam-1.0.0.jar
$ java -jar ./iamoutbox/build/libs/iamoutbox-1.0.0.jar
```





