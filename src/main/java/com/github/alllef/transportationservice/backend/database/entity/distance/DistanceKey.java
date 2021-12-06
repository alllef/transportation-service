package com.github.alllef.transportationservice.backend.database.entity.distance;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
class DistanceKey implements Serializable {
    private Long providerId;
    private Long consumerId;
}
