package com.intuit.iam.dba;

import com.intuit.iam.model.db.User;
import com.mongodb.client.MongoCollection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Instant;

import static com.mongodb.client.model.Filters.eq;

@Singleton
public class UserMongoDbAccessor implements UserAccessor{
    private MongoCollection mongoCollection;

    @Inject
    public UserMongoDbAccessor(@Named("user") MongoCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public User get(String userName) {
        User user =
                (User) mongoCollection.find(eq("userName", userName), User.class).first();
        return user;
    }

    @Override
    public void add(User user) {
        updateMoDetails(user);
        mongoCollection.insertOne(user);
    }

    @Override
    public void update(User user) {
        updateMoDetails(user);
        mongoCollection.findOneAndReplace(eq("userName", user.getUserName().toLowerCase()), user);
    }

    @Override
    public void delete(String userName) {
        mongoCollection.findOneAndDelete(eq("userName", userName));
    }

    private void updateMoDetails(User user) {
        user.setLastUpdatedTime(Instant.now().getEpochSecond());
    }
}
