package com.hxgis.authserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理 client details
 * Created by Cici on 2017/7/5.
 */
@RestController
public class ClientDetailsController {

    @Autowired
    private JdbcClientDetailsService clientDetailsService;

    /**
     * 添加 单点登录 app 客户端
     * @param clientDetails
     * @return
     */
    @RequestMapping(value = "/addClientDetails", method = RequestMethod.POST)
    public ClientDetails addClientDetails(@Validated @RequestBody ClientDetails clientDetails) {
        try {
            clientDetailsService.addClientDetails(clientDetails);
            return clientDetails;
        }catch (Exception ex){
            return null;
        }
    }
    /**
     * 修改 单点登录 app 客户端的 secret
     * @param clientId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/updateClientSecret", method = RequestMethod.POST)
    public boolean updateClientSecret(@Validated @RequestBody String clientId, @Validated @RequestBody String secret) {
        try {
            clientDetailsService.updateClientSecret(clientId, secret);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * 列举 单点登录 app 客户端列表
     * @return
     */
    @RequestMapping(value = "/listClientDetails", method = RequestMethod.GET)
    public List<ClientDetails> listClientDetails() {
        try {
            return clientDetailsService.listClientDetails();
        }catch (Exception ex){
            return null;
        }
    }
}
