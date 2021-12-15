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
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Provider extends TransportPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;
    private int maxCapacity;

    @OneToMany(mappedBy = "provider", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    Set<Distance> distances;
}
