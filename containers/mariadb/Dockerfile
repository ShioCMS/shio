## Derived from official mysql image (our base image)
FROM mariadb

## These variables are using into deployment.yaml and/or ConfigMap and Secrets of Kubernetes
# ENV MYSQL_DATABASE shiohara
# ENV MYSQL_ROOT_PASSWORD DEFINE
# ENV MYSQL_USER shiohara
# ENV MYSQL_PASSWORD DEFINE

COPY /conf/shiohara.cnf /etc/mysql/conf.d/shiohara.cnf