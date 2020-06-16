package com.intuit.iam.model.requestbody;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestBody {
    @NonNull
    String userName;
    @NonNull
    String password;
}
