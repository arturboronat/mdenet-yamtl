# YAMTL Playground

This is an example of YAMTL Groovy model-to-model transformation service based on the [project template](https://github.com/mdenet/educationplatform-tooltemplate) for developing your own tool service for the MDENet Education Platform.

## Starting up the playground

### Start an instance of the education platform

Run instance of the education platform by starting the docker demo from [here](https://github.com/mdenet/educationplatform-docker).

### Checkout the repository 

Use either command to clone the repository.

**Via https -**
```
git config --global core.autocrlf true    # For Windows
git config --global core.autocrlf input   # For Unix/Linux/MacOS
git clone https://github.com/arturboronat/mdenet-yamtl.git
```

**Via ssh -**
```
git clone git@github.com:arturboronat/mdenet-yamtl.git
```
> Note that for ssh access you must [configure](https://docs.github.com/en/authentication/connecting-to-github-with-ssh) your account with a key.

### Build and run the docker image

This builds the docker images and starts the hello world tool service.

```
cd ./mdenet-yamtl
docker compose up --build
```
> Note it may take approximately 10 minutes the first time the tool service is built.

## Access the YAMTL Playground

In short, access: http://127.0.0.1:8080/?activities=http://127.0.0.1:8083/yamtl-cd2db-activity.json

Once the docker tool service has started the YAMTL playground tool service will be available at http://127.0.0.1:8500/ with its function endpoint being http://127.0.0.1:8500/services/RunYAMTL_m2m_groovy.

A model-to-model transformation activity is available at at http://127.0.0.1:8083/yamtl-cd2db-activity.json. 

## Stopping the example

To safely stop the example use `ctrl-c`  in the terminal running the platform.

## To add more model transformation examples

Currently this service only runs model-to-model transformations, which are out-place and from one single input model to a target output model.

For each transformation, a new activity needs to be created by following these steps:

1. Configure a new activity under the folder `./activities/` by copying there:
   - your metamodels in EMFatic notation;
   - your input model in XMI format;
   - the Groovy script corresponding to a YAMTL module;
   - the acivity json file for the transformation, see an example [here](https://github.com/arturboronat/mdenet-yamtl/blob/main/activities/yamtl-cd2db-activity.json), say `<NEW-TRAFO.json>`.
2. Access the transformation at http://127.0.0.1:8080/?activities=http://127.0.0.1:8083/`<NEW-TRAFO.json>`


## Building the tool service for development

The project implementing the transformation service is available [here](https://github.com/arturboronat/mdenet-yamtl/tree/main/services/com.mde-network.ep.toolfunctions.yamtl_m2m_function).

If you were to change the service, then update the configuration under `static.yamtlgroovy`. See the documentation of the [project template](https://github.com/mdenet/educationplatform-tooltemplate) for full details about how to configure the service.