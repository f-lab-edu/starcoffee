package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.domain.member.MemberStatus;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService service;

    @Mock
    MemberRepository repository;

    private MemberDTO member;
    private MemberRequest member2;

    private Optional<Member> resultMember;

    @BeforeEach
    void init() {
        member = MemberDTO.builder()
                .id(UUID.fromString("a999d6e0-4d8b-11ee-ac76-fd3617d1d2aa"))
                .name("testName")
                .tel("010-1111-1111")
                .email("test@test.com")
                .gender("M")
                .nickName("nickName")
                .loginId("testLogin")
                .password("password1234#")
                .birth(LocalDateTime.now())
                .build();

        member2 = MemberRequest.builder()
                .name("testName")
                .tel("010-1111-1111")
                .email("test@test.com")
                .gender("M")
                .nickName("nickName")
                .loginId("testLogin")
                .password("password1234#")
                .birth(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        resultMember = Optional.of(new Member());
        resultMember.ifPresent(memberInfo -> {
            memberInfo.setLoginId("testLogin");
            memberInfo.setPassword("984da22aa86418be811bc17828ebaa91cc9b058de869f223c9461fc85c734b27");
            memberInfo.setMemberId(UUID.fromString("a999d6e0-4d8b-11ee-ac76-fd3617d1d2aa"));
            memberInfo.setEmail("kks@naver.com");
            memberInfo.setGender("M");
            memberInfo.setTel("010-1111-1111");
            memberInfo.setBirth(Timestamp.valueOf(LocalDateTime.now()));
            memberInfo.setNickName("nickName");
            memberInfo.setName("testName");
            memberInfo.setStatus(MemberStatus.DEFAULT);
        });
    }

    @Test
    public void 고객_회원가입_성공() {
        given(repository.saveMember(member2)).willReturn(1);

        service.saveMember(member2);
    }

    @Test
    public void 고객_회원가입_실패_중복아이디() {
        given(repository.checkId(member.getLoginId())).willReturn(1);

        assertThatCode(() -> {
            service.DuplicatedId(member2); }).isInstanceOf(DuplicateIdException.class);
    }

    @Test
    public void 고객_회원가입_실패() {
        given(repository.saveMember(member2)).willReturn(0);
        assertThatCode(() -> {
            service.saveMember(member2);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_로그인_성공() {
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest();
        memberLoginRequest.setLoginId("testLogin");
        memberLoginRequest.setPassword("password1234#");

        given(repository.findByIdAndPassword("testLogin", SHA256Util.encryptSHA256("password1234#")))
                .willReturn(resultMember);

        assertThat(service.login(memberLoginRequest)).isEqualTo(resultMember);
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
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest();
        memberLoginRequest.setLoginId("testLogin");
        memberLoginRequest.setPassword("testPassword");

        given(repository.findByIdAndPassword("testLogin", SHA256Util.encryptSHA256("testPassword")))
                .willReturn(null);

        assertThatCode(() -> {
            service.login(memberLoginRequest);
        }).isInstanceOf(RuntimeException.class);

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
