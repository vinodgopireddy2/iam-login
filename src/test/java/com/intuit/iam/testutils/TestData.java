package com.intuit.iam.testutils;

import com.intuit.iam.model.db.LicenseTier;
import com.intuit.iam.model.db.Product;
import com.intuit.iam.model.db.User;
import com.intuit.iam.model.requestbody.RegisterRequestBody;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TestData {
    public static RegisterRequestBody postBodyInput() {
        return RegisterRequestBody.builder()
                .userName("vinodgopireddy2")
                .firstName("Vinod Reddy")
                .email("vinodgopireddy2@gmail.com")
                .lastName("Gopi Reddy")
                .password("qwerty@1A")
                .build();
    }
}
