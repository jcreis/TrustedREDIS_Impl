FROM sconecuratedimages/apps:redis-6-alpine

# ATTESTATION
ENV SCONE_CONFIG_ID="jreisScone/redis"
ENV SCONE_CAS_ADDR=4-2-1.scone-cas.cf
ENV SCONE_LAS_ADDR=51.210.0.209
ENV SCONE_VERSION=1
ENV SCONE_LOG=7

# CONFIG FILE
COPY ./redis.conf /usr/local/etc/redis/redis.conf

# TLS
#COPY tls/redis.crt /usr/local/etc/redis/redis.crt
#COPY tls/redis.key /usr/local/etc/redis/redis.key
COPY tls/redis.dh /usr/local/etc/redis/redis.dh
COPY tls/ca.crt /usr/local/etc/redis/ca.crt
COPY tls/ca.key /usr/local/etc/redis/ca.key

COPY tls/redis-cli.crt /usr/local/etc/redis/redis-cli.crt
COPY tls/redis-cli.key /usr/local/etc/redis/redis-cli.key


CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]
