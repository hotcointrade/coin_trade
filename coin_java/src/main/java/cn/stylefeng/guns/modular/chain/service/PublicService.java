package cn.stylefeng.guns.modular.chain.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.app.vo.chain.WithdrawDto;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.HttpUtils;
import cn.stylefeng.guns.modular.chain.entity.Public;
import cn.stylefeng.guns.modular.chain.mapper.PublicMapper;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 公账提币Service
 *
 * @author yaying.liu
 * @Date 2020-01-17 18:55:07
 */
@Service
public class PublicService extends ServiceImpl<PublicMapper, Public> {
	
	/**
	 * 钱包key
	 */
	@Value("${eth.key}")
	private String key;
	/**
	 * 钱包secret
	 */
	@Value("${eth.secret}")
	private String secret;
	/**
	 * 钱包memberCode
	 */
	@Value("${eth.memberCode}")
	private String memberCode;
	
	@Value("${eth.createUrl}")
	private String createUrl;
	/**
	 * 校验地址
	 */
	@Value("${eth.checkAddressUrl}")
	private String checkAddressUrl;
	
	@Value("${eth.accountBalanceUrl}")
	private String accountBalanceUrl;
	@Value("${eth.mentionUrl}")
	private String mentionUrl;
	
	@Value("${eth.sendPublicTransaction}")
	private String sendPublicTransaction;
	
	@Value("${eth.findMainAccount}")
	private String findMainAccount;
	
	/**
	 * 查询公账提币
	 */
	public Page<Map<String, Object>> selectByCondition(Map condition) {
		Page page = LayuiPageFactory.defaultPage();
		return this.baseMapper.selectByCondition(page, condition);
	}
	
	/**
	 * 删除公账提币
	 */
	public void deletePublic(Long publicId) {
		Public entity = this.baseMapper.selectById(publicId);
		//将删除标志设置为Y，表示删除
		entity.setDel("Y");
		this.baseMapper.updateById(entity);
	}
	
	/**
	 * 添加公账提币
	 */
	public ResponseData addPublic(Public chainPublic) {
		
		switch (chainPublic.getCoin()) {
			case "ETH_USDT":
			case "ETH":
			case "MGE":
			case "MGM":
				break;
			default:
				return ResponseData.error("暂不支持此币种");
		}
		
		String orderNo = "CP" + IdUtil.simpleUUID();
		
		if (F.me().getSysConfigValueByCode(Constant.CHAIN_OPEN).equals("Y")) {
			/**
			 * TODO:正式环境试验
			 */
			// 校验地址
			JSONObject jsonObject =
					HttpUtils.checkAddress(
							memberCode, key, secret, chainPublic.getCoin(), 1L, chainPublic.getToAddress(), checkAddressUrl);
			if (jsonObject != null) {
				String s = jsonObject.getString("status");
				Boolean data = jsonObject.getBoolean("data");
				if ("200".equals(s)) {
					if (!data) {
						return ResponseData.error(223, "地址错误");
					}
				}
			}
			
			//公账提币
			JSONObject jsonObjectSend = HttpUtils.sendCoinWithdraw(memberCode, key, secret, orderNo,
					chainPublic.getCoin(), 1L, chainPublic.getToAddress(), sendPublicTransaction,
					chainPublic.getPrice());
			if (jsonObjectSend != null) {
				String s = jsonObjectSend.getString("status");
				String msg = jsonObjectSend.getString("message");
				String data = jsonObjectSend.getString("data");
				WithdrawDto withdrawDto = JSONObject.parseObject(data, WithdrawDto.class);
				if ("200".equals(s)) {
					if (withdrawDto != null) {
						chainPublic.setTxHash(withdrawDto.getTxHash());
					}
					chainPublic.setStatus(ProConst.WithdrawStatusEnum.COIN.getCode())
							.setOrderNo(orderNo);
					this.save(chainPublic);
					return ResponseData.success("发起提币成功");
				} else {
					throw new ServiceException(BizExceptionEnum.MENTIONMONEY);
				}
			}
			
		} else {
			/**
			 * TODO：UAT 测试环境
			 */
			String orderNoTest = "TEST" + IdUtil.simpleUUID();
			chainPublic.setStatus(ProConst.WithdrawStatusEnum.COIN.getCode())
					.setOrderNo(orderNoTest);
			this.baseMapper.insert(chainPublic);
			return ResponseData.success("测试环境-提币中");
		}
		return ResponseData.error("错误请求");
	}
	
	/**
	 * 查询余额
	 *
	 * @param type
	 * @return
	 */
	public Object accountBalance(String type) {
		if (StrUtil.isNotBlank(type)) {
			JSONObject jsonObject = HttpUtils.accountBalance(type, key, memberCode, secret, accountBalanceUrl);
			if (jsonObject != null) {
				String s = jsonObject.getString("status");
				String msg = jsonObject.getString("message");
				String data = jsonObject.getString("data");
				if ("200".equals(s)) {
					return ResponseData.success(data);
				} else {
					return ResponseData.error(202, msg);
				}
			}
		}
		return ResponseData.error(202, "查看失败");
	}
	
	public Object findMainAccount(String type) {
		JSONObject jsonObject = HttpUtils.findMainAccount(type, key, memberCode, secret, findMainAccount);
		if (jsonObject != null) {
			String s = jsonObject.getString("status");
			String msg = jsonObject.getString("message");
			String data = jsonObject.getString("data");
			if ("200".equals(s)) {
				return ResponseData.success(data);
			} else {
				return ResponseData.error(202, msg);
			}
		}
		return ResponseData.error("错误");
	}
}