package com.hxgis.authserver.userRepository;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Component
public interface BaseDao<T, PK extends Serializable> {

	/**
	 * add one record function
	 * 
	 * @param entity
	 * @Description:
	 */
	public int add(T entity);

	/**
	 * add multi record function
	 * 
	 * @param entityCollection
	 * @Description:
	 */
	public int addList(Collection<T> entityCollection);

	/**
	 * delete one record function
	 * 
	 * @param entity
	 * @Description:
	 */
	public int delete(T entity);

	/**
	 * delete one record by id
	 * 
	 * @param id
	 * @Description:
	 */
	public int deleteById(PK id);

	/**
	 * delete many record function
	 * 
	 * @param entityCollection
	 * @Description:
	 */
	public int deleteList(Collection<T> entityCollection);

	/**
	 * update the record
	 * 
	 * @param entity
	 * @Description:
	 */
	public int update(T entity);

	/**
	 * update many records meanwhile
	 * 
	 * @param entityCollection
	 * @Description:
	 */
	public int updateList(Collection<T> entityCollection);

	/**
	 * query all record list
	 * 
	 * @return
	 * @Description:
	 */
	public List<T> getAll();

	/**
	 * find one special record
	 * 
	 * @return
	 * @Description:
	 */
	public T get(PK id);
	
	public List<T> getAllFile(PK id);
	
	/**
	 * 定时删除数据库
	 * @param table
	 * @param num
	 * @return
	 */
	public int deltetDB(String table, String num);

}