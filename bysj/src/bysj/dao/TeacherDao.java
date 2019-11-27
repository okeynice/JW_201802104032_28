package bysj.dao;

import bysj.domain.Degree;
import bysj.domain.Department;
import bysj.domain.ProfTitle;
import bysj.domain.Teacher;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}

	public static void main(String[] args) throws SQLException {
		Degree degree = DegreeDao.getInstance().find(29);
		Department department = DepartmentDao.getInstance().find(3);
		ProfTitle profTitle = ProfTitleDao.getInstance().find(2);
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(1);

		teacherDao.update(teacher);
	}

	public Collection<Teacher> findAll() throws SQLException {
		Set<Teacher> teachers = new HashSet<Teacher>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("title_id"));
			Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Teacher teacher = new Teacher(resultSet.getString("name"),profTitle,degree,department);
			//向degrees集合中添加Degree对象
			teachers.add(teacher);
		}
		return teachers;
	}

	public boolean add(Teacher teacher) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String add = "INSERT INTO teacher(name,title_id,degree_id,department_id) VALUES" + " (?,?,?,?)";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(add);
		//为预编译的语句参数赋值
		pstmt.setString(1,teacher.getName());
		pstmt.setInt(2,teacher.getTitle().getId());
		pstmt.setInt(3,teacher.getDegree().getId());
		pstmt.setInt(4,teacher.getDepartment().getId());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}

	public boolean delete(Teacher teacher) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String delete = "DELETE FROM teacher WHERE ID =?";
		PreparedStatement pstmt = connection.prepareStatement(delete);
		pstmt.setInt(1,teacher.getId());
		int delete1 = pstmt.executeUpdate();
		return delete1>0;
	}
	public Teacher find(Integer id) throws SQLException {
		Set<Teacher> teachers = new HashSet<Teacher>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("title_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Teacher teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),profTitle,degree,department);
			//向degrees集合中添加Degree对象
			teachers.add(teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		Department department1 = null;
		Teacher desiredTeacher = null;
		for (Teacher teacher : teachers) {
			if(id.equals(teacher.getId())){
				desiredTeacher =  teacher; 
				break;
			}
		}
		return desiredTeacher;
	}
	
	public boolean update(Teacher teacher) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addSchool_sql = "update teacher set name=? where id=?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,"x12");
        System.out.println();
        pstmt.setInt(2,teacher.getId());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}
}
