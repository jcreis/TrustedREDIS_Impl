FROM redis:6.0.9

# CONFIG FILE
COPY ./redis.conf /usr/local/etc/redis/redis.conf

# TLS
COPY tls/redis.crt /usr/local/etc/redis/redis.crt
COPY tls/redis.key /usr/local/etc/redis/redis.key
COPY tls/redis.dh /usr/local/etc/redis/redis.dh
COPY tls/ca.crt /usr/local/etc/redis/ca.crt
COPY tls/ca.key /usr/local/etc/redis/ca.key

COPY tls/redis-cli.crt /usr/local/etc/redis/redis-cli.crt
COPY tls/redis-cli.key /usr/local/etc/redis/redis-cli.key


CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]
