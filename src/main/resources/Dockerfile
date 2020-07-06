FROM mcr.microsoft.com/java/jdk:8u192-zulu-ubuntu

RUN mkdir /opt/stackoverflow

ENV LC_ALL=C.UTF-8

COPY . /opt/stackoverflow/

RUN chmod +x /opt/stackoverflow/bin/start.sh

EXPOSE 8080

ENTRYPOINT [ "/opt/stackoverflow/bin/start.sh" ]
