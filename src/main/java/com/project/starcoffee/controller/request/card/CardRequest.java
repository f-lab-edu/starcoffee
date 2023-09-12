package com.project.starcoffee.controller.request.card;

import com.project.starcoffee.utils.SessionUtil;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CardRequest {

    private String loginId;

    @NotNull(message = "카드 이름을 입력해주세요.")
    @Size(min = 2, max = 8, message = "이름을 2~8자까지 입력가능합니다.")
    private String cardName;    // 카드 이름

    @NotNull
    @Size(min = 16, max = 16, message = "카드번호는 총 16자리 입니다.")
    private String cardNumber;  // 총 12자리(4자리씩, 하이픈 포함)

    @NotNull
    @Size(min = 8, max = 8, message = "핀번호는 총 8자리 입니다.")
    private String pinNumber;   //  Pin 번호, 총 8자리
}
