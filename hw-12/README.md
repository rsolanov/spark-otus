# Развертывание проекта
- ```docker-compose build```
- ```docker-compose up -d```
- ```sbt assembly```

# Запуск spark-приложения в проекте
- ```spark-submit --class SparkDatasetAPI target/scala-2.12/SparkDatasetAPI.jar```

# Запуск spark-приложения из контейнера
- ```docker exec -it spark-scala bash```
- ```spark-submit --class SparkDatasetAPI target/scala-2.12/SparkDatasetAPI.jar```