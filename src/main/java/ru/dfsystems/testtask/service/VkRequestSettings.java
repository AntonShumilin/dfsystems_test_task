package ru.dfsystems.testtask.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VkRequestSettings {
    private String appId;
    private String clientSecret;
    private String code;
    private String redirectUri;
    private VkApiClient vk;
    private UserActor actor;

    public VkRequestSettings(String code, String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
