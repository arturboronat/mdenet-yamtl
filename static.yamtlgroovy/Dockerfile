# syntax=docker/dockerfile:1

FROM maven:3.8.5-openjdk-17 AS toolfunctions

# Build core mdenet tool dependencies
RUN cd /usr/src && git clone https://github.com/mdenet/platformtools.git
RUN cd /usr/src/platformtools/services && mvn clean install -Pall

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




FROM node:19-bullseye AS toolstaticbuild

WORKDIR /usr/src/mdenet-tool

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY static.yamtlgroovy/package*.json ./

COPY static.yamtlgroovy .

RUN npm install; npm run build; chmod -R 755 dist/



FROM nginx:1.24.0-bullseye AS toolservice

# Needed to avoid prompts blocking the build process
ENV DEBIAN_FRONTEND=noninteractive

# Needed for Cloud Build
ENV PORT=80

# Install Python
RUN apt-get update \
    && apt-get install -y python3-minimal openjdk-17-jdk maven tini netcat \
    && rm -rf /var/lib/apt/lists/*

# Copy built tool and sources
COPY --from=toolfunctions /root/.m2 /root/.m2
COPY --from=toolfunctions /usr/src/toolfunctions /toolservice

# Copy files for webserver
COPY static.yamtlgroovy/nginx.conf.template /etc/nginx.conf.template

RUN rm -r /usr/share/nginx/html/*
COPY --from=toolstaticbuild /usr/src/mdenet-tool/dist /usr/share/nginx/html

WORKDIR /toolservice


# Copy start script and make it executable
ADD static.yamtlgroovy/start.sh /
RUN chmod +x /start.sh

ENTRYPOINT ["/usr/bin/tini", "--", "/start.sh"]
