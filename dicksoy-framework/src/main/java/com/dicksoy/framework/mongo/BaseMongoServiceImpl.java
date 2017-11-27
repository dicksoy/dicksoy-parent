package com.dicksoy.framework.mongo;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class BaseMongoServiceImpl<T> implements BaseMongoService<T>{

	/**   
	 * spring mongodb　集成操作类
	 */    
	@Resource    
	protected MongoTemplate mongoTemplate;

	/**   
	 * 获得泛型类   
	 */ 
	private Class<T> getEntityClass() {          
		return ReflectionUtils.getSuperClassGenricType(getClass());    
	} 

	@Override
	public T save(T entity) {
		mongoTemplate.save(entity);
		return entity;
	}

	@Override
	public T findById(String id) {
		return mongoTemplate.findById(id, this.getEntityClass());
	}

	@Override
	public T findById(String id, String collectionName) {
		return mongoTemplate.findById(id, this.getEntityClass(), collectionName);    
	}

	@Override
	public List<T> findAll() {
		return mongoTemplate.findAll(this.getEntityClass());
	}

	@Override
	public List<T> findAll(String collectionName) {
		return mongoTemplate.findAll(this.getEntityClass(), collectionName);    
	}

	@Override
	public List<T> find(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());    
	}

	@Override
	public T findOne(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());    
	}

	@Override
	public MongoPage<T> findPage(MongoPage<T> page, Query query) {
		//如果没有条件 则所有全部    
		query=query==null?new Query(Criteria.where("_id").exists(true)):query;    
		long count = this.count(query);    
		// 总数    
		page.setTotalCount((int) count);    
		int currentPage = page.getCurrentPage();    
		int pageSize = page.getPageSize();    
		query.skip((currentPage - 1) * pageSize).limit(pageSize);    
		List<T> rows = this.find(query);    
		page.build(rows);    
		return page; 
	}

	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, this.getEntityClass());    
	}

	@Override
	public WriteResult update(Query query, Update update) {
		if (update==null) {    
            return null;    
        }    
        return mongoTemplate.updateMulti(query, update, this.getEntityClass());
	}

	@Override
	public T updateOne(Query query, Update update) {
		if (update==null) {    
            return null;    
        }    
        return mongoTemplate.findAndModify(query, update, this.getEntityClass());    
	}

	@Override
	public WriteResult update(T entity) {
		Field[] fields = this.getEntityClass().getDeclaredFields();    
		if (fields == null || fields.length <= 0) {    
			return null;    
		}    
		Field idField = null;    
		// 查找ID的field    
		for (Field field : fields) {    
			if (field.getName() != null    
					&& "id".equals(field.getName().toLowerCase())) {    
				idField = field;    
				break;    
			}    
		}    
		if (idField == null) {    
			return null;    
		}    
		idField.setAccessible(true);    
		String id=null;    
		try {    
			id = (String) idField.get(entity);    
		} catch (IllegalArgumentException e) {    
			e.printStackTrace();    
		} catch (IllegalAccessException e) {    
			e.printStackTrace();    
		}    
		if (id == null || "".equals(id.trim()))    
			return null;    
		// 根据ID更新    
		Query query = new Query(Criteria.where("_id").is(id));    
		// 更新    
		// Update update = new Update();    
		// for (Field field : fields) {    
		// // 不为空 不是主键 不是序列化号    
		// if (field != null    
		// && field != idField    
		// && !"serialversionuid"    
		// .equals(field.getName().toLowerCase())) {    
		// field.setAccessible(true);    
		// Object obj = field.get(entity);    
		// if (obj == null)    
		// continue;    
		// update.set(field.getName(), obj);    
		// }    
		// }    
		Update update = ReflectionUtils.getUpdateObj(entity);    
		if (update == null) {    
			return null;    
		}    
		return mongoTemplate.updateFirst(query, update, getEntityClass()); 
	}

	@Override
	public void remove(Query query) {
		 mongoTemplate.remove(query, this.getEntityClass());    
	}

}
