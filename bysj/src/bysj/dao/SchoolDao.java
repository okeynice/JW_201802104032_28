package bysj.dao;
import bysj.domain.Degree;
import bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;
	static{
		schools = new TreeSet<School>();
		School school = new School(1,"土木工程","01","");
		schools.add(school);
		schools.add(new School(2,"管理工程","02","最好的学院"));
		schools.add(new School(3,"市政工程","03",""));
		schools.add(new School(4,"艺术","04",""));
	}
	
	public SchoolDao(){}

	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll(){
		return SchoolDao.schools;
	}

	public School find(Integer id) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from school");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			School school = new School(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			schools.add(school);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		Degree desiredDegree = null;
		School desiredSchool = null;
		for (School school : schools) {
			if(id.equals(school.getId())){
				desiredSchool =  school;
			}
		}
		return desiredSchool;
	}

	public boolean update(School school){
		schools.remove(school);
		return schools.add(school);
	}

	public boolean add(School school){
		return schools.add(school);
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String deleteDegree_sql = "DELETE FROM school WHERE ID =?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,id.toString());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}

	public boolean delete(School school){
		return SchoolDao.schools.remove(school);
	}

	public School addWithSP(School school) throws SQLException,ClassNotFoundException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备可调用语句，sp_addWithSP为名称，后面是五个参数
		CallableStatement callableStatement = connection.prepareCall("{CALL sp_school(?,?,?,?)}");
		callableStatement.setString(1,school.getNo());
		callableStatement.setString(2,school.getDescription());
		callableStatement.setString(3,school.getRemarks());
		//第五个设置为输出型
		callableStatement.registerOutParameter(4,Types.BIGINT);

		//执行可调用语句callableStatement
		callableStatement.execute();
		//获得第五个参数的值，数据库生成id
		int id = callableStatement.getInt(4);
		//为参数id赋值
		school.setId(id);
		//关闭
		callableStatement.close();
		connection.close();
		//返回School的值
		return school;
	}
}
