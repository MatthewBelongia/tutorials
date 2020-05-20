package com.baeldung.persistence;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

@Service
public class JDBCQueryService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	@Named("sqlDataSource")
	public DataSource dataSource;
	
	@Inject
	@Named("sqlSimpleJdbc")
	public SimpleJdbcCall simpleJdbcCall;
	
	public String makeJdbcFunctionCall() {
		SqlParameterSource inputParameters = new MapSqlParameterSource().addValue("INPUT_STRING", "sample_value");
		
		String result = simpleJdbcCall.executeFunction(String.class, inputParameters);
		return result;
	}

}
