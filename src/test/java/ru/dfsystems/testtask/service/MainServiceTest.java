package ru.dfsystems.testtask.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.dfsystems.testtask.repository.VkRepository;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
class MainServiceTest {

    @MockBean
    private VkRepository vkRepository;
    @MockBean
    private VkRequestService vkRequestService;

    @Test
    void findUserGroupsWithFriendsBySubstring() {
    }

//    @Test
    void findUserGroupsBySubstring() {
    }

//    @Test
    void getAllRequests() {
    }
}