package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount (AccountRequest request);
    AccountResponse toAccountResponse (Account entity);
    void updateAccount(@MappingTarget Account account, AccountRequest request);
}
