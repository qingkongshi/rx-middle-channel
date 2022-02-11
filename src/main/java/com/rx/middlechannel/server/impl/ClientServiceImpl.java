package com.rx.middlechannel.server.impl;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.bean.ClientVo;
import com.rx.middlechannel.bean.Device;
import com.rx.middlechannel.mapper.ClientMapper;
import com.rx.middlechannel.mapper.DeviceMapper;
import com.rx.middlechannel.server.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 17:41:25
 * @version: 1.0
 * @describe:
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public List<Client> selectList() {
        return clientMapper.selectList();
    }

    @Override
    public List<ClientVo> selectListForInterface() {
        return clientMapper.selectListForInterface();
    }

    @Override
    public Integer updateClientName(Client client) {
        return clientMapper.updateClientName(client);
    }

    @Override
    public String selectById(Integer id) {
        return clientMapper.selectById(id);
    }

    @Override
    public Client selectByClientCode(String clientCode) {
        return clientMapper.selectByClientCode(clientCode);
    }
}
