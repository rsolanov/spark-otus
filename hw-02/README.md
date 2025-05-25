# Развертывание проекта
- ```docker-compose build```
- ```docker-compose up -d```
- ```sbt assembly```

# Запуск jar-файла в проекте
- ```cd target\scala-3.3.6```
- ```java -cp hw-02-assembly-0.1.0-SNAPSHOT.jar Addition```

# Запуск jar-файла из контейнера
- ```docker exec -it spark-scala bash```
- ```cd target\scala-3.3.6```
- ```java -cp hw-02-assembly-0.1.0-SNAPSHOT.jar Addition```