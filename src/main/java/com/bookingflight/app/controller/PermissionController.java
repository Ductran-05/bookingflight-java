package com.bookingflight.app.controller;

import jakarta.validation.Valid;

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

import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.PermissionRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.PermissionResponse;
import com.bookingflight.app.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

        final PermissionService permissionService;

        @GetMapping()
        public ResponseEntity<APIResponse<ResultPaginationDTO>> getAllPermissions(
                        @Filter Specification<Permission> spec, Pageable pageable) {
                APIResponse<ResultPaginationDTO> response = APIResponse.<ResultPaginationDTO>builder()
                                .Code(200)
                                .Message("Success")
                                .data(permissionService.getAllPermissions(spec, pageable))
                                .build();
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<APIResponse<PermissionResponse>> getPermissionById(@PathVariable("id") String id) {
                APIResponse<PermissionResponse> response = APIResponse.<PermissionResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(permissionService.getPermissionById(id))
                                .build();
                return ResponseEntity.ok(response);
        }

        @PostMapping()
        public ResponseEntity<APIResponse<PermissionResponse>> createPermission(
                        @RequestBody PermissionRequest request) {
                APIResponse<PermissionResponse> response = APIResponse.<PermissionResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(permissionService.createPermission(request))
                                .build();
                return ResponseEntity.ok(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<APIResponse<PermissionResponse>> updatePermission(
                        @RequestBody PermissionRequest request, @PathVariable("id") String id) {
                APIResponse<PermissionResponse> response = APIResponse.<PermissionResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(permissionService.updatePermission(id, request))
                                .build();
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<APIResponse<Void>> deletePermission(@PathVariable("id") String id) {
                APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                                .Code(204)
                                .Message("Delete permission successfully")
                                .build();
                permissionService.deletePermission(id);
                return ResponseEntity.ok(apiResponse); // return HTTP status 204 with no content.
        }
}
