package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.dto.MemberDTO;
import com.project.starcoffee.exception.DuplicateIdException;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.utils.SHA256Util;
import com.project.starcoffee.utils.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
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
    @Mock
    private HttpSession httpSession;
    @Mock
    PushService pushService;

    private MemberDTO memberDTO;
    private MemberRequest memberRequest;
    private Optional<Member> memberOptional;

    private MockHttpSession session;

    @BeforeEach
    void init() {
        memberDTO = MemberDTO.builder()
                .id(UUID.fromString("a999d6e0-4d8b-11ee-ac76-fd3617d1d2aa"))
                .name("testName")
                .tel("010-1111-1111")
                .email("test@test.com")
                .gender("M")
                .nickName("nickName")
                .loginId("testLogin")
                .password("password1234#")
                .birth(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        memberRequest = MemberRequest.builder()
                .name("testName")
                .tel("010-1111-1111")
                .email("test@test.com")
                .gender("M")
                .nickName("nickName")
                .loginId("testLogin")
                .password("password1234#")
                .birth(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        memberOptional = Optional.of(new Member());
        memberOptional.ifPresent(memberInfo -> {
            memberInfo.setMemberId(UUID.fromString("a999d6e0-4d8b-11ee-ac76-fd3617d1d2aa"));
            memberInfo.setName("testName");
            memberInfo.setTel("010-1111-1111");
            memberInfo.setEmail("test@test.com");
            memberInfo.setGender("M");
            memberInfo.setNickName("nickName");
            memberInfo.setLoginId("testLogin");
            memberInfo.setPassword("password1234#");
            memberInfo.setBirth(Timestamp.valueOf(LocalDateTime.now()));

        });

        session = new MockHttpSession();
    }

    @Test
    public void 고객_회원가입_성공() {
        given(repository.saveMember(memberRequest)).willReturn(1);

        service.saveMember(memberRequest);
    }

    @Test
    public void 고객_회원가입_실패() {
        given(repository.saveMember(memberRequest)).willReturn(0);
        assertThatCode(() -> {
            service.saveMember(memberRequest);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_회원가입_실패_중복아이디() {
        given(repository.findById(memberDTO.getLoginId())).willReturn(Optional.of(memberDTO.getLoginId()));

        assertThatCode(() -> {
            service.DuplicatedId(memberDTO.getLoginId());
        }).isInstanceOf(DuplicateIdException.class);
    }

    @Test
    public void 고객정보_조회_성공() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        assertThat(service.findByMember(memberDTO.getId().toString())).isEqualTo(memberOptional.get());
    }

    @Test
    public void 고객정보_조회_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(null);

        assertThatCode(() -> {
            service.findByMember(memberDTO.getId().toString());
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_로그인_성공() {
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest();
        memberLoginRequest.setLoginId("testLogin");
        memberLoginRequest.setPassword("password1234#");

        given(repository.findByIdAndPassword("testLogin", SHA256Util.encryptSHA256("password1234#")))
                .willReturn(memberOptional.get());

        assertThat(service.login(memberLoginRequest, session)).isEqualTo(memberOptional.get());
    }

    @Test
    public void 고객_로그인_실패_비밀번호_불일치() {
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest();
        memberLoginRequest.setLoginId("testLogin");
        memberLoginRequest.setPassword("testPassword");

        given(repository.findByIdAndPassword("testLogin", SHA256Util.encryptSHA256("testPassword")))
                .willReturn(null);

        assertThatCode(() -> {
            service.login(memberLoginRequest, session);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_로그아웃_성공() {
        // MockHttpSession 생성
        session.setAttribute(SessionUtil.LOGIN_MEMBER, "testMemberId");

        service.logout(session);

        // 세션에서 속성이 삭제되었는지 확인
        assertThat(session.getAttribute(SessionUtil.LOGIN_MEMBER)).isNull();
    }

    @Test
    public void 고객_비밀번호_변경_성공() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updatePassword(memberDTO.getLoginId(), SHA256Util.encryptSHA256("testPassword")))
                .willReturn(1);

        service.updatePassword(memberDTO.getId().toString(), "password1234#", "testPassword");
        Mockito.verify(repository, Mockito.times(1))
                .updatePassword(memberDTO.getLoginId(), SHA256Util.encryptSHA256("testPassword"));
    }

    @Test
    public void 고객_비밀번호_변경실패_함수확인() {
        Member member = new Member();
        member.setPassword(SHA256Util.encryptSHA256("currentPassword"));

        assertThatThrownBy(() -> {
            member.matchesAndChangePassword("failPassword","currentPassword", "newPassword");
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void 고객_비밀번호_변경_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updatePassword(memberDTO.getId().toString(), SHA256Util.encryptSHA256("newPassword")))
                .willReturn(0);

        assertThatThrownBy(() -> {
            service.updatePassword(memberDTO.getId().toString(), "password1234#", "newPassword");
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_닉네임_변경_성공() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updateNickName(memberDTO.getLoginId(), "updateNickName")).willReturn(1);

        service.updateNickName(memberDTO.getId().toString(), "updateNickName");
        Mockito.verify(repository, Mockito.times(1))
                .updateNickName(memberDTO.getLoginId(), "updateNickName");
    }

    @Test
    public void 고객_이전닉네임_변경닉네임_동일_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);

        assertThatThrownBy(() -> {
            service.updateNickName(memberDTO.getId().toString(), "nickName");
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_닉네임_변경_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updateNickName(memberDTO.getLoginId(), "updateNickName")).willReturn(0);

        assertThatThrownBy(() -> {
            service.updateNickName(memberDTO.getId().toString(), "updateNickName");
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_이메일주소_변경_성공() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updateEmail(memberDTO.getLoginId(), "update@test.com")).willReturn(1);

        service.updateEmail(memberDTO.getId().toString(), "update@test.com");
        Mockito.verify(repository, Mockito.times(1))
                .updateEmail(memberDTO.getLoginId(), "update@test.com");
    }

    @Test
    public void 고객_이메일주소_변경이메일_동일_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);

        assertThatThrownBy(() -> {
            service.updateEmail(memberDTO.getId().toString(), "test@test.com");
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_이메일주소_변경_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updateEmail(memberDTO.getLoginId(), "update@test.com")).willReturn(0);

        assertThatThrownBy(() -> {
            service.updateEmail(memberDTO.getId().toString(), "update@test.com");
        }).isInstanceOf(RuntimeException.class);

    }

    @Test
    public void 고객_휴대폰번호_변경_성공() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updateNumber(memberDTO.getLoginId(), "010-2222-2222")).willReturn(1);

        service.updatePhone(memberDTO.getId().toString(),"010-2222-2222");

        Mockito.verify(repository, Mockito.times(1))
                .updateNumber(memberDTO.getLoginId(), "010-2222-2222");
    }

    @Test
    public void 고객_휴대폰번호_변경휴대폰번호_동일_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);

        assertThatThrownBy(() -> {
            service.updatePhone(memberDTO.getId().toString(), "010-1111-1111");
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 고객_휴대폰번호_변경_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.updateNumber(memberDTO.getLoginId(), "010-2222-2222")).willReturn(0);

        assertThatThrownBy(() -> {
            service.updatePhone(memberDTO.getId().toString(),"010-2222-2222");
        }).isInstanceOf(RuntimeException.class);

    }

    @Test
    public void 고객_삭제_성공() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.deleteMember(memberDTO.getLoginId())).willReturn(1);

        service.deleteMember(memberDTO.getId().toString());
    }

    @Test
    public void 고객_삭제_실패() {
        given(repository.findByMember(memberDTO.getId().toString())).willReturn(memberOptional);
        given(repository.deleteMember(memberDTO.getLoginId())).willReturn(0);

        assertThatCode(() -> {
            service.deleteMember(memberDTO.getId().toString());
        }).isInstanceOf(RuntimeException.class);
    }

}
