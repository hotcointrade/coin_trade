var host =$("#registerCode").attr("url");//h5url
var code = $("#registerCode").val();//邀请码
var agree=$("#registerCode").attr("agreeApi");//h5用户协议跳转
var zhengceApi=$("#registerCode").attr("zhengceApi");
var linkApi=$("#registerCode").attr("linkApi");//注册页跳转
var istel = /^1\d{10}$/;
var isPsd = /^[A-Za-z0-9]{6,18}$/;
var num1 = 0,num2 = 0;
var timerY = null, tn = 120;

var defalutLanguage;
var getLanguage = sessionStorage.getItem('setLangs');

if(getLanguage){
	$("#selectBtn option[value="+ getLanguage +"]").prop("selected",true);
	defalutLanguage = getLanguage;
	var Translater = new Translater();
	Translater.setLang(defalutLanguage);
}else{
	$("#selectBtn option[value='default']").prop("selected",true);
	defalutLanguage = 'defalut';
	var Translater = new Translater();
	Translater.setLang('default');
}

$(function(){
	if(code){
		$('#registerCode').val(code);
		$('#registerCode').attr('disabled',true);
	}
	$('#agree').on('click',function(){
		window.location.href = agree;
	})
	$('#zhengce').on('click',function(){
		window.location.href = zhengceApi;
	})

	$('#selectBtn').change(function(){
		var name = $(this).val();
		Translater.setLang(name);
		defalutLanguage = name;
		sessionStorage.setItem('setLangs',name);
		// location.reload();
	})

	var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
	if(type == 'TEL'){
		//选择区号
		regioNum();
	}
	//切换
	$('#showTel').on('click','.weui-navbar__item',function(){
		var type = $(this).attr('data-type');
		if(defalutLanguage == 'tc'){
			$('#getBtn').val('獲取驗證碼');
		}else if(defalutLanguage == 'en'){
			$('#getBtn').val('Send');
		}else if(defalutLanguage == 'jp'){
			$('#getBtn').val('送信');
		}else if(defalutLanguage == 'ko'){
			$('#getBtn').val('발송 하 다');
		}else{
			$('#getBtn').val('获取验证码');
		}
		$('.weui-input').val('');

		$('#registerCode').val(code);
		$('#getBtn').attr('disabled',false);
		num1 = 0;
		var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
		tn = 120;
		if (type =='TEL'){
			tn = 300;
		}
		clearInterval(timerY);
	})

	//发送验证码
	$('#getBtn').click(function(){
		// var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
		var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
		console.log(type)
		if(type == 'TEL'){
			if(!$('#registerTel').val()){
				if(defalutLanguage == 'tc'){
					$.toast('請輸入手機號碼','text');
				}else if(defalutLanguage == 'en'){
					$.toast('Please enter your mobile number','text');
				}else if(defalutLanguage == 'jp'){
					$.toast('携帯電話の番号を入力してください。','text');
				}else if(defalutLanguage == 'ko'){
					$.toast('핸드폰 번 호 를 입력 하 세 요.','text');
				}else{
					$.toast('请输入手机号码','text');
				}
				return false ;
			}
		}else{
			if(!$('#registerEmail').val()){
				if(defalutLanguage == 'tc'){
					$.toast('請輸入郵箱','text');
				}else if(defalutLanguage == 'en'){
					$.toast('Please edefalutLanguagenter email address','text');
				}else if(defalutLanguage == 'jp'){
					$.toast('メールボックスを入力してください','text');
				}else if(defalutLanguage == 'ko'){
					$.toast('메 일 박스 를 입력 하 세 요.','text');
				}else{
					$.toast('请输入邮箱','text');
				}
				return false;
			}
		}
		num1 += num1+1;
		if(num1 == 1){
			sendInform();
		}

	})

	//	注册
	$('#registerBtn').on('click',function(){
		//校验手机号
		// var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
		var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
		if(type == 'TEL'){
			if(!$('#registerTel').val()){
				if(defalutLanguage == 'tc'){
					$.toast('請輸入手機號碼','text');
				}else if(defalutLanguage == 'en'){
					$.toast('Please enter your mobile number','text');
				}else if(defalutLanguage == 'jp'){
					$.toast('携帯電話の番号を入力してください。','text');
				}else if(defalutLanguage == 'ko'){
					$.toast('핸드폰 번 호 를 입력 하 세 요.','text');
				}else{
					$.toast('请输入手机号码','text');
				}
				return false ;
			}
		}else{
			if(!$('#registerEmail').val()){
				if(defalutLanguage == 'tc'){
					$.toast('請輸入郵箱','text');
				}else if(defalutLanguage == 'en'){
					$.toast('Please enter email address','text');
				}else if(defalutLanguage == 'jp'){
					$.toast('メールボックスを入力してください','text');
				}else if(defalutLanguage == 'ko'){
					$.toast('메 일 박스 를 입력 하 세 요.','text');
				}else{
					$.toast('请输入邮箱','text');
				}
				return false;
			}
		}
		if(!$('#registerPwd').val()){
			if(defalutLanguage == 'tc'){
				$.toast('請輸入登入密碼','text');
			}else if(defalutLanguage == 'en'){
				$.toast('Please enter the login password','text');
			}else if(defalutLanguage == 'jp'){
				$.toast('ログインパスワードを入力してください。','text');
			}else if(defalutLanguage == 'ko'){
				$.toast('로그 인 비밀번호 입력','text');
			}else{
				$.toast('请输入登录密码','text');
			}
			return false;
		}
		//验证密码
		if(!(isPsd.test($('#registerPwd').val()))){
			if(defalutLanguage == 'tc'){
				$.toast('請輸入6-18比特的密碼','text');
			}else if(defalutLanguage == 'en'){
				$.toast('Please enter a 6-18 digit password','text');
			}else if(defalutLanguage == 'jp'){
				$.toast('6-18桁のパスワードを入力してください。','text');
			}else if(defalutLanguage == 'ko'){
				$.toast('6 - 18 비트 비밀 번 호 를 입력 하 세 요.','text');
			}else{
				$.toast('请输入6-18位的密码','text');
			}
			return false;
		}

		if(!$('#registerCode').val()){
			if(defalutLanguage == 'tc'){
				$.toast('請輸入邀請碼','text');
			}else if(defalutLanguage == 'en'){
				$.toast('Please enter the invitation code','text');
			}else if(defalutLanguage == 'jp'){
				$.toast('招待コードを入力してください','text');
			}else if(defalutLanguage == 'ko'){
				$.toast('요청 코드 를 입력 하 세 요','text');
			}else{
				$.toast('请输入邀请码','text');
			}
			return false;
		}
		if(!$('#getCode').val()){
			if(defalutLanguage == 'tc'){
				$.toast('請輸入驗證碼','text');
			}else if(defalutLanguage == 'en'){
				$.toast('Please enter the verification code','text');
			}else if(defalutLanguage == 'jp'){
				$.toast('認証コードを入力してください','text');
			}else if(defalutLanguage == 'ko'){
				$.toast('인증번호 입력','text');
			}else{
				$.toast('请输入验证码','text');
			}
			return false;
		}
		var checklist = document.getElementById('checklist');
		if(!checklist.checked){
			if(defalutLanguage == 'tc'){
				$.toast('請先勾選使用者協定！','text');
			}else if(defalutLanguage == 'en'){
				$.toast('Please check the user agreement first!','text');
			}else if(defalutLanguage == 'jp'){
				$.toast('先にユーザプロトコルをチェックしてください。','text');
			}else if(defalutLanguage == 'ko'){
				$.toast('먼저 사용자 협 의 를 선택 하 세 요!','text');
			}else{
				$.toast('请先勾选用户协议！','text');
			}
			return false;
		}
		num2 += num2+1;
		if(num2 == 1){
			registerAjax()
		}
	});
})


//发送验证码
function sendInform(){
	jQuery.support.cors = true;
	if(defalutLanguage == 'tc'){
		$.showLoading("正在加載...");
	}else if(defalutLanguage == 'en'){
		$.showLoading("Loading...");
	}else if(defalutLanguage == 'jp'){
		$.showLoading("読み込み中...");
	}else if(defalutLanguage == 'ko'){
		$.showLoading("로 딩 중...");
	}else{
		$.showLoading("正在加载...");
	}

	var account = '';
	// var type = $('#showTel>a').attr('data-type');
	var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
	if(type == 'TEL'){
		account = $('#registerTel').val()
	}else{
		account = $('#registerEmail').val()
	}
	var areaCode = $('.addTxt>b').attr('data-action');
	var data = {
		'account':account,
		'code':areaCode
	};
	$.ajax({
		type:"post",
		url:host+'/api/getMsg',
		data:data,
		async:true,
		dataType:'json',
		success:function(data){
			if(data.code == 200){
				$.hideLoading();
				if(defalutLanguage == 'tc'){
					$.toast('發送成功','text');
				}else if(defalutLanguage == 'en'){
					$.toast('Sent successfully','text');
				}else if(defalutLanguage == 'jp'){
					$.toast('送信成功','text');
				}else if(defalutLanguage == 'ko'){
					$.toast('발송 성공','text');
				}else{
					$.toast('发送成功','text');
				}
				$('#getBtn').attr('disabled',true);
				timerY = setInterval(function(){
					$('#getBtn').val('('+ tn +'s)');
					tn --;
					if(tn < 1){
						if(defalutLanguage == 'tc'){
							$('#getBtn').val('獲取驗證碼');
						}else if(defalutLanguage == 'en'){
							$('#getBtn').val('Send');
						}else if(defalutLanguage == 'jp'){
							$('#getBtn').val('送信');
						}else if(defalutLanguage == 'ko'){
							$('#getBtn').val('발송 하 다');
						}else{
							$('#getBtn').val('获取验证码');
						}

						$('#getBtn').attr('disabled',false);
						num1 = 0;
						tn=120;
						clearInterval(timerY);
					}
				},1000);
			}else{
				num1 = 0;
				$.hideLoading();
				if(data.code == 2001){
					if(defalutLanguage == 'tc'){
						$.toast('驗證碼有誤','text');
					}else if(defalutLanguage == 'en'){
						$.toast('Verification code error','text');
					}else if(defalutLanguage == 'jp'){
						$.toast('認証コードが間違っています','text');
					}else if(defalutLanguage == 'ko'){
						$.toast('인증번호 가 잘못 되 었 다.','text');
					}else{
						$.toast('验证码有误','text');
					}
				}else{
					$.toast(data.msg,'text');
				}

			}
		},
		error:function(xhr,type,errorThrown){
			$.hideLoading();
			num1 = 0;
			$.toast("错误提示了："+ xhr.status +" ",'text');
		}
	});
}

//注册
function registerAjax(){
	jQuery.support.cors = true;
	if(defalutLanguage == 'tc'){
		$.showLoading("正在加載...");
	}else if(defalutLanguage == 'en'){
		$.showLoading("Loading...");
	}else if(defalutLanguage == 'jp'){
		$.showLoading("読み込み中...");
	}else if(defalutLanguage == 'ko'){
		$.showLoading("로 딩 중...");
	}else{
		$.showLoading("正在加载...");
	}
	var tel = '';
	// var type = $('#showTel>a').attr('data-type');
	var type = $('#showTel>a.weui-bar__item--on').attr('data-type');
	if(type == 'TEL'){
		tel = $('#registerTel').val()
	}else{
		tel = $('#registerEmail').val()
	}
	var areaCode = $('.addTxt>b').attr('data-action');
	var data = {
		account:tel,
		password:$('#registerPwd').val(),
		inviteCode:$('#registerCode').val(),
		msg:$('#getCode').val(),
		code:areaCode
	};
	$.ajax({
		type:"post",
		url:host+'/api/register',
		async:true,
		data:data,
		dataType:'json',
		success:function(data){
			if(data.code == 200){
				$.hideLoading();
				if(defalutLanguage == 'jp'){
					$.toast("註冊成功");
				}else if(defalutLanguage == 'cn'){
					$.toast("login was successful");
				}else if(defalutLanguage == 'jp'){
					$.toast("登録成功");
				}else if(defalutLanguage == 'ko'){
					$.toast("등록 성공");
				}else{
					$.toast("注册成功");
				}
				setTimeout(function(){
					location.href = "https://down.www.xcstaot2021.com";
				},700)
			}else{
				$.hideLoading();
				if(data.code == 2001){
					if(defalutLanguage == 'tc'){
						$.toast('驗證碼有誤','text');
					}else if(defalutLanguage == 'en'){
						$.toast('Verification code error','text');
					}else if(defalutLanguage == 'jp'){
						$.toast('認証コードが間違っています','text');
					}else if(defalutLanguage == 'ko'){
						$.toast('인증번호 가 잘못 되 었 다.','text');
					}else{
						$.toast('验证码有误','text');
					}
				}else if(data.code == 2007){
					if(defalutLanguage == 'tc'){
						$.toast('未找到推薦人','text');
					}else if(defalutLanguage == 'en'){
						$.toast('No references found','text');
					}else if(defalutLanguage == 'jp'){
						$.toast('推薦者が見つかりませんでした','text');
					}else if(defalutLanguage == 'ko'){
						$.toast('추천 인 을 찾 지 못 했 습 니 다.','text');
					}else{
						$.toast('未找到推荐人','text');
					}
				}else if(data.code == 2012){
					if(defalutLanguage == 'tc'){
						$.toast('帳號已存在','text');
					}else if(defalutLanguage == 'en'){
						$.toast('Account already exists','text');
					}else if(defalutLanguage == 'jp'){
						$.toast('アカウントは既に存在します','text');
					}else if(defalutLanguage == 'ko'){
						$.toast('계 정 이 이미 존재 합 니 다','text');
					}else{
						$.toast('账号已存在','text');
					}
				}else{
					$.toast(data.msg,'text');
				}
				num2 = 0;
			}
		},
		error:function(xhr){
			console.log(xhr);
			$.hideLoading();
			num2 = 0;
			$.toast("错误提示了："+ xhr.status +" ",'text');
		}
	});
}
//获取区号
function regioNum(){
	jQuery.support.cors = true;
	$.ajax({
		type:"post",
		url:host+"/api/common/phoneCode",
		data:{},
		dataType:'json',
		success:function(data){
			if(data.code == 200){
				console.log(data);
				var obj = data.data;
				var v_arry = [];
				if(obj.length>0){
					$('.addTxt>b').text(obj[0].code);
					$('.addTxt>b').attr('data-action',obj[0].code);
					for (var i = 0; i < obj.length; i++) {
						var v_data = {
							label : obj[i].code,
							value : {
								type:obj[i].code,
								npc:obj[i].code,
							}
						};
						v_arry.push(v_data);
						$('#showPicker').on('click', function () {
							weui.picker(v_arry, {
								onChange: function (result) {

								},
								onConfirm: function (result) {
									$('.addTxt>b').text(result[0].npc);
									$('.addTxt>b').attr('data-action',result[0].code);
								}
							});
						});
					}

				}
			}
		},
		error:function(xhr,type,errorThrown){
			console.log(xhr.statusText);
			console.log("错误提示了："+ xhr.status +" ");
		}
	});
}
function GetQueryString(){
	var url = window.location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i ++) {
			theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}