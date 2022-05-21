package com.example.Taxi.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostReqDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String msg;
    private Long groupId;

    public PostReqDto(String accessToken, String msg, Long groupId) {
        this.accessToken = accessToken;
        this.msg = msg;
        this.groupId = groupId;
    }
}
