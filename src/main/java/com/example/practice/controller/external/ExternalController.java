package com.example.practice.controller.external;

import com.example.practice.model.request.ExternalUserRequest;
import com.example.practice.service.external.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExternalController {
    private final ClientService clientService;

    @GetMapping({"/getUser", "/getUser/{id}"})
    public Object getUser(@PathVariable(required = false) Optional<Integer> id){
        log.info("Request received to get sample object");
        return clientService.getDataFromClient(id);
    }

    @PostMapping("/postUser")
    public Object postUserData(@RequestBody ExternalUserRequest user){
        log.info("Request received to post user data");
        return clientService.postDataToClient(user);
    }
}
