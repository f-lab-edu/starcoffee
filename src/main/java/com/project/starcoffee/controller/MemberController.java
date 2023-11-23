package com.project.starcoffee.controller;

import com.project.starcoffee.aop.SessionMemberId;
import com.project.starcoffee.controller.request.member.*;
import com.project.starcoffee.controller.response.member.LoginResponse;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.service.MemberService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원가입 진행
     * 필수입력 정보에 누락이 있으면 GrolbalExceptionHandler 에서 각각의 필드에 대해서 처리한다.
     *
     * @param memberRequest 사용자가 입력한 고객정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid MemberRequest memberRequest) {
        memberService.saveMember(memberRequest);
    }

    /**
     * 로그인 진행
     *
     * @param loginRequest ID, PW가 포함된 DTO
     * @param session      세션
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @NotNull MemberLoginRequest loginRequest,
                                               HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        LoginResponse loginResponse;

        Member member = memberService.login(loginRequest, session);
        loginResponse = LoginResponse.success(member.getMemberId());

        responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);

        return responseEntity;
    }


    /**
     * 회원ID를 기준으로 회원정보를 찾는다.
     *
     * @param memberId aop -> 회원 아이디
     * @return
     */
    @GetMapping("/member")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Member> findByMember(String memberId) {
        Member memberInfo = memberService.findByMember(memberId);

        return Stream.of(memberInfo)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * 로그인 된 사용자가 비밀번호를 변경하고자 할 경우
     *
     * @param passwordRequest 이전 비밀번호, 변경 비밀번호을 담은 DTO
     * @param memberId        aop -> 회원 아이디
     */
    @PatchMapping("/password")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberPassword(@RequestBody @Valid PasswordRequest passwordRequest, String memberId) {
        String beforePassword = passwordRequest.getBeforePassword();
        String afterPassword = passwordRequest.getAfterPassword();

        /*
        유효성 검사에 실패하면 ConstraintViolationException 이 발생하고,
        해당 예외를 적절히 처리하거나 예외 핸들러를 등록하여 처리할 수 있다.
         */
        memberService.updatePassword(memberId, beforePassword, afterPassword);
    }

    /**
     * 회원의 닉네임을 변경한다.
     *
     * @param nickNameRequest 변경할 닉네임
     * @param memberId        aop -> 회원 아이디
     */
    @PatchMapping("/nickname")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberNickName(@RequestBody NickNameRequest nickNameRequest, String memberId) {
        memberService.updateNickName(memberId, nickNameRequest.getAfterNickname());
    }

    /**
     * 회원의 이메일을 변경한다.
     * 이메일 주소가 형식이 맞지 않으면 예외를 던진다.
     *
     * @param emailRequest 변경할 이메일
     * @param memberId     aop -> 회원 아이디
     */
    @PatchMapping("/email")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberEmail(@RequestBody @Valid EmailRequest emailRequest, String memberId) {
        memberService.updateEmail(memberId, emailRequest.getAfterEmail());
    }

    /**
     * 회원의 휴대폰번호를 변경한다.
     *
     * @param phoneRequest 변경할 휴대폰번호
     * @param memberId     aop -> 회원 아이디
     */
    @PatchMapping("/phoneNumber")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberPhone(@RequestBody @Valid PhoneRequest phoneRequest, String memberId) {
        memberService.updatePhone(memberId, phoneRequest.getAfterPhoneNumber());
    }

    /**
     * 회원 로그아웃
     *
     * @param session 세션
     */
    @GetMapping("/logout")
    public void logout(HttpSession session) {
        memberService.logout(session);
    }

    /**
     * 회원이 탈퇴를 한다.
     *
     * @param session 세션
     */
    @DeleteMapping("/member")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberInfo(HttpSession session) {
        String memberId = SessionUtil.getMemberId(session);
        memberService.deleteMember(memberId);
        SessionUtil.logoutMember(session);
    }

    @GetMapping("/session")
    public String getSessionAttribute(WebSession session) {
        // WebSession을 통해 세션 데이터에 접근할 수 있습니다.
        String value = session.getAttribute("key");
        return "Session Attribute Value: " + value;
    }

}
