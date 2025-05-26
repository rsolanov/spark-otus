# Развертывание проекта
- ```docker-compose build```
- ```docker-compose up -d```
- ```sbt assembly```

# Запуск spark-приложения в проекте
- ```spark-submit --class SparkDataFrameAPI target/scala-2.12/SparkDataFrameAPI.jar```

# Запуск spark-приложения из контейнера
- ```docker exec -it spark-scala bash```
- ```cd target\scala-3.3.6```
- ```java -cp hw-02-assembly-0.1.0-SNAPSHOT.jar Addition```