package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.UserRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.AdminResponse;
import africa.semicolon.regcrow.exceptions.AdminNotFoundException;
import africa.semicolon.regcrow.exceptions.AdminRegistrationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.math.BigInteger.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RegrowAdminServiceTest {
    @Autowired
    private AdminService adminService;
    UserRegistrationRequest userRegistrationRequest;





    @BeforeEach
    void setUp() {
        adminService.deleteAllAdmins();
        userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setEmail("kuse");
        userRegistrationRequest.setPassword("1234");
    }

    @Test
    public void testAdminCanRegister() throws AdminRegistrationFailedException {
        var adminRegistrationResponse = adminService.register(userRegistrationRequest);
        assertThat(adminRegistrationResponse).isNotNull();
        String expected = "Admin Registration Successful";
        assertThat(adminRegistrationResponse.getMessage()).isEqualTo(expected);

    }

    @Test
    public void testGetAdminWithAdminId() throws AdminRegistrationFailedException, AdminNotFoundException {
        var adminRegistrationResponse = adminService.register(userRegistrationRequest);
        AdminResponse adminResponse = adminService.findAdminById(7L);
        String email = adminResponse.getEmail();
        assertThat(email).isEqualTo(userRegistrationRequest.getEmail());

    }

    @Test
    public void getAllAdminsTest() throws AdminRegistrationFailedException {
        adminService.register(userRegistrationRequest);
        adminService.register(userRegistrationRequest);
        List<AdminResponse> customers = adminService.getAllAdmins(ONE.intValue(), TEN.intValue());
        assertThat(customers.size()).isEqualTo(TWO.intValue());
    }

    @Test
    public void deleteAdminWithIdTest() throws AdminRegistrationFailedException {
        adminService.register(userRegistrationRequest);
        var adminResponse = adminService.deleteAdminById(3L);
        String expected = "Admin with id number 3 is Deleted Successfully";
        assertThat(adminResponse.getMessage()).isEqualTo(expected);
    }

    @Test
    public void deleteAllAdminsTest() throws AdminRegistrationFailedException, AdminNotFoundException {
        adminService.register(userRegistrationRequest);
        adminService.register(userRegistrationRequest);
       adminService.deleteAllAdmins();
        List <AdminResponse> adminResponses = adminService.getAllAdmins(1, 10);
        assertThat(ZERO).isEqualTo(adminResponses.size());
    }
}