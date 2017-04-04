package com.ontotext.annotation.service;

import com.google.common.hash.Hashing;
import com.ontotext.annotation.exception.MalFormedAnnotation;
import com.ontotext.annotation.exception.UnknownAnnotationId;
import com.ontotext.annotation.exception.UnknownContentId;
import com.ontotext.annotation.representation.AnnotationResult;
import com.ontotext.annotation.util.ResourceUtil;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AnnotationService {

    public static final String MOCK_ANNOTATION_ID = "tsn5dytas6bk";
    public static final String MOCK_CONTENT_ID = "tsk550fnfym8";

    public static final String ANNOTATION_JSON_FILENAME = "json/annotation.json";
    public static final String ANNOTATION_JSON = ResourceUtil.getResourceFileAsString(ANNOTATION_JSON_FILENAME);

    public static final String ANNOTATION_BY_ID_JSON_FILENAME = "json/annotations-by-contentId.json";
    public static final String ANNOTATION_BY_ID_JSON = ResourceUtil.getResourceFileAsString(ANNOTATION_BY_ID_JSON_FILENAME);

    public static final String ASYNCH_PROCESSING_STATE_PROCESSING = "PROCESSING";
    public static final String ASYNCH_PROCESSING_STATE_COMPLETE = "COMPLETE";

    private static final String SEP = "/";


    public String getAnnotationById(String annotationId) {
        if (annotationId.equals(MOCK_ANNOTATION_ID)) {
            return ANNOTATION_JSON;
        } else {
            throw new UnknownAnnotationId(annotationId);
        }

    }

    public AnnotationResult createAnnotation(URI uri, String annotation) {
        if (annotation.equals(ANNOTATION_JSON)) {
            return new AnnotationResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
        } else {
            throw new MalFormedAnnotation();
        }
    }

    public AnnotationResult asynchCreateAnnotation(URI uri, String annotation) {
        UriBuilder uriBuilder = UriBuilder
                .fromPath(uri.toString())
                .path(UUID.randomUUID().toString());

        return new AnnotationResult(uriBuilder.build(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

    public AnnotationResult updateAnnotation(URI uri, String annotation) {
        return new AnnotationResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }

    public AnnotationResult asynchUpdateAnnotation(URI uri, String annotation) {
        UriBuilder uriBuilder = UriBuilder
                .fromPath(uri.toString())
                .scheme("http")
                .path(UUID.randomUUID().toString());

        return new AnnotationResult(uriBuilder.build(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

    public String getAnnotationsByContentId(String contentId) {

        if (contentId.equals(MOCK_CONTENT_ID)) {
            return ANNOTATION_BY_ID_JSON;
        } else {
            throw new UnknownContentId(contentId);
        }
    }

    public AnnotationResult createAnnotations(URI uri, String contentId, String annotations){
        UUID contentUUID = UUID.fromString(contentId);

        return new AnnotationResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }

    public AnnotationResult updateAnnotations(URI uri, String contentId, String annotations){
        UUID contentUUID = UUID.fromString(contentId);

        return new AnnotationResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }


    public AnnotationResult asynchAnnotationStatus(URI uri) {
        return new AnnotationResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }

    public AnnotationResult asynchCreateAnnotations(URI uri, String contentId, String annotations){
        UriBuilder uriBuilder = UriBuilder
                .fromPath(uri.toString())
                .path(UUID.randomUUID().toString());

        return new AnnotationResult(uriBuilder.build(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

    public AnnotationResult asynchUpdateAnnotations(URI uri, String contentId, String annotations){
        UriBuilder uriBuilder = UriBuilder
                .fromPath(uri.toString())
                .scheme("http")
                .path(UUID.randomUUID().toString());

        return new AnnotationResult(uriBuilder.build(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

    public AnnotationResult asynchAnnotationsStatus(URI uri) {
        return new AnnotationResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }

}
