package com.wang.dao;

public interface EmployeeDao {
    //1.系统初始化：系统初始化后清空所有数据
    void init();

    //2.创建员工档案
    //实现创建员工功能，返回创建员工的完整工号，注意：创建员工后，员工为在职状态。
    String createEmployee(String employeeName, boolean employeeType);

    //3.更新员工工号
    boolean updateEmployeeID(String oldEmployeeID, int idNumber);

    //4.更新员工状态
    boolean updateStatus(String employeeID, boolean newStatus);

    //5.查询员工信息
    String getEmployeeIDByName(String employeeName);

}
