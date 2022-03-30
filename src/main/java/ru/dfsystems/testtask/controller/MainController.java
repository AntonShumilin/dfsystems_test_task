package ru.dfsystems.testtask.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dfsystems.testtask.entity.VkRequest;
import ru.dfsystems.testtask.service.MainService;
import ru.dfsystems.testtask.utils.AppUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class MainController {

    public static final String USER_REDIRECT_URI = "http://localhost:8080/user";
    public static final String FRIENDS_REDIRECT_URI = "http://localhost:8080/friends";
    public static final String VK_AUTH_URI = "https://oauth.vk.com/authorize";

    private MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/user")
    public ResponseEntity<VkRequest> getAndStoreUserGroups(
            @RequestParam(required = false)
            String code,
            @RequestParam(required = false)
            String substring,
            @RequestParam(required = false)
            String state,
            HttpServletResponse response)
            throws IOException {

        if (code == null) {
            sendRedirectResponse(response, substring, USER_REDIRECT_URI);
            return null;
        } else {
            return ResponseEntity.ok(mainService.findUserGroupsBySubstring(code, state, USER_REDIRECT_URI));
        }
    }
    @GetMapping("/friends")
    public ResponseEntity<VkRequest> getAllUserGroups(
            @RequestParam(required = false)
                    String code,
            @RequestParam(required = false)
                    String substring,
            @RequestParam(required = false)
                    String state,
            HttpServletResponse response)
            throws IOException {

        if (code == null) {
            sendRedirectResponse(response, substring, FRIENDS_REDIRECT_URI);
            return null;
        } else {
            return ResponseEntity.ok(mainService.findUserGroupsWithFriendsBySubstring(code, state, FRIENDS_REDIRECT_URI));
        }
    }

    @GetMapping("/all_groups")
    public Page<VkRequest> getAll (@PageableDefault(value = 5, page = 0) Pageable pageable) {
        return mainService.getAllRequests(pageable);
    }

    private void sendRedirectResponse(HttpServletResponse response, String substring, String redirectUri) throws IOException {
        String state = StringUtils.hasText(substring) ?
                "&" + "state=" + AppUtils.toUnicode(substring)
                : "";
        response.sendRedirect(
                 VK_AUTH_URI + "?" +
                        "client_id=" + mainService.getAppId() + "&" +
                        "redirect_uri=" + redirectUri + "&" +
                        "response_type=code" + "&" +
                        "scope=groups" +
                        state

        );
    }





}
