package com.thiki.ec.component.sample;

import lombok.val;
import net.thiki.ec.component.exception.AssertionException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum Codes {

    OrderNotFound(10001);

    public int code;

    Codes(int code) {
        this.code = code;
    }

    private static Map<Integer, Codes> codes = Arrays.stream(values()).collect(Collectors.toMap(
           c -> c.code,
           c -> c
    ));

    public static Codes valueOf(int code){
        if (codes == null){
            throw new AssertionException("codes is not initialized yet.");
        }
        val c = codes.get(code);
        if (c == null){
            throw new AssertionException(String.format("codes with code[%d] is not found.", code));
        }
        return c;
    }
}
