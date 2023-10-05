package com.example.springtest.unit;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonPathTest {

    @Test
    public void testJsonPathLibrary() {
        String response = "{\"name\": \"Thiago\", " +
                "\"age\":38, \"items\": [1,2,3,4,5]}";

        Assertions.assertEquals(15,
                JsonPath.parse(response).read("$.items.sum()", Long.class));
    }
}
