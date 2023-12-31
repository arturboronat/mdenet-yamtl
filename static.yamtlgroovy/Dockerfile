# syntax=docker/dockerfile:1

FROM maven:3.8.5-openjdk-17-slim AS toolfunctions

# Install Git
RUN apt-get update && \
    apt-get install -y git && \
    rm -rf /var/lib/apt/lists/*

# Build core mdenet tool dependencies
RUN cd /usr/src && git clone https://github.com/yamtl/platformtools.git
RUN cd /usr/src/platformtools/services && mvn clean install -Pcore

ENV LANG C.UTF-8

#Copy yamtlgroovy toolfunction source and its dependencies
COPY services/com.mde-network.ep.toolfunctions.yamtl_m2m_function /usr/src/toolfunctions/com.mde-network.ep.toolfunctions.yamtl_m2m_function
COPY services/pom.xml /usr/src/toolfunctions/

WORKDIR /usr/src/toolfunctions

RUN mvn clean install -Pyamtl_m2m_function

# Get runtime dependencies 
# Due to https://issues.apache.org/jira/browse/MDEP-568, m-dependency-p
# is not a practical solution for ensuring all dependencies are available.
#
# We use https://github.com/qaware/go-offline-maven-plugin instead.
RUN mvn -B de.qaware.maven:go-offline-maven-plugin:1.2.8:resolve-dependencies -Pyamtl_m2m_function


WORKDIR /usr/src/toolfunctions




# FROM node:19-bullseye AS toolstaticbuild
FROM node:19-alpine AS toolstaticbuild

WORKDIR /usr/src/mdenet-tool

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY static.yamtlgroovy/package*.json ./

COPY static.yamtlgroovy .

RUN npm install; npm run build; chmod -R 755 dist/

# Use Alpine-based Nginx
FROM nginx:1.24.0-alpine AS toolservice

# Environment variables
ENV PORT=80

# Install Python, OpenJDK, Maven, tini, and netcat
RUN apk add --no-cache python3 openjdk17 maven tini netcat-openbsd

# Copy built tool and sources
COPY --from=toolfunctions /root/.m2 /root/.m2
COPY --from=toolfunctions /usr/src/toolfunctions /toolservice

# Copy files for web server
COPY static.yamtlgroovy/nginx.conf.template /etc/nginx.conf.template

# Remove default HTML and copy new HTML
RUN rm -r /usr/share/nginx/html/*
COPY --from=toolstaticbuild /usr/src/mdenet-tool/dist /usr/share/nginx/html

# Set working directory
WORKDIR /toolservice

# Copy start script and make it executable
ADD static.yamtlgroovy/start.sh /
RUN chmod +x /start.sh

# Set the entry point
ENTRYPOINT ["/sbin/tini", "--", "/start.sh"]