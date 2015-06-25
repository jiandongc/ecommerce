package order.controller;

import order.data.AnonCartItemData;
import order.domain.AnonCart;
import order.service.AnonCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/anoncarts")
public class AnonCartController {

    private AnonCartService anonCartService;

    @Autowired
    public AnonCartController(AnonCartService anonCartService){
        this.anonCartService = anonCartService;
    }

    @RequestMapping(method= POST)
    public AnonCart save(@RequestBody AnonCartItemData anonCartItemData){
        if(anonCartItemData.getCartUid() == null)
            return anonCartService.addFirstItem(anonCartItemData);
        else
            return anonCartService.addAnotherItem(anonCartItemData);
    }

}
