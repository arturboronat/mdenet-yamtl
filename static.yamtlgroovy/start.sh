#!/bin/bash

set -e

########################
# Wait for service to be ready
# Globals: none
# Arguments: name host port
# Returns: none
#########################
wait_for_service() {
    local service_name="$1"
    local service_host="$2"
    local service_port="$3"
    local service_address=$(getent hosts "$service_host" | awk '{ print $1 }')
    counter=0
    echo "Connecting to $service_name at $service_address"
    while ! nc -z "$service_address" "$service_port" >/dev/null; do
        counter=$((counter+1))
        if [ $counter == 30 ]; then
            echo "Error: Couldn't connect to $service_name at $service_address."
            exit 1
        fi
        echo "Trying to connect to $service_name at $service_address. Attempt $counter."
        sleep 5
    done
}


# Functions for running yamtlgroovy 
cd com.mde-network.ep.toolfunctions.yamtl_m2m_function
mvn -B -o function:run -Drun.functionTarget=com.mdenetnetwork.ep.toolfunctions.yamtl_m2m_function.RunYAMTL_m2m_groovy -Drun.port=8001 &
wait_for_service validate 127.0.0.1 8001
cd ..


# nginx as frontend + reverse proxy
envsubst < /etc/nginx.conf.template > /etc/nginx/conf.d/default.conf
nginx -g "daemon off;" &

# wait for them all
wait -n
