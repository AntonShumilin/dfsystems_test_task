package ru.dfsystems.testtask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "vk_group")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class VkGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String group_info;
    @ManyToOne
    @JoinColumn(name = "request_id")
    @JsonIgnore
    private VkRequest request;
}
