package com.nilin.meteor.common.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtil {
	/**
	 * redis序列化模式
	 *
	 *
	 */
	public enum SerializerMode {
		NONE, JSON, SERIALIZATION
	}

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}
	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		
		if (keys.size() > 0)
		redisTemplate.delete(keys);
	}
	
	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}
	
	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}
	
	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		Object result = null;
		ValueOperations<String, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}
	
	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			if(expireTime > 0) {
				redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** 
	* 将map写入缓存
	* @param key
	* @param map
	* @param time 失效时间(秒)
	*/
	public <T> void setMap(String key, Map<String, T> map,long time){
	   redisTemplate.opsForHash().putAll(key, map);
	   if(time > 0){
		   redisTemplate.expire(key, time, TimeUnit.SECONDS);
	   }
	}


	/**
	* 向key对应的map中添加缓存对象
	* @param key
	* @param map
	*/
	public <T> void addMap(String key, Map<String, T> map){
	   redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	* 向key对应的map中添加缓存对象
	* @param key   cache对象key
	* @param field map对应的key
	* @param value     值
	*/
	public void addMap(String key, String field, String value){
	  redisTemplate.opsForHash().put(key, field, value);
	}

	/**
	* 向key对应的map中添加缓存对象
	* @param key   cache对象key
	* @param field map对应的key
	* @param obj   对象
	*/
	public <T> void addMap(String key, String field, T obj){
	  redisTemplate.opsForHash().put(key, field, obj);
	}


	/**
	* 获取map缓存
	* @param key
	* @return
	*/
	public <T> Map<String, T> mget(String key){
	  BoundHashOperations<String, String, T> boundHashOperations = redisTemplate.boundHashOps(key);
	  return boundHashOperations.entries();
	}


	/**
	* 获取map缓存中的某个对象
	* @param key
	* @param field
	* @return
	*/
	@SuppressWarnings("unchecked")
	public <T> T getMapField(String key, String field){
	  return (T)redisTemplate.boundHashOps(key).get(field);
	}

	/**
	* 删除map中的某个对象
	* @author lh
	* @param key   map对应的key
	* @param field map中该对象的key
	*/
	public void delMapField(String key, String... field){
	  BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
	  boundHashOperations.delete(field);
	}


	/**
	* 指定缓存的失效时间
	*
	* @param key 缓存KEY
	* @param time 失效时间(秒)
	*/
	public void expire(String key, long time) {
	  if(time > 0){
		  redisTemplate.expire(key, time, TimeUnit.SECONDS);
	  }
	}

	/**
	 * 根据key 获取过期时间
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}
	  
	/**
	* 获取double类型值
	* @param key
	* @return
	*/
	public double getDouble(String key) {
	   String value = ObjectUtils.toString(redisTemplate.boundValueOps(key).get());
	   if(StringUtils.isNotBlank(value)){
		   return Double.valueOf(value);
	   }
	   return 0d;
	}

	/**
	* 设置double类型值
	* @param key
	* @param value
	* @param time 失效时间(秒)
	*/
	public void setDouble(String key, double value, long time) {
	   redisTemplate.opsForValue().set(key, String.valueOf(value));
	   if(time > 0){
		   redisTemplate.expire(key, time, TimeUnit.SECONDS);
	   }
	}

	/**
	* 添加set
	* @param key
	* @param value
	*/
	@SuppressWarnings("unchecked")
	public void sadd(String key, String... value) {
	   redisTemplate.boundSetOps(key).add(value);
	}


	/**
	* 删除set集合中的对象
	* @param key
	* @param value
	*/
	public void srem(String key, String... value) {
	   redisTemplate.boundSetOps(key).remove(value);
	}

	/**
	* set重命名
	* @param oldkey
	* @param newkey
	*/
	public void srename(String oldkey, String newkey){
	   redisTemplate.boundSetOps(oldkey).rename(newkey);
	}


	/**
	* 多个value值向开始位置list添加
	* @param key
	*/
	public void llsadd(String key,String... values) {
	   redisTemplate.boundListOps(key).leftPushAll(values);
	}

	/**
	* 单个value值向开始位置list添加
	* @param key
	* @param value
	*/
	public void lladd(String key,String value) {
	   redisTemplate.boundListOps(key).leftPush(value);
	}

	/**
	* 多个value值向开始位置list添加
	* @param key
	*/
	public void lrsadd(String key,String... values) {
	   redisTemplate.boundListOps(key).rightPushAll(values);
	}

	/**
	* 单个value值向开始位置list添加
	* @param key
	* @param value
	*/
	public void lradd(String key,String value) {
	   redisTemplate.boundListOps(key).rightPush(value);
	}


	/**
	* 移除并获取list第一个元素
	*/
	public Object llpop(String key) {
	   return redisTemplate.boundListOps(key).leftPop();
	}


	/**
	* 移除并获取list最后一个元素
	*/
	public Object lrpop(String key) {
	   return redisTemplate.boundListOps(key).rightPop();
	}

	/**
	* 获取list长度
	* @param key
	* @return
	*/
	public long llength(String key) {
	   return redisTemplate.boundListOps(key).size();
	}

	/**
	*  从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作：
	*  count > 0: 从头往尾移除值为 value 的元素。
	*  count < 0: 从尾往头移除值为 value 的元素。
	*  count = 0: 移除所有值为 value 的元素。
	* @param key
	* @param count
	* @param value
	* @return
	*/
	public long lremove(String key,int count,String value) {
	   return redisTemplate.boundListOps(key).remove(count, value);
	}

	/**
	* 根据下标获取元素
	* @param key
	* @param index
	*/
	public Object lindex(String key,long index) {
	   return redisTemplate.boundListOps(key).index(index);
	}

	/**
	* 根据下标设置值
	*/
	public void lset(String key,int index,String value) {
	   redisTemplate.boundListOps(key).set(index, value);
	}

	/**
	* 根据key获取list
	* @param key
	* @return
	*/
	public List<Object> lget(String key) {
	   return redisTemplate.boundListOps(key).range(0, redisTemplate.boundListOps(key).size());
	}


	/**
	* 模糊查询keys
	* @param pattern
	* @return
	*/
	public Set<String> keys(String pattern){
	   return redisTemplate.keys(pattern);
	}



    /**
     * 设置List缓存
     */
    public void setListCache(String key, List<Object> values, SerializerMode serializerMode, long sec) {
        List<byte[]> serializeList = serializeByteList(values, serializerMode);
        if (null != serializeList && serializeList.size() > 0) {
			remove(key);
            setByteList(key, serializeList, sec);
        }
    }

    /**
     * 序列化为字符串
     *
     * @param values
     * @param serializerMode
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<byte[]> serializeByteList(List<Object> values,SerializerMode serializerMode) {
        List<byte[]> serializeList = new ArrayList<byte[]>();
        for (Object value : values) {
            byte[] serializeStr = serializeByte(value, serializerMode);
            if (!ThreadUtils.getRTex().isEmpty(serializeStr)) {
                serializeList.add(serializeStr);
            }
        }
        return serializeList;
    }

    /**
     * 序列化为字符串
     *
     * @param ObjectValue
     * @param serializerMode
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] serializeByte(Object ObjectValue, SerializerMode serializerMode) {
        byte[] serializaByte = null;
        if (null != ObjectValue) {
            if (serializerMode == serializerMode.NONE) {
                serializaByte = ThreadUtils.getRTex().objSerializeByte(ObjectValue);
            } else if (serializerMode == serializerMode.JSON) {
                serializaByte = ThreadUtils.getRTex().objectSerializeJSON(ObjectValue);
            } else if (serializerMode == serializerMode.SERIALIZATION) {
                serializaByte = ThreadUtils.getRTex().objSerializeByte(ObjectValue);
            }
        }
        return serializaByte;
    }

    /**
     * 添加List类型数据
     *
     * @param key
     * @param value
     * @param sec
     */
    public void setByteList(final String key, final List<byte[]> value, final long sec) {
        if (validate(key, value)) {
            redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) {
                    byte[] serKey = ThreadUtils.getRedisUtil().serializeKey(key);
                    byte[][] byteArray = new byte[value.size()][0];
                    for (int i = 0; i < value.size(); i++) {
                        byteArray[i] = value.get(i);
                    }
					long num = connection.rPush(serKey, byteArray);
//                    long num = connection.lPush(serKey, byteArray);
                    if (sec > 0) {
                        setExpire(key, sec);
                    }
                    return num;
                }
            });
        }
    }

    /**
     * 数据验证
     *
     * @param key
     * @param value
     * @return
     */
    public boolean validate(String key, Object value) {
        boolean flag = true;
        if (ThreadUtils.getRTex().isEmpty(key)) {
            flag = false;
        } else if (ThreadUtils.getRTex().isEmpty(value)) {
            flag = false;
        }
        return flag;
    }

    /**
     * 设置过期时间
     */
    public void setExpire(final String key, final long sec) {
        if (sec > 0) {
            redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) {
                    byte[] serKey = ThreadUtils.getRedisUtil().serializeKey(key);
                    return connection.expire(serKey, sec);
                }
            });
        }
    }

	/**
	 * 获取List数据
	 *
	 * @param key
	 * @return
	 */
	public List<byte[]> getListForPage(final String key, final int startRow, final int endRow) {
		List<byte[]> byteList = new ArrayList<byte[]>();
		if (validate(key)) {
			byteList = redisTemplate.execute(new RedisCallback<List<byte[]>>() {
				public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] serKey = ThreadUtils.getRedisUtil().serializeKey(key);
					List<byte[]> byteList = connection.lRange(serKey, startRow, endRow);
					return byteList;
				}
			});
		}
		return byteList;
	}
	
	
	/**
	 * 数据验证
	 *
	 * @param key
	 * @return
	 */
	public boolean validate(String key) {
		boolean flag = true;
		if (ThreadUtils.getRTex().isEmpty(key)) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Redis key序列化
	 * @param key
	 * @return
	 */
	public byte[] serializeKey(String key){
		byte[] serKey = null;
		if(ThreadUtils.getRTex().isNotEmpty(key)){
			serKey = key.getBytes();
		}
		return serKey;
	}

	/**
	 * 反序列化key
	 * @return
	 */
	public String deserializeKey(byte[] bytes){
		String key = null;
		if(null!=bytes){
			key = new String(bytes);
		}
		return key;
	}

	/**
	 * 反序列化为List对象
	 *
	 * @param values
	 * @param serializerMode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<Object> deserializeListObject(List<byte[]> values, SerializerMode serializerMode) {
		List<Object> serializeList = new ArrayList<Object>();
		for (byte[] value : values) {
			Object objectValue = deserializeObject(value, serializerMode);
			if (null != objectValue) {
				serializeList.add(objectValue);
			}

		}
		return serializeList;
	}

	/**
	 * 反序列化为对象
	 *
	 * @param serializaByte
	 * @param serializerMode
	 * @param serializerMode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Object deserializeObject(byte[] serializaByte, SerializerMode serializerMode){
		Object objectValue = null;
		if (null!=serializaByte) {
			if (serializerMode == serializerMode.NONE) {
				objectValue = ThreadUtils.getRTex().byteSerializeObj(serializaByte);
			} else if (serializerMode == serializerMode.JSON) {
				objectValue = ThreadUtils.getRTex().jsonSerializeObject(serializaByte);
			} else if (serializerMode == serializerMode.SERIALIZATION) {
				objectValue = ThreadUtils.getRTex().byteSerializeObj(serializaByte);
			}
		}

		return objectValue;
	}
	
	/**
	 * 递增
	 */
	public long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 */
	public long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}

}
