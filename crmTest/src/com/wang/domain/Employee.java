package com.wang.domain;

import java.io.Serializable;

/**
 * 员工对象类
 */
public class Employee implements Serializable {
    //uniId            varchar(100) NOT NULL
    //employeeID      varchar(100) NULL
    //employeeName    varchar(100) NULL
    //employeeType    tinyint(1) NULL
    //status          tinyint(1) NULL

    private String employeeID;
    private String employeeName;
    private Boolean employeeType;
    private Boolean status;

    public Employee() {
    }

    public Employee(String employeeID, String employeeName, Boolean employeeType, Boolean status) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeType = employeeType;
        this.status = status;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Boolean getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Boolean employeeType) {
        this.employeeType = employeeType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID='" + employeeID + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeType=" + employeeType +
                ", status=" + status +
                '}';
    }
}
