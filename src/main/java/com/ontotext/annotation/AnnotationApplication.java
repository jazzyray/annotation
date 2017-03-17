package com.ontotext.annotation;

import com.ontotext.annotation.configuration.AnnotationConfiguration;
import com.ontotext.annotation.health.AnnotationHealthCheck;
import com.ontotext.annotation.resource.AnnotationResource;
import com.ontotext.annotation.service.AnnotationService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AnnotationApplication extends Application<AnnotationConfiguration> {

    AnnotationService annotationService;

    public static void main(String[] args) throws Exception {
        new AnnotationApplication().run(args);
    }

    @Override
    public void run(AnnotationConfiguration annotationConfiguration, Environment environment) throws Exception {

        this.annotationService = new AnnotationService();
        final AnnotationResource resource = new AnnotationResource(this.annotationService);

        final AnnotationHealthCheck healthCheck = new AnnotationHealthCheck();
        environment.healthChecks().register("annotation", healthCheck);

        environment.jersey().register(resource);
    }

    @Override
    public void initialize(Bootstrap<AnnotationConfiguration> bootstrap) {
       // @TODO Add health check
       // bootstrap.addBundle(new HealthCheckBundle());
    }
}
