package com.dicksoy.test.mongo;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dicksoy.framework.mongo.MongoPage;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:mongo_config/mongo_test.xml"})
public class MongodbTest {
	
	@Resource   
	private BusinessServiceImpl businessServiceImpl;  

	//测试数据插入  
	@Test 
	public void test1(){    
		Business business =new Business();  
		business.setLat(22.588402);  
		business.setLng(113.867822);   
		business.setOpenid(5);        
		businessServiceImpl.save(business);  
		System.out.println("数据插入成功");  
	}   
	//测试数据获取(id)  
	@Test  
	public void test2(){
		Business business = businessServiceImpl.findById("582174af6e6407195c0cd10a");  
		System.out.println("商家编号："+business.getOpenid());  
		System.out.println("经度："+business.getLat());  
		System.out.println("维度："+business.getLng());  

	}  

	//测试数据获取(id,collectionName)  
	@Test  
	public void test3(){  
		Business business = businessServiceImpl.findById("582175986e64071f888bee69","business");  
		System.out.println("商家编号："+business.getOpenid());  
		System.out.println("经度："+business.getLat());  
		System.out.println("维度："+business.getLng());  

	}  

	//测试数据获取(all)  
	@Test  
	public void test4(){  
		List<Business> list = businessServiceImpl.findAll();  
		if(list !=null && list.size()>0){  
			for(Business business : list){  
				System.out.println("商家编号："+business.getOpenid());  
				System.out.println("经度："+business.getLat());  
				System.out.println("维度："+business.getLng());  
			}  
		}         
	}  

	//测试数据获取(collectionName)  
	@Test  
	public void test5(){  
		List<Business> list = businessServiceImpl.findAll("business");  
		if(list !=null && list.size()>0){  
			for(Business business : list){  
				System.out.println("商家编号："+business.getOpenid());  
				System.out.println("经度："+business.getLat());  
				System.out.println("维度："+business.getLng());  
			}  
		}         
	}  

	//测试数据获取(query);   注意请求参数属性与实体对象属性一一对应。  
	@Test  
	public void test6(){  
		Query query = new Query(Criteria.where("lng").is(114.038804).and("lat").is(22.669214).and("openid").is(2));  
		List<Business> list = businessServiceImpl.find(query);  
		if(list !=null && list.size()>0){  
			for(Business business : list){  
				System.out.println("商家编号："+business.getOpenid());  
				System.out.println("经度："+business.getLat());  
				System.out.println("维度："+business.getLng());  
			}  
		}         
	}  

	//测试数据获取(query);   注意请求参数属性与实体对象属性一一对应。  
	@Test  
	public void test7(){  
		Query query = new Query(Criteria.where("lng").is(114.038804).and("lat").is(22.669214).and("openid").is(2));  
		Business business = businessServiceImpl.findOne(query);  
		if(business !=null){              
			System.out.println("商家编号："+business.getOpenid());  
			System.out.println("经度："+business.getLat());  
			System.out.println("维度："+business.getLng());              
		}         
	}  

	//测试数据获取(page);  
	@Test  
	public void test8(){  
		Query query = new Query();  
		MongoPage<Business> page = new MongoPage<Business>();  
		//	      page.setPageSize(2); //设置分页记录数  
		//	      page.setCurrentPage(3); //设置当前页面      
		MongoPage<Business> pages = businessServiceImpl.findPage(page, query);  
		List<Business> list = pages.getRows();  
		if(list !=null && list.size()>0){  
			for(Business business :list){  
				System.out.println("商家编号："+business.getOpenid());  
				System.out.println("经度："+business.getLat());  
				System.out.println("维度："+business.getLng());              
			}         
		}  
	}  

	//测试数据获取(total)  
	@Test  
	public void test9(){  
		Query query = new Query();    
		long num = businessServiceImpl.count(query);  
		System.out.println("记录总数："+num);          
	}  

	//测试数据修改(update)  注意：修改返回的实体对象是之前存储的数据信息  
	@Test  
	public void test10(){  
		Query query = new Query(Criteria.where("openid").is(2));      
		Update  update =new Update();  
		update.set("lng", 110.649865);  
		Business business = businessServiceImpl.updateOne(query, update);  
		if(business !=null){              
			System.out.println("商家编号："+business.getOpenid());  
			System.out.println("经度："+business.getLat());  
			System.out.println("维度："+business.getLng());              
		}     
	} 
}
