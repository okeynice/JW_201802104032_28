package bysj.dao;


import bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}

	public static void main(String[] args) throws SQLException{
		ProfTitle profTitle = ProfTitleDao.getInstance().find(3);
		profTitleDao.update(profTitle);
		System.out.println("已经修改");
		ProfTitle profTitle1 = new ProfTitle("02","博士","");
		profTitleDao.add(profTitle1);
		System.out.println("111");
		ProfTitle profTitle2 = ProfTitleDao.getInstance().find(4);
		profTitleDao.delete(profTitle2);
		System.out.println("222");
	}

	public Collection<ProfTitle> findAll() throws SQLException{
		Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from proftitle");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			profTitles.add(profTitle);
		}
		return profTitles;
	}
	public ProfTitle find(Integer id) throws SQLException {
		Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from proftitle");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			ProfTitle profTitle= new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			profTitles.add(profTitle);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		ProfTitle desiredProfTitle = null;
		for (ProfTitle profTitle : profTitles) {
			if(id.equals(profTitle.getId())){
				desiredProfTitle =  profTitle;
				break;
			}
		}
		return desiredProfTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException {
//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addP_sql = "update profTitle set no=?,description=?,remarks=? where id=?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addP_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,"0202");
		pstmt.setString(2,"讲师");
		pstmt.setString(3,"");
		pstmt.setInt(4,profTitle.getId());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}

	public boolean delete(ProfTitle profTitle) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String delete = "DELETE FROM proftitle WHERE ID =?";
		PreparedStatement pstmt = connection.prepareStatement(delete);
		pstmt.setInt(1,profTitle.getId());
		int delete1 = pstmt.executeUpdate();
		return delete1>0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addDegree_sql = "INSERT INTO proftitle(no,description,remarks) VALUES" + " (?,?,?)";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,profTitle.getNo());
		pstmt.setString(2,profTitle.getDescription());
		pstmt.setString(3,profTitle.getRemarks());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}
}

