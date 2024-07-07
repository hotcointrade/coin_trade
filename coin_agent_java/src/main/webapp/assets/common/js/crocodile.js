var host = 'http://192.168.101.184:8884/api'

var istel = /^1\d{10}$/;
var num1 = 0;
var num2 = 0;

$(function(){
	$('#registerTel').val('');
	$('#getCode').val('');
	var viteCode = $("#registervite").val();
	//邀请码
	if(viteCode){
		$('#registervite').val(viteCode);
		$('#registervite').attr('disabled',true);
	}
	$('#selectBtn').change(function(){
		var name = $(this).val();
		Translater.setLang(name);
		defalutLanguage = name;
	})

	$('.registerB>a').click(function(){

		location.href = '/reg/argee';
	})
//	发送验证码
	$('#getBtn').click(function(){
		if(!$('#registerTel').val()){
			$.toast('请输入手机号码','text');
			return;
		}

		if(!(istel.test($('#registerTel').val()))){
			$.toast('请输入11位的手机号码','text');
			return;
		}

		num1 += num1+1;
		if(num1 == 1){
			sendInform();
		}

	})

//	注册
	$('#registerBtn').on('click',function(){
		if(!$('#registerTel').val()){
			$.toast('请输入手机号码','text');
			return;
		}
		//验证手机号
		if(!(istel.test($('#registerTel').val()))){
			$.toast('请输入11位的手机号码','text');
			return;
		}
		if(!$('#registerPwd').val()){
			$.toast('请输入登录密码','text');
			return;
		}
		//验证密码
		var isPsd = /^[A-Za-z0-9]{6,16}$/;
		if(!(isPsd.test($('#registerPwd').val()))){
			$.toast('请输入6-16位的密码','text');
			return;
		}
		if(!$('#registerrePwd').val()){
			$.toast('请输入重复登录密码','text');
			return;
		}
		if($('#registerPwd').val() != $('#registerrePwd').val()){
			$.toast('两次密码输入不一致','text');
			return;
		}

		if(!$('#registervite').val()){
			$.toast('请输入邀请码','text');
			return;
		}
		if(!$('#getCode').val()){
			$.toast('请输入验证码','text');
			return;
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
	var data = {
		phone:$('#registerTel').val()
	};
	$.ajax({
		type:"post",
		url:host+'/getMsg',
		async:true,
		data:data,
		dataType:'json',
		success:function(data){
			if(data.code == 200){
				$.toast('发送成功','text');
				var timerY = null, tn = 120;
				$('#getBtn').attr('disabled',true);
				timerY = setInterval(function(){
					$('#getBtn').val('验证码('+ tn +'s)');
					tn --;
					if(tn < 1){
						$('#getBtn').val('验证码');
						$('#getBtn').attr('disabled',false);
						clearInterval(timerY);
					}
				},1000);
			}else{
				num1 = 0;
				alert(data.msg);
			}
		},
		error:function(xhr,type,errorThrown){
			console.log(xhr.statusText);
			console.log("错误提示了："+ xhr.status +" ");
			num1 = 0;
		}
	});
}
//注册
function registerAjax(){
	var data = {
		phone:$('#registerTel').val(),
		password:$('#registerPwd').val(),
		confirmPwd:$('#registerrePwd').val(),
		invite:$('#registervite').val(),
		msg:$('#getCode').val()
	};
	jQuery.support.cors = true;
	$.ajax({
		type:"post",
		url:host+'/register',
		async:true,
		data:data,
		dataType:'json',
		success:function(data){
			if(data.code == 200){
				$.toast('注册成功','text');
				setTimeout(function(){
					location.href = '/reg/link';
				},700)
			}else{
				$.toast(data.msg,'text');
				num2 = 0;
			}
		},
		error:function(xhr,type,errorThrown){
			console.log(xhr.statusText);
			console.log("错误提示了："+ xhr.status +" ");
			num2 = 0;
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