package com.ontotext.annotation.util;

import com.ontotext.annotation.resource.AnnotationResource;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ResourceUtil {

    public static final String getResourceFileAsString(String resource) {
        try {
            return IOUtils.toString(AnnotationResource.class.getResourceAsStream(resource), "UTF-8");
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

}
