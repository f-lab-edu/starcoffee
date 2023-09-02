package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.exception.DuplicateIdException;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.utils.SHA256Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원 가입을 진행한다.
     * @param memberRequest 사용자가 입력한 고객정보
        * Validation 적용하여 비밀번호(영문/특수문자/숫자 포함 8~20자리), 휴대폰번호, 이메일 주소 형식이 올바르게 기입되어야 한다.
        * 이름, 로그인아이디, 비밀번호, 휴대폰번호, 이메일주소, 닉네임 값은 필수 값이다. (생년월일, 성별은 Null을 허용)
     */
    public void saveMember(MemberRequest memberRequest) {
        // 로그인 ID 중복 체크
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

        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();

        String cryptoPassword = SHA256Util.encryptSHA256(password);
        Optional<Member> memberInfo = memberRepository.findByIdAndPassword(loginId, cryptoPassword);

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

        if (!member.isPresent()) {
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
    public void updatePassword(String loginId, String beforePw, String afterPw) {
        Optional<Member> memberOptional = memberRepository.findById(loginId);
        Member memberInfo = memberOptional.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 이전 비밀번호 확인 및 비밀번호 변경
        String enAfterPassword = memberInfo.matchesAndChangePassword(beforePw, afterPw);

        int result = memberRepository.updatePassword(loginId, enAfterPassword);
        if (result != 1) {
            log.error("update Member ERROR! id={}, pw={}", loginId, enAfterPassword);
            throw new RuntimeException("비밀번호 업데이트가 실패하였습니다.");
        }
    }


    /**
     * 회원의 별칭을 변경할 수 있다.
     * 회원은 기존 별칭과 동일한 별칭을 사용할 경우에는 예외를 던지게 된다.
     * @param loginId 로그인 아이디
     * @param afterNickname 변경할 닉네임
     */
    public void updateNickName(String loginId, String afterNickname) {
        Optional<Member> memberInfo = memberRepository.findById(loginId);

        Optional<String> matchNickName = memberInfo.stream()
                .filter(m -> m.getNickName().equals(afterNickname))
                .map(Member::getEmail)
                .findFirst();

        matchNickName.ifPresent(n -> {
            log.error("same NickName ERROR! id={}", afterNickname);
            throw new RuntimeException("변경할 닉네임이 이전 닉네임과 같습니다.");
        });

        int result = memberRepository.updateNickName(loginId, afterNickname);
        if (result != 1) {
            log.error("update NickName ERROR! nickname={}", afterNickname);
            throw new RuntimeException("닉네임을 변경 할 수 없습니다.");
        }
    }

    /**
     * 회원의 이메일 주소를 변경한다.
     * 회원은 기존 이메일과 동일한 이메일을 사용할 경우에는 예외를 던지게 된다.
     * @param loginId
     * @param email
     */
    public void updateEmail(String loginId, String email) {
        Optional<Member> memberInfo = memberRepository.findById(loginId);

        // 이전 이메일과 변경 이메일이 동일한지 확인
        Optional<String> matchEmail = memberInfo.stream()
                .filter(m -> m.getEmail().equals(email))
                .map(Member::getEmail)
                .findFirst();

        matchEmail.ifPresent(e -> {
            log.error("same Email ERROR! email={}", email);
            throw new RuntimeException("변경할 이메일이 이전 이메일과 같습니다.");
        });

        int result = memberRepository.updateEmail(loginId, email);
        if (result != 1) {
            log.error("update email ERROR! email={}", email);
            throw new RuntimeException("이메일을 변경 할 수 없습니다.");
        }

    }

    /**
     * 회원의 Status 를 'DELETED' 로 변경한다.
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
