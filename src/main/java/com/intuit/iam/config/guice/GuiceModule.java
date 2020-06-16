package com.intuit.iam.config.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.intuit.iam.dba.SessionAccessor;
import com.intuit.iam.dba.SessionMongoDbAccessor;
import com.intuit.iam.dba.UserAccessor;
import com.intuit.iam.dba.UserMongoDbAccessor;
import com.intuit.iam.model.db.Session;
import com.intuit.iam.model.db.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.inject.Named;
import javax.inject.Singleton;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserAccessor.class).to(UserMongoDbAccessor.class);
        bind(SessionAccessor.class).to(SessionMongoDbAccessor.class);
    }

    @Provides
    @Singleton
    @Named("user")
    public MongoCollection getUserMongoCollection() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://mongo:27017"));
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase("iam")
                .withCodecRegistry(pojoCodecRegistry);
        MongoCollection collection = database.getCollection("users", User.class);
        return collection;
    }

    @Provides
    @Singleton
    @Named("session")
    public MongoCollection getSessionMongoCollection() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://mongo:27017"));
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase("iam")
                .withCodecRegistry(pojoCodecRegistry);
        MongoCollection collection = database.getCollection("sessions", Session.class);
        return collection;
    }

}
