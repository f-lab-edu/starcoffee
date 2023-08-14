package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.PasswordRequest;
import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.response.member.LoginResponse;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.domain.member.MemberStatus;
import com.project.starcoffee.dto.MemberDTO;
import com.project.starcoffee.service.MemberService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/home")
    public String home() {
        return "ok";
    }

    /**
     * 회원가입 진행
     * 필수입력 정보에 누락이 있으면 NullPointerException 을 처리한다.
     *
     * @param memberInfo
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody MemberDTO memberInfo) {
        if (MemberDTO.hasNullDataBeforeSignUp(memberInfo)) {
            throw new NullPointerException("회원가입 시, 필수 데이터를 모두 입력해야합니다.");
        }

        memberService.saveMember(memberInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @NonNull MemberLoginRequest loginRequest,
                                               HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = loginRequest.getLoginId();
        String password = loginRequest.getPassword();
        LoginResponse loginResponse;

        Member memberInfo = memberService.login(id, password);
        // 회원을 찾지 못했을 경우
        if (memberInfo == null) {
            loginResponse = LoginResponse.FAIL;
            responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);

        // 회원을 찾았을 경우, 세션에 ID를 저장
        } else if (MemberStatus.DEFAULT.equals(memberInfo.getStatus())) {
            loginResponse = LoginResponse.success(memberInfo);
            SessionUtil.setLoginMemberId(session, id);
            responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);

        // 그 외 오류
        } else {
            log.error("Login ERROR! {}", responseEntity);
            throw new RuntimeException("로그인 에러입니다.");
        }

        return responseEntity;
    }


    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberPassword(@RequestBody PasswordRequest passwordRequest,
                                     HttpSession session) {

        String beforePassword = passwordRequest.getBeforePassword();
        String afterPassword = passwordRequest.getAfterPassword();

        String memberId = SessionUtil.getLoginMemberId(session);

        if (beforePassword == null || afterPassword == null) {
            throw new NullPointerException("패스워드를 입력하세요.");
        } else {
            memberService.updatePassword(memberId, beforePassword, afterPassword);
        }

    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Member> findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @DeleteMapping("/myInfo/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberInfo(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}
