package com.capedbaldy.braindumpstr.models;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AudioFile {

    @Id
    @GeneratedValue
    private Long id;
    @Basic
    private String title;

    @Basic
    private String path;

    @Basic
    private String mimeType;
}
