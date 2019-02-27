package com.devglan.springbootsoapclient;

import com.devglan.springbootsoapclient.generated.blz.DetailsType;
import com.devglan.springbootsoapclient.generated.blz.GetBankResponseType;
import com.devglan.springbootsoapclient.generated.blz.GetBankType;
import com.devglan.springbootsoapclient.generated.blz.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BlzController {

    @Autowired
    private BlzServiceAdapter blzServiceAdapter;

    @GetMapping
    public DetailsType sum(@RequestParam String code){
        ObjectFactory objectFactory = new ObjectFactory();
        GetBankType type = new GetBankType();
        type.setBlz(code);
        GetBankResponseType response =  blzServiceAdapter.getBank("http://www.thomas-bayer.com/axis2/services/BLZService", objectFactory.createGetBank(type));
        return response.getDetails();
    }
}
