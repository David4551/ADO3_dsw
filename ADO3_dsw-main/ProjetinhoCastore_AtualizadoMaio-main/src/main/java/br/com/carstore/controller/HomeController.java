package br.com.carstore.controller;

import br.com.carstore.dto.CarDTO;
import br.com.carstore.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private CarService service;

    @Autowired
    public HomeController(CarService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("carDTO", new CarDTO());
        return "redirect:/cars";
    }



    @GetMapping("/cars")
    public String getCars(Model model) {
        List<CarDTO> allCars = service.findAll();
        model.addAttribute("cars", allCars);
        return "public/dashboard";
    }



}
