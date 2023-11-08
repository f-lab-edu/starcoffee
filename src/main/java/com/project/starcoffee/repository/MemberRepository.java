package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.member.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository {

    int saveMember(MemberRequest memberInfo);

    Optional<Member> findById(String memberId);

    Member findByIdAndPassword(String loginId, String password);

    int updatePassword(String loginId, String password);

    int updateNickName(String loginId, String nickName);

    int updateEmail(String loginId, String email);

    int updateNumber(String loginId, String phoneNumber);

    int deleteMember(String loginId);
}
