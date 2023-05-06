package com.capedbaldy.braindumpstr.repositories;

import com.capedbaldy.braindumpstr.models.Dump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DumpRepository extends JpaRepository<Dump, Long> {

}
