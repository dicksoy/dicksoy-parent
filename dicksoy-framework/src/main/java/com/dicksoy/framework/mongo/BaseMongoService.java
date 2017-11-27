package com.dicksoy.framework.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public interface BaseMongoService<T> {

	/**
	 * 插入
	 */
	T save(T entity);

	/**   
     * 根据ID查询   
     */    
    T findById(String id);   
    
    /**   
     * 通过ID获取记录,并且指定了集合名(表的意思)   
     */    
    T findById(String id, String collectionName);    
   
    /**   
     * 获得所有该类型记录   
     */    
    List<T> findAll();    
    
    /**   
     * 获得所有该类型记录,并且指定了集合名(表的意思)   
     */    
    List<T> findAll(String collectionName);    
    /**   
     * 根据条件查询   
     */    
    List<T> find(Query query);    
    
    /**   
     * 根据条件查询一个   
     */    
    T findOne(Query query);    
    
    /**   
     * 分页查询   
     */    
    MongoPage<T> findPage(MongoPage<T> page, Query query);    
    
    /**   
     * 根据条件 获得总数   
     */    
    long count(Query query);    
    
    /**   
     * 根据条件 更新   
     */    
    WriteResult update(Query query, Update update);    
    
    /**   
     * 更新符合条件并sort之后的第一个文档 并返回更新后的文档   
     */    
    T updateOne(Query query, Update update);    
    
    /**   
     * 根据传入实体ID更新   
     */    
    WriteResult update(T entity);    
    
    /**   
     * 根据条件 删除   
     *    
     * @param query   
     */    
    void remove(Query query);
}
