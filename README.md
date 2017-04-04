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
curl -X GET --header 'Accept: application/ld+json' --header 'X-Request-ID: 62bf23fe-0fbe-11e7-93ae-92361f002671' 'http://localhost:9101/annotations/tsn5dytas6bk'

```

### Create annoation
```
curl -X POST --header 'Content-Type: application/ld+json; profile="http://www.w3.org/ns/anno.jsonld"' --header 'Accept: application/json' --header 'X-Request-ID: 234234' -d '{
  "@graph": [{
    "@id": "http://data.ontotext.com/annotation/tsn5dytas6bk",
    "type": "Annotation",
    "motivation": "tagging",
    "body": {
      "@id": "http:/data.ontotext.com/annotation/body/tsk9k4aunvgg",
      "type": "SpecificResource",
      "purpose": "tagging",
      "source": "http://ontology.ontotext.com/resource/tslsht48nsw0",
      "ontoa:hasFeatures": {
        "@id": "http:/data.ontotext.com/annotation/features/tsk9k4aunvg2",
        "ontoa:class": { "@id": "http://ontology.ontotext.com/taxonomy/Keyphrase" },
        "ontoa:confidence": "0.89",
        "ontoa:relevanceScore": "0.63",
        "ontoa:tagType": "http://ontotext.com/annotation/about",
        "ontoa:type": "concept",
        "ontoa:status": "suggested"
      }
    },
    "target": {
      "@id": "http://data.ontotext.com/content/tsk550fnfym8",
      "type": "Text",
      "selector": {
        "type": "TextPositionSelector",
        "start": 412,
        "end": 795
      }
    }
  }],
  "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontology.ontotext.com/annotation#"}],
  "@id": "http://data.ontotext.com/content/tsk550fnfym8"
}' 'http://localhost:9101/annotations/tsn5dytas6bk?asynch=false'
```

### Create annotation asynchronously
```
curl -X POST --header 'Content-Type: application/ld+json; profile="http://www.w3.org/ns/anno.jsonld"' --header 'Accept: application/json' --header 'X-Request-ID: 234234' -d '{
  "@graph": [{
      "@id": "http://data.ontotext.com/annotation/tsn5dytas6bk",
      "type": "Annotation",
      "motivation": "tagging",
      "body": {
        "@id": "http:/data.ontotext.com/annotation/body/tsk9k4aunvgg",
        "type": "SpecificResource",
        "purpose": "tagging",
        "source": "http://ontology.ontotext.com/resource/tslsht48nsw0",
        "ontoa:hasFeatures": {
          "@id": "http:/data.ontotext.com/annotation/features/tsk9k4aunvg2",
          "ontoa:class": { "@id": "http://ontology.ontotext.com/taxonomy/Keyphrase" },
          "ontoa:confidence": "0.89",
          "ontoa:relevanceScore": "0.63",
          "ontoa:tagType": "http://ontotext.com/annotation/about",
          "ontoa:type": "concept",
          "ontoa:status": "suggested"
        }
      },
      "target": {
        "@id": "http://data.ontotext.com/content/tsk550fnfym8",
        "type": "Text",
        "selector": {
          "type": "TextPositionSelector",
          "start": 412,
          "end": 795
        }
      }
    }],
    "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontology.ontotext.com/annotation#"}],
    "@id": "http://data.ontotext.com/content/tsk550fnfym8"
  }' 'http://localhost:9101/annotations/tsn5dytas6bk?asynch=true'
```

### Get annoation creation asynchronous status
```
curl -X GET --header 'Accept: application/json' --header 'X-Request-ID: 2342342' 'http://localhost:9101/annotations/tsn5dytas6bk/status/13f8f147-7cd9-48f9-8054-48098a730e9a'
```

### Get annotations by contentId
```
curl -X GET --header 'Accept: application/json' --header 'X-Request-ID: 65464' 'http://localhost:9101/annotations?contentId=tsk550fnfym8&max=10&offset=0'
```

### Create annotations by contentId
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-Request-ID: 45645345' -d '{
  "items": [{
    "@graph": [{
      "@id": "http://data.ontotext.com/annotation/tsn5dytas6bk",
      "type": "Annotation",
      "motivation": "tagging",
      "body": {
        "@id": "http:/data.ontotext.com/annotation/body/tsk9k4aunvgg",
        "type": "SpecificResource",
        "purpose": "tagging",
        "source": "http://ontology.ontotext.com/resource/tslsht48nsw0",
        "ontoa:hasFeatures": {
          "@id": "http:/data.ontotext.com/annotation/features/tsk9k4aunvg2",
          "ontoa:class": { "@id": "http://ontology.ontotext.com/taxonomy/Keyphrase" },
          "ontoa:confidence": "0.89",
          "ontoa:relevanceScore": "0.63",
          "ontoa:tagType": "http://ontotext.com/annotation/about",
          "ontoa:type": "concept",
          "ontoa:status": "suggested"
        }
      },
      "target": {
        "@id": "http://data.ontotext.com/content/tsk550fnfym8",
        "type": "Text",
        "selector": {
          "type": "TextPositionSelector",
          "start": 412,
          "end": 795
        }
      }
    }],
    "@context": ["http://www.w3.org/ns/anno.jsonld", {"ontoa": "http://ontology.ontotext.com/annotation#"}],
    "@id": "http://data.ontotext.com/content/tsk550fnfym8"
  }]
}' 'http://localhost:9101/annotations?contentId=tsk550fnfym8&asynch=false'
```

### Create annotations asynchronously 
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-Request-ID: 456453454' -d '{
  "items": [{
    "@graph": [{
      "@id": "http://data.ontotext.com/annotation/tsn5dytas6bk",
      "type": "Annotation",
      "motivation": "tagging",
      "body": {
        "@id": "http:/data.ontotext.com/annotation/body/tsk9k4aunvgg",
        "type": "SpecificResource",
        "purpose": "tagging",
        "source": "http://ontology.ontotext.com/resource/tslsht48nsw0",
        "ontoa:hasFeatures": {
          "@id": "http:/data.ontotext.com/annotation/features/tsk9k4aunvg2",
          "ontoa:class": { "@id": "http://ontology.ontotext.com/taxonomy/Keyphrase" },
          "ontoa:confidence": "0.89",
          "ontoa:relevanceScore": "0.63",
          "ontoa:tagType": "http://ontotext.com/annotation/about",
          "ontoa:type": "concept",
          "ontoa:status": "suggested"
        }
      },
      "target": {
        "@id": "http://data.ontotext.com/content/tsk550fnfym8",
        "type": "Text",
        "selector": {
          "type": "TextPositionSelector",
          "start": 412,
          "end": 795
        }
      }
' 'http://localhost:9101/annotations?contentId=tsk550fnfym8&asynch=true'
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