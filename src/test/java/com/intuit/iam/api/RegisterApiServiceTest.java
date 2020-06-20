package com.intuit.iam.api;

import com.google.gson.Gson;
import com.intuit.iam.api.register.RegisterApiService;
import com.intuit.iam.dba.UserAccessor;
import com.intuit.iam.model.db.User;
import com.intuit.iam.model.requestbody.RegisterRequestBody;
import com.intuit.iam.testutils.TestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class RegisterApiServiceTest {

    private RegisterRequestBody registerRequestBody;
    private User user;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private UserAccessor userAccessorMock;

    @InjectMocks
    RegisterApiService ras;

    @Test
    public void registerUserTest() throws InvocationTargetException, IllegalAccessException {
        registerRequestBody = TestData.postBodyInput();
        Response response = ras.register(registerRequestBody);
        doNothing().when(userAccessorMock).add(user);

        //Validate the response
        verify(userAccessorMock).add(userCaptor.capture());
        RegisterRequestBody r = new Gson().fromJson(String.valueOf(response.getEntity()), RegisterRequestBody.class);
        assertThat(r.getEmail(), is(registerRequestBody.getEmail()));
        assertThat(userCaptor.getValue().getEmailLowerCase(), is(registerRequestBody.getEmail().toLowerCase()));
    }
}
