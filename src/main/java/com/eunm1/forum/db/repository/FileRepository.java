package com.eunm1.forum.db.repository;

import com.eunm1.forum.db.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
