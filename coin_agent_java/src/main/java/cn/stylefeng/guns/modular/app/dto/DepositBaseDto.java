package cn.stylefeng.guns.modular.app.dto;

/**
 * 充值回调接收类
 */
public class DepositBaseDto {
	
	 	private Long id;

	    private String coin;

	    private String userId;

	    private String fromAddress;

	    private String toAddress;

	    private String gases;

	    private String amount;

	    private String txHash;

	    private Long blockIndex;

	    private String blockHash;

	    private Long tranIndex;

	    private Long timeStart;

	    private Long timeReceived;

	    private String memberCode;

	    private String key;

	    private String timeStamp;

	    private String nonceStr;

	    private String sign;

	    private String vout;

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

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
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

		public Long getTimeReceived() {
			return timeReceived;
		}

		public void setTimeReceived(Long timeReceived) {
			this.timeReceived = timeReceived;
		}

		public String getMemberCode() {
			return memberCode;
		}

		public void setMemberCode(String memberCode) {
			this.memberCode = memberCode;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}

		public String getNonceStr() {
			return nonceStr;
		}

		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getVout() {
			return vout;
		}

		public void setVout(String vout) {
			this.vout = vout;
		}

	    

}
