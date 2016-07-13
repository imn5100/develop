package com.shaw.mallmapper;

import java.io.Serializable;
import java.math.BigDecimal;

public class RestshopReportStatistics implements Serializable {
	private static final long serialVersionUID = 8449820647450251168L;
	private Integer id;
	private String reportTime;
	private String entityId;
	private BigDecimal discountAmount;
	private BigDecimal resultAmount;
	private Long saleOrder;
	private Long saleNumber;
	private BigDecimal pushMoney;
	private Long invoiceCount;
	private BigDecimal recieveAmount;
	private Long createTime;
	private Byte isValid;
	private Long opTime;
	private Long lastVer;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getResultAmount() {
		return resultAmount;
	}

	public void setResultAmount(BigDecimal resultAmount) {
		this.resultAmount = resultAmount;
	}

	public Long getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(Long saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Long getSaleNumber() {
		return saleNumber;
	}

	public void setSaleNumber(Long saleNumber) {
		this.saleNumber = saleNumber;
	}

	public BigDecimal getPushMoney() {
		return pushMoney;
	}

	public void setPushMoney(BigDecimal pushMoney) {
		this.pushMoney = pushMoney;
	}

	public Long getInvoiceCount() {
		return invoiceCount;
	}

	public void setInvoiceCount(Long invoiceCount) {
		this.invoiceCount = invoiceCount;
	}

	public BigDecimal getRecieveAmount() {
		return recieveAmount;
	}

	public void setRecieveAmount(BigDecimal recieveAmount) {
		this.recieveAmount = recieveAmount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Byte getIsValid() {
		return isValid;
	}

	public void setIsValid(Byte isValid) {
		this.isValid = isValid;
	}

	public Long getOpTime() {
		return opTime;
	}

	public void setOpTime(Long opTime) {
		this.opTime = opTime;
	}

	public Long getLastVer() {
		return lastVer;
	}

	public void setLastVer(Long lastVer) {
		this.lastVer = lastVer;
	}

}
