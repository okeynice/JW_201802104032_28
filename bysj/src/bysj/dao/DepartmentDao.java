package bysj.dao;
import bysj.domain.Department;
import bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class DepartmentDao {
	private static DepartmentDao departmentDao = new DepartmentDao();

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Department department = DepartmentDao.getInstance().find(3);
		departmentDao.update(department);
		System.out.println("已经修改");
	}


	private DepartmentDao() {
	}

    public Collection<Department> findAll() throws SQLException{
		Set<Department> departments = new HashSet<Department>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from department");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
        	School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            Department department = new Department(resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school,resultSet.getInt("id"));
            //向degrees集合中添加Degree对象
			departments.add(department);
        }
        return departments;
    }

	public boolean add(Department department) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addDegree_sql = "INSERT INTO department(no,description,remarks,school_id) VALUES" + " (?,?,?,?)";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,department.getNo());
		pstmt.setString(2,department.getDescription());
		pstmt.setString(3,department.getRemarks());
		pstmt.setInt(4,department.getSchool().getId());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}

	public boolean delete(Department department) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String delete = "DELETE FROM DEGREE WHERE ID =?";
		PreparedStatement pstmt = connection.prepareStatement(delete);
		pstmt.setInt(1,department.getId());
		int delete1 = pstmt.executeUpdate();
		return delete1>0;
	}

	public static DepartmentDao getInstance() {
		return departmentDao;
	}

	public Department find(Integer id) throws SQLException {
		Set<Department> departments = new HashSet<Department>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from Department");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Department department = new Department(resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school,resultSet.getInt("id"));
			//向degrees集合中添加Degree对象
			departments.add(department);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		Department department1 = null;
		for (Department department:departments) {
			if(id.equals(department.getId())){
				department1 = department;
				break;
			}
		}
		return department1;
	}

	public boolean update(Department department) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addDegree_sql = "update department set description=?,no=?,remarks=? where id=?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,"工业管理");
        pstmt.setString(2,"10");
        pstmt.setString(3,"");
        pstmt.setInt(4,department.getId());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}
    public Collection<Department> findAllBySchool(Integer school_id) throws SQLException {
        Set<Department> departments = new HashSet<Department>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from Department");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            Department department = new Department(resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school,resultSet.getInt("id"));
            //向degrees集合中添加Degree对象
            departments.add(department);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        Set<Department> department1 = new HashSet<Department>();
        for (Department department:departments) {
            if(school_id.equals(department.getSchool().getId())){
                department1.add(department);
            }
        }
        System.out.println(department1);
        return department1;
    }
}

