<template>
    <div class="content">
        <div class="login-wrapper">
            <div class="left-img">
                <div class="glass">
                    <div class="tip">
                        <div class="tips">
                            <!--                            <div class="title1">标题1</div>-->
                            <h2>有爱,<br/>有梦想,<br/>有向往的方向</h2>
                            <!--                            <span>去拼搏,去创造,敢为人先,追求卓越</span>-->
                            <!--                            <span>我们邀请您加入我们的圈子</span>-->
                        </div>
                    </div>
                </div>
            </div>
            <div class="right-login-form">
                <div class="form-wrapper">
                    <h1>登录界面</h1>

                    <div class="input-items">
                        <span class="input-tips"> 账号 </span>
                        <input type="text" class="inputs" placeholder="请输入您的账号" v-model="user.username">
                    </div>
                    <div class="input-items">
                        <span class="input-tips"> 密码</span>
                        <input type="password" class="inputs" placeholder="请输入您的密码" v-model="user.password">
                        <span class="forgot">忘记密码?</span>
                    </div>
                    <button class="btn" @click="login">登录</button>


                    <div class="other-login">
                        <div class="divider">
                            <span class="line"></span>
                            <span class="divider-text">or</span>
                            <span class="line"></span>
                        </div>
                        <div class="signup-tips">
                            <span>没有账号?</span>
                            <span @click="jumpTo()">注册</span>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

const router = useRouter()
const user = reactive({
    username: "",
    password: ""
})

function login() {
    request.post("/user/login", user).then(res => {
        if (res.status === 200) {
            ElMessage.success({
                message: "登陆成功",
                showClose: true
            })
            //localStorage保存当前登录信息
            const userInfo = res.data;
            localStorage.setItem('user', JSON.stringify(userInfo));
            console.log("userInfo",userInfo);
            setTimeout(() => {
                router.push("/")    //回到主页
            }, 500);
        } else {
            ElMessage.error({
                message: "登陆失败",
                showClose: true
            })
        }
    })
}

function jumpTo() {
    router.push("/register");
}
</script>

<style scoped>
/*设置内外边距 字体 */
* {
    padding: 0;
    margin: 0;
    font-family: "Aguazyuan", Courier, sans-serif;
}

.content {
    /* 设置整个页面的窗口宽度 */
    width: 100vw;
    /* 设置整个页面的窗口高度 */
    height: 100vh;
    /* 设置背景颜色 */
    background-color: rgb(253, 235, 240);
    position: relative;
    overflow-y: auto;
}

.content .login-wrapper {
    width: 70vw;
    height: 80vh;
    /* 背景颜色 */
    background-color: #fff; /* 设置边框锐度 */
    border-radius: 40px; /* 绝对定位 */
    position: absolute;
    left: 5%;
    top: 50%;
    /*将元素对齐 */
    transform: translate(15%, -50%);
    display: flex;
}


.left-img {
    color: white;
    border-radius: 40px;
    flex: 1;
    background: url("../assets/login.jpg") no-repeat;
    background-size: cover;
    position: relative;
}

.content .login-wrapper .left-img .glass {
    width: 40%;
    padding: 20px;
    /* color: #fff; */
    /* 设置绝对定位 */
    position: absolute;
    top: 15%;
    left: 40%;
    /*将元素对齐 */
    transform: translate(-50%, -20%);
    /*背景颜色 */
    background-color: rgba(255, 255, 255, 0.1); /* 设置阴影 */
    box-shadow: 0 8px 32px rgba(31, 38, 135, 0.7); /* 背景毛玻璃 */
    backdrop-filter: blur(10px);
    /* 针对内核浏览器是旧版浏览器的,设置 */
    -webkit-backdrop-filter: blur(10px);
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.48);
}

.content .login-wrapper .left-img .glass .tips .title1 {
    width: 25%;
    font-weight: 600;
    font-size: 15px;
    text-align: center;
    padding: 10px;
    overflow: hidden;
    background-color: rgba(255, 255, 255, 0.1);
    box-shadow: 0 8px 32px 0 rgba(255, 255, 255, 0.37);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.48);
}

.content .login-wrapper .left-img .glass .tips h2 {
    margin: 15px 0;
}

.content .login-wrapper .left-img .glass .tips span {
    margin: 5px 0;
    display: block;
    font-family: "Gill Sans", sans-serif;
}

.content .login-wrapper .right-login-form {
    flex: 1;
    position: relative;
}

.form-wrapper {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}

.content .login-wrapper .right-login-form .form-wrapper h1 {
    font-size: 30px;
    text-align: center;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items {
    margin: 20px 0;
}

.input-tips {
    display: inline-block;
    font-weight: 600;
    font-size: 20px;
    margin: 10px;
}

.inputs {
    width: 100%;
    height: 50px;
    border-radius: 10px;
    border: 1px solid rgb(227, 227, 227);
    padding: 20px;
    box-sizing: border-box;
    outline: none;
    transition: 0.5s;
}

.inputs:focus {
    border: 1px solid rgb(224, 123, 150);
}

.forgot {
    float: right;
    font-weight: 600;
    text-align: right;
    margin: 5px 0;
    font-size: 13px; /* 鼠标放上变小手 */
    cursor: pointer;
}

.btn {
    width: 100%;
    height: 50px;
    background-color: rgb(224, 123, 150);
    border: 0;
    border-radius: 10px;
    color: #fff;
    font-size: 25px;
    margin: 20px;
    /* 鼠标放上变小手 */
    cursor: pointer;
}

.signup-tips {
    text-align: center;
    font-weight: 600;
    margin: 10px 0;
}

.signup-tips span:last-child {
    color: rgb(224, 123, 150); /* 鼠标放上变小手 */
    cursor: pointer;
}

.divider {
    width: 100%;
    margin: 20px 0;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.line {
    display: inline-block;
    max-width: 35%;
    width: 35%;
    flex: 1;
    height: 1px;
    background-color: rgb(162, 172, 185);
}

.divider-text {
    vertical-align: middle;
    margin: 0 20px;
    display: inline-block;
    width: 50px;
    color: rgb(162, 172, 185);
    white-space: normal;
}

.other-login-wrapper {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

.other-login-item {
    width: 70px;
    padding: 10px;
    text-align: center;
    border-radius: 10px;
    cursor: pointer;
    font-weight: 600;
    color: rgb(51, 59, 116);
    margin: 0 10px;
    transition: .4s;
}

.other-login-item img {
    width: 40px;
    height: 40px;
    /*设置元素垂直对齐方式 */
    vertical-align: middle;
}

.other-login-item span {
    /* 设置元素垂直对齐方式 */
    vertical-align: middle;
}

.other-login-item:hover {
    width: 80px;
    height: 50%;
    background-color: #fff;
    border: 0;
    box-shadow: 1px 10px 32px 1px rgba(186, 175, 221, 0.37);
}
</style>