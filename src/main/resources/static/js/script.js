
var alist;
function imgList(getlist){
    alist = JSON.parse(getlist);
}

$(function(){
   $(window).resize(function(){
       var width = parseInt($(this).width());
       var height = parseInt($(this).height());
       var main_cover_height = $(".main-cover-img").css('padding-bottom');
       
       //width가 일정 크기 이하로 줄어들면 notice height비율 늘리기(핸드폰 버전)
       var board_height = height - parseInt(main_cover_height) - 70;
       console.log(width +" "+ height +" "+main_cover_height+" "+board_height);

       $(".forum-wrap").css('height', board_height);
    
   }).resize();

        $(".login-content").click(function(e){
            //$(".login-form").attr("action","/login");
            var send = e.target;
    //        $(".login-content").toggleClass("active");
    //        $(".login-form").toggleClass("active");
    //
            if($(e.target).is(".login-content") || $(e.target).is(".login-content > p")){
                $(".login-content").toggleClass("active");
                $(".login-form").toggleClass("active");

                $(".signup-btn").removeClass("active");
                $(".name").removeClass("active");
                $(".login").removeClass("active");

                $(".login-btn-box-bottom > .signup").text("Sign up");
                $(".login-form").attr("action","/login");
                $(".hidden-login-form").attr("action","/login");
            }else{

            }

        });

        $(".login-btn-box-bottom > .signup").click(function(){
           $(".signup-btn").toggleClass("active");
            $(".name").toggleClass("active");
            $(".login").toggleClass("active");

            $(".login-form").attr("action","/forum/signup");
            $(".hidden-login-form").attr("action","/forum/signup");

            if($(this).text() == "Sign up"){
                $(this).text("Log in");
            }else{
                $(this).text("Sign up");
                $(".login-form").attr("action","/login");
                $(".hidden-login-form").attr("action","/login");
            }

        });



        if($(".test").text() == "success"){
            alert("로그인 성공");
        }else if($(".test").text() == "fail"){
            alert("로그인 실패");
        }else if($(".test").text() == "need"){
            alert("로그인 필요");
        }

        $(".add-file").change(function(e){
                   var file = $(this).get(0).files;
                     var reader = new FileReader();
                     reader.readAsDataURL(file[0]);
                     reader.addEventListener("load", function(e) {
                     var image = e.target.result;
                     $(".add-img").attr('src', image);
                     });
         });

        $("tbody tr").mouseover(function(){
            var count = $(this).index();
            var imgPath = alist[count];
            var path = "../imgFiles/";
            $(".main-cover-img").css({"background-image" : "url(\""+path+imgPath+"\")"});
       });

       $("tbody tr").mouseleave(function(){
            $(".main-cover-img").css({"background-image" : "none"});
       });


});