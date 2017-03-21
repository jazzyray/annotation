Annotation API Mock and Specificationb
=

Mock Annotation API for the annotation of content with named entities


# Quick test

```
docker-compose up
```

## For swagger documentation
```
http://localhost:8089/swagger
```

## Sample Curl Requests

### Get annotation
```
curl -X GET --header 'Accept: application/ld+json' --header 'X-Request-ID: 1231' 'http://localhost:8089/annotations/21b4a142-af56-4dce-9f6c-0a6875933353'
```

### Get annotations by contentId
```
curl -X GET --header 'Accept: application/json' --header 'X-Request-ID: 1233212' 'http://localhost:8089/annotations?contentId=963db081-4200-430f-89dc-5756ca8edf04&max=10&offset=0'
```

### Create annoations 
```
curl -X POST --header 'Content-Type: application/ld+json; profile="http://www.w3.org/ns/anno.jsonld"' --header 'Accept: application/json' --header 'X-Request-ID: 1234' -d '{
  "@graph": [{
    "@id": "http://localhost:8089/annotations/21b4a142-af56-4dce-9f6c-0a6875933353",
    "type": "Annotation",
    "motivation": "tagging",
    "body": {
      "@id": "http://locahost:8089/annotations/UUID-B",
      "type": "SpecificResource",
      "purpose": "tagging",
      "source": "http://somehost/concept/UUID-X",
      "ontoa:confidence": "0.89",
      "ontoa:relevance": "0.63",
      "ontoa:tagType": "http://ontotext.com/annotation/about"
    },
    "target": {
      "@id": "http://localhost:8090/content/UUID-C",
      "type": "Text",
      "selector": {
        "type": "TextPositionSelector",
        "start": 412,
        "end": 795
      }
    }
  }],
  "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontotext.com/annoation/0.1"}],
  "@id": "http://localhost:8089/annotationgraph/UUID-D"
}' 'http://localhost:8089/annotations/{annotationId}?annotationdId=21b4a142-af56-4dce-9f6c-0a6875933353'
```

### Update annotation
``` 
curl -X PUT --header 'Content-Type: application/ld+json; profile="http://www.w3.org/ns/anno.jsonld"' --header 'Accept: application/json' --header 'X-Request-ID: 2343453333' -d '{
  "@graph": [{
    "@id": "http://localhost:8089/annotations/21b4a142-af56-4dce-9f6c-0a6875933353",
    "type": "Annotation",
    "motivation": "tagging",
    "body": {
      "@id": "http://locahost:8089/annotations/UUID-B",
      "type": "SpecificResource",
      "purpose": "tagging",
      "source": "http://somehost/concept/UUID-X",
      "ontoa:confidence": "0.89",
      "ontoa:relevance": "0.63",
      "ontoa:tagType": "http://ontotext.com/annotation/about"
    },
    "target": {
      "@id": "http://localhost:8090/content/UUID-C",
      "type": "Text",
      "selector": {
        "type": "TextPositionSelector",
        "start": 412,
        "end": 795
      }
    }
  }],
  "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontotext.com/annoation/0.1"}],
  "@id": "http://localhost:8089/annotationgraph/UUID-D"
}' 'http://localhost:8089/annotations/{annotationId}?annotationdId=21b4a142-af56-4dce-9f6c-0a6875933353'
```

### Create annotation asynchronously 
```
curl -X POST --header 'Content-Type: application/ld+json; profile="http://www.w3.org/ns/anno.jsonld"' --header 'Accept: application/json' --header 'X-Request-ID: 12344' -d '{
  "@graph": [{
    "@id": "http://localhost:8089/annotations/21b4a142-af56-4dce-9f6c-0a6875933353",
    "type": "Annotation",
    "motivation": "tagging",
    "body": {
      "@id": "http://locahost:8089/annotations/UUID-B",
      "type": "SpecificResource",
      "purpose": "tagging",
      "source": "http://somehost/concept/UUID-X",
      "ontoa:confidence": "0.89",
      "ontoa:relevance": "0.63",
      "ontoa:tagType": "http://ontotext.com/annotation/about"
    },
    "target": {
      "@id": "http://localhost:8090/content/UUID-C",
      "type": "Text",
      "selector": {
        "type": "TextPositionSelector",
        "start": 412,
        "end": 795
      }
    }
  }],
  "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontotext.com/annoation/0.1"}],
  "@id": "http://localhost:8089/annotationgraph/UUID-D"
}' 'http://localhost:8089/annotations/asynch/{annotationId}?annotationdId=21b4a142-af56-4dce-9f6c-0a6875933353'

```


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