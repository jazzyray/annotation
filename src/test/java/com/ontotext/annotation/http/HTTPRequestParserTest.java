package com.ontotext.annotation.http;

import com.ontotext.annotation.com.ontotext.annotation.http.HTTPRequestParser;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static com.ontotext.annotation.util.ResourceUtil.getResourceFileAsStream;
import static org.assertj.core.api.Assertions.assertThat;


public class HTTPRequestParserTest {


    @Test
    public void testParse() throws Exception{
        InputStream is = getResourceFileAsStream("http-message-payload/create-annotations-by-content-id.http");
        HTTPRequestParser parser = new HTTPRequestParser(is);

        int status = parser.parseRequest();

        assertThat(status).isEqualTo(HttpURLConnection.HTTP_OK);
        System.out.println("status: " + status);
        System.out.println("body: " + parser.getBody());
        System.out.println("accept: " + parser.getHeader(HttpHeaders.ACCEPT));


    }

}
