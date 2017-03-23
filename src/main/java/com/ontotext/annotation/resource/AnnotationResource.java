package com.ontotext.annotation.resource;

import com.codahale.metrics.annotation.Timed;
import com.ontotext.annotation.exception.MalFormedAnnotation;
import com.ontotext.annotation.representation.AnnotationResult;

import com.ontotext.annotation.service.AnnotationService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.*;
import java.net.HttpURLConnection;
import java.net.URI;

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

    // GET /annotations/{annotationId}
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
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Processing",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class),
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)})})
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

    // POST /annotations/{annotationId}?asynch=true|false
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({ANNOTATION_MIME_TYPE})
    @ApiOperation(value = "Publication/creation of annotation by If")
    @Timed
    @Path("/{annotationId}")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotations supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Processing",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)})})
    public Response createAnnotation(@ApiParam(name = "body", value = "Annotation to create", required = true ) String annotation,
                                     @ApiParam(value = "Annotation Id to be retrieved. Must be a valid UUID", required = true, defaultValue = MOCK_ANNOTATION_ID) @PathParam("annotationId") String annotationId,
                                     @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                     @ApiParam(value = "Asynchronous", required = true, defaultValue = "false") @QueryParam("asynch") Boolean asynch,
                                     @Context UriInfo uriInfo) {

        if (annotationId.equals(MOCK_ANNOTATION_ID)) {
            try {
                if (!asynch) {
                    URI syncLnk = uriInfo.getBaseUriBuilder().path("annotations").path(annotationId.toString()).build();
                    AnnotationResult annotationResult = this.annotationService.createAnnotation(syncLnk, annotation);
                    return Response.ok(annotationResult.getLocation()).entity(annotationResult)
                            .header("Location", annotationResult.getLocation())
                            .header(HttpHeaders.VARY, "Accept")
                            .header(HttpHeaders.ETAG, "_87e52ce126126")
                            .header(HttpHeaders.ALLOW, HttpMethod.POST)
                            .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                            .build();
                } else {
                    URI asyncLnk = uriInfo.getBaseUriBuilder().path("annotations").path("status").build();
                    AnnotationResult annotationResult = this.annotationService.asynchCreateAnnotation(asyncLnk, annotation);
                    return Response.accepted(annotationResult.getLocation()).entity(annotationResult)
                            .header("Location", annotationResult.getLocation())
                            .header(HttpHeaders.VARY, "Accept")
                            .header(HttpHeaders.ETAG, "_87e52ce126126")
                            .header(HttpHeaders.ALLOW, HttpMethod.POST)
                            .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                            .build();
                }
            } catch (MalFormedAnnotation mfa) {
                URI link = uriInfo.getBaseUriBuilder().path("annotations").path(annotationId.toString()).build();
                AnnotationResult result = new AnnotationResult(link, "Bad Request Malformed Annotation");
                return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(result).build();
            }
        } else {
            URI link = uriInfo.getBaseUriBuilder().path("annotations").path(annotationId.toString()).build();
            AnnotationResult result = new AnnotationResult(link, "NOT FOUND");
            return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(result).build();
        }
    }

    // PUT /annotations/{annotationId}?asynch=true|false
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({ANNOTATION_MIME_TYPE})
    @ApiOperation(value = "Publication/update of an annotation by Id")
    @Timed
    @Path("/{annotationId}")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotations supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Ok",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Ok",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)})})
    public Response updateAnnotation(@ApiParam(name = "body", value = "Annotation to create", required = true ) String annotation,
                                     @ApiParam(value = "Annotation Id to be retrieved. Must be a valid UUID", required = true, defaultValue = MOCK_ANNOTATION_ID) @PathParam("annotationId") String annotationId,
                                     @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                     @ApiParam(value = "Asynchronous", required = true, defaultValue = "false") @QueryParam("asynch") Boolean asynch,
                                     @Context UriInfo uriInfo) {

        if (annotationId.equals(MOCK_ANNOTATION_ID)) {
            try {
                if (!asynch) {
                    URI syncLnk = uriInfo.getBaseUriBuilder().path("annotations").path(annotationId).build();
                    AnnotationResult annotationResult = this.annotationService.updateAnnotation(syncLnk, annotation);
                    return Response.ok(annotationResult.getLocation()).entity(annotationResult)
                            .header("Location", annotationResult.getLocation())
                            .header(HttpHeaders.VARY, "Accept")
                            .header(HttpHeaders.ETAG, "_87e52ce126126")
                            .header(HttpHeaders.ALLOW, HttpMethod.POST)
                            .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                            .build();
                } else {
                    URI asyncLnk = uriInfo.getBaseUriBuilder().path("annotations").path(annotationId).path("status").build();
                    AnnotationResult annotationResult = this.annotationService.asynchUpdateAnnotation(asyncLnk, annotation);
                    return Response.accepted(annotationResult.getLocation()).entity(annotationResult)
                            .header("Location", annotationResult.getLocation())
                            .header(HttpHeaders.VARY, "Accept")
                            .header(HttpHeaders.ETAG, "_87e52ce126126")
                            .header(HttpHeaders.ALLOW, HttpMethod.POST)
                            .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                            .build();
                }
            } catch (MalFormedAnnotation mfa) {
                URI link = uriInfo.getBaseUriBuilder().path("annotations").path(annotationId.toString()).build();
                AnnotationResult result = new AnnotationResult(link, "Bad Request Malformed Annotation");
                return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(result).build();
            }
        } else {
            URI link = uriInfo.getBaseUriBuilder().path("annotations").path(annotationId.toString()).build();
            AnnotationResult result = new AnnotationResult(link, "NOT FOUND");
            return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(result).build();
        }
    }

    // GET /annotations/{annotationId}/status/{processId}
    @GET
    @Path("/{annotationId}/status/{processId}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Asynchronous publication/creation annotation status")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Unknown annotation and processId"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Ok",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}) })
    public Response statusAnnotationAsynch(@ApiParam(value = "Asynchronous Transaction Id", required = true) @PathParam("processId") String processId,
                                           @ApiParam(value = "Annotation Id", required = false ) @PathParam("annotationId") String annotationId,
                                           @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {
        URI link = uriInfo.getBaseUriBuilder().path("annoations").path(annotationId).path("status").path(processId).build();
        AnnotationResult annotationResult = this.annotationService.asynchAnnotationStatus(link);

        return Response.ok().entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }


    // GET /annotations?contentId=X&max=10&offset=Y
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

    // POST /annotations?contentId=X&aysch=true|false
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
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Success",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class),
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)})})
    public Response createAnnotations(@ApiParam(name = "body", value = "Annotations to create", required = true ) String annotations,
                                      @ApiParam(value = "Target content Id", required = false, defaultValue = MOCK_CONTENT_ID) @QueryParam("contentId") String contentId,
                                      @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                      @ApiParam(value = "Asynchronous", required = true, defaultValue = "false") @QueryParam("asynch") Boolean asynch,
                                      @Context UriInfo uriInfo) {

        if (!asynch) {
            URI syncLnk = uriInfo.getBaseUriBuilder().path("annotations").queryParam("contentId", contentId).queryParam("asynch", false).build();
            AnnotationResult synchAnnotationResult = this.annotationService.createAnnotations(syncLnk, contentId, annotations);
            return Response.ok(synchAnnotationResult.getLocation()).entity(synchAnnotationResult)
                    .header("Location", synchAnnotationResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        } else {
            URI asyncLnk = uriInfo.getBaseUriBuilder().path("annotations").path("status").build();
            AnnotationResult synchAnnotationResult = this.annotationService.asynchCreateAnnotations(asyncLnk, contentId, annotations);
            return Response.accepted(synchAnnotationResult.getLocation()).entity(synchAnnotationResult)
                    .header("Location", synchAnnotationResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        }
    }

    // PUT /annotations?contentId=X&aysch=true|false
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
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Success",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class),
                            @ResponseHeader(name = HttpHeaders.LINK, description = "Linked Data Platform resource type", response = String.class)})})
    public Response updateAnnotations(@ApiParam(name = "body", value = "Annotations to create", required = true ) String annotations,
                                      @ApiParam(value = "Target content Id", required = false, defaultValue = MOCK_CONTENT_ID) @QueryParam("contentId") String contentId,
                                      @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                      @ApiParam(value = "Asynchronous", required = true, defaultValue = "false") @QueryParam("asynch") Boolean asynch,
                                      @Context UriInfo uriInfo) {
        if (!asynch) {
            URI syncLnk = uriInfo.getBaseUriBuilder().path("annotations").queryParam("contentId", contentId).queryParam("asynch", false).build();
            AnnotationResult synchAnnotationResult = this.annotationService.updateAnnotations(syncLnk, contentId, annotations);
            return Response.ok(synchAnnotationResult.getLocation()).entity(synchAnnotationResult)
                    .header("Location", synchAnnotationResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        } else {
            URI asyncLnk = uriInfo.getBaseUriBuilder().path("annotations").path("status").build();
            AnnotationResult synchAnnotationResult = this.annotationService.asynchUpdateAnnotations(asyncLnk, contentId, annotations);
            return Response.accepted(synchAnnotationResult.getLocation()).entity(synchAnnotationResult)
                    .header("Location", synchAnnotationResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        }
    }

    // GET /annotations/status/{processId}
    @GET
    @Path("/status/{processId}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Asynchronous publication/creation annotation status")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid ProcessId supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Ok",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}) })
    public Response statusAnnotationsAsynch(@ApiParam(value = "Asynchronous Transaction Id", required = true) @PathParam("processId") String processId,
                                           @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {
        URI link = uriInfo.getBaseUriBuilder().path("annoations").path("status").path(processId).build();
        AnnotationResult annotationResult = this.annotationService.asynchAnnotationsStatus(link);

        return Response.ok().entity(annotationResult)
                .header("Location",annotationResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }

}
