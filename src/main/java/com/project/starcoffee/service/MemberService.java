package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.member.MemberLoginRequest;
import com.project.starcoffee.controller.request.member.MemberRequest;
import com.project.starcoffee.controller.request.member.PhoneRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.member.Member;
import com.project.starcoffee.exception.DuplicateIdException;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.utils.SHA256Util;

import com.project.starcoffee.utils.SessionUtil;
import com.project.starcoffee.utils.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PushService pushService;

    @Autowired
    public MemberService(MemberRepository memberRepository, PushService pushService) {
        this.memberRepository = memberRepository;
        this.pushService = pushService;
    }

    /**
     * 회원 가입을 진행한다.
     * @param memberRequest 사용자가 입력한 고객정보
        * Validation 적용하여 비밀번호(영문/특수문자/숫자 포함 8~20자리), 휴대폰번호, 이메일 주소 형식이 올바르게 기입되어야 한다.
        * 이름, 로그인아이디, 비밀번호, 휴대폰번호, 이메일주소, 닉네임 값은 필수 값이다. (생년월일, 성별은 Null을 허용)
     */
    public void saveMember(MemberRequest memberRequest) {
        // 로그인 ID 중복 체크
        DuplicatedId(memberRequest.getLoginId());

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
     * @param loginId 로그인 아이디
     */
    public synchronized void DuplicatedId(String loginId) {
        memberRepository.findById(loginId)
                .ifPresent(member -> {
                    throw new DuplicateIdException("아이디가 이미 존재합니다.");
                });
    }


    /**
     * 로그인을 한다.
     *
     * @param loginRequest 로그인 요청정보
     * @return
     */
    public Member login(MemberLoginRequest loginRequest, HttpSession session) {

        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();

        String cryptoPassword = SHA256Util.encryptSHA256(password);
        Member member = memberRepository.findByIdAndPassword(loginId, cryptoPassword);

        if (member == null) {
            log.error("not found Member ERROR! {}", member);
            throw new RuntimeException("not found Member ERROR! 회원을 찾을 수 없습니다.");
        }

        SessionUtil.setMemberId(session, member.getMemberId());

        // 고객의 토큰정보 저장
        String token = TokenGenerator.generateToken();
        String memberId = member.getMemberId().toString();

        pushService.addMemberToken(token, memberId);

        return member;
    }

    /**
     * 회원의 로그아웃
     * @param session
     */
    public void logout(HttpSession session) {
        String memberId = SessionUtil.getMemberId(session);

        // 고객의 토큰정보 삭제
        pushService.deleteMemberToken(memberId);
        SessionUtil.logoutMember(session);
    }


    /**
     * 고객ID로 회원정보를 찾는다.
     * @param memberId 고객ID
     * @return
     */
    public Member findByMember(String memberId) {
        Optional<Member> memberOptional = memberRepository.findByMember(memberId);
        Member member = memberOptional.orElseThrow(() -> new RuntimeException("not found Member ERROR!"));

        return member;
    }

    /**
     * 회원의 비밀번호를 변경한다.
     * @param memberId 회원 ID
     * @param beforePw 변경 전 비밀번호
     * @param afterPw 변경 할 비밀번호
     */
    @Transactional
    public void updatePassword(String memberId, String beforePw, String afterPw) {
        Member memberInfo = findByMember(memberId);
        String memberPassword = memberInfo.getPassword();

        // 이전 비밀번호 확인 및 비밀번호 변경
        String enAfterPassword = memberInfo.matchesAndChangePassword(memberPassword, beforePw, afterPw);
        String loginId = memberInfo.getLoginId();

        int result = memberRepository.updatePassword(loginId, enAfterPassword);
        if (result != 1) {
            log.error("update Member ERROR! id={}, pw={}", loginId, enAfterPassword);
            throw new RuntimeException("비밀번호 업데이트가 실패하였습니다.");
        }
    }


    /**
     * 회원의 별칭을 변경할 수 있다.
     * 회원은 기존 별칭과 동일한 별칭을 사용할 경우에는 예외를 던지게 된다.
     * @param memberId 회원 ID
     * @param afterNickname 변경할 닉네임
     */
    @Transactional
    public void updateNickName(String memberId, String afterNickname) {
        Member memberInfo = findByMember(memberId);

        if (memberInfo.getNickName().equals(afterNickname)) {
            log.error("update nickName ERROR! nickName={}", afterNickname);
            throw new RuntimeException("변경할 닉네임이 이전 닉네임과 같습니다.");
        }

        String loginId = memberInfo.getLoginId();
        int result = memberRepository.updateNickName(loginId, afterNickname);
        if (result != 1) {
            log.error("update NickName ERROR! nickname={}", afterNickname);
            throw new RuntimeException("닉네임을 변경 할 수 없습니다.");
        }
    }

    /**
     * 회원의 이메일 주소를 변경한다.
     * 회원은 기존 이메일과 동일한 이메일을 사용할 경우에는 예외를 던지게 된다.
     * @param memberId 회원 ID
     * @param email 변경할 이메일 주소
     */
    @Transactional
    public void updateEmail(String memberId, String email) {
        Member memberInfo = findByMember(memberId);

        // 이전 이메일과 변경 이메일이 동일한지 확인
        if (memberInfo.getEmail().equals(email)) {
            log.error("same Email ERROR! email={}", email);
            throw new RuntimeException("변경할 이메일이 이전 이메일과 같습니다.");
        }

        String loginId = memberInfo.getLoginId();
        int result = memberRepository.updateEmail(loginId, email);
        if (result != 1) {
            log.error("update email ERROR! email={}", email);
            throw new RuntimeException("이메일을 변경 할 수 없습니다.");
        }
    }

    /**
     * 회원의 휴대폰번호를 변경한다.
     * @param memberId 회원 ID
     * @param afterPhoneNumber 변경할 휴대폰번호
     */
    @Transactional
    public void updatePhone(String memberId, String afterPhoneNumber) {
        Member memberInfo = findByMember(memberId);

        // 이전 휴대폰 번호와 변경 휴대폰번호가 동일한지 확인
        if (memberInfo.getTel().equals(afterPhoneNumber)) {
            log.error("same PhoneNumber ERROR! phoneNumber={}", afterPhoneNumber);
            throw new RuntimeException("변경할 휴대폰번호가 이전 휴대폰 번호와 같습니다.");
        }

        String loginId = memberInfo.getLoginId();
        int result = memberRepository.updateNumber(loginId, afterPhoneNumber);
        if (result != 1) {
            log.error("update PhoneNumber ERROR! PhoneNumber={}", afterPhoneNumber);
            throw new RuntimeException("휴대폰번호를 변경 할 수 없습니다.");
        }
    }

    /**
     * 회원의 Status 를 'DELETED' 로 변경한다.
     * @param memberId 탈퇴할 회원 ID
     */
    @Transactional
    public void deleteMember(String memberId) {
        Member memberInfo = findByMember(memberId);
        String loginId = memberInfo.getLoginId();

        int result = memberRepository.deleteMember(loginId);
        if (result != 1) {
            log.error("delete Member ERROR! id={}", loginId);
            throw new RuntimeException("delete Member ERROR! 회원을 삭제할 수 없습니다.");
        }
    }


}
