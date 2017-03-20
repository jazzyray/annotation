package com.ontotext.annotation.resource;

import com.codahale.metrics.annotation.Timed;
import com.ontotext.annotation.representation.AnnotationAysnchResult;
import com.ontotext.annotation.representation.AnnotationResult;
import com.ontotext.annotation.service.AnnotationService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.UUID;

import static com.ontotext.annotation.resource.AnnotationResource.ANNOTATION_MIME_TYPE;

@Api("Annotation API")
@Path("/annotations")
@Produces({ANNOTATION_MIME_TYPE})
public class AnnotationResource {

    public static final String ANNOTATION_MIME_TYPE = "application/ld+json; profile=\"http://www.w3.org/ns/anno.jsonld\"";

    AnnotationService annotationService;

    public AnnotationResource(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @POST
    @Timed
    public Response annotateAsynch(@Context UriInfo uriInfo) {
        UUID uuid = UUID.randomUUID();

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(uuid.toString());
        URI locationURI = builder.build();

        AnnotationAysnchResult annotationResult = new AnnotationAysnchResult(locationURI.toString(), "PROCESSING");
        return Response.accepted(locationURI).entity(annotationResult).header("Location",locationURI.getPath()).build();
    }

    @GET
    @Timed
    @Path("/{annotationId}")
    @Produces(ANNOTATION_MIME_TYPE)
    @ApiOperation(value = "Finds Annotation by id",
            notes = "A single annotation can be returned")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotation Id supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Annotation not found"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success", responseHeaders = @ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class)) })
    public Response annotation(@ApiParam(value = "Annotation Id to be retrieved. Must be a valid UUID", required = true) @PathParam("annotationId") String annotationId) {
        AnnotationResult annotationResult = new AnnotationResult();
        return Response.ok().entity("{\n" +
                "  \"@context\": \"http://www.w3.org/ns/anno.jsonld\",\n" +
                "  \"id\": \"http://example.org/annotations/anno1\",\n" +
                "  \"type\": \"Annotation\",\n" +
                "  \"created\": \"2015-01-31T12:03:45Z\",\n" +
                "  \"body\": {\n" +
                "    \"type\": \"TextualBody\",\n" +
                "    \"value\": \"I like this page!\"\n" +
                "  },\n" +
                "  \"target\": \"http://www.example.com/index.html\"\n" +
                "}").build();
    }



}
