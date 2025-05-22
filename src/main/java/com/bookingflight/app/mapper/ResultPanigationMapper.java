package com.bookingflight.app.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.bookingflight.app.dto.Panigation;
import com.bookingflight.app.dto.ResultPaginationDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Component
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultPanigationMapper {
    public ResultPaginationDTO toResultPanigationMapper(Page<?> page) {
        return ResultPaginationDTO.builder()
                .panigation(Panigation.builder()
                        .page(page.getNumber() + 1)
                        .pageSize(page.getSize())
                        .pages(page.getTotalPages())
                        .total(page.getTotalElements())
                        .build())
                .result(page.getContent())
                .build();
    }
}
