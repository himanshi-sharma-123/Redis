package org.example.controller;

import org.example.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rate-limiter")
public class RateLimiterController {

    @Autowired
    RateLimiterService rateLimiterService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id){
        if(!rateLimiterService.isAllowed(id)){
            long wait = rateLimiterService.getRemainingTTL(id);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeded. Try again in " + wait + " seconds.");
        }
        return ResponseEntity.ok("Here is your data user: " + id);
    }
}