package com.rx.middlechannel.server.impl;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.bean.Device;
import com.rx.middlechannel.bean.DeviceVo;
import com.rx.middlechannel.common.cons.DownCommand;
import com.rx.middlechannel.mapper.DeviceMapper;
import com.rx.middlechannel.server.ClientService;
import com.rx.middlechannel.server.DeviceService;
import com.rx.middlechannel.server.strategy.impl.*;
import com.rx.middlechannel.utils.StatusMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 18:46:22
 * @version: 1.0
 * @describe:
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ClientService clientService;
    @Autowired
    private InitCmdStrategy initCmdStrategy;
    @Autowired
    private OpenLockStrategy openLockStrategy;
    @Autowired
    private LockedStrategy lockedStrategy;
    @Autowired
    private GetLockStatusStrategy getLockStatusStrategy;
    @Autowired
    private ChangePwdStrategy changePwdStrategy;

    @Override
    public Integer initialization(Device device) {
        //先写入硬件，接到返回后写入数据库
        DownCommand downCommand = new DownCommand();
        downCommand.setLowerPositionAddress(Integer.valueOf(device.getAddressCode()));
        downCommand.setFunctionCode(0x00);
        downCommand.setNumber(Integer.valueOf(device.getNumber()));
        downCommand.setData(device.getData());

        String uuid = clientService.selectById(device.getClientId());
        downCommand.setUuid(uuid);

        Device byClientIdAndAddressCode = deviceMapper.getByClientIdAndAddressCode(device);
        Integer integer = 0;
        if (byClientIdAndAddressCode == null){
            initCmdStrategy.encode(downCommand);

            if (device.getDeviceName() == null){
                device.setDeviceName("新设备"+device.getAddressCode());
            }
            device.setStatus(0);

            integer = deviceMapper.insertDevice(device);
        }
        return integer;
    }

    @Override
    public List<Client> getAllList() {
        List<Client> clients = clientService.selectList();
        for (Client client : clients){
            List<Device> devices = deviceMapper.getByClientId(client.getId());
            client.setDeviceList(devices);
        }
        return clients;
    }

    @Override
    public List<DeviceVo> getListByClientCode(String clientCode) {
        Client client = clientService.selectByClientCode(clientCode);
        return deviceMapper.getByClientIdForInterface(client.getId());
    }

    @Override
    public void open(Integer id) {
        Device device = deviceMapper.getById(id);
        DownCommand downCommand = new DownCommand();
        downCommand.setLowerPositionAddress(Integer.valueOf(device.getAddressCode()));
        downCommand.setFunctionCode(0x01);
        downCommand.setNumber(Integer.valueOf(device.getNumber()));
        downCommand.setData(device.getPassword());

        String uuid = clientService.selectById(device.getClientId());
        downCommand.setUuid(uuid);
        openLockStrategy.encode(downCommand);
    }

    @Override
    public boolean openClient(String clientCode) {
        Client client = clientService.selectByClientCode(clientCode);
        List<Device> devices = deviceMapper.getByClientId(client.getId());
        for (Device device : devices){
            DownCommand downCommand = new DownCommand();
            downCommand.setLowerPositionAddress(Integer.valueOf(device.getAddressCode()));
            downCommand.setFunctionCode(0x01);
            downCommand.setNumber(Integer.valueOf(device.getNumber()));
            downCommand.setData(device.getPassword());
            downCommand.setUuid(client.getUuid());
            openLockStrategy.encode(downCommand);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0 ;i<10;i++){
            List<Device> statusByClientId = deviceMapper.getStatusByClientId(client.getId());
            if (statusByClientId.size()==0){
                return true;
            }
            try {
                Thread.sleep(i*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void close(Integer id) {
        Device device = deviceMapper.getById(id);
        DownCommand downCommand = new DownCommand();
        downCommand.setLowerPositionAddress(Integer.valueOf(device.getAddressCode()));
        downCommand.setFunctionCode(0x02);
        downCommand.setNumber(Integer.valueOf(device.getNumber()));

        String uuid = clientService.selectById(device.getClientId());
        downCommand.setUuid(uuid);
        lockedStrategy.encode(downCommand);
    }

    @Override
    public boolean closeClient(String clientCode) {
        Client client = clientService.selectByClientCode(clientCode);
        List<Device> devices = deviceMapper.getByClientId(client.getId());
        for (Device device : devices){
            DownCommand downCommand = new DownCommand();
            downCommand.setLowerPositionAddress(Integer.valueOf(device.getAddressCode()));
            downCommand.setFunctionCode(0x02);
            downCommand.setNumber(Integer.valueOf(device.getNumber()));
            downCommand.setUuid(client.getUuid());
            lockedStrategy.encode(downCommand);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0 ;i<10;i++){
            List<Device> statusByClientId = deviceMapper.getStatusByClientId(client.getId());
            if (statusByClientId.size()>0){
                return true;
            }
            try {
                Thread.sleep(i*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String changePassword(String deviceId , String oldPassword , String newPassword) {
        Device device = deviceMapper.getById(Integer.valueOf(deviceId));
        String uuid = clientService.selectById(device.getClientId());
        if (!device.getPassword().equals(oldPassword)){
            return "原始密码不正确";
        }
        DownCommand downCommand = new DownCommand();
        downCommand.setLowerPositionAddress(Integer.valueOf(device.getAddressCode()));
        downCommand.setFunctionCode(0x03);
        downCommand.setNumber(Integer.valueOf(device.getNumber()));
        downCommand.setData(oldPassword+newPassword);
        downCommand.setUuid(uuid);
        changePwdStrategy.encode(downCommand);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0 ;i<10;i++){
            Boolean changePassword = StatusMap.getStatusByName("changePassword");
            if (changePassword != null && changePassword == true){
                StatusMap.removeStatus("changePassword");
                Device updatePass  = new Device();
                updatePass.setId(Integer.valueOf(deviceId));
                updatePass.setPassword(newPassword);
                deviceMapper.updatePasswordById(updatePass);
                return "修改成功";
            }
            try {
                Thread.sleep(i*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "修改失败";
    }

    @Override
    public void refresh(Integer id){
        Device device = deviceMapper.getById(id);
        DownCommand downCommand = new DownCommand();
        downCommand.setLowerPositionAddress(Integer.valueOf(device.getAddressCode()));
        downCommand.setFunctionCode(0x05);
        downCommand.setNumber(Integer.valueOf(device.getNumber()));
        downCommand.setData(device.getPassword());

        String uuid = clientService.selectById(device.getClientId());
        downCommand.setUuid(uuid);
        getLockStatusStrategy.encode(downCommand);
    }
}
