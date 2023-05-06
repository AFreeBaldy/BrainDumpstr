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
public class Dump {
    @Id
    @GeneratedValue
    private Long dumpId;

    @Builder.Default
    @OneToMany
    private HashSet<AudioFile> audioFiles = new HashSet<>();

    @Builder.Default
    @ManyToMany
    private HashSet<Tag> tags = new HashSet<>();

    @Basic
    private String context;

    @Basic
    private String dump;
}
