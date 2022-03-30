package ru.dfsystems.testtask.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.dfsystems.testtask.entity.VkRequest;

public interface VkRepository extends PagingAndSortingRepository<VkRequest, Long> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"vkGroupsList"}
    )
    Page<VkRequest> findAll(Pageable pageable);
}
