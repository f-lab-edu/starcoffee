package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberMapper memberMapper;

    @Autowired
    public MemberRepositoryImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public int saveMember(MemberRequest memberInfo) {
        return memberMapper.saveMember(memberInfo);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberMapper.findById(memberId);
    }

    @Override
    public Member findByIdAndPassword(String loginId, String password) {
        return memberMapper.findByIdAndPassword(loginId, password);
    }

    @Override
    public int deleteMember(String loginId) {
        return memberMapper.deleteMember(loginId);
    }

    @Override
    public int updateNumber(String loginId, String phoneNumber) {
        return memberMapper.updateNumber(loginId, phoneNumber);
    }

    @Override
    public int updatePassword(String loginId, String password) {
        return memberMapper.updatePassword(loginId, password);
    }

    @Override
    public int updateNickName(String loginId, String nickName) {
        return memberMapper.updateNickName(loginId, nickName);
    }

    @Override
    public int updateEmail(String loginId, String email) {
        return memberMapper.updateEmail(loginId, email);
    }

}
