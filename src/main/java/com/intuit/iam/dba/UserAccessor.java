package com.intuit.iam.dba;

import com.intuit.iam.model.db.User;
public interface UserAccessor {
    public User get(String userName);
    public void add(User user);
    public void update(User user);
    public void delete(String userName);
}
