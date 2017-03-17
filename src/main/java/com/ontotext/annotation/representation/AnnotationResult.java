package com.ontotext.annotation.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnotationResult {

        private String location;
        private String status;

        public AnnotationResult() {
            // Jackson deserialization
        }

        public AnnotationResult(String location, String status) {
            this.location = location;
            this.status = status;
        }

        @JsonProperty
        public String getLocation() {
            return location;
        }

        @JsonProperty
        public String getStatus() {
            return status;
        }

}
