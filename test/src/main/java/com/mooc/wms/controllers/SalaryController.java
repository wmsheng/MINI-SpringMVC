package com.mooc.wms.controllers;

import com.mooc.wms.beans.Autowired;
import com.mooc.wms.service.SalaryService;
import com.mooc.wms.web.mvc.Controller;
import com.mooc.wms.web.mvc.RequestMapping;
import com.mooc.wms.web.mvc.RequestParam;

/**
 * @author Bennett_Wang on 2020/5/3
 */
@Controller
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @RequestMapping("/get_salary.json")
    public Integer getSalary(@RequestParam("name") String name,
                             @RequestParam("experience") String experience) {
        return salaryService.calSalary(Integer.parseInt(experience));
    }
}
