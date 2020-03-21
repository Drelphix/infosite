package info.infosite.controller;

import info.infosite.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private PasswordRepository passwordRepository;
    private ChangesRepository changesRepository;
    private RegionRepository regionRepository;
    private AddressRepository addressRepository;

    @Autowired
    public void SetPasswordRepository(PasswordRepository passwordRepository){ this.passwordRepository=passwordRepository; }
    @Autowired
    public void SetRegionRepository(RegionRepository regionRepository){
        this.regionRepository=regionRepository;
    }
    @Autowired
    public void SetChangesRepository(ChangesRepository changesRepository){ this.changesRepository=changesRepository; }
    @Autowired
    public void SetAddressRepository(AddressRepository addressRepository){ this.addressRepository=addressRepository; }

 @GetMapping(value = "/")
    public String IndexPage(Model model){
        model.addAttribute("regions",regionRepository.findAll());
     return "index";
 }
 @GetMapping(value = "/show")
    public String ShowTables(Model model, @RequestParam (name="id") int id){
        model.addAttribute("regions",regionRepository.findAll());
        model.addAttribute("address",addressRepository.getOne(id));
        return "index";
 }
}
