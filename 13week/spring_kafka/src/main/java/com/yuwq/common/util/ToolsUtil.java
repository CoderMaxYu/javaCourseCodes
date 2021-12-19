package com.yuwq.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * 类的功能描述：
 * 工具类
 *
 */
public class ToolsUtil {

    public synchronized static String getNextCode() {
        return UUID.randomUUID().toString();
    }


    public synchronized static Float getFee() {
        Random rand = new Random();
        float fee = rand.nextFloat();

        return fee;
    }


}
