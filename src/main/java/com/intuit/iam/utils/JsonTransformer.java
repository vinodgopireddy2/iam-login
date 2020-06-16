package com.intuit.iam.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTransformer  {

    private static Gson gson = new Gson();

    public static String render(Object model) {
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();
        return gson.toJson(model);
    }

}
