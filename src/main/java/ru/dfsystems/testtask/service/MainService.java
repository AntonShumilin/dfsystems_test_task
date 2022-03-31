package ru.dfsystems.testtask.service;

import com.jayway.jsonpath.JsonPath;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dfsystems.testtask.entity.VkGroup;
import ru.dfsystems.testtask.entity.VkRequest;
import ru.dfsystems.testtask.repository.VkRepository;
import ru.dfsystems.testtask.utils.AppUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Slf4j
public class MainService {

    private static final String ITEMS_PATTERN = "$.response.items";

    @Value("${APP_ID}")
    private String appId;
    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    private VkRepository vkRepository;
    private VkRequestService vkRequestService;

    public MainService(VkRepository vkRepository, VkRequestService vkRequestService) {
        this.vkRepository = vkRepository;
        this.vkRequestService = vkRequestService;
    }

    public VkRequest findUserGroupsWithFriendsBySubstring(String code, String unicodeString, String redirectUri) {
        try {
            List<String> requestItemsList = getUserAndFriendsGroups(code, redirectUri);
            VkRequest vkRequest = buildVkRequestEntity(requestItemsList, unicodeString);

            log.info("Request substring is \"{}\", response size = {}",
                    AppUtils.fromUnicode(unicodeString).orElse(null),
                    vkRequest.getVkGroupsList().size()
            );
            return vkRequest;
        } catch (ClientException e) {
            throw new RuntimeException("Error getting VK response", e);
        }
    }


    public VkRequest findUserGroupsBySubstring(String code, String unicodeString, String redirectUri) {
        try {
            List<String> requestItemsList = getUserGroups(code, redirectUri);
            VkRequest vkRequest = buildVkRequestEntity(requestItemsList, unicodeString);

            log.info("Request substring is \"{}\", response size = {}",
                    AppUtils.fromUnicode(unicodeString).orElse(null),
                    vkRequest.getVkGroupsList().size()
            );

            vkRepository.save(vkRequest);

            log.info("Request successfully stored");

            return vkRequest;

        } catch (ClientException e) {
            throw new RuntimeException("Error getting VK response", e);
        }
    }

    public Page<VkRequest> getAllRequests(Pageable pageable) {
        log.info("Requesting all groups from database, pagination {}", pageable);

        return vkRepository.findAll(pageable);
    }

    private VkRequest buildVkRequestEntity(List<String> requestItemsList, String unicodeString) {
        VkRequest vkRequest = VkRequest.builder()
                .created(LocalDateTime.now())
                .params(AppUtils.fromUnicode(unicodeString).orElse(null)).build();
        List<VkGroup> vkGroupsList = filterRequestItemsList(requestItemsList, unicodeString).stream()
                .map(it -> VkGroup.builder()
                        .group_info(it)
                        .request(vkRequest)
                        .build())
                .collect(Collectors.toList());
        vkRequest.setVkGroupsList(vkGroupsList);
        return vkRequest;
    }

    private List<String> getUserGroups(String code, String redirectUri) throws ClientException {
        VkRequestSettings settings = new VkRequestSettings(appId, clientSecret, code, redirectUri);

        log.info("Requesting all groups fo user {}", vkRequestService.requestCurrentUser(settings));

        String jsonItemsList = vkRequestService.requestCurrentUserGroups(settings);

        return ((JSONArray) JsonPath.parse(jsonItemsList).read(ITEMS_PATTERN)).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private List<String> getUserAndFriendsGroups(String code, String redirectUri) throws ClientException {
        VkRequestSettings settings = new VkRequestSettings(appId, clientSecret, code, redirectUri);

        log.info("Requesting all groups and friends groups  fo user {}", vkRequestService.requestCurrentUser(settings));

        String friendsJson = vkRequestService.requestFriendsId(settings);

        JSONArray friendsIdArray = JsonPath.parse(friendsJson).read(ITEMS_PATTERN);
        List<Integer> friendsIdList = friendsIdArray.stream()
                .map(Object::toString)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<String> result = new ArrayList<>();
        for (Integer id : friendsIdList) {
            String responseString = vkRequestService.requestGroupsByUserId(id, settings);
            JSONArray responseItemsArray = JsonPath.parse(responseString).read(ITEMS_PATTERN);
            result.addAll(responseItemsArray.stream().map(Object::toString).collect(Collectors.toList()));
        }
        return result;
    }

    private List<String> filterRequestItemsList(List<String> requestItemsArray, String unicodeString) {

        return AppUtils.fromUnicode(unicodeString).map(substring ->
                requestItemsArray.stream()
                        .filter(it -> it.toLowerCase().contains(substring.toLowerCase()))
                        .collect(Collectors.toList())
        ).orElse(requestItemsArray);
    }
}
