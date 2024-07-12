<template>
    <Header/>
  <!-- 主体部分 -->
    <el-row class="background-container">
        <el-col :span="2"></el-col>
        <!--左栏-->
        <el-col :span="3">
            <el-menu
                    default-active="reply"
                    @select="handleMenuSelect"
                    class="el-menu-vertical-demo space-left"
            >
                <el-menu-item index="0" class="centered-menu-item">
                    <h2 style="margin-top: 20px">
                        消息中心
                    </h2>
                </el-menu-item>

                <el-menu-item index="reply" style="margin-top: 20px">
                    <el-icon>
                        <Connection/>
                    </el-icon>
                    <span class="bold-text">回复我的</span>
                    <div v-if="Nums.reply>0" style="margin-bottom: 36px;margin-left: 10px">
                        <el-badge :value="Nums.reply" type="primary"/>
                    </div>
                </el-menu-item>
                <el-menu-item index="like">
                    <el-icon>
                        <Star/>
                    </el-icon>
                    <span class="bold-text">收到的赞</span>
                    <div v-if="Nums.like>0" style="margin-bottom: 36px;margin-left: 10px">
                        <el-badge :value="Nums.like" type="primary"/>
                    </div>
                </el-menu-item>
                <el-menu-item index="system">
                    <el-icon>
                        <Bell/>
                    </el-icon>
                    <span class="bold-text">系统通知</span>
                    <div v-if="Nums.system>0" style="margin-bottom: 36px;margin-left: 10px">
                        <el-badge :value="Nums.system" type="primary"/>
                    </div>
                </el-menu-item>
                <el-menu-item index="others">
                    <el-icon>
                        <ChatDotRound/>
                    </el-icon>
                    <span class="bold-text">新的朋友</span>
                    <div v-if="Nums.friend>0" style="margin-bottom: 36px;margin-left: 10px">
                        <el-badge :value="Nums.friend" type="primary"/>
                    </div>
                </el-menu-item>
                <el-menu-item index="success">
                    <el-icon>
                        <CircleCheck/>
                    </el-icon>
                    <span class="bold-text">申请通过</span>
                    <div v-if="Nums.success > 0" style="margin-bottom: 36px;margin-left: 10px">
                        <el-badge :value="Nums.success" type="primary"/>
                    </div>
                </el-menu-item>
            </el-menu>
        </el-col>
        <!--右栏-->
        <el-col :span="16">
            <NoticeReply v-if="select==='reply'" :uid="storedUser.id"/>
            <NoticeLike v-if="select==='like'" :uid="storedUser.id"/>
            <NoticeSys v-if="select==='system'" :uid="storedUser.id"/>
            <NoticeOthers v-if="select==='others'" :uid="storedUser.id"/>
            <NoticeSuccess v-if="select === 'success'" :uid="storedUser.id"/>
        </el-col>
        <el-col :span="3"></el-col>
    </el-row>

</template>

<script setup>

import Header from "@/components/Header.vue";
import {Bell, ChatDotRound, CircleCheck, Connection, Star} from "@element-plus/icons-vue";
import {onBeforeMount, onMounted, ref} from "vue";
import NoticeReply from "@/views/user/NoticeReply.vue";
import NoticeLike from "@/views/user/NoticeLike.vue";
import NoticeSys from "@/views/user/NoticeSys.vue";
import NoticeOthers from "@/views/user/NoticeOthers.vue";
import request from "@/utils/request";
import NoticeSuccess from "@/views/user/NoticeSuccess.vue";

const storedUser = JSON.parse(localStorage.getItem('user'));
const select = ref('reply')

function handleMenuSelect(index) {
    console.log("当前选中的菜单项索引是：" + index);
    getNum();
    setTimeout(() => {
        select.value = index;
    }, 200);
}

const Nums = ref({
    total: 9999,
    reply: 2,
    like: 3,
    system: 0,
    friend: 66,
    success: 666
})

function getNum() {
    request.get("/notice/getNewNum", {params: {uid: storedUser.id}}).then(res => {
        if (res.status === 200) {
            Nums.value = res.data;
            console.log("未读数", res.data)
        } else {
            console.error("获取未读数失败！")
        }
    })
}

function addAccess() {
    request.get("access/addAccess",
        {params: {userId: storedUser.id, pageName: "Notice"}}).then(res => {
        if (res.status === 200) {
            console.log("访问量+1")
        }
    })
}

onBeforeMount(() => {
    console.log('Component will be mounted soon!');
    addAccess();
    getNum();
});
</script>

<style scoped>
.background-container {
    /* 设置背景图的URL，可以替换为你自己的背景图路径 */
    background-image: url('../../image/message-bg.jpg');

    /* 设置背景图的样式，比如平铺、居中等 */
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    /* 设置容器的高度，确保背景图充满整个容器 */
    min-height: calc(100vh - 60px);
}

.space-left {
    background-color: rgba(255, 255, 255, 0.8) !important; /* 使用rgba设置背景颜色，最后一个值是透明度 */
    height: 100%;
}

.centered-menu-item {
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
}

.bold-text {
    font-weight: bold;
    font-size: 16px;
    margin-left: 5px;
}

.space-right {
    background-color: rgba(255, 255, 255, 0.3) !important; /* 使用rgba设置背景颜色，最后一个值是透明度 */
    height: 100%;
}

.right-title {
    margin: 0 15px 0 15px;
    padding: 10px 20px 10px 20px;
    background-color: #fff; /* 纯白色背景 */
    border-radius: 10px; /* 圆角半径，根据需要调整 */
    border: 1px solid #ccc; /* 可选：添加边框 */
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 可选：添加阴影效果 */
    font-weight: bold; /* 加粗 */
    color: #757575; /* 浅灰色 */
}

.notice-card {
    border-radius: 10px !important; /* 圆角半径，根据需要调整 */
}

.comments {
    margin-top: 15px;
    margin-left: 15px;
    margin-right: 15px;
}

.comment {
    margin-bottom: 10px;
}

.comment-header {
    display: flex;
    align-items: center;
}

.comment-header img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-right: 10px;
}

.comment-id {
    margin-right: 10px;
    font-weight: bold;
}

.comment-time {
    margin-left: 8px;
    color: #888;
}

.comment-text {
    margin-top: 5px;
    margin-left: 40px;
}

.comment-actions {
    margin-top: 5px;
}

.comment-actions button {
    margin-right: 10px;
}
</style>