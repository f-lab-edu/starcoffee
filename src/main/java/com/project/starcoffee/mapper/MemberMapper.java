package com.project.starcoffee.mapper;

import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    int saveMember(MemberRequest memberInfo);

    Optional<String> findById(String loginId);

    Optional<Member> findByMember(String memberId);

    Member findByIdAndPassword(@Param("loginId") String loginId, @Param("password") String password);

    int updatePassword(@Param("loginId") String loginId, @Param("password") String password);

    int updateNickName(@Param("loginId") String loginId, @Param("nickName") String nickName);

    int updateEmail(@Param("loginId") String loginId, @Param("email") String email);

    int updateNumber(@Param("loginId") String loginId, @Param("phoneNumber") String phoneNumber);

    int deleteMember(String loginId);

}
