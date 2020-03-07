## Derived from official mysql image (our base image)
FROM mariadb

## These variables are using into deployment.yaml and/or ConfigMap and Secrets of Kubernetes
# ENV MYSQL_DATABASE shio
# ENV MYSQL_ROOT_PASSWORD DEFINE
# ENV MYSQL_USER shio
# ENV MYSQL_PASSWORD DEFINE

COPY /conf/shio.cnf /etc/mysql/conf.d/shio.cnf