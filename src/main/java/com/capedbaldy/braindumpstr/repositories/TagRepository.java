package com.capedbaldy.braindumpstr.repositories;

import com.capedbaldy.braindumpstr.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    HashSet<Tag> findTagsByValueIn(Set<String> values);
}
