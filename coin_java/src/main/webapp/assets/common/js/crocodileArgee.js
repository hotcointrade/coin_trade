$(function(){
	jQuery.support.cors=true;
	$.ajax({
		type:"post",
		url:"http://192.168.101.184:8884/api/declares",
		async:true,

		data:{
			type:1
		},
		success:function(data){
			if(data.code == 200){
				var x = document.getElementsByClassName("content");
				x[0].innerHTML = data.data.content;
			}else{
				console.log(data)
			}
		}
	});
	
	
	$('.title a').click(function(){
		window.history.back(-1);
	})
})