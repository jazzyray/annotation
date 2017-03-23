Annotation API Mock and Specification
=

Mock Annotation API for the annotation of content with named entities


# Documented on Apiary
http://docs.annotation2.apiary.io/#

# Quick test

```
docker-compose up -d
```

## For swagger documentation
```
http://localhost:8089/swagger
```

## Sample Curl Requests

### Get annotation
```
curl -X GET --header 'Accept: application/ld+json' --header 'X-Request-ID: 62bf23fe-0fbe-11e7-93ae-92361f002671' 'http://localhost:8089/annotations/21b4a142-af56-4dce-9f6c-0a6875933353'
```

### Create annoation
```
curl -X POST --header 'Content-Type: application/ld+json; profile="http://www.w3.org/ns/anno.jsonld"' --header 'Accept: application/json' --header 'X-Request-ID: 234234' -d '{
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
      "ontoa:tagType": "http://ontotext.com/annotation/about",
      "ontoa:type": "concept",
      "ontoa:status": "suggested"
    },
    "target": {
      "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04",
      "type": "Text",
      "selector": {
        "type": "TextPositionSelector",
        "start": 412,
        "end": 795
      }
    }
  }],
  "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontotext.com/annoation/0.1"}],
  "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04"
}' 'http://localhost:8089/annotations/21b4a142-af56-4dce-9f6c-0a6875933353?asynch=false'
```

### Create annotation asynchronously
```
{
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
      "ontoa:tagType": "http://ontotext.com/annotation/about",
      "ontoa:type": "concept",
      "ontoa:status": "suggested"
    },
    "target": {
      "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04",
      "type": "Text",
      "selector": {
        "type": "TextPositionSelector",
        "start": 412,
        "end": 795
      }
    }
  }],
  "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontotext.com/annoation/0.1"}],
  "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04"
}
```

### Get annoation creation asynchronous status
```
curl -X GET --header 'Accept: application/json' --header 'X-Request-ID: 123123' 'http://localhost:8089/annotations/21b4a142-af56-4dce-9f6c-0a6875933353/status/970e5044-1821-4dc0-8aae-a28e971a0531'

```

### Get annotations by contentId
```
curl -X GET --header 'Accept: application/json' --header 'X-Request-ID: 234234' 'http://localhost:8089/annotations?contentId=963db081-4200-430f-89dc-5756ca8edf04&max=10&offset=0'
```

### Create annotations by contentId
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-Request-ID: 123123' -d '{
  "items": [{
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
        "ontoa:tagType": "http://ontotext.com/annotation/about",
        "ontoa:type": "concept",
        "ontoa:status": "suggested"
      },
      "target": {
        "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04",
        "type": "Text",
        "selector": {
          "type": "TextPositionSelector",
          "start": 412,
          "end": 795
        }
      }
    }],
    "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontotext.com/annoation/0.1"}],
    "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04"
  }]
}' 'http://localhost:8089/annotations?contentId=963db081-4200-430f-89dc-5756ca8edf04&asynch=false'
```

### Create annotations asynchronously 
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-Request-ID: 123123' -d '{
  "items": [{
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
        "ontoa:tagType": "http://ontotext.com/annotation/about",
        "ontoa:type": "concept",
        "ontoa:status": "suggested"
      },
      "target": {
        "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04",
        "type": "Text",
        "selector": {
          "type": "TextPositionSelector",
          "start": 412,
          "end": 795
        }
      }
    }],
    "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontotext.com/annoation/0.1"}],
    "@id": "http://localhost:8090/content/963db081-4200-430f-89dc-5756ca8edf04"
  }]
}' 'http://localhost:8089/annotations?contentId=963db081-4200-430f-89dc-5756ca8edf04&asynch=true'
```

### Get aysnch status
```
curl -X GET --header 'Accept: application/json' --header 'X-Request-ID: 123123' 'http://localhost:8089/annotations/status/feda3dc4-0111-4fc5-8c1a-be2ccb7a13f6'

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