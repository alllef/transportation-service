package com.github.alllef.transportationservice.backend.database.entity;

import lombok.*;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TransportPoint {
    private String name;
    private String address;
}
