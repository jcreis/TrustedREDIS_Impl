FROM sconecuratedimages/apps:openjdk-8-alpine

WORKDIR /home/project

COPY ./target/proxyTRedis-1.0-SNAPSHOT.jar /home/project/proxyTRedis-1.0-SNAPSHOT.jar
COPY ./proxySSL/proxyServerKeyStore.p12 /home/project/proxySSL/proxyServerKeyStore.p12
COPY ./proxySSL/proxyRedisKeyStore.p12 /home/project/proxySSL/proxyRedisKeyStore.p12
COPY ./proxySSL/proxy_redis_truststore /home/project/proxySSL/proxy_redis_truststore
COPY ./proxySSL/keycloak_truststore.p12 /home/project/proxySSL/keycloak_truststore.p12

CMD SCONE_VERSION=1 java -jar /home/project/proxyTRedis-1.0-SNAPSHOT.jar


