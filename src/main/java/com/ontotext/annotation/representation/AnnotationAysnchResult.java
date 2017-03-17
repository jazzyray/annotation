package com.ontotext.annotation.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnotationAysnchResult {

        private String location;
        private String status;

        public AnnotationAysnchResult() {
            // Jackson deserialization
        }

        public AnnotationAysnchResult(String location, String status) {
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
