package com.project.starcoffee.controller;

import com.project.starcoffee.controller.argument.IdPass;
import com.project.starcoffee.controller.request.PasswordRequest;
import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.controller.response.member.LoginResponse;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.domain.member.MemberStatus;
import com.project.starcoffee.dto.MemberDTO;
import com.project.starcoffee.service.MemberService;
import com.project.starcoffee.utils.SessionUtil;
import com.project.starcoffee.validation.password.ValidPassword;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 회원가입 진행
     * 필수입력 정보에 누락이 있으면 NullPointerException 을 처리한다.
     *
     * @param memberRequest
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid MemberRequest memberRequest) {
        memberService.saveMember(memberRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @IdPass @NonNull MemberLoginRequest loginRequest,
                                               HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        LoginResponse loginResponse;

        Optional<Member> memberInfo = memberService.login(loginRequest);

        loginResponse = LoginResponse.success(memberInfo.get());
        SessionUtil.setLoginMemberId(session, loginRequest.getLoginId());
        responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);


        return responseEntity;
    }



    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberPassword(@RequestBody @ValidPassword PasswordRequest passwordRequest,
                                     HttpSession session) {

        String beforePassword = passwordRequest.getBeforePassword();
        String afterPassword = passwordRequest.getAfterPassword();

        String memberId = SessionUtil.getLoginMemberId(session);

        /*
        유효성 검사에 실패하면 ConstraintViolationException 이 발생하고,
        해당 예외를 적절히 처리하거나 예외 핸들러를 등록하여 처리할 수 있다.
         */
        memberService.updatePassword(memberId, beforePassword, afterPassword);

    }

    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Member> findById(HttpSession session) {
        String loginId = SessionUtil.getLoginMemberId(session);
        return memberService.findById(loginId);
    }

    @DeleteMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberInfo(HttpSession session) {
        String loginId = SessionUtil.getLoginMemberId(session);
        memberService.deleteMember(loginId);
    }
}
