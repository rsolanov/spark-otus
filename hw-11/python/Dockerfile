FROM openjdk:11-slim

# Установка зависимостей
RUN apt-get update && apt-get install -y \
    wget curl python3 python3-pip && \
    rm -rf /var/lib/apt/lists/*

# Установка Spark
ENV SPARK_VERSION=3.5.1
ENV HADOOP_VERSION=3
ENV SPARK_HOME=/opt/spark
ENV PATH=$SPARK_HOME/bin:$PATH

RUN wget https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz && \
    tar -xzf spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz && \
    mv spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} ${SPARK_HOME} && \
    rm spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz

# Установка PySpark и Pandas (на случай диагностики)
RUN pip3 install pyspark==${SPARK_VERSION} pandas

# Создание рабочей директории
WORKDIR /app
COPY main.py .
COPY countries.json .

CMD ["python3", "main.py"]
