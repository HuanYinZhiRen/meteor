package com.nilin.meteor.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.activerecord.Model;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class RText {

	private static Logger logger = LoggerFactory.getLogger(RText.class);
	
	public final static Date DateNull = new Date(0);

	public static List<String> exclusives = new ArrayList<String>();
	
	private static final String[] templateStr = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
			"z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "E", "T",
			"U", "V", "W", "X", "Y", "Z" };
	private static final String[] templateNum = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	
	/**
	 * 获取一个double类型数值的百分比 example: FormatPercent(0.33333333,2) = 33%
	 * 
	 * @param number
	 * @param newValue
	 * @return
	 */
	public String FormatPercent(double number, int newValue) {
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(newValue);
		return nf.format(number);
	}

	/**
	 * 获取数值 example:FormatNumber("50%") = 0.5
	 * 
	 * @param value
	 * @return
	 */
	public double FormatNumber(String value) {
		double n = 0;
		if (value != null) {
			NumberFormat nf = NumberFormat.getPercentInstance();
			try {
				n = nf.parse(value).doubleValue();
			} catch (ParseException e) {
			}
		}
		return n;
	}

	/**
	 * 截取float类型 小数位数 example:FormatNumber(12.35678f,2) = 12.36
	 * 
	 * @param number
	 * @param dogits
	 * @return
	 */
	public float FormatNumber(float number, int dogits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(dogits);
		nf.setMinimumFractionDigits(0);
		return NumberUtils.toFloat(nf.format(number));
	}

	/**
	 * 截取double类型 小数位数 example:FormatNumber(12.35678,2) = 12.36
	 * 
	 * @param number
	 * @param dogits
	 * @return
	 */
	public double FormatNumber(double number, int dogits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(dogits);
		nf.setMinimumFractionDigits(0);
		return NumberUtils.toDouble(nf.format(number));
	}

	/**
	 * 对中文字符编码，防止乱码
	 * 
	 * @param plainText
	 * @return
	 */
	public String encodeURL(String plainText) {
		String value = null;
		try {
			value = new String(plainText.getBytes("iso-8859-1"), "gb2312");
		} catch (UnsupportedEncodingException e) {
			value = "";
		}
		return value;
	}

	/**
	 * 将iso8859-1转为utf-8
	 * 
	 * @param plainText
	 *            原始字符串
	 * @param t
	 *            t为true发生异常时返回原始字符串否则返回空串
	 * @return
	 */
	public String encodeURLToUTF8KT(String plainText, boolean t) {
		String value = null;
		if (StringUtils.isEmpty(plainText)) {
			return "";
		}
		try {
			value = new String(plainText.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			if (t) {
				value = plainText;
			} else {
				value = "";
			}
		}
		return value;
	}

	/**
	 * 对中文字符编码，防止乱码
	 * 
	 * @param plainText
	 * @return
	 */
	public String encodeURLToUTF8(String plainText) {
		String value = null;
		try {
			value = new String(plainText.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			value = "";
		}
		return value;
	}

	/**
	 * 编码从GBK转换成Unicode
	 * 
	 * @param str
	 * @return
	 */
	public String Gbk2Unicode(String str) {
		StringBuffer buffer = new StringBuffer();
		String splitString = "";
		for (int i = 0; i < str.length(); i++) {
			buffer.append(splitString);
			buffer.append(Integer.toHexString(str.charAt(i)));
			splitString = ",";
		}
		return buffer.toString();
	}

	/**
	 * 编码从Unicode转换成GBK
	 * 
	 * @param str
	 * @return
	 */
	public String Unicode2Gbk(String str) {
		StringBuffer buffer = new StringBuffer();
		String[] arr = StringUtils.split(str, ',');
		for (int i = 0; i < arr.length; i++) {
			buffer.append((char) Integer.parseInt(arr[i], 16));
		}
		return buffer.toString();
	}

	/**
	 * 编码从Ascii转换成String
	 * 
	 * @param str
	 * @return
	 */
	public String Ascii2String(String str) {
		StringBuffer buffer = new StringBuffer();
		try {
			if (str != null && !"".equals(str)) {
				String[] chars = str.split("-");
				for (int i = 0; i < chars.length; i++) {
					buffer.append((char) Long.parseLong(chars[i]));
				}
			}
		} catch (Exception e) {
			buffer = new StringBuffer();
			buffer.append(str);
		}
		return buffer.toString();
	}

	/**
	 * 编码从String转换成Ascii
	 * 
	 * @param str
	 * @return
	 */
	public String String2Ascii(String str) {
		StringBuffer buffer = new StringBuffer();
		if (str != null && !"".equals(str)) {
			String splitString = "";
			char[] chars = str.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				buffer.append(splitString);
				buffer.append((int) chars[i]);
				splitString = "-";
			}
		}
		return buffer.toString();
	}

	/**
	 * 对字符串排序
	 * 
	 * @param str
	 * @return String
	 */
	public String Sort(String str) {
		String value = "";
		if (str != null) {
			char[] chr = str.toCharArray();
			Arrays.sort(chr);
			value = new String(chr);
		}
		return value;
	}

	/**
	 * 对字符串MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public String Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString();// 32位的加密

			// buf.toString().substring(8,24);//16位的加密

		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

	// 将全角字符转换成半角字符
	public String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	// 将半角字符转换成全角字符
	public String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	// my test
	public boolean type(String filename, String str) {
		if (filename != null && filename.length() != 0 && filename.contains(".") && str != null && str.length() != 0) {
			int lastDotIndex = filename.lastIndexOf(".");
			int length = filename.length();
			String type = filename.toLowerCase().substring(lastDotIndex, length);
			if (str.equals(type)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 根据文件名获得文件类型
	 * 
	 * @param filename
	 * @return real media flash flv
	 */
	public String FileType(String filename) {
		String fileType = "other";
		if (filename != null && !"".equals(filename)) {
			if (type(filename, ".swf")) {
				fileType = "flash";
			} else if (type(filename, ".flv")) {
				fileType = "flv";
			} else if (type(filename, ".txt")) {
				fileType = "txt";
			} else if (type(filename, ".doc")) {
				fileType = "doc";
			} else if (type(filename, ".xls")) {
				fileType = "xls";
			} else {
				String[] imageExt = { ".jpg", ".jpeg", ".gif", ".png", ".bmp" };
				for (int i = 0; i < imageExt.length; i++) {
					if (type(filename, imageExt[i])) {
						fileType = "image";
						break;
					}
				}

				String[] rmExt = { ".rm", ".ra", ".ram", ".rmvb" };
				for (int i = 0; i < rmExt.length; i++) {
					if (type(filename, rmExt[i])) {
						fileType = "real";
						break;
					}
				}

				String[] mediaExt = { ".mp3", ".wav", ".wma", ".wmv", ".avi", ".asf", ".asx", ".mid", ".midi", ".mpg",
						".mpeg" };
				for (int i = 0; i < mediaExt.length; i++) {
					if (type(filename, mediaExt[i])) {
						fileType = "media";
						break;
					}
				}
			}
		}
		return fileType;
	}

	/**
	 * 根据文件名获得文件类型
	 * 
	 * @param filename
	 * @return real media flash flv
	 */
	public String ImageType(String filename) {
		String fileType = "other";
		if (filename != null && !"".equals(filename)) {
			if (type(filename, ".swf")) {
				fileType = "flash";
			} else if (type(filename, ".flv")) {
				fileType = "flv";
			} else if (type(filename, ".txt")) {
				fileType = "txt";
			} else if (type(filename, ".doc")) {
				fileType = "doc";
			} else if (type(filename, ".xls")) {
				fileType = "xls";
			} else if (type(filename, ".jpg") || type(filename, ".jpeg")) {
				fileType = "jpg";
			} else if (type(filename, ".gif")) {
				fileType = "gif";
			} else if (type(filename, ".png")) {
				fileType = "png";
			} else if (type(filename, ".bmp")) {
				fileType = "bmp";
			} else {

				String[] rmExt = { ".rm", ".ra", ".ram", ".rmvb" };
				for (int i = 0; i < rmExt.length; i++) {
					if (type(filename, rmExt[i])) {
						fileType = "real";
						break;
					}
				}

				String[] mediaExt = { ".mp3", ".wav", ".wma", ".wmv", ".avi", ".asf", ".asx", ".mid", ".midi", ".mpg",
						".mpeg" };
				for (int i = 0; i < mediaExt.length; i++) {
					if (type(filename, mediaExt[i])) {
						fileType = "media";
						break;
					}
				}
			}
		}
		return fileType;
	}

	public Boolean photoType(String filename) {
		Boolean typeValue = false;
		String[] rmExt = { ".jpg", ".jpeg", ".gif", ".bmp", ".png" };
		for (int i = 0; i < rmExt.length; i++) {
			if (rmExt[i].equals(filename.toLowerCase())) {
				typeValue = true;
				break;
			}
		}

		return typeValue;
	}

	// 特殊字符转换
	public String toHtml(String str) {
		String html = str;
		html = replace(html, "&", "&amp;");
		html = replace(html, "<", "&lt;");
		html = replace(html, ">", "&gt;");
		html = replace(html, "\"", "&quot;");
		html = replace(html, " ", "&nbsp;");
		return html;
	}

	// 特殊字符转换
	public String toHtml(String str, boolean newline) {
		String html = toHtml(str);
		html = replace(str, "\n", "<br />");
		return html;
	}

	public String replace(String source, String oldString, String newString) {
		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length(); // 源字符串长度
		int lengthOfOld = oldString.length(); // 老字符串长度
		int posStart = 0; // 开始搜索位置
		int pos; // 搜索到老字符串的位置
		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengthOfOld;
		}
		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	/**
	 * 
	 * 把秒数转换成 小时：分钟：秒
	 * 
	 * @param seconds
	 * @return
	 */
	public String secondToHHMISS(BigDecimal seconds) {
		int h = 0, m = 0, s = 0;
		int theSecond = seconds.intValue();
		h = (int) Math.floor(theSecond / 3600);
		if (h > 0) {
			m = (int) Math.floor((theSecond - h * 3600) / 60);
			if (m > 0) {
				s = theSecond - h * 3600 - m * 60;
			} else {
				s = theSecond - h * 3600;
			}
		} else {
			m = (int) Math.floor(theSecond / 60);
			if (m > 0) {
				s = theSecond - m * 60;
			} else {
				s = theSecond;
			}
		}
		return h + " 时" + m + " 分" + s + " 秒";

	}

	// 获取当前系统日期
	public final String getTimeString() {
		String TimeString = "";
		Calendar c = Calendar.getInstance();

		int yyyy = c.get(Calendar.YEAR);
		int mm = c.get(Calendar.MONTH) + 1;
		int dd = c.get(Calendar.DAY_OF_MONTH);
		int hh = c.get(Calendar.HOUR_OF_DAY);
		int MM = c.get(Calendar.MINUTE);
		int SS = c.get(Calendar.SECOND);

		String sMonth = mm < 10 ? "0" + mm : "" + mm;
		String sDay = dd < 10 ? "0" + dd : "" + dd;
		String sHour = hh < 10 ? "0" + hh : "" + hh;
		String sMinute = MM < 10 ? "0" + MM : "" + MM;
		String sSecond = SS < 10 ? "0" + SS : "" + SS;

		TimeString = yyyy + "-" + sMonth + "-" + sDay + " " + sHour + ":" + sMinute + ":" + sSecond;

		return TimeString;
	}

	// 页面输出内容流
	public void Print(HttpServletResponse resp, String msg) {
		try {
			PrintWriter out = resp.getWriter();
			out.println(msg);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取静态密码和动态密码
	public String getPassWordType(String password, int typeValue) {

		int length = password.length();

		if (typeValue == 0) {
			return password.substring(length - 6, length);
		} else {
			return password.substring(0, length - 6);
		}

	}

	// 获取文件扩展名
	public String GetFileExtendedName(String inFileName) {
		try {
			int intPos = inFileName.lastIndexOf('.');
			return inFileName.substring(intPos);
		} catch (Exception e) {
		}
		return "";
	}

	public final byte[] Key = "21huarui".getBytes();
	private final String Algorithm = "DES";

	// 解密字符串
	public byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// from javahelper.java
	public Integer ToInteger(Object inValue, Integer inDefaultValue) {
		try {
			Double _double = Double.parseDouble(inValue.toString());
			return _double.intValue();
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	public Boolean ToBoolean(Object inValue, Boolean inDefaultValue) {
		try {
			return Boolean.parseBoolean(ObjectUtils.toString(inValue));
		} catch (Exception e) {
			return inDefaultValue;
		}
	}

	public Boolean ToBoolean(Object inValue) {
		try {
			return Boolean.parseBoolean(ObjectUtils.toString(inValue));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 字符串转boolean 类型
	 * 
	 * @param str
	 * @return
	 */
	public Boolean toBoolean(String str) {
		Boolean bool = null;
		if (isEmpty(str)) {
			return bool;
		}
		try {
			bool = Boolean.parseBoolean(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bool;
	}
	

	// from javahelper.java
	public Double ToDouble(Object inValue, Double inDefaultValue) {
		try {
			return Double.parseDouble(inValue.toString());
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	// from javahelper.java
	public float ToFloat(Object inValue, float inDefaultValue) {
		try {
			Float _float = Float.parseFloat(inValue.toString());
			return _float.floatValue();
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	public boolean checkDate(String date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			Date date1 = format.parse(date);
			System.out.println(format.format(date1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public String formateDate(String date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			Date date1 = format.parse(date);
			date = format.format(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return date;

	}

	public Date ToJavaDate(double date, Date inDefaultValue) {
		return ToJavaDate(date, false, inDefaultValue);
	}

	private static final int SECONDS_PER_MINUTE = 60;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int HOURS_PER_DAY = 24;
	private static final int SECONDS_PER_DAY = (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE);
	private static final long DAY_MILLISECONDS = SECONDS_PER_DAY * 1000L;

	public void setCalendar(Calendar calendar, int wholeDays, int millisecondsInDay, boolean use1904windowing) {
		int startYear = 1900;
		int dayAdjust = -1; // Excel thinks 2/29/1900 is a valid date, which it
		// isn't
		if (use1904windowing) {
			startYear = 1904;
			dayAdjust = 1; // 1904 date windowing uses 1/2/1904 as the first day
		} else if (wholeDays < 61) {
			// Date is prior to 3/1/1900, so adjust because Excel thinks
			// 2/29/1900 exists
			// If Excel date == 2/29/1900, will become 3/1/1900 in Java
			// representation
			dayAdjust = 0;
		}
		calendar.set(startYear, 0, wholeDays + dayAdjust, 0, 0, 0);
		calendar.set(GregorianCalendar.MILLISECOND, millisecondsInDay);
	}

	public Date ToJavaDate(double date, boolean use1904windowing, Date inDefaultValue) {
		try {
			int wholeDays = (int) Math.floor(date);
			int millisecondsInDay = (int) ((date - wholeDays) * DAY_MILLISECONDS + 0.5);
			Calendar calendar = new GregorianCalendar(); // using default
			// time-zone
			setCalendar(calendar, wholeDays, millisecondsInDay, use1904windowing);
			return calendar.getTime();
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	// 可以返回指定字符串
	public String toString(Object inValue, String inDefaultValue) {
		try {
			return inValue.toString();
		} catch (Exception e) {
		}
		return inDefaultValue;
	}
	
	/**
	 * toString 方法重载 转换成String类型
	 * 
	 * @param obj
	 * @return
	 */
	public String toString(Object obj) {
		if (isEmpty(obj)) {
			return "";
		}
		if (obj instanceof String) {
			String str = obj.toString();
			return str;
		} else {
			String str = "";
			try {
				str = String.valueOf(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return str;
		}
	}

	// 试题选项进行格式化
	public String formatStr(Object obj) {
		String str = ObjectUtils.toString(obj, "");
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		return str.replace("\n", "").replace("\r", "").replace("；", ";");
	}

	// 试题选项进行格式化
	public String formatStr(Object obj, boolean isLowerCase) {
		String str = ObjectUtils.toString(obj, "");
		if (StringUtils.isEmpty(str)) {
			return "";
		}

		str = StringUtils.replace(str, "；", ";");
		str = StringUtils.replace(str, "\r\n", "");
		str = StringUtils.replace(str, "\n", "");
		str = StringUtils.replace(str, "\r", "");
		str = StringUtils.replace(str, " ", "");

		str = str.replaceAll(";{2,}", ";");
		str = StringUtils.removeStart(str, ";");
		str = StringUtils.removeEnd(str, ";");

		if (isLowerCase) {
			str = StringUtils.replace(str, ";", "");
			str = str.toLowerCase();
		}
		return str;
	}

	// like简化
	public String WhereForStringLike(String inFieldName, Object inValue, String inDefalutValue) {
		String strv = toString(inValue, "");
		if ("".equals(strv) == false) {
			return " and (" + inFieldName + " like '%" + SqlValue(strv) + "%')";
		}
		return inDefalutValue;
	}

	public String WhereForInt100(String inFieldName, Object inValue, String inDefalutValue) {
		int intv = ToInteger(inValue, -100);
		if (intv == -100) {
			return inDefalutValue;
		}
		return " and (" + inFieldName + " = " + inValue + ")";
	}
	
	/**
	 * 字符串转Integer 类型
	 * 
	 * @param str
	 * @return
	 */
	public Integer toInteger(String str) {
		Integer number = 0;
		if (isEmpty(str)) {
			return number;
		}
		try {
			number = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}
	
	/**
	 * Object转Integer 类型
	 * 
	 * @param obj
	 * @return
	 */
	public Integer toInteger(Object obj) {
		String str = toString(obj);
		return toInteger(str);
	}
	
	/**
	 * 数据转换float
	 * 
	 * @return
	 */
	public float toFloat(String key) {
		return NumberUtils.toFloat(key);
	}

	public String WhereForString(String inFieldName, Object inValue, String inDefalutValue) {
		String strv = toString(inValue, "");
		if ("".equals(strv) == false) {
			return " and (" + inFieldName + " = '" + SqlValue(strv) + "')";
		}
		return inDefalutValue;
	}

	public String WhereForInt100Between(String inFieldName, Object inMin, Object inMax, String inDefalutValue) {
		int intV = ToInteger(inMin, -100);
		int intV2 = ToInteger(inMax, -100);
		if (intV != 0 && intV2 > 0) {
			return " and ( " + inFieldName + " between  " + inMin + " and " + inMax + " ) ";
		}
		if (intV != 0) {
			return " and ( " + inFieldName + " >= " + inMin + " ) ";
		}
		if (intV2 > 0) {
			return " and ( " + inFieldName + " <= " + inMax + " ) ";
		}
		return inDefalutValue;
	}

	public List<String> SplitString(String inString, String inSplitString) {
		try {
			List<String> tmpList = new ArrayList<String>();
			if(isEmpty(inString)) {
				return tmpList;
			}
			String[] tmpArr = inString.split(inSplitString);
			for (int i = 0; i < tmpArr.length; i++) {
				tmpList.add(i, tmpArr[i]);
			}
			return tmpList;
		} catch (Exception e) {

		}
		return null;
	}

	public String MapToString(Map inMap, int inCount, String inSplit) {
		try {
			Iterator iterator = inMap.keySet().iterator();
			String retString = "";
			String strValue;
			Object objValue;

			strValue = iterator.next().toString();
			objValue = inMap.get(strValue);
			retString = objValue.toString();
			if (inCount < 1) {
				inCount = inMap.size();
			}
			while (iterator.hasNext() && --inCount > 0) {
				strValue = iterator.next().toString();
				objValue = inMap.get(strValue);
				retString += inSplit + objValue.toString();
			}
			return retString;
		} catch (Exception e) {
		}
		return "";
	}

	public String ListToString(List inList, String inSplit1, String inSplit2, int inMapSize,
			String inEmptyDefault) {
		try {
			String retValue = "";
			Object obj;
			obj = inList.get(0);
			if (obj instanceof Map) {
				Map map = (Map) obj;
				retValue = MapToString(map, inMapSize, inSplit2);
				for (int i = 1; i < inList.size(); i++) {
					map = (Map) inList.get(i);
					retValue += inSplit1 + MapToString(map, inMapSize, inSplit2);
				}
			} else {
				retValue = obj.toString();
				for (int i = 1; i < inList.size(); i++) {
					obj = inList.get(i);
					retValue += inSplit1 + obj.toString();
				}
			}
			return retValue;
		} catch (Exception e) {
		}
		return inEmptyDefault;
	}

	public String ListToString(List inList) {
		return ListToString(inList, ";", ":", 0, "");
	}

	public String ListToString(List inList, String split) {
		return ListToString(inList, split, ":", 0, "");
	}

	public Map ListToStringForMap(List<Map> inList, String split) {
		Map initMap = new HashMap();
		if (inList == null || inList.size() <= 0) {
			return null;
		}
		Map map0 = inList.get(0);
		Set<String> set = map0.keySet();
		StringBuffer sb = new StringBuffer();
		for (String str : set) {
			for (Map map : inList) {
				sb.append(map.get(str)).append(",");
			}
			initMap.put(str, sb.substring(0, sb.length() - 1));
			sb.setLength(0);
		}

		return initMap;
	}

	public String SqlValue(Object inValue) {
		String strValue = toString(inValue, "");
		return strValue.replace("'", "''");
	}

	public String WhereForIntBetween(String inFieldName, Object inMin, Object inMax, String inDefalutValue) {
		int intV = ToInteger(inMin, 0);
		int intV2 = ToInteger(inMax, 0);
		if (intV != 0 && intV2 != 0) {
			return " and ( " + inFieldName + " between  " + inMin + " and " + inMax + " ) ";
		}
		if (intV != 0) {
			return " and ( " + inFieldName + " >= " + inMin + " ) ";
		}
		if (intV2 != 0) {
			return " and ( " + inFieldName + " <= " + inMax + " ) ";
		}
		return inDefalutValue;
	}

	public String ToStringURLZH(Object inString, String inDefault) {
		try {
			return new String(inString.toString().getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
		}
		return inDefault;
	}

	public FileInputStream FileInputStream(String inFile) {
		try {
			return new FileInputStream(inFile);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public String ToStringZH(Object inString, String inDefault) {
		try {
			return new String(inString.toString().getBytes(), "ISO8859-1");
		} catch (Exception e) {
		}
		return inDefault;
	}

	public long DateDiff(Long inStart, Long inEnd, int inDiffType) {
		int[] DTDiv = new int[] { 86400000, 86400000, 86400000, 3600000, 60000, 1000 };
		long diff = inEnd - inStart;
		return diff / DTDiv[inDiffType];
	}

	/**
	 * 是否为空验证为空时返回一个默认值，否则返回源字符串
	 * 
	 * @param str
	 *            需要验证的字符串
	 * @param def
	 *            为空时默认字符串
	 * @return
	 */
	public String isEmptyConvert(String str, String def) {
		if (StringUtils.isEmpty(str)) {
			return def;
		}
		return str;
	}

	public Object[] isEmptyConvert(Object[] args, String def) {
		for (int i = 0; i < args.length; i++) {
			args[i] = isEmptyConvert(args[i], def);
		}
		return args;
	}

	public String isEmptyConvert(Object obj, String def) {
		String str = "";
		if (StringUtils.isEmpty(ObjectUtils.toString(obj)) || ObjectUtils.toString(obj).equals("undefined")) {
			str = def;
		} else {
			str = ObjectUtils.toString(obj);
		}

		return str;
	}

	public boolean isEmpty(Object obj) {
		if (StringUtils.isEmpty(ObjectUtils.toString(obj)) || ObjectUtils.toString(obj).equals("undefined")) {
			return true;
		}
		return false;
	}

	public String splitStr(Object str, int n) {
		String st = ObjectUtils.toString(str, "");
		if (st.length() > n) {
			st = st.substring(0, n);
			if (st.length() > 0) {
				st += "...";
			}
		}
		return st;
	}

	public String[] removeEmpty(String[] array) {
		List<String> list = new ArrayList<String>();
		if (array != null && array.length >= 1) {
			for (String s : array) {
				if (!StringUtils.isEmpty(s)) {
					list.add(s);
				}
			}
		}

		String[] newarray = new String[list.size()];
		list.toArray(newarray);
		return newarray;
	}

	public String wm_concat(String sql, String column) {
		String wm_sql = " ltrim((SELECT LEFT(columns,(LEN(columns) - 1)) share "
				+ "FROM (SELECT (SELECT convert(varchar, " + column + ") + ',' " + "FROM (" + sql
				+ ") a0 FOR XML PATH('')) AS columns) AS T)) ";
		return wm_sql;
	}

	public Object[] formatArray(Object[] args) {

		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				args[i] = "";
			}
		}
		return args;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] formatArray(T[] args, T mapping) {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (StringUtils.isEmpty(toString(args[i], ""))) {
					args[i] = mapping;
				}
			}
		} else {
			args = (T[]) Array.newInstance(mapping.getClass(), 1);
			args[0] = mapping;
		}

		return args;
	}

	/**
	 * 难度转换
	 * 
	 * @param obj
	 *            难度
	 * @return 数值型难度
	 */
	public int diffculty(Object obj) {
		String str = toString(obj, "").trim();
		int n = 1;
		if ("容易".equals(str) || "1".equals(str)) {
			n = 1;
		} else if ("较易".equals(str) || "2".equals(str)) {
			n = 2;
		} else if ("一般".equals(str) || "3".equals(str)) {
			n = 3;
		} else if ("较难".equals(str) || "4".equals(str)) {
			n = 4;
		} else if ("非常难".equals(str) || "5".equals(str)) {
			n = 5;
		} else {
			n = 0;
		}
		return n;
	}

	public boolean MapContainsKey(Map map, Object obj) {
		if (map != null) {
			Set set = map.keySet();
			for (Object str : set) {
				String key = ObjectUtils.toString(str, "");
				if (!StringUtils.isEmpty(key)) {
					key = key.trim();
					if (!StringUtils.isEmpty(key)) {
						if (key.equals(obj)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否包含或者排除
	 * 
	 * @param str
	 *            需要验证字符串
	 * @param Sequence
	 *            验证字符序列
	 * @param flag
	 *            为true包含，为false排除
	 * @return
	 */
	public boolean Contain(String str, String Sequence, boolean flag) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(Sequence)) {
			return true;
		}

		String sque[] = Sequence.split(",");
		boolean t = true;
		for (String s : sque) {
			if (str.toLowerCase().contains(s.toLowerCase())) {
				if (flag) {
					return true;
				} else {
					t = false;
				}
			}
		}
		if (t) {
			return true;
		}
		return false;
	}

	public String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	/**
	 * 去掉空值
	 * 
	 * @param str
	 *            原值
	 * @return String 处理后值
	 */
	public String nvlString(Object str) {
		String s_temp = "" + str;
		if (s_temp == null || s_temp.toUpperCase().equals("NULL")) {
			s_temp = "";
		}
		return s_temp;
	}

	/**
	 * 将数组中的NUll换为‘’空值
	 * 
	 * @param args
	 *            原值
	 * @return Object[] 处理后值
	 */
	public Object[] argsNvlString(Object[] args) {
		Object[] argsString = null;

		if (args.length > 0) {
			Object[] argsString2 = new Object[args.length];
			for (int i = 0; i < args.length; i++) {
				args[i] = nvlString(args[i]);
				argsString2[i] = args[i];
			}
			argsString = argsString2;
		}

		return argsString;
	}

	public Map mapsNvlString(Map map) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1 = map;
		if (map1 != null) {
			for (String key : map1.keySet()) {
				map1.put(key, nvlString(map1.get(key)));
			}
		}

		return map1;
	}

	public String getPathExt(String path) {
		if (!isEmpty(path)) {
			return path.substring(path.lastIndexOf(".") + 1);
		}
		return "";
	}

	public String getPath(String path, String ext) {
		if (!isEmpty(path)) {
			return path.substring(0, path.lastIndexOf(".") + 1) + ext;
		}
		return "";
	}

	public String getWebServicePath(String path) {
		if (!isEmpty(path)) {
			return path.substring(0, path.lastIndexOf("/"));
		}
		return "";
	}

	public String getNowDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public String getNowDate1() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	public String getNowDate(String pattern) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public List StringToList(String str, String split, boolean isUpper) {
		String[] strs = str.split(split);
		List<String> list = new ArrayList<String>();
		for (String s : strs) {
			if (isUpper) {
				list.add(s.toUpperCase());
			} else {
				list.add(s);
			}

		}
		return list;
	}

	public boolean contentsPath(String path) {
		for (String p : exclusives) {
			if (path.toUpperCase().contains(p)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public String join(Collection collection, String keyname, String separator) {

		String join = "";

		String key = "";
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();

			String name = ObjectUtils.toString(map.get(keyname), "");
			if (!name.equals(key)) {
				join += name + ",";
				key = name;
			}
		}
		join = StringUtils.substringBeforeLast(join, ",");

		return join;
	}

	// 在火狐 IE 等下载文件名称不乱码
	public String downFileNameTransformation(HttpServletRequest request, String doc)
			throws UnsupportedEncodingException {
		String Agent = request.getHeader("User-Agent");
		String filename = "";
		if (null != Agent) {
			Agent = Agent.toLowerCase();
			if (Agent.indexOf("firefox") != -1) {
				filename = new String(doc.getBytes(), "iso8859-1");
			} else if (Agent.indexOf("msie") != -1) {
				filename = java.net.URLEncoder.encode(doc, "UTF-8");
			} else {
				filename = java.net.URLEncoder.encode(doc, "UTF-8");
			}
		}
		return filename;
	}

	public String URLEncoder(String str, String encoder) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			return java.net.URLEncoder.encode(decodeParameters(str), encoder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String URLEncoderUTF8(String str) {
		return URLEncoder(str, "UTF-8");
	}

	public String URLDecoder(String str, String encoder) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			return URLDecoder.decode(decodeParameters(str), encoder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String URLDecoderUTF8(String str) {
		return URLDecoder(str, "UTF-8");
	}

	public String guessEncoding(String str) {
		String encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {

		}
		return "";
	}

	public String transCoding(String str, String charSet) {
		/*
		 * try { return new String(str.getBytes("gbk"), "utf-8"); } catch
		 * (UnsupportedEncodingException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		String encoding = guessEncoding(str);
		if (encoding.equalsIgnoreCase(charSet)) {
			return str;
		} else {
			try {
				return new String(str.getBytes(encoding), charSet);
			} catch (UnsupportedEncodingException e) { // TODO Auto-generated
				// catch block
				e.printStackTrace();
			}
		}

		return str;
	}


	// 当年第一天
	public String getCurrentYearFirst() {

		int yearPlus = getYearPlus();

		GregorianCalendar currentDate = new GregorianCalendar();

		currentDate.add(GregorianCalendar.DATE, yearPlus);

		Date yearDay = currentDate.getTime();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		String preYearDay = df.format(yearDay);

		return preYearDay;

	}

	private int getYearPlus() {

		Calendar cd = Calendar.getInstance();

		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天

		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天

		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。

		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);

		if (yearOfNumber == 1) {

			return -MaxYear;

		} else {

			return 1 - yearOfNumber;

		}

	}

	// 当前日期
	public String getCurrentDate() {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dt.format(date);
	}

	/**
	 * 获取当年的最后一天
	 * 
	 * @return
	 */
	public String getCurrYearLast() {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		Date date = getYearLast(currentYear + 1);

		return dt.format(date);
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return currYearLast;
	}


	public int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public String getFileName(String filePath) {
		if (!StringUtils.isEmpty(filePath)) {
			filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
		}
		return filePath;
	}

	// 获取三个月后的日期
	public String getLastMonth() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 3);
		Date otherDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
		// System.out.println( "today: "+dateFormat.format(date)+
		// " 3 months after: "+dateFormat.format(otherDate));
		return dateFormat.format(otherDate);
	}

	// 二进制转16进制
	public String binaryString2hexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	// 16进制转二进制
	public String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	public String byteHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * 获得纯文本
	 * 
	 * @param s
	 * @return 获得纯文本
	 */
	public String getSimpleCharacters(String s) {
		Pattern pattern = Pattern.compile("<style.+?>.+?</style>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(s);
		s = matcher.replaceAll("");

		pattern = Pattern.compile("<script.+?>.+?</script>", Pattern.DOTALL);
		matcher = pattern.matcher(s);
		s = matcher.replaceAll("");

		pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
		matcher = pattern.matcher(s);
		s = matcher.replaceAll("").replaceAll("&nbsp;", "").replaceAll(" ", "");

		if (s.length() > 260) {
			s = s.substring(0, 240) + "......";
		}
		return s;
	}

	public String[] toStringArray(Object[] arrays) {
		String[] array = new String[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			array[i] = ObjectUtils.toString(arrays[i]);
		}
		return array;
	}

	public String getRandomNumber() {
		String ran = ObjectUtils.toString((int) (Math.random() * 9999));
		int n = 4 - ran.length();
		for (int i = 0; i < n; i++) {
			ran = 0 + ran;
		}
		return ran;
	}

	public Date toDate(String date, String pattern) {
		SimpleDateFormat dt = new SimpleDateFormat(pattern);
		try {
			return dt.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String toDate(Date date, String pattern) {
		SimpleDateFormat dt = new SimpleDateFormat(pattern);
		return dt.format(date);
	}

	public String dateDiff(String date1, String date2) {
		long n = DateDiff(toDate(date1, "yyyy-MM-dd HH:mm").getTime(), toDate(date2, "yyyy-MM-dd HH:mm").getTime(), 3);
		return ObjectUtils.toString(n);
	}

	// 获取时间戳
	public String accessToDate(String str) {
		int year = 0;
		int month = 0;
		int day = 0;
		Calendar c = Calendar.getInstance();// 获得系统当前日期
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;// 系统日期从0开始算起
		day = c.get(Calendar.DAY_OF_MONTH);
		if (str.equals("1")) {
			day = day - 7;
		}
		if (str.equals("2")) {
			day = day - 15;
		}
		if (str.equals("3")) {
			month = month - 1;
		}
		String smonth = month < 10 ? "0" + month : "" + month;
		String sday = day < 10 ? "0" + day : "" + day;
		return ObjectUtils.toString(year) + "-" + smonth + "-" + sday;
	}

	public String ToStringForArray(Object[] args, int i) {
		if (args.length > i) {
			return toString(args[i], "");
		}
		return "";
	}

	public String ToStringForArray(Object[] args, int i, String n) {
		if (args.length > i) {
			return toString(args[i], n);
		}
		return n;
	}

	public String toUTF8(String str) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	public String checkBrowser(String browser) {
		String browserVersion = "IE11,IE10,IE9,IE8,IE7,IE6,IE5,Firefox,Netscape,Opera,Google,Safari";
		if (!browserVersion.contains(browser)) {
			browser = "其他";
		}
		return browser;
	}

	public String checkOperate(String operatesys) {
		String operateVersion = "Windows 98,Windows 2000,Windows XP,Windows 2003,Vista,Windows 7,Windows 8,Linux,Unix";
		if (!operateVersion.contains(operatesys)) {
			operatesys = "其他";
		}
		return operatesys;
	}

	public float formatFloat(double f, int num) {
		BigDecimal b = new BigDecimal(f);
		return b.setScale(num, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public String getTimeFromLong(long time) {

		if (time <= 0) {
			return "0:00";
		}
		int secondnd = (int) (time / 1000) / 60;
		int million = (int) (time / 1000) % 60;
		String f = String.valueOf(secondnd);
		String m = million >= 10 ? String.valueOf(million) : "0" + String.valueOf(million);
		return f + ":" + m;
	}

	/**
	 * 获取数组的最大值
	 * 
	 * @param arr
	 * @return
	 */
	public float ArrayMaxValue(String[] arr) {
		float max = 0f;
		for (int i = 0; i < arr.length; i++) {
			float _value = NumberUtils.toFloat(arr[i], 0f);
			if (_value > max)
				max = _value;
		}
		return max;
	}

	/**
	 * 解码转换字符串，用于防注入
	 * 
	 * @param value
	 * @return
	 */
	public String decodeParameters(String value) {
		if (value != null) {
			value = ObjectUtils.toString(value).replace("♂1♀", "|");
			value = ObjectUtils.toString(value).replace("♂2♀", "&");
			value = ObjectUtils.toString(value).replace("♂3♀", ";");
			value = ObjectUtils.toString(value).replace("♂4♀", "$");
			value = ObjectUtils.toString(value).replace("♂5♀", "%");
			value = ObjectUtils.toString(value).replace("♂6♀", "@");
			value = ObjectUtils.toString(value).replace("♂7♀", "'");
			value = ObjectUtils.toString(value).replace("♂8♀", "\"");
			value = ObjectUtils.toString(value).replace("♂9♀", "<");
			value = ObjectUtils.toString(value).replace("♂10♀", ">");
			value = ObjectUtils.toString(value).replace("♂11♀", "(");
			value = ObjectUtils.toString(value).replace("♂12♀", ")");
			value = ObjectUtils.toString(value).replace("♂13♀", "+");
			value = ObjectUtils.toString(value).replace("♂14♀", "\n");
			value = ObjectUtils.toString(value).replace("♂15♀", "\r");
			value = ObjectUtils.toString(value).replace("♂16♀", ",");
			value = ObjectUtils.toString(value).replace("♂17♀", "\\");
		}

		return value;
	}

	/**
	 * 解码转换字符串，用于防注入
	 * 
	 * @param args
	 * @return
	 */
	public Object[] decodeParameters(Object[] args) {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof String) {
					args[i] = decodeParameters(ObjectUtils.toString(args[i]));
				}
			}
		}

		return args;
	}

	/**
	 * 解码转换字符串，用于防注入
	 * 
	 * @param map
	 * @return
	 */
	public Map decodeParameters(Map map) {
		if (map != null) {
			for (Iterator keys = map.keySet().iterator(); keys.hasNext();) {
				String key = (String) keys.next();
				Object value = map.get(key);

				if (value instanceof String) {
					value = decodeParameters(ObjectUtils.toString(value));
					map.put(key, value);
				}
			}
		}

		return map;
	}

	public String emailENC(String email) {
		if (!email.contains("@")) {
			return null;
		}
		String[] semail = email.split("@");
		String prefix = ToStringForArray(semail, 0);
		String em = prefix.substring(0, 2);
		prefix = prefix.replaceAll("[\\w\\W]", "*");
		email = em += prefix.substring(2) + "@" + ToStringForArray(semail, 1);
		return email;
	}

	public String[] listToArray(List<String> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		String[] temp = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			temp[i] = list.get(i);
		}
		return temp;
	}

	public int sign(Object obj) {
		int def = ToInteger(obj, 0);
		if (def >= 0) {
			return def;
		}
		return 0;

	}

	public String getIpAddr(HttpServletRequest request) {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Real-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	public String stringLeftReplaceRight(String dates, String[] left, String[] right) {
		if (left.length != right.length) {
			return dates;
		}
		for (int i = 0; i < left.length; i++) {
			dates = dates.replaceAll(left[i], right[i]);
		}
		return dates;
	}

	/**
	 * 格式化Map
	 * 
	 * @param map
	 * @return
	 */
	public Map formatMap(Map map) {
		 Map newMap = new HashMap();
		if(null == map){
			map = new HashMap();
		}else{
			Set<Object> set = map.keySet();
			for (Object key : set) {
				Object value = map.get(key);
				if (isEmpty(value)) {
					value = "";
				}else {
					if(value instanceof Clob) {
						Clob c = (Clob)value;
						try {
							value = (String)ClobToString(c);
						} catch (SQLException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				newMap.put(key.toString().toLowerCase(), value);
			}
		}
		return newMap;
	}
	
	// 将字Clob转成String类型  
    public String ClobToString(Clob sc) throws SQLException, IOException {
        String reString = "";  
        Reader is = sc.getCharacterStream();// 得到流  
        BufferedReader br = new BufferedReader(is);  
        String s = br.readLine();  
        StringBuffer sb = new StringBuffer();  
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING  
            sb.append(s);  
            s = br.readLine();  
        }  
        reString = sb.toString();  
        return reString;  
    }  
    
	/**
	 * 格式化List
	 *
	 * @param list
	 * @param pk 该列表的主键
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public List formatList(List list, String pk) {

		List<Object> flist = new ArrayList<Object>();
		if(null != list){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object o = iterator.next();
				if(o instanceof Map) {
					Map map = (Map) o;
					map = formatMap(map);
					if(pk != null && !pk.isEmpty()) {
						map.put("key", map.get(pk));//主键
					}
					flist.add(map);
				}else if(o instanceof Model) {
					Map map = EntityToMap(o);
					map = formatMap(map);
					if(pk != null && !pk.isEmpty()) {
						map.put("key", map.get(pk));//主键
					}
					flist.add(map);
				}else {
					Map map = EntityToMap(o);
					map = formatMap(map);
					if(pk != null && !pk.isEmpty()) {
						map.put("key", map.get(pk));//主键
					}
					flist.add(map);
				}

			}
		}

		return flist;
	}

	/**
	 * 格式化List
	 *
	 * @param list
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public List formatList(List list) {
		return formatList(list, "");
	}

	public Object MapToEntity(Map map, Object obj) {
		if (map == null || obj == null) {
			return null;
		}
//		System.out.println("-------------------------");
//		System.out.println(obj);
//		System.out.println("-------------------------");
//		System.out.println(map);
//		System.out.println("-------------------------");
		Field[] field = getEntityFieldSupers(obj.getClass());
		for (Field f : field) {
			try {
				Class<?> type = f.getType();
				if (null == map) {
					continue;
				}
				Object value = map.get(f.getName());
				if (null == value) {
					continue;
				}
				if (value != null) {
					Method method = obj.getClass().getMethod(
							methodTo("set", f.getName()), f.getType());
					method.invoke(obj, value);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		return obj;
	}
	
	public Map EntityToMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] field = obj.getClass().getDeclaredFields();
		for (Field f : field) {
			try {
				Class<?> type = f.getType();
				String name = f.getName();
				if(!"serialVersionUID".equals(name)){
					Method method = obj.getClass().getMethod(
							methodTo("get", f.getName()));
					Object value = method.invoke(obj);
				/*	if (null == value) {
						continue;
					}*/
					map.put(f.getName(), value);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		return map;
	}
	
	private String methodTo(String pre, String str) {
		return pre + String.valueOf(str.charAt(0)).toUpperCase()
				+ str.substring(1);
	}

	public Field[] getEntityFieldSupers(Class clazz) {
		Field[] fs = getEntityFields(clazz);
		fs = (Field[]) ArrayUtils.addAll(fs,
				getEntityFields(clazz.getSuperclass()));
		if (clazz.getSuperclass().getSuperclass() != null) {
			fs = (Field[]) ArrayUtils.addAll(fs,
					getEntityFields(clazz.getSuperclass().getSuperclass()));
		}
		
		return fs;
	}
	
	public Field[] getEntityFields(Class clazz) {
		Field[] fs = clazz.getDeclaredFields();
		return fs;
	}
	
	/**
	 * 使用java正则表达式去掉多余的.与0
	 * 
	 * @param s
	 * @return
	 */
	public String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	// 阿拉伯数字转中文小写
	public String transition(String si) {
		String[] aa = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿" };
		String[] bb = { "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		String tranStr = "";
		char[] ch = si.toCharArray();
		int maxindex = ch.length;
		// 字符的转换//两位数的特殊转换
		if (maxindex == 2) {
			for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
				if (ch[j] != 48) {
					if (j == 0 && ch[j] == 49) {
						tranStr += aa[i] + "";
					} else {
						tranStr += bb[ch[j] - 49] + aa[i] + "";
					}
				}
			} // 其他位数的特殊转换，使用的是int类型最大的位数为十亿
		} else {
			for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
				if (ch[j] != 48) {
					tranStr += bb[ch[j] - 49] + aa[i] + "";
				}
			}
		}
		return tranStr;
	}

	/**
	 * 将主讲人与单位进行分割合并
	 * 
	 * @param map
	 *            包含有PNAMETEXT,WORKUNIT字段的Map集合
	 * @return 格式为 主讲人1 单位1,主讲人2 单位2 的字符串
	 */
	public String pntAndWuToPnametexts(Map map) {

		// 分别取出PNAMETEXT,WORKUNIT字段 并进行分割
		String pnametexts = ObjectUtils.toString(map.get("PNAMETEXT"), "");
		String workunits = ObjectUtils.toString(map.get("WORKUNIT"), "");

		String[] pts = pnametexts.split(",");
		String[] wus = workunits.split(",");

		StringBuffer speaker = new StringBuffer();

		for (int j = 0; j < pts.length; j++) {
			if (!pts[j].equals("--")) {
				if (j == pts.length - 1 && pts[pts.length - 1] != null) {
					if (j < wus.length) {
						if (wus[j].equals("--")) {
							speaker.append(pts[j]);
						} else {
							speaker.append(pts[j]).append(" ").append(wus[j]);
						}
					} else {
						speaker.append(pts[j]);
					}
				} else if (j != pts.length - 1 && pts[pts.length - 1] != null) {
					if (j < wus.length) {
						if (wus[j].equals("--")) {
							speaker.append(pts[j]).append(",");
						} else {
							speaker.append(pts[j]).append(" ").append(wus[j]).append(",");
						}
					} else {
						speaker.append(pts[j]).append(",");
					}
				}

			} else {
				speaker.append("--");
			}
		}
		return speaker.toString();
	}

	public String getOracleSQLIn(List<?> ids, int count, String field) {
		count = Math.min(count, 1000);
		int len = ids.size();
		int size = len % count;
		if (size == 0) {
			size = len / count;
		} else {
			size = (len / count) + 1;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			int fromIndex = i * count;
			int toIndex = Math.min(fromIndex + count, len);
			// System.out.println(ids.subList(fromIndex, toIndex));
			String productId = StringUtils.defaultIfEmpty(StringUtils.join(ids.subList(fromIndex, toIndex), "','"), "");
			if (i != 0) {
				builder.append(" OR ");
			}
			builder.append(field).append(" IN ('").append(productId).append("')");
		}

		return StringUtils.defaultIfEmpty(builder.toString(), field + " IN ('') ");
	}

	// escape编码
	public String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	// escape解码
	public String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	/**
	 * 将oracle clob字段转为String
	 * @param clob
	 * @return
	 */
	public String oracleClob2Str(Clob clob)  {
	    try {  
	        return (clob != null ? clob.getSubString(1, (int) clob.length()) : null);  
	    } catch (SQLException e) {  
	        e.printStackTrace();  
	    }  
	    return "";  
	}  
	
	/** 
	* 获得纯文本
	* @param s 
	* @return 获得纯文本
	*/ 
	public String getSimpleContent(String s){
	   Pattern pattern = Pattern.compile("<style.+?>.+?</style>", Pattern.DOTALL); 
 	   Matcher matcher = pattern.matcher(s); 
 	   s = matcher.replaceAll(""); 
 	
 	   pattern = Pattern.compile("<script.+?>.+?</script>", Pattern.DOTALL); 
 	   matcher = pattern.matcher(s); 
 	   s = matcher.replaceAll(""); 
 	
 	   pattern = Pattern.compile("<.+?>", Pattern.DOTALL); 
 	   matcher = pattern.matcher(s); 
 	   s = matcher.replaceAll("").replaceAll("&nbsp;","").replaceAll(" ",""); 
	   return s;   
	}
	
	/**
	 * 序列化为对象
	 * 
	 * @return
	 */
	public <T> T jsonSerializeObject(String json, Class<T> clazz) {
		return JSONObject.parseObject(json, clazz);
	}

	/**
	 * 对象序列化为json
	 *
	 * @param value
	 * @return
	 */
	public byte[] objectSerializeJSON(Object value) {
		return JSONObject.toJSONBytes(value, SerializerFeature.WriteClassName);
	}

	/**
	 * 序列化为对象
	 *
	 * @param bytesJson
	 * @return
	 */
	public Object jsonSerializeObject(byte[] bytesJson) {
		try {
			return JSONObject.parse(new String(bytesJson, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * 随机得到一串数组
	 * 
	 * @param min
	 *            大于等于
	 * @param max
	 *            小于等于
	 * @return
	 */
	public int getRandomNumber(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}
	
	/**
	 * 随机得到一串数字
	 * 
	 * @return
	 */
	public String getRandomNum(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(toStringForArray(templateNum, getRandomNumber(templateNum.length - 1), ""));
		}
		return sb.toString();
	}
	
	/**
	 * 随机得到一串数组
	 * 
	 * @param n
	 *            小于多少
	 * @return
	 */
	public int getRandomNumber(int n) {
		Random random = new Random((long) (Math.random() * 1000000));
		return random.nextInt(n);
	}
	
	/**
	 * 根据数组下标获取值 Object[] array = {1,3,5} toObjectForArray(array,0) = 1
	 * 
	 * toObjectForArray(array,1) = 3
	 * 
	 * toObjectForArray(array,3) = null
	 * 
	 * @return T
	 */
	public <T> T toObjectForArray(T[] args, int index) {
		if (null == args) {
			return null;
		}
		if (index < args.length) {
			return args[index];
		} else {
			return null;
		}
	}
	
	/**
	 * 根据数组下标获取值 String[] array = {"A","B","C"} toStringForArray(array,0,"def")
	 * = "A"
	 * 
	 * toStringForArray(array,1,"def") = "B"
	 * 
	 * toStringForArray(array,3,"def") = "def"
	 * 
	 * @param def
	 * 
	 * @return
	 */
	public String toStringForArray(Object[] args, int index, String def) {
		Object obj = toObjectForArray(args, index);
		String str = toString(obj);
		if (!isEmpty(str)) {
			return str;
		}
		return def;
	}
	
	/**
	 * 随机得到一个字符串
	 * 
	 * @return
	 */
	public String getRandomStr(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(toStringForArray(templateStr, getRandomNumber(0, templateStr.length - 1), ""));
		}
		return sb.toString();
	}
	
	/**
	 * jwt验证
	 */
	
	public com.auth0.jwt.algorithms.Algorithm getAlgorithm() {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		kpg.initialize(1024);
		KeyPair kp = kpg.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
		com.auth0.jwt.algorithms.Algorithm algorithm = com.auth0.jwt.algorithms.Algorithm.RSA256(publicKey, privateKey);
		return algorithm;
	} 
	
	/**
	 * 判断是否为空 当数组任意一个为空 返回true
	 * 
	 * @return
	 */
	public boolean isAnyEmpty(Object... objs) {
		boolean flag = false;
		for (Object obj : objs) {
			if (isEmpty(obj)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 判断是否为空 当数组所有值为空 返回true
	 * 
	 * @return
	 */
	public boolean isAllEmpty(String... strs) {
		boolean flag = true;
		for (String str : strs) {
			if (!isEmpty(str)) {
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	
	/**
	 * 判断是否为空 当数组所有值为空 返回true
	 * 
	 * @return
	 */
	public boolean isAllNotEmpty(Object... objs) {
		return !isAnyEmpty(objs);
	}

	/**
	 * 判断是否为空 当数组所有值为空 返回true
	 * 
	 * @return
	 */
	public boolean isAllEmpty(Object... objs) {
		boolean flag = true;
		for (Object obj : objs) {
			if (!isEmpty(obj)) {
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 首字母转大写
	 * 
	 * @return
	 */
	public String toUpperCaseFirstOne(String name) {
		StringBuilder sb = new StringBuilder(name);
		String c = toString(sb.charAt(0));
		if (c.toUpperCase().equals(c)) {
			return sb.toString();
		}
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * 首字母转小写
	 * 
	 * @return
	 */
	public String toLowerCaseFirstOne(String name) {
		StringBuilder sb = new StringBuilder(name);
		String c = toString(sb.charAt(0));
		if (c.toLowerCase().equals(c)) {
			return sb.toString();
		}
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}
	
	/**
	 * 根据value 得到key 按照最先找到的算
	 * 
	 * @return
	 */
	public String getKeyForMap(Map map, Object value) {
		String propertity = null;
		if (null != map && null != value) {
			Set<String> keys = map.keySet();
			for (String key : keys) {
				if (value.equals(map.get(key))) {
					propertity = key;
				}
			}
		}
		return propertity;
	}
	
	/**
	 * 判断是否为非空 非空返回true
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isNotEmpty(Object obj) {
		String str = toString(obj);
		if (null != str && (!"".equals(str))) {
			return true;
		}
		return false;
	}
	


	/**
	 * 字符串转Integer 类型
	 * 
	 * @param str
	 * @return
	 */
	public Integer toInteger(String str, Integer def) {
		Integer number = def;
		if (isEmpty(str)) {
			return number;
		}
		try {
			number = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}

	/**
	 * 字符串转Integer 类型
	 * 
	 * @return
	 */
	public Integer toInteger(Object obj, Integer def) {
		String str = toString(obj);
		return toInteger(str, def);
	}
	
	/**
	 * 根据数组下标获取值 String[] array = {"A","B","C"} toStringForArray(array,0) = "A"
	 * 
	 * toStringForArray(array,1) = "B"
	 * 
	 * toStringForArray(array,3) = ""
	 * 
	 * @return
	 */
	public String toStringForArray(Object[] args, int index) {
		Object obj = toObjectForArray(args, index);
		String str = toString(obj);
		if (!isEmpty(str)) {
			return str;
		}
		return "";
	}
	
	/**
	 * json转Object
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public Object fromJson(String json, Class<?> clazz) {
		Object value = null;
		if (ThreadUtils.getRTex().isNotEmpty(json)) {
			value = JSONObject.parseObject(json, clazz);
		}
		return value;
	}
	
	/**
	 * 将Map中的key转为大写
	 * @param obj
	 * @return
	 */
	public Map EntityToMapKeytoUpper(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] field = obj.getClass().getDeclaredFields();
		for (Field f : field) {
			try {
				Class<?> type = f.getType();
				String name = f.getName();
				if(!"serialVersionUID".equals(name)){
					Method method = obj.getClass().getMethod(
							methodTo("get", f.getName()));
					Object value = method.invoke(obj);
					if (null == value) {
						continue;
					}
					map.put(f.getName().toUpperCase(), value);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		return map;
	}
	

	/**
	 * 字符串分割并转list
	 *
	 * @param str
	 * @param split
	 * @return
	 */
	public List<String> splitToList(String str, String split) {
		String[] splits = org.apache.commons.lang3.StringUtils.split(str, split);
		return ThreadUtils.getCollectionUtils().Array2List(splits);
	}

	/**
	 * 删除最后一次出现的字符
	 *
	 * @param str
	 *            需要处理的字符串
	 * @param lastStr
	 *            需要删除的字符串
	 * @return
	 */
	public String removeLastStr(String str, String lastStr) {
		return replaceLast(str, lastStr, "");
	}

	/**
	 * 替换最后一次出现字符
	 *
	 * @param str
	 *            需要处理的字符串
	 * @param oldStr
	 *            需要替换的字符
	 * @param newStr
	 *            替换为新的字符
	 * @return
	 */
	public String replaceLast(String str, String oldStr, String newStr) {
		StringBuilder sb = new StringBuilder(str);
		if (isAllNotEmpty(str, oldStr)) {
			if (-1 != str.indexOf(oldStr)) {
				sb.setLength(0);
				int newStrLength = newStr.length() == 0 ? 1 : newStr.length();
				int lastIndex = str.lastIndexOf(oldStr);
				sb.append(str.substring(0, lastIndex));
				sb.append(newStr);
				sb.append(str.substring(lastIndex + newStrLength));
			}
		}

		return sb.toString();
	}



	/**
	 * 获取decode类型转换器的值
	 *
	 * @param decode
	 *            decode类型转换器
	 * @param value
	 *            需要转换的值
	 * @return
	 */
	public String getDecodeKey(String decode, Object value) {
		return getDecodeKey(decode, value, true);
	}
	/**
	 * 获取decode类型转换器的值
	 *
	 * @param decode
	 *            decode类型转换器
	 * @param value
	 *            需要转换的值
	 * @param forward
	 *            方向：true正向，例如 a,1,b,2,c,3   输入a返回1。
	 *                 false反向，输入1返回a
	 * @return
	 */
	public String getDecodeKey(String decode, Object value, boolean forward) {
		String converKey = "";
		if (isAllNotEmpty(decode, value)) {
			Map<String, String> decodeMap = ThreadUtils.getCollectionUtils().splitStringToMap(decode, ",");
			String decodeValue = toString(value);
			if (decodeValue.indexOf(",") == -1) {
				for (Map.Entry<String, String> entry : decodeMap.entrySet()) {
					if(forward){
						if (toString(value).equals(entry.getKey())) {
							converKey = entry.getValue();
						}
					}else{
						if (toString(value).equals(entry.getValue())) {
							converKey = entry.getKey();
						}
					}

				}
			}
		}
		return converKey;
	}

	/**
	 * 对象序列化
	 *
	 * @param obj
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public byte[] objSerializeByte(Object obj) {
		byte[] bytes = null;
		if (obj instanceof Serializable) {
			bytes = SerializationUtils.serialize((Serializable) obj);
		} else {
			logger.error("对象没有集成序列化接口，不能进行序列化转换...");
		}
		return bytes;
	}

	/**
	 * 对象反序列化
	 *
	 * @param bytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Object byteSerializeObj(byte[] bytes) {
		Object value = null;
		if (null != bytes) {
			value = SerializationUtils.deserialize(bytes);
		}
		return value;
	}

	/**
	 * url 乱码解析
	 *
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String decodeURI(String value) throws UnsupportedEncodingException {
		return decodeURI(value, "UTF-8");
	}

	/**
	 * url乱码解析
	 *
	 * @param value
	 * @param character
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String decodeURI(String value, String character) throws UnsupportedEncodingException {
		String decodeValue = value;
		if (isNotEmpty(value)) {
			value = value.replaceAll("%", "%25");
			decodeValue = URLDecoder.decode(value, character);
		}
		return decodeValue;
	}

	/**
	 * 数组转字符串
	 *
	 * @param array
	 *            需要转换的数组
	 *
	 * @return 例如 arrayToString(new[]{"1","2","3"}) = "1,2,3"
	 */
	public String arrayToString(Object[] array) {
		if (null == array) {
			return null;
		}
		return arrayToString(array, ",");
	}

	/**
	 * 数组转字符串
	 *
	 * @param array
	 *            需要转换的数组
	 * @param split
	 *            分隔符
	 * @return 例如 arrayToString(new[]{"1","2","3"},",") = "1,2,3"
	 */
	public String arrayToString(Object[] array, String split) {
		if (null == array) {
			return null;
		}
		String result = "";
		StringBuilder sb = new StringBuilder();
		for (Object obj : array) {
			sb.append(toString(obj)).append(split);
		}
		if (!isEmpty(split)) {
			result = removeLastStr(sb.toString(), split);
		}
		return result;
	}
	
	//byte数组转成16进制字符串
	public static String byte2hex(byte[] bytes) {
	    StringBuilder sign = new StringBuilder();
	    for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(bytes[i] & 0xFF);
	        if (hex.length() == 1) {
	            sign.append("0");
	        }
	        sign.append(hex.toLowerCase());
	    }
	    return sign.toString();
	}
	//Md5摘要
	public static byte[] encryptMD5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    MessageDigest md5 = MessageDigest.getInstance("MD5"); 
	    return md5.digest(data.getBytes("UTF-8")); 
	}

	/**
	 * 对比
	 * @return
	 */
	public boolean equals(Object obj1, Object obj2){
		if(obj1 != null && obj2 != null){
			return obj1.equals(obj2);
		}

		return false;
	}
	
	/**
	 * 根据当前时间+-N天
	 * @param day
	 * @return 返回date 
	 */
	public static Date getDateTime(int day) {
		Calendar calendar = Calendar.getInstance(); //创建Calendar 的实例
		calendar.add(Calendar.DAY_OF_MONTH,day); 
		return calendar.getTime();
	}

}
