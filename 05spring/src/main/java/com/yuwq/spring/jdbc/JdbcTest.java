package com.yuwq.spring.jdbc;

import java.sql.*;

public class JdbcTest {

    private static final String url ="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF8";
    private static final String name ="root";
    private static final String pwd ="123456";
    private static final String driver ="com.mysql.cj.jdbc.Driver";
    private static Connection connection;
    public static Connection getConnection() {

        try {

            Class.forName(driver); // 加载驱动

            System.out.println("加载驱动成功!!!");
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        try {

            //通过DriverManager类的getConenction方法指定三个参数,连接数据库
            connection = DriverManager.getConnection(url, name, pwd);
            System.out.println("连接数据库成功!!!");

            //返回连接对象
            return connection;

        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws SQLException{
        try {
            Connection connection = getConnection();
            String querySql = "select * from person";
            String insertSql = "insert into Person(name,birthday) values('test',null)";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(querySql);
            while (rs.next()) {
                System.out.println(rs.getString("id") + "name" + rs.getString("name"));
            }
            System.out.println("insertSql:" + insertSql);
            statement.executeUpdate(insertSql);
            String updateSql = "delete from person where id=999";
            int i1 = statement.executeUpdate(updateSql);
            System.out.println("删除了" + i1 + "条数据");

            String preSql = "insert into Person(name,birthday) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(preSql);
            preparedStatement.setString(1, "test1");
            preparedStatement.setDate(2, new Date(new java.util.Date().getTime()));
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
            connection.setAutoCommit(Boolean.FALSE);
            for (int i = 0; i < 10; i++) {
                preparedStatement.setString(1, "S_10" + i);
                preparedStatement.setDate(2, new Date(new java.util.Date().getTime()));
                preparedStatement.addBatch();
            }
            int[] j = preparedStatement.executeBatch();
            System.out.println("使用preparedStatement批量插入了" + j.length);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
