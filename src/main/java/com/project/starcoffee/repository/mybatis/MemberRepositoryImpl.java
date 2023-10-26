package com.project.starcoffee.repository.mybatis;

import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.repository.mybatis.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public int updateTel(String loginId, String tel) {
        return 0;
    }

    @Override
    public Card findCard(String cardNumber, String pinNumber) {
        return memberMapper.findCard(cardNumber, pinNumber);
    }

    @Override
    public int enrollCard(UUID memberId, UUID cardId) {
        return memberMapper.enrollCard(memberId, cardId);
    }


}
