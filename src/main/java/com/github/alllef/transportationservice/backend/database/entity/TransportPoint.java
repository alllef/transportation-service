package com.github.alllef.transportationservice.backend.database.entity;

import lombok.*;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class TransportPoint {
    private String name;
    private String address;
}
