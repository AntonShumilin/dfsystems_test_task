package ru.dfsystems.testtask.service;

import com.jayway.jsonpath.JsonPath;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
public class VkRequestService {

    public String requestCurrentUser (VkRequestSettings settings) throws ClientException {
        String response =  settings
                .getVk()
                .users()
                .get(settings.getActor())
                .executeAsString();
        Map<String, String> responseMap =  JsonPath.parse(response).read("$.response[0]");
        return responseMap.toString();
    }

    public String requestCurrentUserGroups (VkRequestSettings settings) throws ClientException {
        return settings
                .getVk()
                .groups()
                .get(settings.getActor())
                .extended(true)
                .executeAsString();
    }

    public String requestFriendsId (VkRequestSettings settings) throws ClientException {
        return  settings
                .getVk()
                .friends()
                .get(settings.getActor())
                .executeAsString();
    }

    public String requestGroupsByUserId (Integer id, VkRequestSettings settings) throws ClientException {
        return settings
                .getVk()
                .groups()
                .get(settings.getActor())
                .userId(id)
                .extended(true)
                .executeAsString();
    }
}
