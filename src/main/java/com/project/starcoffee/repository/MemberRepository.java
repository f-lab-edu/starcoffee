package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {

    int saveMember(MemberRequest memberInfo);

    Member findById(String memberId);

    int checkId(String loginId);

    Member findByIdAndPassword(@Param("loginId") String loginId, @Param("password") String password);

    int updatePassword(@Param("loginId") String loginId, @Param("password") String password);

    int updateNickName(@Param("loginId") String loginId, @Param("nickName") String nickName);

    int updateEmail(@Param("loginId") String loginId, @Param("email") String email);

    int updateTel(@Param("loginId") String loginId, @Param("tel") String tel);

    int deleteMember(String loginId);

}
