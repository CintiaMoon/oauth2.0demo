package com.hxgis.authserver.userRepository;


import com.hxgis.authserver.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class BaseDaoImpl<T, PK extends Serializable> extends
		NamedParameterJdbcDaoSupport implements BaseDao<T, PK> {

	public String tableName;
	public Class<T> persistentClass;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@SuppressWarnings("unchecked")
	public static Class<Object> getSuperClassGenricType(final Class clazz,
			final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class) params[index];
	}

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		persistentClass = (Class<T>) getSuperClassGenricType(getClass(), 0);
		try {
			//get name from table annotation
			tableName = getTableName(persistentClass);
			if(tableName != null && tableName.length()>0){

			}else{
				// get class name
				tableName = persistentClass.getSimpleName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("load... " + tableName);
	}

	@Autowired
	@Qualifier("dataSource")
	protected void inject(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	/**
	 * @return
	 * @Description:
	 */
	protected SimpleJdbcInsert getSimpleJdbcInsert() {
		return new SimpleJdbcInsert(this.getJdbcTemplate());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morningstar.planning.dao.BaseDao#add(java.lang.Class)
	 */
	public int add(T entity) {
		int i = 0;
		String tableName = entity.getClass().getSimpleName();
		SimpleJdbcInsert insertActor = getSimpleJdbcInsert();
		insertActor.setTableName(tableName.toLowerCase());
		i = insertActor.execute(new BeanPropertySqlParameterSource(entity));
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morningstar.planning.dao.BaseDao#addAll(java.util.Collection)
	 */

	public int addList(Collection<T> entityCollection) {
		SimpleJdbcInsert insertActor = getSimpleJdbcInsert();
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils
				.createBatch(entityCollection.toArray());
		insertActor.setTableName(tableName.toLowerCase());
		int[] result = insertActor.executeBatch(batchArgs);
		return result.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morningstar.planning.dao.BaseDao#delete(java.lang.Object)
	 */

	public int delete(T entity) {
		String tableName = entity.getClass().getSimpleName();
		String sql = "DELETE FROM " + tableName
				+ " WHERE  D_RECORD_ID =:D_RECORD_ID";
		return this.getNamedParameterJdbcTemplate().update(sql,
				new BeanPropertySqlParameterSource(entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morningstar.planning.dao.BaseDao#deleteById(java.lang.Class,
	 * java.io.Serializable)
	 */

	public int deleteById(PK id) {
		String sql = "DELETE FROM " + tableName + " WHERE  D_RECORD_ID=?";
		return this.getJdbcTemplate().update(sql, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morningstar.planning.dao.BaseDao#update(java.lang.Object)
	 */

	@SuppressWarnings("rawtypes")
	public int update(T entity) {
		int r = 0;
		String tableName = entity.getClass().getSimpleName();
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
		Field[] fields = entity.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field fied = fields[i];
			String fiedName = fied.getName();
			Class fiedType = fied.getType();
			if (!fiedName.equalsIgnoreCase("DRecordId")
					&& !fiedType.equals(Collections.class)
					&& !fiedType.equals(Map.class)
					&& !fiedType.equals(List.class)
					&& !fiedType.equals(Set.class)) {
				Object o = ReflectionUtils.getFieldValue(entity, fiedName);
				if (o != null) {
					String getterMethodName = "get"
							+ StringUtils.capitalize(fied.getName());
					try {
						Method method = entity.getClass().getDeclaredMethod(
								getterMethodName, new Class[] {});
						if (method
								.isAnnotationPresent(javax.persistence.Column.class)) {
							javax.persistence.Column c = method
									.getAnnotation(javax.persistence.Column.class);
							sql.append(c.name() + " = :" + fiedName + ",");
						} else {
							sql.append(fiedName + " = :" + fiedName + ",");
						}
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		sql.replace(sql.lastIndexOf(","), sql.length(), "");
		sql.append(" WHERE D_RECORD_ID" + " = :DRecordId");

		SqlParameterSource ps = new BeanPropertySqlParameterSource(entity);
		logger.debug("update sql : " + sql);
		r = this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.morningstar.planning.dao.BaseDao#updateList(java.util.Collection,
	 * java.lang.Class)
	 */

	@SuppressWarnings("rawtypes")
	public int updateList(Collection<T> entityCollection) {
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
		Field[] fields = persistentClass.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field fied = fields[i];
			String fiedName = fied.getName();
			Class fiedType = fied.getType();
			if (!fiedName.equalsIgnoreCase("D_RECORD_ID")
					&& !fiedType.equals(Collections.class)
					&& !fiedType.equals(Map.class)
					&& !fiedType.equals(List.class)
					&& !fiedType.equals(Set.class)) {
				sql.append(fiedName + " = :" + fiedName + ",");
			}
		}
		sql.replace(sql.lastIndexOf(","), sql.length(), "");
		sql.append(" WHERE D_RECORD_ID= :D_RECORD_ID");

		SqlParameterSource[] batchArgs = SqlParameterSourceUtils
				.createBatch(entityCollection.toArray());
		int[] result = this.getNamedParameterJdbcTemplate().batchUpdate(
				sql.toString(), batchArgs);
		return result.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morningstar.planning.dao.BaseDao#findAll(java.lang.Class)
	 */

	public List<T> getAll() {
		String sql = "SELECT * FROM " + tableName;
		return this.getNamedParameterJdbcTemplate().query(sql,
				new HashMap<String, String>(),
				BeanPropertyRowMapper.newInstance(persistentClass));
	}

	public List<T> getAllFile(PK id) {
		String sql = "SELECT * FROM " + tableName + " WHERE "
				+ tableName.toLowerCase() + "pid = :D_RECORD_ID";
		Map<String, PK> param = new HashMap<String, PK>();
		param.put("D_RECORD_ID", id);
		return this.getNamedParameterJdbcTemplate().query(sql, param,
				BeanPropertyRowMapper.newInstance(persistentClass));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.morningstar.planning.dao.BaseDao#findById(java.lang.Class,
	 * java.io.Serializable)
	 */

	public T get(PK id) {
		String sql = "SELECT * FROM " + tableName + " WHERE "
				+ "D_RECORD_ID = :D_RECORD_ID";
		Map<String, PK> param = new HashMap<String, PK>();
		param.put("D_RECORD_ID", id);
		T t = null;
		try {
			t = this.getNamedParameterJdbcTemplate().queryForObject(sql, param,
					BeanPropertyRowMapper.newInstance(persistentClass));
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			logger.debug("没找到对应记录：" + e.getMessage());
		}
		return t;
	}

	public List<T> findByProperty(String p, String pval) {
		String sql = "SELECT * FROM " + tableName + " WHERE " + p + " = :p";
		Map<String, String> param = new HashMap<String, String>();
		param.put("p", pval);
		return this.getNamedParameterJdbcTemplate().query(sql, param,
				BeanPropertyRowMapper.newInstance(persistentClass));
	}

	public List<T> findByPropertyOrderBy(String p, String pval, String ord,
			String asc) {
		String sql = "SELECT * FROM " + tableName + " WHERE " + p
				+ " = :p order by :ord " + asc;
		Map<String, String> param = new HashMap<String, String>();
		param.put("p", pval);
		param.put("ord", ord);
		return this.getNamedParameterJdbcTemplate().query(sql, param,
				BeanPropertyRowMapper.newInstance(persistentClass));
	}

	public T findByUniqueProperty(String p, String pval) {
		String sql = "SELECT * FROM " + tableName + " WHERE " + p + " = :p";
		logger.debug(sql);
		Map<String, String> param = new HashMap<String, String>();
		param.put("p", pval);
		T t = null;
		try {
			t = this.getNamedParameterJdbcTemplate().queryForObject(sql, param,
					BeanPropertyRowMapper.newInstance(persistentClass));
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return t;
	}

	public int deleteList(Collection<T> entityCollection) {
		String sql = "DELETE FROM " + tableName
				+ " WHERE D_RECORD_ID=:DRecordId";
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils
				.createBatch(entityCollection.toArray());
		int[] result = this.getNamedParameterJdbcTemplate().batchUpdate(sql,
				batchArgs);
		return result.length;
	}

	public int count() {
		String sql = "select count(1) from " + tableName;
		return this.getJdbcTemplate().queryForObject(sql,Integer.class);
	}

	public int delbyDate(String date) {
		String sql = "DELETE FROM " + tableName
				+ " WHERE  D_DATETIME <= :D_DATETIME";
		Map<String, String> param = new HashMap<String, String>();
		param.put("D_DATETIME", date);
		this.getNamedParameterJdbcTemplate().update(sql, param);
		return count();
	}
	@Override
	public int deltetDB(String table, String num) {
		String sql = "delete from "
				+ table
				+ " tb where tb.d_record_id not in (select id from "
				+ "(select id ,d_datetime from (select d_record_id id ,d_datetime from "
				+ table + " t order by t.d_datetime desc)where rownum < :num))";
		Map<String, String> param = new HashMap<String, String>();
		param.put("num", num);
		this.getNamedParameterJdbcTemplate().update(sql, param);
		return count();
	}

	/**
	 * 通过实体的anno获得Mapping的数据库表名
	 * @param clazz 实体类
	 * @return
	 * @throws Exception
	 */
	public static String getTableName(Class<?> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			return table.name();
		} else {
			throw new Exception(clazz.getName() + " is not Entity Annotation.");
		}
	}
}