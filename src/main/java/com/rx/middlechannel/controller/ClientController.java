package com.rx.middlechannel.controller;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.server.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 16:00:56
 * @version: 1.0
 * @describe:
 */
@RestController
@RequestMapping("client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("list")
    public List<Client> getList(){
        return clientService.selectList();
    }

    @PutMapping("name")
    public Integer updateName(Client client){
        return clientService.updateClientName(client);
    }
}
