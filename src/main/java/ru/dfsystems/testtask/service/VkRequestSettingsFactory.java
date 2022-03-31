package ru.dfsystems.testtask.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dfsystems.testtask.exception.MainServiceException;

@Component
public class VkRequestSettingsFactory {

    @Value("${APP_ID}")
    private String appId;
    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    public VkRequestSettings getSettings(String code, String redirectUri) {
        VkRequestSettings settings = new VkRequestSettings(code, redirectUri);
        settings.setAppId(appId);
        settings.setClientSecret(clientSecret);
        VkApiClient vk = buildClient();
        settings.setVk(vk);
        settings.setActor(getUserActor(code, vk, redirectUri));
        return settings;
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
