package com.bookingflight.app.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.UpdatePasswordRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.mapper.AccountMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.service.MyProfileService;
import com.turkraft.springfilter.boot.Filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/api/my-profile")
@Data
@AllArgsConstructor
public class MyProfileController {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final MyProfileService myProfileService;

    // lay nguoi dung da dang nhap
    @GetMapping()
    public ResponseEntity<APIResponse<AccountResponse>> getUserLogin() {
        return myProfileService.getUserLogin();
    }

    @PutMapping("/update-password")
    public ResponseEntity<APIResponse<Void>> updatePassword(@RequestBody UpdatePasswordRequest request) {
        return myProfileService.updatePassword(request);
    }

    @GetMapping("/tickets")
    public ResponseEntity<APIResponse<ResultPaginationDTO>> getAllTickets(@Filter Specification<Ticket> spec,
            Pageable pageable) {
        APIResponse<ResultPaginationDTO> response = APIResponse.<ResultPaginationDTO>builder()
                .Code(200)
                .Message("Success")
                .data(myProfileService.getAllTickets(spec, pageable))
                .build();
        return ResponseEntity.ok().body(response);
    }
}
