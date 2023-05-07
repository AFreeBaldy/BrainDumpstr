package com.capedbaldy.braindumpstr.repositories;

import com.capedbaldy.braindumpstr.models.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends JpaRepository<AudioFile, Long> {
}
