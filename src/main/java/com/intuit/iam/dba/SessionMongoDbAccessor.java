package com.intuit.iam.dba;

import com.intuit.iam.model.db.Session;
import com.mongodb.client.MongoCollection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.mongodb.client.model.Filters.eq;

@Singleton
public class SessionMongoDbAccessor implements SessionAccessor{
    private MongoCollection mongoCollection;

    @Inject
    public SessionMongoDbAccessor(@Named("session") MongoCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public Session get(String access_token) {
        Session session =
                (Session) mongoCollection.find(eq("access_token", access_token), Session.class).first();
        return session;
    }

    @Override
    public void add(Session session) {
        mongoCollection.insertOne(session);
    }

    @Override
    public void update(Session session) {
        mongoCollection.findOneAndReplace(eq("access_token", session.getAccess_token()), session);
    }

    @Override
    public void delete(String access_token) {
        mongoCollection.findOneAndDelete(eq("access_token", access_token));
    }

}
