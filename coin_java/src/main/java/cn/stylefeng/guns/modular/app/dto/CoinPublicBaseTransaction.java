package cn.stylefeng.guns.modular.app.dto;

import java.io.Serializable;

public class CoinPublicBaseTransaction implements Serializable {

	private static final long serialVersionUID = 6682753995701480249L;
	
	private Long id;
	
	private String coin;
	
	private String fromMemberCode;
	
	private String toMemberCode;
	
	private Integer type;
	
	private Integer success;
	
	private String token;
	
	private String fromAddress;
	
	 private String toAddress;
	 
	 private String gases;
	 
	 private String amount;

    private String gasPrice;

    private Double gasLimit;

    private String nonce;

    private Integer status;
    
    private String txHash;
    
    private Long blockIndex;
    
    private String blockHash;
    
    private Long tranIndex;
    
    private Long timeStart;
    
    private String message;
    
    private Long timeReceived;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getFromMemberCode() {
		return fromMemberCode;
	}

	public void setFromMemberCode(String fromMemberCode) {
		this.fromMemberCode = fromMemberCode;
	}

	public String getToMemberCode() {
		return toMemberCode;
	}

	public void setToMemberCode(String toMemberCode) {
		this.toMemberCode = toMemberCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getGases() {
		return gases;
	}

	public void setGases(String gases) {
		this.gases = gases;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}

	public Double getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(Double gasLimit) {
		this.gasLimit = gasLimit;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public Long getBlockIndex() {
		return blockIndex;
	}

	public void setBlockIndex(Long blockIndex) {
		this.blockIndex = blockIndex;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public Long getTranIndex() {
		return tranIndex;
	}

	public void setTranIndex(Long tranIndex) {
		this.tranIndex = tranIndex;
	}

	public Long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(Long timeReceived) {
		this.timeReceived = timeReceived;
	}
    
    
    
    
    
    
}
