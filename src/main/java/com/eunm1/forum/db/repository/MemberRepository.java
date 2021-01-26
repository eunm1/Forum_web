package com.eunm1.forum.db.repository;

import com.eunm1.forum.db.entity.Member;
import com.eunm1.forum.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findAllByLoginId(String loginId);

    Member findOneById(Long Id);
}
