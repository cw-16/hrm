package com.wang.controller;
import com.wang.dao.EmployeeDaoImpl;
import com.wang.service.EmployeeService;
import com.wang.service.EmployeeServiceImpl;
import org.junit.Test;


/**
 * 测试
 */
public class EmployeeController {
    private static EmployeeDaoImpl eop = new EmployeeDaoImpl();

    EmployeeService es = new EmployeeServiceImpl();

    /**
     * 系统初始化后清空所有数据
     */
    @Test
    public void init() {
        es.init();
    }

    /**
     * 实现创建新员工档案功能
     */
    @Test
    public void createEmployee() {
        String employeeID = es.createEmployee("cccc", true);
        System.out.println(employeeID);
    }


    /**
     * 实现创建新员工档案功能
     */
    @Test
    public void updateEmpID() {
        boolean isOK = es.updateEmployeeID("B6666", 1001);
        if (isOK){
            System.out.println(isOK+":更改成功...");
        }else {
            System.out.println(isOK+":更改失败...");

        }
    }

    /**
     * 实现更改员工在职状态
     */
    @Test
    public void updateEmpStatus() {
        boolean isOK = es.updateStatus("B1001", true);
        if (isOK){
            System.out.println(isOK+":更改成功...");
        }else {
            System.out.println(isOK+":更改失败...");

        }
    }

    /**
     * 实现更改员工在职状态
     */
    @Test
    public void getEmployeeIDByName() {
        String empID = es.getEmployeeIDByName("cccc");
        System.out.println(empID);
    }
}
