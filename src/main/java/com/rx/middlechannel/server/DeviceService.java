package com.rx.middlechannel.server;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.bean.Device;
import com.rx.middlechannel.bean.DeviceVo;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 18:45:53
 * @version: 1.0
 * @describe:
 */
public interface DeviceService {

    Integer initialization(Device device);

    List<Client> getAllList();

    List<DeviceVo> getListByClientCode(String clientCode);

    void open(Integer id);

    boolean openClient(String clientCode);

    void close(Integer id);

    boolean closeClient(String clientCode);

    String changePassword(String deviceId , String oldPassword , String newPassword);

    void refresh(Integer id);
}
