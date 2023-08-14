package com.project.starcoffee.repository;

import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface MemberMapper {

    int saveMember(MemberDTO memberInfo);

    Optional<Member> findById(String loginId);

    Member findByIdAndPassword(@Param("id") String id, @Param("password") String password);

    int checkId(String id);

    int deleteMember(String loginId);

    int updatePassword(String id, String password);

}
