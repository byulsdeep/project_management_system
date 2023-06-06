package com.pms.main;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class HikariTest {
	@Autowired
	private DataSource dataSource;	
	@Autowired
	private SqlSessionFactory sqlSession;

	@Test
	public void dbcpTest() {
		try {
			System.out.println("DataSource accessed");
			Connection conn = dataSource.getConnection();
			log.info(conn);
			System.out.println("SqlSession opened");
			SqlSession session = sqlSession.openSession();
			log.info(session);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
