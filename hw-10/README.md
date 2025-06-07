# Развертывание проекта
- ```docker-compose build```
- ```docker-compose up -d```
- ```sbt assembly```

# Запуск spark-приложения в проекте
- ```spark-submit --class RDDAPI target/scala-2.12/RDDAPI.jar```

# Запуск spark-приложения из контейнера
- ```docker exec -it spark-scala bash```
- ```spark-submit --class RDDAPI target/scala-2.12/RDDAPI.jar```

# Результат работы
- ```output/part-00000```