package com.rx.middlechannel.controller;

import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.rx.middlechannel.bean.IndexForm;
import com.rx.middlechannel.server.ClientService;
import com.rx.middlechannel.server.DeviceService;
import com.rx.middlechannel.utils.AESUtils;
import com.rx.middlechannel.utils.AjaxResult;
import com.rx.middlechannel.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： KeA
 * @date： 2021-05-15 17:43:00
 * @version: 1.0
 * @describe:
 */
@RestController
@RequestMapping("api/v1")
public class ApiController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DeviceService deviceService;



    @RequestMapping("client")
    public AjaxResult getClient(String cipher){
        IndexForm decode = decode(cipher);
        if (null == decode.getSalt()||!decode.getSalt().equals("rongxiaointerface")){
            return AjaxResult.error("验证码错误");
        }
        return AjaxResult.success(clientService.selectListForInterface());
    }

    @RequestMapping("device")
    public AjaxResult getDevice(String cipher){
        IndexForm decode = decode(cipher);
        if (null == decode.getSalt()||!decode.getSalt().equals("rongxiaointerface")){
            return AjaxResult.error("验证码错误");
        }
        return AjaxResult.success(deviceService.getListByClientCode(decode.getClientCode()));
    }

    @RequestMapping("open")
    public AjaxResult open(String cipher){
        IndexForm decode = decode(cipher);
        if (null == decode.getSalt()||!decode.getSalt().equals("rongxiaointerface")){
            return AjaxResult.error("验证码错误");
        }
        boolean success = deviceService.openClient(decode.getClientCode());
        return success?AjaxResult.success("开锁成功"):AjaxResult.error("获取锁具状态超时，请重试");
    }

    @RequestMapping("close")
    public AjaxResult close(String cipher){
        IndexForm decode = decode(cipher);
        if (null == decode.getSalt()||!decode.getSalt().equals("rongxiaointerface")){
            return AjaxResult.error("验证码错误");
        }
        boolean success = deviceService.closeClient(decode.getClientCode());
        return success?AjaxResult.success("关锁成功"):AjaxResult.error("获取锁具状态超时，请重试");
    }

    @RequestMapping("change")
    public AjaxResult change(String cipher){
        IndexForm decode = decode(cipher);
        if (null == decode.getSalt()||!decode.getSalt().equals("rongxiaointerface")){
            return AjaxResult.error("验证码错误");
        }
        String s = deviceService.changePassword(decode.getDeviceId(), decode.getOldPassword(), decode.getNewPassword());
        return AjaxResult.success(s);
    }

    private IndexForm decode(String cipher){
        String decrypt = null;

        try {
//            decrypt = RSAUtils.decrypt(cipher, RSAUtils.getPrivateKey());

            decrypt = AESUtils.decrypt(cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IndexForm indexForm = JSONUtil.toBean(decrypt, IndexForm.class);
        return  indexForm;
    }
}
