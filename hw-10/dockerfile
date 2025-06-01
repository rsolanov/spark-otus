FROM openjdk:11

RUN apt-get update && apt-get install -y curl gnupg2 bash && rm -rf /var/lib/apt/lists/*

RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add - && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" > /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" >> /etc/apt/sources.list.d/sbt.list

RUN apt-get update && apt-get install -y sbt

ENV SPARK_VERSION=3.5.0
ENV HADOOP_VERSION=3

RUN curl -L -o spark.tgz https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz && \
    tar xzf spark.tgz -C /opt && rm spark.tgz

RUN ln -s /opt/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} /opt/spark

ENV PATH="/opt/spark/bin:/opt/spark/sbin:/usr/share/sbt/bin:${PATH}"