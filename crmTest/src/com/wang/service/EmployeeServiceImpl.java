package com.wang.service;

import com.wang.dao.EmployeeDao;
import com.wang.dao.EmployeeDaoImpl;

public class EmployeeServiceImpl implements EmployeeService {

    EmployeeDao ed = new EmployeeDaoImpl();

    /**
     * 系统初始化后清空所有数据
     */
    @Override
    public void init() {
        ed.init();
    }

    /**
     * 实现创建新员工档案功能
     *
     * @param employeeName 员工姓名(输入侧保证合法性)。
     * @param employeeType 员工类型，
     * @return 返回该员工的完整工号字串
     */
    @Override
    public String createEmployee(String employeeName, boolean employeeType) {
        return ed.createEmployee(employeeName, employeeType);
    }

    /**
     * 实现变更工号的功能
     *
     * @param oldEmployeeID 待变更员工ID
     * @param idNumber      申请的新工号数字部分，为4位数数字，输入侧保证在1000-9999之间，
     *                      程序中建议对合法性进行检测
     * @return 准许变更，返回true， 否则，返回false)
     */
    @Override
    public boolean updateEmployeeID(String oldEmployeeID, int idNumber) {
        return ed.updateEmployeeID(oldEmployeeID,idNumber);
    }

    /**
     * 实现根据工号更新员工在职/离职状态信息功能。
     *
     * @param employeeID 员工完整工号（例如，z1002）
     * @param newStatus  将在职/离职状态修改为 newStatus
     * @return true or false 参见readme.md中的系统约束
     */
    @Override
    public boolean updateStatus(String employeeID, boolean newStatus) {
        return ed.updateStatus(employeeID,newStatus);
    }

    /**
     * 实现根据员工姓名查询员工完整工号功能。
     *
     * @param employeeName 员工姓名全拼写
     * @return 参见readme.md中的系统约束
     */
    @Override
    public String getEmployeeIDByName(String employeeName) {
        return ed.getEmployeeIDByName(employeeName);
    }
}
