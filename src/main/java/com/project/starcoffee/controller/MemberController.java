package com.project.starcoffee.controller;

import com.project.starcoffee.controller.argument.LoginProcess;
import com.project.starcoffee.controller.request.PasswordRequest;
import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.controller.response.member.LoginResponse;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.service.MemberService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

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
     * @param loginRequest Id, Pw가 포함된 DTO
     * @param session 세션
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @NotNull MemberLoginRequest loginRequest,
                                               HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        LoginResponse loginResponse;

        Optional<Member> memberInfo = memberService.login(loginRequest);

        loginResponse = LoginResponse.success(memberInfo.get());
        SessionUtil.setLoginMemberId(session, loginRequest.getLoginId());
        responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);

        return responseEntity;
    }


    /**
     * 로그인 된 사용자가 비밀번호를 변경하고자 할 경우
     * @param passwordRequest 이전 비밀번호, 변경 비밀번호을 담은 DTO
     * @param session 세션
     */
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberPassword(@RequestBody @Valid PasswordRequest passwordRequest,
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

    /**
     * 로그인 아이디를 기준으로 회원정보를 찾는다.
     * @param session 세션
     * @return
     */
    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Member> findById(HttpSession session) {
        String loginId = SessionUtil.getLoginMemberId(session);
        Optional<Member> memberInfo = memberService.findById(loginId);

        return Stream.of(memberInfo)
                .filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * 회원이 탈퇴를 한다.
     * @param session 세션
     */
    @DeleteMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberInfo(HttpSession session) {
        String loginId = SessionUtil.getLoginMemberId(session);
        memberService.deleteMember(loginId);
    }

}
