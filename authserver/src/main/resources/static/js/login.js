$(function () {
    $("#btn-login").click(function () {
        $(".login-main-title a").removeClass("login-btn-selected");
        $(this).addClass("login-btn-selected");
        $(".login-main-con").hide();
        $("#login-main").show();
    });
    $("#btn-register").click(function () {
        $(".login-main-title a").removeClass("login-btn-selected");
        $(this).addClass("login-btn-selected");
        $(".login-main-con").hide();
        $("#register-main").show();
    });
    $("#formsubmit").click(function () {
        $("#password").val(md5($("#password").val()));
        $("#login-main").submit()
    });
    $("#regissubmit").click(function () {
        if($('#regisspassword').val()!='')  $("#regisspassword").val(md5($("#regisspassword").val()));
        $('#register-main').submit()
    })

    $("#login-main").validate({
        submitHandler: function (form) {
            form.submit();
        },
        rules: {
            username: "required",
            password: "required"

        },
        messages: {
            username: "*请输入账号",
            password: "*请输入密码"
        }
    });

    //检测手机号是否正确
    jQuery.validator.addMethod("isMobile", function (value, element) {
        var length = value.length;
        var regPhone = /^1([3578]\d|4[57])\d{8}$/;
        return this.optional(element) || ( length == 11 && regPhone.test(value) );
    }, "*请正确填写您的手机号码");
    //检测下拉选项是否选中
    jQuery.validator.addMethod("isSelected", function (value, element) {
        if (value == "请选择单位") {
            return false
        } else {
            return true
        }
    }, "*请选中你的单位");

    $("#register-main").validate({
        submitHandler: function (form) {
            form.submit();
        },
        rules: {
            regisname: "required",
            regisspassword: {"required": true, "minlength": 5},
            regisrealname: "required",
            regisdeaprtment: {
                "required": true,
                "isSelected": true
            },
            regisphone: {
                "required": true,
                "isMobile": true
            }
        },
        messages: {
            regisname: "*请输入账户",
            regisspassword: {"required": "请输入密码", "minlength": "*密码长度不能小于5位"},
            regisrealname: "*请输入真实姓名",
            // regisdeaprtment: "",
            regisphone: {
                "required": "*请输入手机号",
                "isMobile": "*请正确输入手机号"
            }
        }
    });

    $(document).keyup(function (event) {
        if (event.keyCode == '13') {
            $("#password").val(md5($("#password").val()));
            $('#login-main').submit();
        }
    if (event.keyCode == 13) window.event.keyCode = 9;
    });
})