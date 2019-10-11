package com.hollykunge.util;

import java.util.UUID;

public class UUIDUtils {

    /**
     * 获取uuid
     *
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
