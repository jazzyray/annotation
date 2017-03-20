package com.ontotext.annotation.resource;

import com.codahale.metrics.annotation.Timed;
import com.ontotext.annotation.representation.AnnotationAysnchResult;

import com.ontotext.annotation.service.AnnotationService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.UUID;

import static com.ontotext.annotation.resource.AnnotationResource.ANNOTATION_MIME_TYPE;
import static com.ontotext.annotation.service.AnnotationService.MOCK_ANNOTATION_ID;
import static com.ontotext.annotation.service.AnnotationService.MOCK_CONTENT_ID;


@Api("Annotation API")
@Path("/annotations")
@Produces({ANNOTATION_MIME_TYPE})
public class AnnotationResource {

    public static final String ANNOTATION_MIME_TYPE = "application/ld+json; profile=\"http://www.w3.org/ns/anno.jsonld\"";

    AnnotationService annotationService;

    public AnnotationResource(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @GET
    @Timed
    @Path("/{annotationId}")
    @Produces(ANNOTATION_MIME_TYPE)
    @ApiOperation(value = "Finds Annotation by id")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotation Id supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Annotation not found"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class),
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)}) })
    public Response annotation(@ApiParam(value = "Annotation Id to be retrieved. Must be a valid UUID", required = true, defaultValue = MOCK_ANNOTATION_ID) @PathParam("annotationId") String annotationId) {

        AnnotationService annotationService = new AnnotationService();

        String result = "";
        try {
            result = annotationService.getAnnotationById(annotationId);
        } catch (IllegalArgumentException ia) {
            Response.status(HttpURLConnection.HTTP_BAD_REQUEST).build();
        }
        if (!result.equals("")) {
            return Response.ok().entity(result)
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.GET)
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .header(HttpHeaders.LINK, "<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"")
                    .build();
        } else {
          return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Finds Annotations, by contentId if required, pageable with offsets")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Content Id supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class),
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)}) })
    public Response annotations(@ApiParam(value = "Target content Id", required = false, defaultValue = MOCK_CONTENT_ID) @QueryParam("contentId") String contentId,
                                @ApiParam(value = "The number of records to display per page", required = false, defaultValue = "10") @QueryParam("max") Integer max,
                                @ApiParam(value = "The page to be returned", required = false, defaultValue = "0") @QueryParam("offset") Integer offset) {


        String result = "";
        try {
            result = annotationService.getAnnotationsByContentId(contentId);
        } catch (IllegalArgumentException ia) {
            Response.status(HttpURLConnection.HTTP_BAD_REQUEST).build();
        }

        if (!result.equals("")) {
            return Response.ok().entity(result)
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.GET)
                .build();
        } else {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).build();
        }
    }


    @POST
    @Timed
    @Path("/asynch")
    public Response createAnnotationAsynch(@Context UriInfo uriInfo) {
        UUID uuid = UUID.randomUUID();

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(uuid.toString());
        URI locationURI = builder.build();

        AnnotationAysnchResult annotationResult = new AnnotationAysnchResult(locationURI.toString(), "PROCESSING");
        return Response.accepted(locationURI).entity(annotationResult).header("Location",locationURI.getPath()).build();
    }

    @POST
    @Timed
    public Response createAnnotation(@Context UriInfo uriInfo) {

        return Response.accepted().build();
    }

    @POST
    @Timed
    @Path("/{annotationId}")
    public Response updateAnnotation(@Context UriInfo uriInfo) {

        return Response.accepted().build();
    }

}
