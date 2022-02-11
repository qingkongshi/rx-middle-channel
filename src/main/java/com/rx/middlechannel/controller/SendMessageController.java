package com.rx.middlechannel.controller;

import com.rx.middlechannel.common.cons.DownCommand;
import com.rx.middlechannel.common.enums.FunctionEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-03-29 15:19:35
 * @version: 1.0
 * @describe:
 */
@RestController
@RequestMapping("send")
public class SendMessageController {

    @RequestMapping("down")
    public String send(DownCommand downCommand){
        Integer functionCode = downCommand.getFunctionCode();
        Map<Integer, String> allClazz = FunctionEnum.getAllClazz();
        String className = allClazz.get(functionCode);
        try {
            Class clazz = Class.forName(className);
            Method method = clazz.getMethod("encode",DownCommand.class);
            method.invoke(clazz.newInstance(),downCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

}
