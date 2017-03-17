package com.ontotext.annotation.resource;

import com.codahale.metrics.annotation.Timed;
import com.ontotext.annotation.representation.AnnotationResult;
import com.ontotext.annotation.service.AnnotationService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.UUID;

@Path("/annotation")
@Produces(MediaType.APPLICATION_JSON)
public class AnnotationResource {

    AnnotationService annotationService;

    public AnnotationResource(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @POST
    @Timed
    public Response annotate(@Context UriInfo uriInfo) {
        UUID uuid = UUID.randomUUID();

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(uuid.toString());
        URI locationURI = builder.build();

        AnnotationResult annotationResult = new AnnotationResult(locationURI.toString(), "PROCESSING");
        return Response.accepted(locationURI).entity(annotationResult).header("Location",locationURI.getPath()).build();
    }
}
