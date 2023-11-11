package controllers;

import exceptions.UsernameAlreadyTaken;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import service.Baloot;
import exceptions.NotExistentUser;
import exceptions.IncorrectPassword;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    Baloot baloot;
    @InjectMocks
    AuthenticationController authenticationController;

    @Test
    void loginSuccessfully(){
        String username = "folan";
        String password = "bahman";
        var input = new HashMap<String, String>();
        input.put("username", username);
        input.put("password", password);
        assertThat(authenticationController.login(input).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void loginFailedBecauseUnauthorized() throws NotExistentUser, IncorrectPassword {
        String username = "folan";
        String password = "bahman";
        var input = new HashMap<String, String>();
        input.put("username", username);
        input.put("password", password);
        doThrow(IncorrectPassword.class).when(baloot).login(username, password);
        assertThat(authenticationController.login(input).getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    void loginFailedBecauseNotExistingUser() throws NotExistentUser, IncorrectPassword {
        String username = "folan";
        String password = "bahman";
        var input = new HashMap<String, String>();
        input.put("username", username);
        input.put("password", password);
        doThrow(NotExistentUser.class).when(baloot).login(username, password);
        assertThat(authenticationController.login(input).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void signupSuccessfully(){
        var input = new HashMap<String, String>();
        input.put("username", "folan");
        input.put("password", "bahman");
        input.put("email", "folani@gmail.com");
        input.put("birthdate", "folan rooz");
        input.put("address", "folan kadeh");
        assertThat(authenticationController.signup(input).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void signupFailedBecauseBadRequest() throws UsernameAlreadyTaken {
        var input = new HashMap<String, String>();
        input.put("username", "folan");
        input.put("password", "bahman");
        input.put("email", "folani@gmail.com");
        input.put("birthdate", "folan rooz");
        input.put("address", "folan kadeh");
        doThrow(UsernameAlreadyTaken.class).when(baloot).addUser(any(User.class));
        assertThat(authenticationController.signup(input).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
