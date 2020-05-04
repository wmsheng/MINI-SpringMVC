package com.mooc.wms.service;

import com.mooc.wms.beans.Bean;

/**
 * @author Bennett_Wang on 2020/5/4
 */
@Bean
public class SalaryService {
    public Integer calSalary(Integer experience) {
        return experience * 5000;
    }
}
