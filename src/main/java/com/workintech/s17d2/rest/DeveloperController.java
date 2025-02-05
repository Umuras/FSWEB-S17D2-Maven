package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {


    public Map<Integer, Developer> developers;
    private Taxable taxable;

    @PostConstruct
    public void init()
    {
        developers = new HashMap<>();
    }


    @Autowired
    public DeveloperController(Taxable taxable)
    {
        this.taxable = taxable;
    }


    @GetMapping()
    public List<Developer> allDevelopers()
    {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer findDeveloper(@PathVariable int id)
    {
        return developers.get(id);
    }

    @PostMapping()
    //Bu şekilde başarılı bir işlemin http status kodunu 201 olarak döndürmesini sağlıyor.
    @ResponseStatus(code = HttpStatus.CREATED)
    public Developer saveDeveloper(@RequestBody Developer developer)
    {
        Developer newDeveloper;
        if(Experience.JUNIOR.equals(developer.getExperience()))
        {
            double netSalary = developer.getSalary() - (taxable.getSimpleTaxRate()*developer.getSalary()/100);
            newDeveloper = new JuniorDeveloper(developer.getId(),developer.getName(),netSalary);
            developers.put(newDeveloper.getId(),newDeveloper);
            return newDeveloper;
        }else if(Experience.MID.equals(developer.getExperience()))
        {
            double netSalary = developer.getSalary() - (taxable.getMiddleTaxRate()*developer.getSalary()/100);
            newDeveloper = new MidDeveloper(developer.getId(),developer.getName(),netSalary);
            developers.put(newDeveloper.getId(),newDeveloper);
            return newDeveloper;
        }else if(Experience.SENIOR.equals(developer.getExperience()))
        {
            double netSalary = developer.getSalary() - (taxable.getUpperTaxRate()*developer.getSalary()/100);
            newDeveloper = new SeniorDeveloper(developer.getId(),developer.getName(),netSalary);
            developers.put(newDeveloper.getId(),newDeveloper);
            return newDeveloper;
        }

        return developer;
    }

    @PutMapping("/{id}")
    public Developer update(@PathVariable int id, @RequestBody Developer developer)
    {
        developers.put(id,developer);
        return developers.get(id);
    }


    @DeleteMapping("/{id}")
    public Developer delete(@PathVariable int id)
    {
        Developer developer = developers.get(id);
        developers.remove(id);
        return developer;
    }
}
