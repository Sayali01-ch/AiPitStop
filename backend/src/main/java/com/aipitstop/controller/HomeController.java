package com.aipitstop.controller;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;

import java.util.Map;

@Controller
public class HomeController implements ErrorController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> home() {
        String body = "<html><body><h1>AiPitStop Backend</h1><p>Available endpoints: <code>/api/ai/text</code> (POST), <code>/api/ai/image</code> (POST), <code>/api/doc/extract</code> (POST)." +
                "<br/>Frontend runs on port 3000 by default.</p></body></html>";
        return ResponseEntity.ok().body(body);
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> handleError(WebRequest webRequest) {
        DefaultErrorAttributes attrs = new DefaultErrorAttributes();
        Map<String,Object> errorAttributes = attrs.getErrorAttributes((ServletWebRequest)webRequest, ErrorAttributeOptions.defaults());
        int status = (int) errorAttributes.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(status).body(Map.of(
                "status", status,
                "error", errorAttributes.getOrDefault("error", "Unknown"),
                "message", errorAttributes.getOrDefault("message", ""),
                "path", errorAttributes.getOrDefault("path", "")
        ));
    }
}
