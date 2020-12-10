package com.wang.dao;

import com.wang.domain.Employee;
import com.wang.utils.HrmUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDaoImpl implements EmployeeDao {

    /**
     * 1.系统初始化后清空所有数据
     */
    @Override
    public void init() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //连接数据库
            conn = HrmUtil.getConn();
            //声明sql语句
            String sql = "DELETE FROM db_hrm";
            //预编译sql
            ps = conn.prepareStatement(sql);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            HrmUtil.close(conn, ps);
        }
    }

    /**
     * 2.实现创建新员工档案功能
     *
     * @param employeeName 员工姓名(输入侧保证合法性)。
     * @param employeeType 员工类型，
     * @return 返回该员工的完整工号字串
     */
    @Override
    public synchronized String createEmployee(String employeeName, boolean employeeType) {
        Connection conn = null;
        PreparedStatement ps = null;
        String empID = null;
        try {
            //连接数据库
            conn = HrmUtil.getConn();
            //声明sql
            //INSERT INTO db_hrm VALUES ('2','eee',1,1);
            String sql = "INSERT INTO db_hrm VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);

            //创建生成员工工号的方法
            empID = createID(employeeName, employeeType, conn);

            ps.setString(1, empID);
            ps.setString(2, employeeName);
            ps.setBoolean(3, employeeType);
            ps.setBoolean(4, true);

            int i = ps.executeUpdate();
            System.out.println(i);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            HrmUtil.close(conn, ps);
        }
        return empID;
    }


    /**
     * 2.1. 生成员工工号方法
     *
     * @param employeeName 员工姓名
     * @param employeeType 是否是正式员工
     * @param conn         数据库的连接对象
     * @return 返回员工的工号
     */
    public String createID(String employeeName, boolean employeeType, Connection conn) {
        //判断名字合法性
        char[] chars = employeeName.toCharArray();
        if (chars.length > 18) {
            System.out.println("名字长度非法！！！");
            return null;
        }
        for (int i = 0; i < chars.length; i++) {
            if (!(chars[i] >= 'a' && chars[i] <= 'z')) {
                System.out.println("名字包含空格、@、#等特殊字符！！！");
                return null;
            }
        }
        //定义变量存员工id
        String empID = null;
        //获取名字首字母
        char ch = (char) (employeeName.charAt(0) - 32);
        //判断是否未正式员工
        for (int i = 1000; i < 10000; i++) {
            //若是正式员工，findEmpID(i, conn)判断员工是否存在和是否已经离职
            if (employeeType && (findEmpID(i, conn))) {
                empID = String.valueOf(ch) + i;
                break;
            }
            //若是非正式员工
            if ((!employeeType) && (findEmpID(i, conn))) {
                empID = "WX" + ch + i;
                break;
            }
        }
        return empID;
    }


    /**
     * 2.1.1 // 4.1判断员工是否存在和是否已经离职
     *
     * @param i    需要判断的工号数字部分
     * @param conn 数据库的同一个连接
     * @return 返回是否存在或在职状态
     */
    private boolean findEmpID(int i, Connection conn) {
        boolean flag = false;
        ResultSet rs = null;
        try {
            //SELECT employeeID FROM db_hrm WHERE employeeName='wwwan';
            String sql = "SELECT * FROM db_hrm";
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String empIDStr = rs.getString(1);
                //在职状态参数
                boolean boolStatus = rs.getBoolean(4);

                if (empIDStr.length() == 5) {
                    String substring = empIDStr.substring(1);
                    if (substring.equals(i + "")) {
                        //若在职
                        if (boolStatus) {
                            flag = true;
                            break;
                        }
                    }
                } else {
                    String substring = empIDStr.substring(3);
                    if (substring.equals(i + "")) {
                        //若在职
                        if (boolStatus) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                assert rs != null;
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return !flag;
    }


    /**
     * 3.实现变更工号的功能
     *
     * @param oldEmployeeID 待变更员工ID
     * @param idNumber      申请的新工号数字部分，为4位数数字，输入侧保证在1000-9999之间，
     *                      程序中建议对合法性进行检测
     * @return 准许变更，返回true， 否则，返回false)
     */
    @Override
    public boolean updateEmployeeID(String oldEmployeeID, int idNumber) {

        Connection conn = null;
        PreparedStatement ps = null;

        boolean flag = false;
        //判断是否是正式员工,非正式员工共不能更改
        if (oldEmployeeID.length() == 7) {
            System.out.println("该员工属于非正式员工，无法更改！！！");
            return flag;
        }
        //正式员工截取字母部分保留

        try {
            //连接数据库
            conn = HrmUtil.getConn();
            //UPDATE db_hrm SET employeeID='W1002' WHERE employeeID='W1000';
            String sql = "UPDATE db_hrm SET employeeID=? WHERE employeeID=?";
            ps = conn.prepareStatement(sql);
            //changeID(oldEmployeeID,idNumber) 生成更新后的id方法；
            String changeID = changeID(oldEmployeeID, idNumber, conn);
            //判断工号是否存在
            if (changeID == null) {
                return flag;
            } else {
                ps.setString(1, changeID);
                ps.setString(2, oldEmployeeID);
            }


            int i = ps.executeUpdate();
            if (i == 1) {
                flag = true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            HrmUtil.close(conn, ps);
        }

        return flag;
    }

    /**
     * 3.1 生成更新后的id方法
     *
     * @param oldEmployeeID
     * @param idNumber
     * @param conn
     * @return
     */
    private String changeID(String oldEmployeeID, int idNumber, Connection conn) {
        String newID = null;

        try {
            String sql = "SELECT * FROM db_hrm WHERE employeeID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, oldEmployeeID);

            ResultSet rs = ps.executeQuery();
            //若不存在
            if (!rs.next()) {
                System.out.println("员工不存在！！！");
                return newID;
            }
            rs.beforeFirst();
            while (rs.next()) {
                String oldId = rs.getString(1);
                boolean empStatus = rs.getBoolean(4);
                //判断是否离职，若已离职
                if (!empStatus) {
                    System.out.println("该员工已离职，无法更改...");
                    return newID;
                }
                //若未离职,并且申请的工号数字部分合法；
                if (ifOk(idNumber, conn)) {
                    //若合法
                    char chPre = oldId.charAt(0);
                    newID = String.valueOf(chPre) + idNumber;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return newID;
    }

    /**
     * 3.2判断更改目标number是否合法
     *
     * @param idNumber
     * @param conn
     * @return
     */
    private boolean ifOk(int idNumber, Connection conn) {
        boolean flag = false;
        if (idNumber < 1000 || idNumber > 9999) {
            System.out.println("idNumber不合法，请重新申请...");
            return flag;
        }

        ResultSet rs = null;
        try {
            //SELECT employeeID FROM db_hrm WHERE employeeName='wwwan';
            String sql = "SELECT employeeID FROM db_hrm";
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String empIDStr = rs.getString(1).substring(1);
                //判断number是否存在
                if (empIDStr.equals(String.valueOf(idNumber))) {
                    System.out.println("您想变更的用户已存在，请重新输入需要变更的工号...:");
                    return flag;
                } else {
                    flag = true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return flag;
    }


    /**
     * 4.实现根据工号更新员工在职/离职状态信息功能。
     *
     * @param employeeID 员工完整工号（例如，z1002）
     * @param newStatus  将在职/离职状态修改为 newStatus
     * @return true or false 参见readme.md中的系统约束
     */
    @Override
    public boolean updateStatus(String employeeID, boolean newStatus) {
        boolean flag = false;
        try {
            Connection conn = HrmUtil.getConn();

            if (employeeID.length() == 5) {
                String substring = employeeID.substring(1);
                int intID = Integer.parseInt(substring);
                //判断用户是否存在或是否离职
                if (findEmpID(intID, conn)) {
                    System.out.println("用户不存在或已离职");
                    return flag;
                }
            } else {
                String substring = employeeID.substring(3);
                int intID = Integer.parseInt(substring);
                //判断用户是否存在或是否离职
                if (findEmpID(intID, conn)) {
                    System.out.println("用户不存在或已离职");
                    return flag;
                }
            }
            //若存在且在职
            //UPDATE db_hrm SET STATUS='0' WHERE employeeID='C1001';
            String sql = "UPDATE db_hrm SET STATUS=? WHERE employeeID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, newStatus);
            ps.setString(2, employeeID);

            int i = ps.executeUpdate();
            if (i == 1) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    /**
     * 5.实现根据员工姓名查询员工完整工号功能。
     *
     * @param employeeName 员工姓名全拼写
     * @return 参见readme.md中的系统约束
     */
    @Override
    public String getEmployeeIDByName(String employeeName) {
        ArrayList<Employee> list = new ArrayList<>();
        String returnID = null;
        try {
            Connection conn = HrmUtil.getConn();
            String sql = "SELECT * FROM db_hrm WHERE employeeName=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, employeeName);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("员工工号不存在...");
                return "unknown";
            }
            rs.beforeFirst();
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                boolean type = rs.getBoolean(3);
                boolean stat = rs.getBoolean(4);
                Employee emp = new Employee(id, name, type, stat);
                //若在职，则封装到集合
                if (stat) {
                    list.add(emp);
                }
            }

            //若集合大小为零，则查询到的员工全部离职
            if (list.size() == 0) {
                return "quit";
            }

            //若集合大小为1，直接返回该员工
            if (list.size() == 1) {
                returnID = list.get(0).getEmployeeID();
            }

            //否则进行遍历，找出number部分小的员工
            returnID = findMinID(list);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return returnID;
    }

    /**
     * 5.1 找出number部分小的员工
     *
     * @param list
     * @return
     */
    public String findMinID(ArrayList<Employee> list) {
        String substring = null;
        //假设第一个最小
        String employeeID = list.get(0).getEmployeeID();
        if (employeeID.length() == 5) {
            substring = employeeID.substring(1);
        } else {
            substring = employeeID.substring(3);
        }
        int idNumber = Integer.parseInt(substring);
        String finalID = employeeID;

        //然后进行比较
        for (int i = 1; i < list.size(); i++) {
            int idNum = 0;
            employeeID = list.get(i).getEmployeeID();

            if (employeeID.length() == 5) {
                substring = employeeID.substring(1);
                idNum = Integer.parseInt(substring);
                if (idNum < idNumber) {
                    idNumber = idNum;
                    finalID = employeeID;
                }
            } else {
                substring = employeeID.substring(3);
                idNum = Integer.parseInt(substring);
                if (idNum < idNumber) {
                    idNumber = idNum;
                    finalID = employeeID;
                }
            }
        }

        return finalID;
    }
}
