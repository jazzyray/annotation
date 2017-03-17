Annotation API
=

Annotation API for the annotation of content with named entities


# Docker

## Build

```
docker build .
```

## Tag
### Get the image id

```
docker images
```

## Push to quay

### Login

```
docker login -e="." -u="ontotext+ontotext" -p="XXXX" quay.io
```

### tag
```
docker tag ${IMAGE} annotation

docker tag ${IMAGE} quay.io/ontotext/annotation

```

### push to quay
```
docker push quay.io/ontotext/annotation

```

## Run Interactive
```
docker run --name annotation -it annotation /bin/bash
```   

## Run Daemon
```
docker run --name annotation -d annotation
```

## Shell to docker container



### Get container ids
```
docker ps -a
```

```
docker exec -i -t ${CONTAINER_ID} /bin/bash
```



## Invoke

## Run via docker-compose

### Environment

Create a .env file with the correct environment settings

```
SOME_THING=XXX

```

### Interactive
```
docker-compose up
```

### Daemon
```
docker-compose up -d
```