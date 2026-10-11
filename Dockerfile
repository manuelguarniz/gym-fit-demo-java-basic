FROM mysql:8.4
COPY sql/gymfit.sql               /docker-entrypoint-initdb.d/01-gymfit.sql
COPY sql/02-docker-app-user.sql   /docker-entrypoint-initdb.d/02-docker-app-user.sql