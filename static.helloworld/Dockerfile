# syntax=docker/dockerfile:1

FROM maven:3.8.5-openjdk-17 AS toolfunctions

# Build core mdenet tool dependencies
RUN cd /usr/src && git clone https://github.com/mdenet/platformtools.git
RUN cd /usr/src/platformtools/services && mvn clean install -Pall

#Copy helloworld toolfunction source and its dependencies
COPY services/com.mde-network.ep.toolfunctions.helloworld /usr/src/toolfunctions/com.mde-network.ep.toolfunctions.helloworld
COPY services/com.mde-network.ep.toolfunctions.helloworldfunction /usr/src/toolfunctions/com.mde-network.ep.toolfunctions.helloworldfunction
COPY services/pom.xml /usr/src/toolfunctions/

WORKDIR /usr/src/toolfunctions

RUN mvn clean install -Phelloworld

# Get runtime dependencies 
RUN mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.0:get -Dartifact=com.google.cloud.functions:function-maven-plugin:0.9.5 && mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.0:get -Dartifact=org.apache.maven.plugins:maven-deploy-plugin:2.7 

WORKDIR /usr/src/toolfunctions




FROM node:19-bullseye AS toolstaticbuild

WORKDIR /usr/src/mdenet-tool

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY static.helloworld/package*.json ./

COPY static.helloworld .

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
COPY static.helloworld/nginx.conf.template /etc/nginx.conf.template

RUN rm -r /usr/share/nginx/html/*
COPY --from=toolstaticbuild /usr/src/mdenet-tool/dist /usr/share/nginx/html

WORKDIR /toolservice


# Due to https://issues.apache.org/jira/browse/MDEP-568, m-dependency-p
# is not a practical solution for ensuring all dependencies are available.
#
# We use https://github.com/qaware/go-offline-maven-plugin instead.
#RUN mvn -B de.qaware.maven:go-offline-maven-plugin:1.2.8:resolve-dependencies

# Copy start script and make it executable
ADD static.helloworld/start.sh /
RUN chmod +x /start.sh

ENTRYPOINT ["/usr/bin/tini", "--", "/start.sh"]
