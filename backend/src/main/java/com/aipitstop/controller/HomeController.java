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

    @org.springframework.beans.factory.annotation.Value("${FRONTEND_URL:http://localhost:3000/}")
    private String frontendUrl;

    @GetMapping(value = "/")
    public ResponseEntity<Void> home() {
        // Redirect to the frontend SPA (configurable via FRONTEND_URL env var)
        return ResponseEntity.status(org.springframework.http.HttpStatus.FOUND)
                .header(org.springframework.http.HttpHeaders.LOCATION, frontendUrl)
                .build();
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
