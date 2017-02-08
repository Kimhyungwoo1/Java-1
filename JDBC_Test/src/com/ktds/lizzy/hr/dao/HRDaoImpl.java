package com.ktds.lizzy.hr.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ktds.lizzy.hr.vo.*;

public class HRDaoImpl implements HRDao {
	
 @Override
    public List<EmployeesVO> getAllEmployees() {
        // 1. Oracle driver loading
        try {
            // Instances of the class Class represent classes and interfaces in
            // a running Java application.
            // �ܿ��
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("����Ŭ ����̹� �ε� ����. �ý��� ����.");
            return null;
        }
        // 2. "DB�� �������ϰ�, ������ ������(����), �� ����� �޾ƿ���"
        // 2-1. JDBC Instance ����
        /**
         * java +������(����,�����Ͱ� Stream�������� ��ȯ. �޸𸮸� ȸ������ ������ x)+ oracle ȸ���� �ϱ�����
         * �Ʒ��Ͱ��� �ۿ� �����ϰ� null�� �����صа�.
         */
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        // 2-2. Oracle instance�� ����
        // oracleUrl �ܿ��: windows�� localhost / ����Ŭ port: 1521
        // mac os : �ڽ��� IP�ּ�
        String oracleUrl = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            conn = DriverManager.getConnection(oracleUrl, "hr", "chzhqhf486");
            // 2-3. Query�� �����.
            String query =
                    // ����ǥ�� ���� ���̿��� ������� ���ֱ�. error �߻�.
                    " SELECT  " + " EMPLOYEE_ID, FIRST_NAME, LAST_NAME,  " + "   EMAIL, PHONE_NUMBER, HIRE_DATE,  "
                            + "   JOB_ID, SALARY, COMMISSION_PCT,  " + "   MANAGER_ID, DEPARTMENT_ID "
                            + " FROM HR.EMPLOYEES ";
            // 2-4. Query�� �����Ѵ�.
            stmt = conn.prepareStatement(query);
            // 2-5. Query ���� ����� ���´�.
            rs = stmt.executeQuery();
            // 2-5-1. Query ���� ����� List��ü�� �Ҵ��Ѵ�.
            // next() : ���� �� ���´�. / ���پ� List�� �־��ֱ�.
            EmployeesVO employeesVO = null;
            List<EmployeesVO> employees = new ArrayList<EmployeesVO>();
            while (rs.next()) {
                // 2-5-2 row�� ������ employeesVO�� �����Ѵ�.
                employeesVO = new EmployeesVO();
                employeesVO.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
                employeesVO.setCommissionPct(rs.getDouble("COMMISSION_PCT"));
                employeesVO.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
                /**
                 * java.sql.SQLException: �������� �� �̸� ����Ŭ �ν��Ͻ��� ���� ����. �ý��� ����. at
                 * oracle.jdbc.driver.OracleStatement.getColumnIndex(OracleStatement.java:3724)
                 * at
                 * oracle.jdbc.driver.OracleResultSetImpl.findColumn(OracleResultSetImpl.java:2799)
                 * at
                 * oracle.jdbc.driver.OracleResultSet.getString(OracleResultSet.java:498)
                 * at
                 * com.ktds.ronanam.hr.dao.HRDaoImpl.getAllEmployees(HRDaoImpl.java:70)
                 * at com.ktds.ronanam.hr.Main.main(Main.java:20) Exception in
                 * thread "main" java.lang.NullPointerException at
                 * com.ktds.ronanam.hr.Main.main(Main.java:21)
                 */
                // �÷����� �߸� ����. ��Ÿ
                employeesVO.setEmail(rs.getString("EMAIL"));
                employeesVO.setFirstName(rs.getString("FIRST_NAME"));
                employeesVO.setHireDate(rs.getString("HIRE_DATE"));
                /**
                 * java.sql.SQLException: ���� ǥ��� ��ȯ�� �� �����ϴ� ����Ŭ �ν��Ͻ��� ���� ����. �ý���
                 * ����. at
                 * oracle.jdbc.driver.CharCommonAccessor.getInt(CharCommonAccessor.java:147)
                 * at
                 * oracle.jdbc.driver.T4CVarcharAccessor.getInt(T4CVarcharAccessor.java:830)
                 * at
                 * oracle.jdbc.driver.OracleResultSetImpl.getInt(OracleResultSetImpl.java:942)
                 * at
                 * oracle.jdbc.driver.OracleResultSet.getInt(OracleResultSet.java:438)
                 * at
                 * com.ktds.ronanam.hr.dao.HRDaoImpl.getAllEmployees(HRDaoImpl.java:83)
                 * at com.ktds.ronanam.hr.Main.main(Main.java:20) Exception in
                 * thread "main" java.lang.NullPointerException at
                 * com.ktds.ronanam.hr.Main.main(Main.java:21)
                 */
                // Job_id�� String�ε� java���� int�� ����
                employeesVO.setJobId(rs.getString("JOB_ID"));
                employeesVO.setLastName(rs.getString("LAST_NAME"));
                employeesVO.setManagerId(rs.getInt("MANAGER_ID"));
                employeesVO.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                employeesVO.setSalary(rs.getInt("SALARY"));
                // 2-5-3 employees�� employeesVO�� add�Ѵ�.
                employees.add(employeesVO);
            }
            /**
             * return�� ������, return���ϰ� ���ᰡ �Ǵµ�, finally�� ������ finally�� �����ϰ� �����Ѵ�.
             * while()������ �� �о������, list�� return������ϴµ�, ���� ���⿡ ���Ϲ��� �������� �ʾƼ�
             * nullPointerException �߻�
             */
            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("����Ŭ �ν��Ͻ��� ���� ����. �ý��� ����.");
            return null; // try/catch�� �̱� ������ �� ����Ǿ ��ȯ �ƴϾ ��ȯ�Ѵ�.
        } finally {
            // ������ �ݴ� ������ �ݾ�����Ѵ�.
            // ����ó�� ���� �ɾ�����Ѵ�.
            // rs�� �ݴٰ� ���������� �����ϰ� stmt�� �ݾƶ�. �� �ݴٰ� �������� �����ϰ� conn�� �ݾƶ�
            // �׷��� ���� �� �ִ� error�� nullpointerException
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        /**
         * �� ó���� null�� �Ҵ�Ǿ��ִµ�, conn = class.getName();�ϴٰ� ������ �߻��ϸ� catch������
         * �̵��ϰ� �Ǵµ� �̶��� if���� �ɸ��� �ʱ� ������ �ƹ��͵� ���� �ʰ� ����. �������� �����ϰ��� null�� �ƴϱ� ������
         * �ڿ��� ȸ�� �� close();
         */
    }
	
	@Override
	public List<DepartmentVO> getAllDepartments() {
		//1. Oracle Driver Loading.
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); //ACL : Auto Class Loading = ��ü�� �ҷ��´�. (�޸𸮿� ��ü�� �÷���)
		} catch(ClassNotFoundException e) {
			System.out.println("����Ŭ ����̹� �ε� ����! �ý��� ����");
			return null;
		}
		
		//2. JDBC Instance ����
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		//3. Oracle Instance�� ����
		try {
			String oracleUrl = "jdbc:oracle:thin:@localhost:1521:XE"; // windows : loacalhost , mac : �ڱ� ip �ּ�, oracle port num = 1521
			conn = DriverManager.getConnection(oracleUrl, "hr", "chzhqhf486");
			
			//4. ������ �����.
			String query = " SELECT  " +
					"   DEPARTMENT_ID, DEPARTMENT_NAME, MANAGER_ID,  " +
					"   LOCATION_ID " +
					"   FROM HR.DEPARTMENTS" +
					"   ORDER BY DEPARTMENT_ID DESC"; // ���� -> Stringȭ : ctrl + m
		
			//5. ������ �����Ѵ�.
			stmt = conn.prepareStatement(query);
			
			//6. ������ �������� ���´�.
			rs = stmt.executeQuery();
			
			//6-1. ������ �������� List ��ü�� �Ҵ��Ѵ�.
			
			DepartmentVO departmentVO = null;
			List<DepartmentVO> departments = new ArrayList<DepartmentVO>();
			
			while (rs.next()) {
				//6-2. ROW�� ������ employees�� �����Ѵ�.
				
				departmentVO = new DepartmentVO();
				departmentVO.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
				departmentVO.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
				departmentVO.setManagerId(rs.getInt("MANAGER_ID"));
				departmentVO.setLocationId(rs.getInt("LOCATION_ID"));
				
				//6-3. employees�� employeesVO�� add�Ѵ�.
				departments.add(departmentVO);
			}
			//7. return �Ѵ�.
			return departments;
			
		} catch(SQLException e) {
			System.out.println("Oracle �ν��Ͻ��� �������� ���߽��ϴ�. �ý��� ����");
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch(SQLException e) {
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch(SQLException e) {
				
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				
			}
		}
		
	}

	@Override
	public List<EmployeesVO> getAllEmployeesWithDepartments() {
		 // 1. Oracle driver loading
        try {
            // Instances of the class Class represent classes and interfaces in
            // a running Java application.
            // �ܿ��
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("����Ŭ ����̹� �ε� ����. �ý��� ����.");
            return null;
        }
        // 2. "DB�� �������ϰ�, ������ ������(����), �� ����� �޾ƿ���"
        // 2-1. JDBC Instance ����
        /**
         * java +������(����,�����Ͱ� Stream�������� ��ȯ. �޸𸮸� ȸ������ ������ x)+ oracle ȸ���� �ϱ�����
         * �Ʒ��Ͱ��� �ۿ� �����ϰ� null�� �����صа�.
         */
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        // 2-2. Oracle instance�� ����
        // oracleUrl �ܿ��: windows�� localhost / ����Ŭ port: 1521
        // mac os : �ڽ��� IP�ּ�
        String oracleUrl = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            conn = DriverManager.getConnection(oracleUrl, "hr", "chzhqhf486");
            // 2-3. Query�� �����.
            String query =
            		// ����ǥ�� ���� ���̿��� ������� ���ֱ�. error �߻�.
            		" SELECT  E.EMPLOYEE_ID, E.FIRST_NAME, E.LAST_NAME,  " 
            				+ "   E.EMAIL, E.PHONE_NUMBER, E.HIRE_DATE,  "
            		        + "   E.JOB_ID, E.SALARY, E.COMMISSION_PCT,  " 
            				+ "   E.MANAGER_ID, E.DEPARTMENT_ID,  "
            		        + "   D.DEPARTMENT_ID D_DEPARTMENT_ID, D.DEPARTMENT_NAME, D.MANAGER_ID, D.LOCATION_ID"
            		        + " FROM EMPLOYEES E, "
            		        + " DEPARTMENTS D "
            		        + " WHERE E.DEPARTMENT_ID = D.DEPARTMENT_ID ";
            // 2-4. Query�� �����Ѵ�.
            stmt = conn.prepareStatement(query);
            // 2-5. Query ���� ����� ���´�.
            rs = stmt.executeQuery();
            // 2-5-1. Query ���� ����� List��ü�� �Ҵ��Ѵ�.
            // next() : ���� �� ���´�. / ���پ� List�� �־��ֱ�.
            EmployeesVO employeesVO = null;
           
            List<EmployeesVO> employees = new ArrayList<EmployeesVO>();
            while (rs.next()) {
                // 2-5-2 row�� ������ employeesVO�� �����Ѵ�.
                employeesVO = new EmployeesVO();
                
                employeesVO.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
                employeesVO.setCommissionPct(rs.getDouble("COMMISSION_PCT"));
                employeesVO.setDepartmentId(rs.getInt("DEPARTMENT_ID"));
               
                // �÷����� �߸� ����. ��Ÿ
                employeesVO.setEmail(rs.getString("EMAIL"));
                employeesVO.setFirstName(rs.getString("FIRST_NAME"));
                employeesVO.setHireDate(rs.getString("HIRE_DATE"));
               
                // Job_id�� String�ε� java���� int�� ����
                employeesVO.setJobId(rs.getString("JOB_ID"));
                employeesVO.setLastName(rs.getString("LAST_NAME"));
                employeesVO.setManagerId(rs.getInt("MANAGER_ID"));
                employeesVO.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                employeesVO.setSalary(rs.getInt("SALARY"));
                employeesVO.getDepartments().setDepartmentId(rs.getInt("D_DEPARTMENT_ID"));
                employeesVO.getDepartments().setDepartmentName(rs.getString("DEPARTMENT_NAME"));
                employeesVO.getDepartments().setManagerId(rs.getInt("MANAGER_ID"));
                employeesVO.getDepartments().setLocationId(rs.getInt("LOCATION_ID"));
                
                // 2-5-3 employees�� employeesVO�� add�Ѵ�.
                employees.add(employeesVO);
            }
            /**
             * return�� ������, return���ϰ� ���ᰡ �Ǵµ�, finally�� ������ finally�� �����ϰ� �����Ѵ�.
             * while()������ �� �о������, list�� return������ϴµ�, ���� ���⿡ ���Ϲ��� �������� �ʾƼ�
             * nullPointerException �߻�
             */
            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("����Ŭ �ν��Ͻ��� ���� ����. �ý��� ����.");
            return null; // try/catch�� �̱� ������ �� ����Ǿ ��ȯ �ƴϾ ��ȯ�Ѵ�.
        } finally {
            // ������ �ݴ� ������ �ݾ�����Ѵ�.
            // ����ó�� ���� �ɾ�����Ѵ�.
            // rs�� �ݴٰ� ���������� �����ϰ� stmt�� �ݾƶ�. �� �ݴٰ� �������� �����ϰ� conn�� �ݾƶ�
            // �׷��� ���� �� �ִ� error�� nullpointerException
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        /**
         * �� ó���� null�� �Ҵ�Ǿ��ִµ�, conn = class.getName();�ϴٰ� ������ �߻��ϸ� catch������
         * �̵��ϰ� �Ǵµ� �̶��� if���� �ɸ��� �ʱ� ������ �ƹ��͵� ���� �ʰ� ����. �������� �����ϰ��� null�� �ƴϱ� ������
         * �ڿ��� ȸ�� �� close();
         */
	}
}










