package com.ontotext.annotation.service;

import com.ontotext.annotation.representation.AnnotationResult;
import com.ontotext.annotation.util.ResourceUtil;

import java.net.URI;
import java.util.UUID;

public class AnnotationService {

    public static final String MOCK_ANNOTATION_ID = "21b4a142-af56-4dce-9f6c-0a6875933353";
    public static final String MOCK_CONTENT_ID = "963db081-4200-430f-89dc-5756ca8edf04";

    public static final String ANNOTATION_JSON_FILENAME = "/annotation.json";
    public static final String ANNOTATION_JSON = ResourceUtil.getResourceFileAsString(ANNOTATION_JSON_FILENAME);

    public static final String ANNOTATION_BY_ID_JSON_FILENAME = "/annotations-by-contentid.json";
    public static final String ANNOTATION_BY_ID_JSON = ResourceUtil.getResourceFileAsString(ANNOTATION_BY_ID_JSON_FILENAME);

    public static final String ASYNCH_PROCESSING_STATE_PROCESSING = "PROCESSING";
    public static final String ASYNCH_PROCESSING_STATE_COMPLETE = "COMPLETE";

    private static final String SEP = "/";


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

    public AnnotationResult asynchAnnotation(URI uri) {
        return new AnnotationResult(uri.toString() + SEP + UUID.randomUUID().toString(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

    public AnnotationResult asynchAnnotationStatus(URI uri) {
        return new AnnotationResult(uri.toString(), ASYNCH_PROCESSING_STATE_COMPLETE);
    }

    public AnnotationResult annotation(URI uri) {
        return new AnnotationResult(uri.toString(), ASYNCH_PROCESSING_STATE_COMPLETE);
    }

}
