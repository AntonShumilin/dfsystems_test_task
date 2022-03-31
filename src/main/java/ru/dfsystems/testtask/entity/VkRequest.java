package ru.dfsystems.testtask.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="vk_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class VkRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime created;
    @Column
    private String params;
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    List<VkGroup> vkGroupsList;
}
