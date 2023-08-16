package com.project.starcoffee.service;

import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.dto.MemberDTO;
import com.project.starcoffee.exception.DuplicateIdException;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.repository.mybatis.mapper.MemberMapper;
import com.project.starcoffee.utils.SHA256Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService service;

    @Mock
    MemberRepository repository;

    private MemberDTO member;
    private Optional<Member> resultMember;

    @BeforeEach
    void init() {
        member = MemberDTO.builder()
                .id(1L)
                .name("testName")
                .tel("010-1111-1111")
                .email("test@test.com")
                .gender("M")
                .nickName("nickName")
                .loginId("testLogin")
                .password("password1234#")
                .birth(LocalDateTime.now())
                .build();

        resultMember = Optional.of(new Member());
        resultMember.ifPresent(memberInfo -> {
            member.setLoginId("testLogin");
            // password1234# 비밀번호의 암호화
            member.setPassword("984da22aa86418be811bc17828ebaa91cc9b058de869f223c9461fc85c734b27");
        });
    }

    @Test
    public void 고객_회원가입_성공() {
        given(repository.saveMember(member)).willReturn(1);

        service.saveMember(member);
    }

    @Test
    public void 고객_회원가입_실패_중복아이디() {
        given(repository.checkId(member.getLoginId())).willReturn(1);

        assertThatCode(() -> {
            service.DuplicatedId(member); }).isInstanceOf(DuplicateIdException.class);
    }

    @Test
    public void 고객_회원가입_실패() {
        given(repository.saveMember(member)).willReturn(0);
        assertThatCode(() -> {
            service.saveMember(member);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_로그인_성공() {
        given(repository.findByIdAndPassword("testLogin", SHA256Util.encryptSHA256("password1234#")))
                .willReturn(resultMember);

        assertThat(service.login("testLogin", "password1234#")).isEqualTo(resultMember);
    }

    @Test
    public void 로그인아이디_고객정보_조회_성공() {
        given(repository.findById(member.getLoginId())).willReturn(resultMember);

        assertThat(service.findById(member.getLoginId())).isEqualTo(resultMember);
    }

    @Test
    public void 로그인아이디_고객정보_조회_실패() {
        given(repository.findById(member.getLoginId())).willReturn(null);

        assertThatCode(() -> {
            service.findById(member.getLoginId());
        }).isInstanceOf(RuntimeException.class);
    }
    @Test
    public void 고객_로그인_실패_비밀번호_불일치() {
        given(repository.findByIdAndPassword("testLogin", SHA256Util.encryptSHA256("testPassword")))
                .willReturn(null);

        assertThat(service.login("testLogin", "testPassword")).isNull();
    }

    @Test
    public void 고객_비밀번호_변경_성공() {
        given(repository.updatePassword(member.getLoginId(), SHA256Util.encryptSHA256("testPassword")))
                .willReturn(1);
        given(repository.findByIdAndPassword(member.getLoginId(), SHA256Util.encryptSHA256("password1234#")))
                .willReturn(resultMember);

        service.updatePassword(member.getLoginId(), "password1234#", "testPassword");
    }

    @Test
    public void 고객_비밀번호_변경_실패_이전_비밀번호_불일치() {
        given(repository.findByIdAndPassword(member.getLoginId(), SHA256Util.encryptSHA256("failPassword")))
                .willReturn(null);

        assertThatCode(() -> {
            service.updatePassword(member.getLoginId(), "failPassword", "updatePassword");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 고객_비밀번호_변경_실패() {

        given(repository.updatePassword(member.getLoginId(), SHA256Util.encryptSHA256("updatePassword")))
                .willReturn(0);

        assertThatCode(() -> {
            service.updatePassword(member.getLoginId(), "password1234#", "updatePassword");
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_삭제_성공() {
        given(repository.deleteMember(member.getLoginId())).willReturn(1);
        service.deleteMember(member.getLoginId());
    }

    @Test
    public void 고객_삭제_실패() {
        given(repository.deleteMember(member.getLoginId())).willReturn(0);
        assertThatCode(() -> {
            service.deleteMember(member.getLoginId());
        }).isInstanceOf(RuntimeException.class);
    }

}
