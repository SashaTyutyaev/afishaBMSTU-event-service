package ru.afishaBMSTU.controller.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;
import ru.afishaBMSTU.model.user.CustomUserDetails;
import ru.afishaBMSTU.service.request.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/requests")
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam Long eventId) {
        return requestService.createRequest(userDetails.getId(), eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return requestService.getRequests(userDetails.getId());
    }

    @PatchMapping("{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable Long requestId) {
        return requestService.cancelRequest(userDetails.getId(), requestId);
    }
}
