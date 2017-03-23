package com.ontotext.annotation.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class AnnotationResult {

        private URI location;
        private String status;

        public AnnotationResult() {
            // Jackson deserialization
        }

        public AnnotationResult(URI location, String status) {
            this.location = location;
            this.status = status;
        }

        @JsonProperty
        public URI getLocation() {
            return location;
        }

        @JsonProperty
        public String getStatus() {
            return status;
        }

}
