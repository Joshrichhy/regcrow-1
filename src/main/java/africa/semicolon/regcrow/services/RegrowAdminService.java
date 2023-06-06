package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.UserRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.AdminResponse;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.exceptions.AdminNotFoundException;
import africa.semicolon.regcrow.exceptions.AdminRegistrationFailedException;
import africa.semicolon.regcrow.models.Admin;
import africa.semicolon.regcrow.models.BioData;
import africa.semicolon.regcrow.repositories.AdminRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static africa.semicolon.regcrow.utils.AppUtils.buildPageRequest;
import static africa.semicolon.regcrow.utils.ExceptionUtils.ADMIN_REGISTRATION_FAILED;
import static africa.semicolon.regcrow.utils.ExceptionUtils.ADMIN_WITH_ID_NOT_FOUND;
import static africa.semicolon.regcrow.utils.ResponseUtils.ADMIN_DELETED_SUCCESSFULLY;
import static africa.semicolon.regcrow.utils.ResponseUtils.ADMIN_REGISTRATION_SUCCESSFUL;


@Service
@AllArgsConstructor
public class RegrowAdminService implements AdminService{
    @Autowired
   private final ModelMapper modelMapper;

    @Autowired
    private final AdminRepository adminRepository;

    @Override
    public ApiResponse<?> register(UserRegistrationRequest userRegistrationRequest) throws AdminRegistrationFailedException {
        BioData bioData = modelMapper.map(userRegistrationRequest, BioData.class);
        Admin admin = new Admin();
                admin.setBioData(bioData);
      Admin savedAdmin =  adminRepository.save(admin);

        boolean isSavedAdmin = savedAdmin.getId() != null;
        if (!isSavedAdmin) throw new AdminRegistrationFailedException(String.format(ADMIN_REGISTRATION_FAILED, userRegistrationRequest.getEmail()));

        return ApiResponse.builder().message(ADMIN_REGISTRATION_SUCCESSFUL).build();
    }

    @Override
    public AdminResponse findAdminById(Long id) throws AdminNotFoundException {
     Optional <Admin> foundAdmin = adminRepository.findById(id);
             Admin admin = foundAdmin.orElseThrow(()-> new AdminNotFoundException(ADMIN_WITH_ID_NOT_FOUND));

        return AdminResponse.builder().id(admin.getId()).email(admin.getBioData().getEmail()).build();
    }

    @Override
    public List<AdminResponse> getAllAdmins(int page, int items) {
        Pageable pageable = buildPageRequest(page, items);
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        List<Admin> customers= adminPage.getContent();
       return customers.stream().map(x -> AdminResponse.builder().id(x.getId()).email(x.getBioData().getEmail()).build()).collect(Collectors.toList());

    }

    @Override
    public ApiResponse deleteAdminById(long id) {
        adminRepository.deleteById(id);
        return  ApiResponse.builder().message(String.format(ADMIN_DELETED_SUCCESSFULLY, id)).build();
    }

    @Override
    public void deleteAllAdmins() {
        adminRepository.deleteAll();
    }
}
