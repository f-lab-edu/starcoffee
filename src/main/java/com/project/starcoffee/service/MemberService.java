package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.domain.member.MemberStatus;
import com.project.starcoffee.dto.MemberDTO;
import com.project.starcoffee.exception.DuplicateIdException;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.utils.SHA256Util;
import com.project.starcoffee.validation.password.ValidPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void saveMember(MemberRequest memberRequest) {

        // ID 중복체크
        DuplicatedId(memberRequest);

        // 비밀번호 암호화 후, 저장
        memberRequest.setPassword(SHA256Util.encryptSHA256(memberRequest.getPassword()));

        // 회원가입 진행
        int result = memberRepository.saveMember(memberRequest);
        if (result != 1) {
            log.error("save Member ERROR! : {}", memberRequest);
            throw new RuntimeException("insert Member ERROR! 회원가입 메서드를 확인해주세요.\n"
                    + "Param : " + memberRequest);
        }
    }

    /**
     * 중복된 아이디가 있는지 확인한다.
     * @param memberInfo 회원정보
     */
    public void DuplicatedId(MemberRequest memberInfo) {
        boolean resultId = memberRepository.checkId(memberInfo.getLoginId()) == 1;
        if (resultId) {
            throw new DuplicateIdException("중복된 아이디 입니다.");
        }
    }


    /**
     * 로그인을 한다.
     *
     * @param loginRequest
     * @return
     */
    public Optional<Member> login(MemberLoginRequest loginRequest) {

        String id = loginRequest.getLoginId();
        String password = loginRequest.getPassword();

        String cryptoPassword = SHA256Util.encryptSHA256(password);
        Optional<Member> memberInfo = memberRepository.findByIdAndPassword(id, cryptoPassword);

        if (memberInfo.isEmpty()) {
            log.error("not found Member ERROR! {}", memberInfo);
            throw new RuntimeException("not found Member ERROR! 회원을 찾을 수 없습니다.");
        }

        return memberInfo;
    }


    /**
     * 로그인 아이디로 회원정보를 찾는다.
     * @param loginId 로그인 아이디
     * @return
     */
    public Optional<Member> findById(String loginId) {
        Optional<Member> member = memberRepository.findById(loginId);

        if (member.isPresent() == false) {
            log.error("not found Member ERROR! : {}", member);
            throw new RuntimeException("not found Member ERROR! 회원을 찾을 수 없습니다.\n"
                    + "Param : " +member);
        }

        return member;
    }

    /**
     * 회원의 비밀번호를 변경한다.
     * @param id 회원의 아이디
     * @param beforePw 변경 전 비밀번호
     * @param afterPw 변경 할 비밀번호
     */
    @Transactional
    public void updatePassword(String id, String beforePw, String afterPw) {
        String enBeforePassword = SHA256Util.encryptSHA256(beforePw);

        // 이전 비밀번호 확인
        checkBeforePassword(id, enBeforePassword);

        String enAfterPassword = SHA256Util.encryptSHA256(afterPw);
        int result = memberRepository.updatePassword(id, enAfterPassword);
        if (result != 1) {
            log.error("update Member ERROR! id={}, pw={}", id, enAfterPassword);
            throw new RuntimeException("비밀번호 업데이트가 실패하였습니다.");
        }
    }

    private void checkBeforePassword(String id, String enBeforePassword) {
        if (memberRepository.findByIdAndPassword(id, enBeforePassword) == null) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 회원의 Status 를 'DELETED' 로 변경한다.
     *
     * @param loginId 탈퇴할 아이디
     */
    @Transactional
    public void deleteMember(String loginId) {
        int result = memberRepository.deleteMember(loginId);
        if (result != 1) {
            log.error("delete Member ERROR! id={}", loginId);
            throw new RuntimeException("delete Member ERROR! 회원을 삭제할 수 없습니다.");
        }
    }



}
