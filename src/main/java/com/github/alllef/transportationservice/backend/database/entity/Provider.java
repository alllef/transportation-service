package com.github.alllef.transportationservice.backend.database.entity;

import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;
    private String name;
    private String address;
    private int maxCapacity;

    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    Set<Distance> distances;
}
