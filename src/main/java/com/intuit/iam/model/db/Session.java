package com.intuit.iam.model.db;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.intuit.iam.serde.DateTimeDeserializer;
import com.intuit.iam.serde.DateTimeSerializer;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Session {
    @NonNull
    String access_token; //8hrs
    @NonNull
    String refresh_token; //30 days

    @NonNull
    String userName;

    long createTime;

    //Access token expiration, update this new expiration when we "create new access_token by providing refresh_token"
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonSerialize(using = DateTimeSerializer.class)
    Instant idleTimeExpiration;

    @NonNull
    String clientIpAddress;
    @NonNull
    String userAgent;
}