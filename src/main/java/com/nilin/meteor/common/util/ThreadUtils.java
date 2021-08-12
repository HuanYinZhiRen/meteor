package com.nilin.meteor.common.util;


public class ThreadUtils {

	private static ThreadLocal<RText> rText = new ThreadLocal<RText>() {
		public synchronized RText initialValue() {
			return new RText();
		}
	};
	
	private static ThreadLocal<RedisUtil> redisUtil = new ThreadLocal<RedisUtil>() {
		public synchronized RedisUtil initialValue() {
			return new RedisUtil();
		}
	};
	
	private static ThreadLocal<FtpUtil> ftpUtil = new ThreadLocal<FtpUtil>() {
		public synchronized FtpUtil initialValue() {
			return new FtpUtil();
		}
	};

	private static ThreadLocal<CollectionUtil> collectionUtil = new ThreadLocal<CollectionUtil>() {
		public synchronized CollectionUtil initialValue() {
			return new CollectionUtil();
		}
	};



	public static RText getRTex() {
		return rText.get();
	}
	
	public static FtpUtil getFtpUtil() {
		return ftpUtil.get();
	}

	
	public static RedisUtil getRedisUtil() {
		return redisUtil.get();
	}

	public static CollectionUtil getCollectionUtils() {
		return collectionUtil.get();
	}



	
}
