package com.project.starcoffee.controller;

import com.project.starcoffee.aop.session.SessionMemberId;
import com.project.starcoffee.controller.request.member.*;
import com.project.starcoffee.controller.response.member.LoginResponse;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.service.MemberService;
import com.project.starcoffee.utils.SessionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

@Tag(name = "Member Controller", description = "회원관리 API")
@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입 API",
               description = "회원가입 기능을 제공합니다. " +
                             "필수입력 정보에 누락이 있으면 GrolbalExceptionHandler 에서 각각의 필드에 대해서 처리합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid MemberRequest memberRequest) {
        memberService.saveMember(memberRequest);
    }

    @Operation(summary = "로그인 API", description = "로그인 기능을 제공합니다.")
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


    @Operation(summary = "회원조회 API", description = "회원ID를 기준으로 회원정보를 조회하는 기능을 제공합니다.")
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


    @Operation(summary = "회원 비밀번호 변경 API",
               description = "로그인 사용자가 비밀번호를 변경하는 기능을 제공합니다. " +
                             "이전 비밀번호를 입력하면 예외사항이 발생하게 됩니다.")
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

    @Operation(summary = "회원 닉네임 변경 API",
            description = "로그인 사용자가 닉네임을 변경하는 기능을 제공합니다." +
                    "이전 닉네임을 입력하면 예외사항이 발생하게 됩니다.")
    @PatchMapping("/nickname")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberNickName(@RequestBody NickNameRequest nickNameRequest, String memberId) {
        memberService.updateNickName(memberId, nickNameRequest.getAfterNickname());
    }


    @Operation(summary = "회원 이메일 변경 API",
            description = "로그인 사용자가 이메일을 변경하는 기능을 제공합니다. " +
                    "이전 이메일주소를 입력하면 예외사항이 발생하게 됩니다.")
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
    @Operation(summary = "회원 휴대폰번호 변경 API",
            description = "로그인 사용자가 휴대폰번호를 변경하는 기능을 제공합니다. " +
                    "이전 휴대폰번호를 입력하면 예외사항이 발생하게 됩니다.")
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
    @Operation(summary = "로그아웃 API",
            description = "로그인 사용자가 서비스에서 로그아웃 기능을 할 수 있는 기능을 제공합니다.")
    @GetMapping("/logout")
    public void logout(HttpSession session) {
        memberService.logout(session);
    }


    @Operation(summary = "회원 탈퇴 API",
            description = "회원인 사용자가 서비스에서 탈퇴하게 됩니다. " +
                    "데이터베이스에서 삭제되지 않으며 삭제태그로 변경됩니다.")
    @DeleteMapping("/member")
    @SessionMemberId
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberInfo(HttpSession session) {
        String memberId = SessionUtil.getMemberId(session);
        memberService.deleteMember(memberId);
        SessionUtil.logoutMember(session);
    }

}
