package com.project.starcoffee.repository.mybatis;

import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.dto.MemberDTO;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.repository.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public int saveMember(MemberDTO memberInfo) {
        return memberMapper.saveMember(memberInfo);
    }

    @Override
    public Optional<Member> findById(String loginId) {
        return memberMapper.findById(loginId);
    }

    @Override
    public Optional<Member> findByIdAndPassword(String id, String password) {
        return memberMapper.findByIdAndPassword(id, password);
    }

    @Override
    public int checkId(String id) {
        return memberMapper.checkId(id);
    }

    @Override
    public int deleteMember(String loginId) {
        return memberMapper.deleteMember(loginId);
    }

    @Override
    public int updatePassword(String id, String password) {
        return memberMapper.updatePassword(id, password);
    }
}
