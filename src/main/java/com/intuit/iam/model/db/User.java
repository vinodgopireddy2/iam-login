package com.intuit.iam.model.db;

import com.google.gson.annotations.Expose;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //Object created timestamp
    @Expose
    @NonNull
    long createTime;

    @Expose
    @NonNull
    long lastUpdatedTime;

    //Last login timestamp
    @Expose
    @NonNull
    long lastLoginTime;

    //Account locked till
    @NonNull
    long accLockTime;
    @NonNull
    int failedLoginAttempts;

    @Expose
    @NonNull
    Map<String, LicenseTier> licenseMap; //Stores product to license tier relation

    @Expose
    @NonNull
    String userName;

    @Expose
    @NonNull
    String email;

    @NonNull
    String emailLowerCase;

    @Expose
    @NonNull
    String firstName;

    @Expose
    @NonNull
    String lastName;

    @NonNull
    String password; //securedPasswordHash
}
