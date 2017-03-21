package com.ontotext.annotation.resource;

import com.codahale.metrics.annotation.Timed;
import com.ontotext.annotation.representation.AnnotationResult;

import com.ontotext.annotation.service.AnnotationService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.*;
import java.net.HttpURLConnection;

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
    public Response annotation(@ApiParam(value = "Annotation Id to be retrieved. Must be a valid UUID", required = true, defaultValue = MOCK_ANNOTATION_ID) @PathParam("annotationId") String annotationId,
                               @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId) {
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
    @Timed
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
                                @ApiParam(value = "The page to be returned", required = false, defaultValue = "0") @QueryParam("offset") Integer offset,
                                @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId) {
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
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Create Annotations by contentId")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Content Id supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class),
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)}) })
    public Response createAnnotations(@ApiParam(name = "body", value = "Annotations to create", required = true ) String annotations,
                                      @ApiParam(value = "Target content Id", required = false, defaultValue = MOCK_CONTENT_ID) @QueryParam("contentId") String contentId,
                                      @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                      @Context UriInfo uriInfo) {

        AnnotationResult annotationResult = this.annotationService.annotations(uriInfo.getBaseUriBuilder().path("annoations/" + contentId).build(), contentId, annotations);

        return Response.ok(annotationResult.getLocation()).entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update Annotations by contentId")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Content Id supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class),
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)}) })
    public Response updateAnnotations(@ApiParam(name = "body", value = "Annotations to create", required = true ) String annotations,
                                      @ApiParam(value = "Target content Id", required = false, defaultValue = MOCK_CONTENT_ID) @QueryParam("contentId") String contentId,
                                      @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                      @Context UriInfo uriInfo) {

        AnnotationResult annotationResult = this.annotationService.annotations(uriInfo.getBaseUriBuilder().path("annoations/" + contentId).build(), contentId, annotations);

        return Response.ok(annotationResult.getLocation()).entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({ANNOTATION_MIME_TYPE})
    @ApiOperation(value = "Asynchronous publication/creation of annotations")
    @Timed
    @Path("/asynch/{annotationId}")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotations supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Processing",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}) })
    public Response createAnnotationAsynch(@ApiParam(name = "body", value = "Annotation to create", required = true ) String annotation,
                                           @ApiParam(name = "annotationdId", value = "Annotation id uuid to create", required = true ) @QueryParam("annotationdId") String annotationId,
                                           @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {
       AnnotationResult annotationResult = this.annotationService.asynchAnnotation(uriInfo.getBaseUriBuilder().path("annoations/aysch/").build(), annotation);

        return Response.accepted(annotationResult.getLocation()).entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }

    @GET
    @Path("/asynch/{processId}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Asynchronous publication/creation annotation status")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid ProcessId supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Ok",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}) })
    public Response statusAnnotationAsynch(@ApiParam(value = "Asynchronous Transaction IUd", required = true) @QueryParam("processId") String processId,
                                           @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {
        AnnotationResult annotationResult = this.annotationService.asynchAnnotationStatus(uriInfo.getBaseUriBuilder().path("annoations/aysch/" + processId).build());

        return Response.ok().entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({ANNOTATION_MIME_TYPE})
    @ApiOperation(value = "Publication/creation of annotation by If")
    @Timed
    @Path("/{annotationId}")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotations supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Processing",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}) })
    public Response createAnnotation(@ApiParam(name = "body", value = "Annotation to create", required = true ) String annotation,
                                           @ApiParam(name = "annotationdId", value = "Annotation id uuid to create", required = true ) @QueryParam("annotationdId") String annotationId,
                                           @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {
        AnnotationResult annotationResult = this.annotationService.annotation(uriInfo.getBaseUriBuilder().path("annoations/" + annotationId).build(), annotation);

        return Response.ok(annotationResult.getLocation()).entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({ANNOTATION_MIME_TYPE})
    @ApiOperation(value = "Publication/update of an annotation by Id")
    @Timed
    @Path("/{annotationId}")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotations supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Processing",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}) })
    public Response updateAnnotation(@ApiParam(name = "body", value = "Annotation to create", required = true ) String annotation,
                                     @ApiParam(name = "annotationdId", value = "Annotation id uuid to create", required = true ) @QueryParam("annotationdId") String annotationId,
                                     @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                     @Context UriInfo uriInfo) {
        AnnotationResult annotationResult = this.annotationService.annotation(uriInfo.getBaseUriBuilder().path("annoations/" + annotationId).build(), annotation);

        return Response.ok(annotationResult.getLocation()).entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }

}
