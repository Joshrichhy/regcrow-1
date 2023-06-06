package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.UserRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.AdminResponse;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.exceptions.AdminNotFoundException;
import africa.semicolon.regcrow.exceptions.AdminRegistrationFailedException;

import java.util.List;


public interface AdminService {
    ApiResponse <?> register(UserRegistrationRequest userRegistrationRequest) throws AdminRegistrationFailedException;

    AdminResponse findAdminById(Long id) throws AdminNotFoundException;

    List<AdminResponse> getAllAdmins(int page, int items);

    ApiResponse deleteAdminById(long l);

    void deleteAllAdmins();
}
