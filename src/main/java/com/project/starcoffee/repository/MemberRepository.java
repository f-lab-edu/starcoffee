package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.dto.MemberDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

public interface MemberRepository {

    int saveMember(MemberRequest memberInfo);

    Optional<Member> findById(String loginId);

    Optional<Member> findByIdAndPassword(@Param("id")String id, @Param("password")String password);

    int checkId(String id);

    int deleteMember(String loginId);

    int updatePassword(@Param("id")String id, @Param("password")String password);

}
