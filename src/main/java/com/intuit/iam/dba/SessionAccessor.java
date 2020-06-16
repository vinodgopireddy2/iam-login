package com.intuit.iam.dba;

import com.intuit.iam.model.db.Session;
public interface SessionAccessor {
    public Session get(String access_token);
    public void add(Session session);
    public void update(Session session);
    public void delete(String acceess_token);
}
