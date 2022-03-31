package ru.dfsystems.testtask.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import lombok.Getter;
import ru.dfsystems.testtask.exception.MainServiceException;

@Getter
public class VkRequestSettings {
    private String appId;
    private String clientSecret;
    private String code;
    private String redirectUri;
    private VkApiClient vk;
    private UserActor actor;

    public VkRequestSettings(String appId, String clientSecret, String code, String redirectUri) {
        this.appId = appId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.vk = buildClient();
        this.actor = getUserActor(code, vk, redirectUri);
    }

    private VkApiClient buildClient() {
        TransportClient transportClient = new HttpTransportClient();
        return new VkApiClient(transportClient);
    }

    private UserActor getUserActor(String code, VkApiClient vk, String redirectUri){
        try {
            UserAuthResponse authResponse = vk.oAuth()
                    .userAuthorizationCodeFlow(Integer.parseInt(appId), clientSecret, redirectUri, code)
                    .execute();
            return new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
        } catch (ApiException | ClientException e) {
            throw new MainServiceException("Error getting VK token", e);
        }
    }
}
