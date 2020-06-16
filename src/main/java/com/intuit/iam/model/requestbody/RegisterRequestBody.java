package com.intuit.iam.model.requestbody;

import com.google.gson.annotations.Expose;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestBody {
    @Expose
    @NonNull
    String userName;
    @Expose
    @NonNull
    String email;
    @Expose
    @NonNull
    String firstName;
    @Expose
    @NonNull
    String lastName;
    @NonNull
    String password;
}
