package com.github.alllef.transportationservice.backend.database.entity.distance;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DistanceKey implements Serializable {
    private Long providerId;
    private Long consumerId;
}
