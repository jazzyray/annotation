package com.ontotext.annotation.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ontotext.annotation.representation.AnnotationResult;
import com.ontotext.annotation.service.AnnotationService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.*;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationResourceTest {

    private static final AnnotationService ANNOTATION_SERVICE = mock(AnnotationService.class);

    @Rule
    public final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(new AnnotationResource(ANNOTATION_SERVICE))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();

    @Before
    public void setUp() {}

    // GET /annotations/{annotationId}
    @Test
    public void getAnnotationSucess() throws JsonProcessingException {
        URI uri = UriBuilder.fromPath("/annotations")
                .path(AnnotationService.MOCK_ANNOTATION_ID).build();

        AnnotationResult annotationResult = new AnnotationResult(uri, AnnotationService.ASYNCH_PROCESSING_STATE_COMPLETE);
        when(ANNOTATION_SERVICE.createAnnotation(any(URI.class),eq(AnnotationService.ANNOTATION_JSON))).thenReturn(annotationResult);

        final Response response = RULE.getJerseyTest().target("/annotations")
                .path(AnnotationService.MOCK_ANNOTATION_ID)
                .request(AnnotationResource.ANNOTATION_MIME_TYPE)
                .get();

        String annotationResultFromCall = response.readEntity(String.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        assertThat(annotationResultFromCall).isEqualTo(AnnotationService.ANNOTATION_JSON);
        System.out.println("Status: " + response.getStatusInfo());
        System.out.println("Response: " + annotationResultFromCall);
    }

    // POST /annotations/{annotationId}?asynch=false
    @Test
    public void createAnnotationSucess() throws JsonProcessingException {
        URI uri = UriBuilder.fromPath("/annotations")
                .path(AnnotationService.MOCK_ANNOTATION_ID)
                .queryParam("asych", "false").build();

        AnnotationResult annotationResult = new AnnotationResult(uri, AnnotationService.ASYNCH_PROCESSING_STATE_COMPLETE);
        when(ANNOTATION_SERVICE.createAnnotation(any(URI.class),eq(AnnotationService.ANNOTATION_JSON))).thenReturn(annotationResult);

        final Response response = RULE.getJerseyTest().target("/annotations")
                                    .path(AnnotationService.MOCK_ANNOTATION_ID)
                                    .queryParam("asynch", "false")
                                    .request(MediaType.APPLICATION_JSON)
                                    .post(Entity.entity(AnnotationService.ANNOTATION_JSON, AnnotationResource.ANNOTATION_MIME_TYPE));

        AnnotationResult annotationResultFromCall = response.readEntity(AnnotationResult.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        assertThat(annotationResultFromCall.getLocation()).isEqualTo(uri);
        assertThat(annotationResultFromCall.getStatus()).isEqualTo(AnnotationService.ASYNCH_PROCESSING_STATE_COMPLETE);
    }


    // PUT /annotations/{annotationId}?asynch=false
    @Test
    public void updateAnnotationSucess() throws JsonProcessingException {
        URI uri = UriBuilder.fromPath("/annotations")
                .path(AnnotationService.MOCK_ANNOTATION_ID)
                .queryParam("asych", "false").build();

        AnnotationResult annotationResult = new AnnotationResult(uri, AnnotationService.ASYNCH_PROCESSING_STATE_COMPLETE);
        when(ANNOTATION_SERVICE.createAnnotation(any(URI.class),eq(AnnotationService.ANNOTATION_JSON))).thenReturn(annotationResult);

        final Response response = RULE.getJerseyTest().target("/annotations")
                .path(AnnotationService.MOCK_ANNOTATION_ID)
                .queryParam("asynch", "false")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(AnnotationService.ANNOTATION_JSON, AnnotationResource.ANNOTATION_MIME_TYPE));

        AnnotationResult annotationResultFromCall = response.readEntity(AnnotationResult.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        assertThat(annotationResultFromCall.getLocation()).isEqualTo(uri);
        assertThat(annotationResultFromCall.getStatus()).isEqualTo(AnnotationService.ASYNCH_PROCESSING_STATE_COMPLETE);
    }

}
