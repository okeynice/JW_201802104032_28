package bysj.dao;

import bysj.domain.Degree;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DegreeDao {
	private static DegreeDao degreeDao=
			new DegreeDao();
	private DegreeDao(){}
	public static DegreeDao getInstance(){
		return degreeDao;
	}
	private static Collection<Degree> degrees;

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Degree degree = DegreeDao.getInstance().find(30);
		degreeDao.update(degree);
		System.out.println("已经修改");
		Degree degree1 = DegreeDao.getInstance().find(30);
		degreeDao.delete(degree1);
		System.out.println("已删除");
		Degree degree2 = new Degree("02","博士","");
		degreeDao.add(degree2);
		System.out.println("已添加");
	}

	public Set<Degree> findAll() throws SQLException{
		Set<Degree> degrees = new HashSet<Degree>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM Degree");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//以当前记录中的id,description,no,remarks值为参数，创建Degree对象
			Degree degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			degrees.add(degree);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return degrees;
	}



	public Degree find(Integer id) throws SQLException{
		Set<Degree> degrees = new HashSet<Degree>();
		//获得连接对象
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from Degree");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Degree degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			degrees.add(degree);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		Degree desiredDegree = null;
		for (Degree degree : degrees) {
			if(id.equals(degree.getId())){
				desiredDegree =  degree;
			}
		}
		return desiredDegree;
	}

	public boolean update(Degree degree) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addDegree_sql = "update degree set description=?,no=?,remarks=? where id=?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,"博士");
		pstmt.setString(2,"100");
		pstmt.setString(3,"");
		pstmt.setInt(4,degree.getId());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}

	public boolean add(Degree degree) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addDegree_sql = "INSERT INTO degree(no,description,remarks) VALUES" + " (?,?,?)";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,degree.getNo());
		pstmt.setString(2,degree.getDescription());
		pstmt.setString(3,degree.getRemarks());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}

	public boolean delete(Degree degree) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String delete = "DELETE FROM DEGREE WHERE ID =?";
		PreparedStatement pstmt = connection.prepareStatement(delete);
		pstmt.setInt(1,degree.getId());
		int delete1 = pstmt.executeUpdate();
		return delete1>0;
	}

	public boolean delete(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String deleteDegree_sql = "DELETE FROM DEGREE WHERE ID =id";
		Statement stmt = connection.createStatement();
		stmt.execute(deleteDegree_sql);
		Degree degree = this.find(id);
		return degreeDao.delete(degree);
	}
}
