package com.rx.middlechannel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-03-31 09:44:08
 * @version: 1.0
 * @describe:
 */
@Controller
@RequestMapping("")
public class IndexController {

    @GetMapping(value={"","index"})
    public String index(){
        return "index2";
    }

    @GetMapping(value={"index2"})
    public String index2(){
        return "index";
    }

    @GetMapping("change")
    public String changeDevice(){
        return "changeDevice";
    }


}
