package com.ontotext.annotation.resource;

import com.codahale.metrics.annotation.Timed;
import com.ontotext.annotation.representation.AnnotationAysnchResult;
import com.ontotext.annotation.representation.AnnotationResult;
import com.ontotext.annotation.service.AnnotationService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.UUID;

@Path("/annotations")
@Produces(MediaType.APPLICATION_JSON)
public class AnnotationResource {

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
    public Response status(@PathParam("annotationId") String annotationId) {
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
