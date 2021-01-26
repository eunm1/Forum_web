package com.eunm1.forum.service;

import com.eunm1.forum.config.MyMemberDetail;
import com.eunm1.forum.db.entity.Member;
import com.eunm1.forum.db.repository.MemberRepository;
import com.eunm1.forum.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveMember(MemberDto memberDto){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        memberDto.setRole("USER");
        memberDto.setLoginPw(passwordEncoder.encode(memberDto.getLoginPw()));
        Member member = memberRepository.findAllByLoginId(memberDto.getLoginId());
        if(member != null){
            return null;
        }
        return memberRepository.save(memberDto.toEntity()).getId();
    }

    //유저 정보 가져오기
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        //여기서 받은 유저 패스워드와 비교하여 로그인인증
        Member member = memberRepository.findAllByLoginId(loginId);
        if(member.getId() == null){
            throw new UsernameNotFoundException(loginId);
        }
        
        return new MyMemberDetail(member);
    }

/*    @Transactional
    public MemberDto getMemberDetail(String loginId, String loginPw){

    }*/
}
