package com.rx.middlechannel.controller;

import com.rx.middlechannel.bean.Device;
import com.rx.middlechannel.server.ClientService;
import com.rx.middlechannel.server.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-04-23 18:47:08
 * @version: 1.0
 * @describe:
 */
@RestController
@RequestMapping("device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("initialization")
    public Integer initialization(Device device){
        Integer initialization = deviceService.initialization(device);
        return initialization;
    }
    @GetMapping("list")
    public Map getAllList(){
        Map<String ,Object> result = new HashMap<>();
        result.put("code",200);
        result.put("message","success");
        List allList = deviceService.getAllList();
        result.put("list",allList);
        result.put("length",allList.size());
        return result;
    }
    @GetMapping("open/{id}")
    public Integer open(@PathVariable("id") int id){
        deviceService.open(id);
        return 200;
    }
    @GetMapping("close/{id}")
    public Integer close(@PathVariable("id") int id){
        deviceService.close(id);
        return 200;
    }
    @GetMapping("refresh/{id}")
    public Integer refresh(@PathVariable("id") int id){
        deviceService.refresh(id);
        return 200;
    }
}
