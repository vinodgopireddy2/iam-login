package com.intuit.iam.validation.Validator;


import com.intuit.iam.validation.Rules.Rule;

import java.util.HashMap;

public class Validator <R extends Rule<S>, S>{
    private HashMap<R ,S> map = new HashMap<>();

    public void validate() throws Exception{
        for(Rule<S> rule : map.keySet()){
            rule.applyRule(map.get(rule));
        }
    }
    public void addRule(R rule, S data){
        this.map.put(rule, data);
    }
}
