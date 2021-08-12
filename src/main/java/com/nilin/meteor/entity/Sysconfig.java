package com.nilin.meteor.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;



/**
 * <p>
 * 
 * </p>
 *
 * @author ywq
 * @since 2021-03-06
 */
@TableName("met_sysconfig")
public class Sysconfig extends Model<Sysconfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="sysc001", type= IdType.AUTO)
	private Integer sysc001;
    /**
     * 配置项类型：0系统配置 1微信 2支付宝 3redis
     */
	private Integer sysc002;
    /**
     * 配置项分类编码
     */
	private String sysc003;
    /**
     * 配置项key
     */
	private String sysc004;
    /**
     * 配置项value（多个用;分割）
     */
	private String sysc005;
    /**
     * 排序号
     */
	private Integer sysc006;
    /**
     * 配置项状态：0禁用 1启用
     */
	private Integer sysc007;
    /**
     * 配置项描述
     */
	private String sysc008;


	@Override
	protected Serializable pkVal() {
		return this.sysc001;
	}

	public Integer getSysc001() {
		return sysc001;
	}

	public void setSysc001(Integer sysc001) {
		this.sysc001 = sysc001;
	}

	public Integer getSysc002() {
		return sysc002;
	}

	public void setSysc002(Integer sysc002) {
		this.sysc002 = sysc002;
	}

	public String getSysc003() {
		return sysc003;
	}

	public void setSysc003(String sysc003) {
		this.sysc003 = sysc003;
	}

	public String getSysc004() {
		return sysc004;
	}

	public void setSysc004(String sysc004) {
		this.sysc004 = sysc004;
	}

	public String getSysc005() {
		return sysc005;
	}

	public void setSysc005(String sysc005) {
		this.sysc005 = sysc005;
	}

	public Integer getSysc006() {
		return sysc006;
	}

	public void setSysc006(Integer sysc006) {
		this.sysc006 = sysc006;
	}

	public Integer getSysc007() {
		return sysc007;
	}

	public void setSysc007(Integer sysc007) {
		this.sysc007 = sysc007;
	}

	public String getSysc008() {
		return sysc008;
	}

	public void setSysc008(String sysc008) {
		this.sysc008 = sysc008;
	}
}
