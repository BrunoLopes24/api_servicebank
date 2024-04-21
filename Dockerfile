FROM eclipse-temurin:21-jdk

RUN apt-get update && \
apt-get upgrade -y && \
apt-get update && \
apt-get install -y maven && \
groupadd -g 1000 appuser &&  \
useradd -r -u 1000 -g appuser appuser

WORKDIR /home/appuser

COPY --chown=appuser:appuser . /home/appuser

RUN chown -R appuser:appuser /home/appuser

EXPOSE 8080

USER appuser

CMD ["mvn", "spring-boot:run"]