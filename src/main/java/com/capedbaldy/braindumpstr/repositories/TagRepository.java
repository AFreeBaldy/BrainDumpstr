package com.capedbaldy.braindumpstr.repositories;

import com.capedbaldy.braindumpstr.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
