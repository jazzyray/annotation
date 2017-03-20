package com.ontotext.annotation.service;

import com.ontotext.annotation.resource.AnnotationResource;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.UUID;

public class AnnotationService {

    public static final String MOCK_ANNOTATION_ID = "21b4a142-af56-4dce-9f6c-0a6875933353";
    public static final String MOCK_CONTENT_ID = "963db081-4200-430f-89dc-5756ca8edf04";

    private static final String ANNOTATION_JSON_FILENAME = "/annotation.json";
    private final String ANNOTATION_JSON;

    private static final String ANNOTATION_BY_ID_JSON_FILENAME = "/annotations-by-contentid.json";
    private final String ANNOTATION_BY_ID_JSON;

    public AnnotationService() {
        try {
            ANNOTATION_JSON = IOUtils.toString(AnnotationResource.class.getResourceAsStream(ANNOTATION_JSON_FILENAME), "UTF-8");
            ANNOTATION_BY_ID_JSON = IOUtils.toString(AnnotationResource.class.getResourceAsStream(ANNOTATION_BY_ID_JSON_FILENAME), "UTF-8");

        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    public String getAnnotationById(String annotationId) {
        UUID annotationUUID = UUID.fromString(annotationId);

        if (annotationId.equals(MOCK_ANNOTATION_ID)) {
            return ANNOTATION_JSON;
        } else {
            return "";
        }

    }

    public String getAnnotationsByContentId(String contentId) {
        UUID contentUUID = UUID.fromString(contentId);

        if (contentId.equals(MOCK_CONTENT_ID)) {
            return ANNOTATION_BY_ID_JSON;
        } else {
            return "";
        }
    }

}
