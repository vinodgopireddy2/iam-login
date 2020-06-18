package com.intuit.iam.model.requestbody;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestBody {
    @NonNull
    String userName;
    @NonNull
    String password;
}
