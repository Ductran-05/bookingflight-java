package com.bookingflight.app.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookingflight.app.domain.Role;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.RoleRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.RoleResponse;
import com.bookingflight.app.service.RoleService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    final RoleService roleService;

    @GetMapping()
    public ResponseEntity<APIResponse<ResultPaginationDTO>> getAllRoles(@Filter Specification<Role> spec,
            Pageable pageable) {
        APIResponse<ResultPaginationDTO> response = APIResponse.<ResultPaginationDTO>builder().Code(200)
                .Message("Success")
                .data(roleService.getAllRoles(spec, pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<RoleResponse>> getRoleById(@PathVariable("id") String id) {
        APIResponse<RoleResponse> response = APIResponse.<RoleResponse>builder()
                .Code(200)
                .Message("Success")
                .data(roleService.getRole(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<APIResponse<RoleResponse>> createRole(@RequestBody RoleRequest roleRequest) {
        APIResponse<RoleResponse> response = APIResponse.<RoleResponse>builder()
                .Code(200)
                .Message("Success")
                .data(roleService.createRole(roleRequest))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<RoleResponse>> updateRole(@PathVariable("id") String id,
            @RequestBody RoleRequest roleRequest) {
        APIResponse<RoleResponse> response = APIResponse.<RoleResponse>builder()
                .Code(200)
                .Message("Success")
                .data(roleService.updateRole(id, roleRequest))
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteRole(@PathVariable("id") String id) {
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .Code(204)
                .Message("Delete role successfully")
                .build();
        roleService.deleteRole(id);
        return ResponseEntity.ok(apiResponse); // return HTTP status 204 with no content.
    }
}
