package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: Location
 * @Description: 定位
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Embeddable
public class Location implements Serializable {
	private static final long serialVersionUID = 27L;
	/** 伟度 x */
	@Column(columnDefinition="double comment '伟度'")
	private double lat;
	/** 经度 y */
	@Column(columnDefinition="double comment '经度'")
	private double lng;
	@Column(columnDefinition="varchar(255) comment '位置'")
	private String addr;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}