package com.example.practice.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalUserRequest {
    private String name;
    private String job;
}
