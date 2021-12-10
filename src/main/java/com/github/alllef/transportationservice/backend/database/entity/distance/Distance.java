package com.github.alllef.transportationservice.backend.database.entity.distance;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Distance {
    @EmbeddedId
    private DistanceKey id;

    @ManyToOne
    @MapsId("providerId")
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToOne
    @MapsId("consumerId")
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    private int distance;
}
