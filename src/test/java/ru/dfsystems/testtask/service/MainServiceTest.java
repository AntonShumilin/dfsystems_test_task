package ru.dfsystems.testtask.service;

import com.vk.api.sdk.exceptions.ClientException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.dfsystems.testtask.entity.VkGroup;
import ru.dfsystems.testtask.entity.VkRequest;
import ru.dfsystems.testtask.repository.VkRepository;
import ru.dfsystems.testtask.utils.AppUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
class MainServiceTest {

    @Autowired
    MainService mainService;

    @MockBean
    VkRepository vkRepository;
    @MockBean
    VkRequestService vkRequestService;
    @MockBean
    VkRequestSettingsFactory settingsFactory;

    @Test
    void findUserGroupsWithFriendsBySubstring() throws ClientException, IOException {
        String userGroups = new String(Files.readAllBytes(new ClassPathResource("userGroups.json").getFile().toPath()));
        String friends = new String(Files.readAllBytes(new ClassPathResource("friends.json").getFile().toPath()));
        Mockito.when(vkRequestService.requestFriendsId(any())).thenReturn(friends);
        Mockito.when(vkRequestService.requestGroupsByUserId(any(), any())).thenReturn(userGroups);

        VkRequest vkRequest =
                mainService.findUserGroupsWithFriendsBySubstring("someCode", null, "someUri");
        assertAll(
                ()->assertEquals(2, vkRequest.getVkGroupsList().size()),
                ()->assertEquals(
                        "{\"id\":26419239,\"name\":\"Title_1\"}",
                        vkRequest.getVkGroupsList().get(0).getGroup_info()),
                ()->assertEquals(
                        "{\"id\":36164349,\"name\":\"Title_2\"}",
                        vkRequest.getVkGroupsList().get(1).getGroup_info())
        );

        VkRequest vkRequestFiltered =
                mainService.findUserGroupsWithFriendsBySubstring("someCode", AppUtils.toUnicode("2"), "someUri");
        assertAll(
                ()->assertEquals(1, vkRequestFiltered.getVkGroupsList().size()),
                ()->assertEquals(
                        "{\"id\":36164349,\"name\":\"Title_2\"}",
                        vkRequestFiltered.getVkGroupsList().get(0).getGroup_info())
        );
    }

    @Test
    void findUserGroupsBySubstring() throws ClientException, IOException {
        String userGroups = new String(Files.readAllBytes(new ClassPathResource("userGroups.json").getFile().toPath()));
        Mockito.when(vkRequestService.requestCurrentUserGroups(any())).thenReturn(userGroups);

        VkRequest vkRequest =
                mainService.findUserGroupsBySubstring("someCode", null, "someUri");
        assertAll(
                ()->assertEquals(2, vkRequest.getVkGroupsList().size()),
                ()->assertEquals(
                        "{\"id\":26419239,\"name\":\"Title_1\"}",
                        vkRequest.getVkGroupsList().get(0).getGroup_info()),
                ()->assertEquals(
                        "{\"id\":36164349,\"name\":\"Title_2\"}",
                        vkRequest.getVkGroupsList().get(1).getGroup_info())
        );

        Mockito.verify(vkRepository,Mockito.times(1)).save(vkRequest);

        VkRequest vkRequestFiltered =
                mainService.findUserGroupsBySubstring("someCode", AppUtils.toUnicode("2"), "someUri");
        assertAll(
                ()->assertEquals(1, vkRequestFiltered.getVkGroupsList().size()),
                ()->assertEquals(
                        "{\"id\":36164349,\"name\":\"Title_2\"}",
                        vkRequestFiltered.getVkGroupsList().get(0).getGroup_info())
        );

        Mockito.verify(vkRepository,Mockito.times(1)).save(vkRequestFiltered);
    }

    @Test
    void getAllRequests() {
        PageRequest pageable = PageRequest.of(0, 1);
        mainService.getAllRequests(pageable);
        Mockito.verify(vkRepository,Mockito.times(1)).findAll(pageable);
    }
}