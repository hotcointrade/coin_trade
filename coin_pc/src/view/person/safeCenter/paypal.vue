<template>
	<div class="paypal_div">
		<el-row :gutter="60">
			<el-breadcrumb separator-class="el-icon-arrow-right">
				<el-breadcrumb-item :to="{ path: '/set' }">{{ $t('person.set') }}</el-breadcrumb-item>
				<el-breadcrumb-item :to="{ path: '/pay' }">{{ $t('legal.methods') }}</el-breadcrumb-item>
				<el-breadcrumb-item>PayPal</el-breadcrumb-item>
			</el-breadcrumb>
			<el-divider></el-divider>
			<el-col :xs="24" :sm="24" :md="12" :lg="12">
				<el-form ref="form" :model="form">
					<Account />

					<el-form-item :label="$t('nav.account')" prop="account"
						:rules="[{ required: true, message: $t('verification.account') }]">
						<el-input v-model="form.account" autocomplete="off" :placeholder="$t('payMethods.paypalInput')">
						</el-input>
					</el-form-item>

					<el-button class="themeBtn" @click="bindFun('form')">{{ $t('table.keep') }}</el-button>

				</el-form>
			</el-col>
			<el-col :xs="24" :sm="24" :md="12" :lg="12">

			</el-col>
		</el-row>
	</div>
</template>

<script>
import codeStatus from '@/config/codeStatus'
import { addReciveApi } from '@/api/getData'
import Account from '@/components/Account'
export default {
	inject: ['reload'],
	data() {
		return {
			form: {
				name: '',
				account: ''
			},
		}
	},
	methods: {
		bindFun(form) {
			var that = this;
			that.$refs[form].validate(async (valid) => {
				if (valid) {
					var dataArr = new URLSearchParams();
					dataArr.set('idcard', that.form.account);
					dataArr.set('type', 'PAYPAL');
					var res = await addReciveApi(dataArr);
					if (res.success) {
						that.$message({
							type: 'success',
							message: res.msg
						})
						setTimeout(() => {
							that.$router.back(-1)
						}, 800)
					} else {
						codeStatus(res.code, function (msg) {
							that.$message.error(msg);
						})
					}
				}
			})
		}
	},
	components: {
		Account
	}
}
</script>

<style lang="less">
.paypal_div {
	.el-breadcrumb {
		padding: 0px 0 0px 30px;

		.el-breadcrumb__inner {
			color: #8E8E8E;
		}
	}

	.el-form-item {
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
		border: 1px solid #828ab359;
		padding: 10px;
		border-radius: 4px;

		.el-form-item__label {
			text-align: left;
		}

		.el-form-item__content {
			.el-select {
				width: 94%;
			}
		}

		.el-input-group__append {
			border: none;
			background-color: transparent;
			color: #87D8EA;
			cursor: pointer;
		}

		.el-input__inner {
			border: none;
			background-color: transparent;
			padding: 0;
		}
	}

	.el-button {
		width: 100%;
		margin: 20px 0;
	}

	.el-input__icon {
		width: 100%;
		cursor: pointer;
		font-style: normal;
	}
}
</style>