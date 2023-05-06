package com.capedbaldy.braindumpstr.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tag {
    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @Column(nullable = false, unique = true)
    private String value;

    @Builder.Default
    @ManyToMany(mappedBy = "tags")
    private HashSet<Dump> dumps = new HashSet<>();
}
