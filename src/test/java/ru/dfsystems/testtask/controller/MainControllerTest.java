package ru.dfsystems.testtask.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.dfsystems.testtask.entity.VkGroup;
import ru.dfsystems.testtask.entity.VkRequest;
import ru.dfsystems.testtask.service.MainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class MainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MainService mainService;

    @Test
    @DisplayName("/user - 200")
    void testUserOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                            .get("/user"))
                .andExpect(status().is3xxRedirection()
        );

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user?code=somecode"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("/user - 500")
    void testUserError() throws Exception {

        Mockito
                .when(mainService.findUserGroupsBySubstring(any(), any(), any()))
                .thenThrow(new RuntimeException("ErrorText"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user?code=somecode"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("ErrorText"));
    }

    @Test
    @DisplayName("/friends - 200")
    void testFriendsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/friends"))
                .andExpect(status().is3xxRedirection()
                );

        mockMvc.perform(MockMvcRequestBuilders
                .get("/friends?code=somecode"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("/friends - 500")
    void testFriendsError() throws Exception {

        Mockito
                .when(mainService.findUserGroupsWithFriendsBySubstring(any(), any(), any()))
                .thenThrow(new RuntimeException("ErrorText"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/friends?code=somecode"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("ErrorText"));
    }

    @Test
    @DisplayName("/all_groups - 200")
    void testAllGroupsOk() throws Exception {
        VkRequest vkRequest = VkRequest.builder()
                .created(LocalDateTime.now())
                .params("paramsString").build();
        List<VkGroup> vkGroupsList = List.of(
                VkGroup.builder().id(1L).group_info("group_1_InfoText").build(),
                VkGroup.builder().id(2L).group_info("group_2_InfoText").build());
        vkRequest.setVkGroupsList(vkGroupsList);

        List<VkRequest> resultList = Collections.singletonList(vkRequest);

        Mockito
                .when(mainService.getAllRequests(any()))
                .thenReturn(new PageImpl<>(resultList));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/all_groups"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[0].params")
                        .value("paramsString"))
                .andExpect(jsonPath("$.content[0].vkGroupsList[0].group_info")
                        .value("group_1_InfoText"))
                .andExpect(jsonPath("$.content[0].vkGroupsList[1].group_info")
                        .value("group_2_InfoText"));
    }

    @Test
    @DisplayName("/all_groups - 500")
    void testAllGroupsError() throws Exception {

        Mockito
                .when(mainService.getAllRequests(any()))
                .thenThrow(new RuntimeException("ErrorText"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/all_groups"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("ErrorText"));
    }
}