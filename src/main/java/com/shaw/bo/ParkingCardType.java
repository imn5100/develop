package com.shaw.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ParkingCardType implements Serializable {
	private static final long serialVersionUID = -4426046654709508949L;

	private Integer id;

	private String cardType;

	private BigDecimal fee;

	private Short isValid;

	private Long createTime;

	private Short validTime;

	public Short getValidTime() {
		return validTime;
	}

	public void setValidTime(Short validTime) {
		this.validTime = validTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType == null ? null : cardType.trim();
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Short getIsValid() {
		return isValid;
	}

	public void setIsValid(Short isValid) {
		this.isValid = isValid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		ParkingCardType other = (ParkingCardType) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getCardType() == null ? other.getCardType() == null
						: this.getCardType().equals(other.getCardType()))
				&& (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
				&& (this.getIsValid() == null ? other.getIsValid() == null
						: this.getIsValid().equals(other.getIsValid()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getCardType() == null) ? 0 : getCardType().hashCode());
		result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
		result = prime * result + ((getIsValid() == null) ? 0 : getIsValid().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		return result;
	}
}