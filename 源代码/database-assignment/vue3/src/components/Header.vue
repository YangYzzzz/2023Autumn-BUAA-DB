<template>
  <!--上方：导航栏-->
    <el-menu
            :default-active="$route.path"
            class="el-menu-demo"
            mode="horizontal"
            :ellipsis="false"
            :router=true
    >
        <!--          background-color="#fff"-->
        <!--          text-color="#800080"-->
        <!--          active-text-color="#FF00FF"-->
        <!--          @select="handleSelect"-->
        <!-- 最左端的图标 -->
        <el-menu-item>
            <el-icon size="55px" style="color: hotpink">
                <Promotion/>
            </el-icon>
        </el-menu-item>
        <!-- 主页 -->
        <!--        <el-menu-item index="/home">主页</el-menu-item>-->
        <!-- 广场 -->
        <el-menu-item index="/square">广场</el-menu-item>
        <!-- 聊天 -->
        <el-menu-item index="/chat">聊天</el-menu-item>
        <!-- 灵魂匹配 -->
        <el-menu-item index="/match">灵魂匹配</el-menu-item>
        <!-- 发表帖子 -->
        <el-menu-item index="/publish">发布帖子/照片</el-menu-item>
        <!-- 后台 -->
        <el-menu-item @click="jumpToBack()">后台管理</el-menu-item>
        <!-- 关于 -->
        <el-menu-item index="/about">关于</el-menu-item>
        <div class="flex-grow"/>
        <!-- 用户中心 -->
        <!--        <el-menu-item @click="gotoPersonalPage">-->
        <!--            <el-popover-->
        <!--                    placement="top-start"-->
        <!--                    :width="160"-->
        <!--                    trigger="hover"-->
        <!--            >-->
        <!--                <p>退出登录？</p>-->
        <!--                <div style="text-align: right; margin: 0">-->
        <!--                    <el-button size="small" type="primary" @click="logout">确定</el-button>-->
        <!--                </div>-->
        <!--                <template #reference>-->
        <!--                    <el-avatar :src="userAvatar"/>-->
        <!--                </template>-->
        <!--            </el-popover>-->
        <!--            <h4 style="margin-left: 10px">用户中心</h4>-->
        <!--        </el-menu-item>-->
        <el-sub-menu @click="gotoPersonalPage">
            <template #title>
                <el-avatar :src="userAvatar"/>
                <h4 style="margin-left: 10px">用户中心</h4>
            </template>
            <el-menu-item @click="openPW()">修改密码</el-menu-item>
            <el-menu-item @click="showOut=true">退出登录</el-menu-item>
        </el-sub-menu>
        <!-- 消息 -->
        <el-badge :key="fresh" :value="getNewNum()" :max="99" class="menu_item">
            <el-button @click="gotoMessage">消息</el-button>
        </el-badge>
    </el-menu>

    <el-dialog
            v-model="showOut"
            title="警告"
            width="30%"
    >
        <span style="font-size: large">您确认要退出当前登录账户吗？</span>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="showOut = false">取消</el-button>
        <el-button type="danger" @click="logout()">
          确认
        </el-button>
      </span>
        </template>
    </el-dialog>

    <el-dialog v-model="showPassWord" title="修改密码" width="600px">
        <el-form :model="pform" style="margin-right: 50px">
            <el-form-item label="原有密码" label-width="140px">
                <el-input v-model="pform.old"/>
            </el-form-item>
            <el-form-item label="新密码" label-width="140px">
                <el-input type="password" v-model="pform.new1"/>
            </el-form-item>
            <el-form-item label="再次确认" label-width="140px">
                <el-input type="password" v-model="pform.new2"/>
            </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="showPassWord = false">取消</el-button>
        <el-button type="primary" @click="checkPW()">
          确认
        </el-button>
      </span>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>

import {useRouter} from "vue-router";
import request from "../utils/request.js";
import {onMounted, reactive, ref} from "vue";
import {UToast} from "undraw-ui";
import {Promotion} from "@element-plus/icons-vue";

const router = useRouter()
const storedUser = JSON.parse(localStorage.getItem('user'));
const userAvatar = ref();
const num = ref();
const fresh = ref(0);
const showOut = ref(false)
const showPassWord = ref(false)
const pform = reactive({
    old: "",
    new1: "",
    new2: ""
})
const changePW = reactive({
    uid: storedUser.id,
    old: "",
    pw: ""
})

onMounted(() => {
    fetchUserData();
})

function fetchUserData() {
    request.get('user/getInfo', {params: {id: storedUser.id}}).then(res => {
        if (res.status === 200) {
            userAvatar.value = res.data.avatarUrl
        } else {
            console.error('error：获取用户信息失败！');
        }
    })
}

function logout() {
    showOut.value = false;
    localStorage.removeItem('user');// 清除用户信息
    UToast({message: '退出登录成功!', type: 'success'})
    setTimeout(() => {
        router.push("/login")    //回到登录界面
    }, 200)
}

function openPW() {
    pform.old = ''
    pform.new1 = ''
    pform.new2 = ''
    showPassWord.value=true;
}

function checkPW() {
    if (pform.new1 !== pform.new2) {
        UToast({message: '两次输入不一致!', type: 'error'})
    } else {
        changePW.old = pform.old;
        changePW.pw = pform.new1;
        request.post('/user/changePassword', changePW).then(res => {
            if (res.status === 200) {
                UToast({message: '修改密码成功!', type: 'success'})
                showPassWord.value = false;
            } else if (res.status === 100) {
                UToast({message: '原密码错误!', type: 'error'})
            } else {
                console.log("请求失败");
            }
        })
    }
}

function gotoPersonalPage() {
    const userId = storedUser ? storedUser.id : null
    router.push({name: 'PersonalPage', params: {userId: userId}});
}

function gotoMessage() {
    console.log("jump to Message")
    router.push('Message')
}

function getNewNum() {
    request.get("/notice/getNewNum", {params: {uid: storedUser.id}}).then(res => {
        if (res.status === 200) {
            num.value = res.data.total;
            setTimeout(() => {
                fresh.value++;
            }, 1000);
        } else {
            console.error("获取未读数失败！")
        }
    })
    return num.value === 0 ? '' : num.value;
}

function jumpToBack() {
    if (storedUser.role === "管理员") {
        router.push("/backend");
    } else {
        UToast({message: '没有权限', type: 'error'})
    }
}
</script>

<style>
.flex-grow {
    flex-grow: 1;
}

.menu_item {
    margin-top: 15px;
    margin-left: 10px;
    margin-right: 30px;
}

.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
    text-align: center;
}

.dialog-footer button:first-child {
    margin-right: 10px;
}
</style>
