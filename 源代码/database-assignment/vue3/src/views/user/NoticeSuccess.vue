<template>
    <div class="space-right">
        <div style="height: 10px"></div>
        <div class="right-title">
            <el-row>
                <div style="margin-top: 3px">
                    申请通过
                </div>
                <div class="flex-grow"/>
                <div>
                    <el-button @click="deleteAll()" type="danger" :icon="Delete" round>清空</el-button>
                </div>
            </el-row>
        </div>
        <div>
            <el-scrollbar max-height="720px">
                <div class="likes" :key="refresh">
                    <div v-for="notice in nList" :key="notice.id" class="comment">
                        <el-card class="notice-card">
                            <!-- 操作按钮 -->
                            <el-row>
                                <div class="comment-header">
                                    <img :src="notice.avatarUrl" alt="头像" @click="jumpIMG(notice.uid)">
                                    <span class="comment-id">{{ notice.nickname }}</span>
                                    <span class="reply-info"> 通过了你的好友申请 </span>
                                </div>
                                <el-link type="info" style="margin-left: 20px">
                                    {{new Date(notice.time).toString().substring(0, new Date(notice.time).toString().indexOf("GMT")) }}
                                </el-link>
                                <div class="flex-grow"/>
                                <div>
                                    <el-button @click="deleteIt(notice.id)" type="danger" :icon="Delete" round plain>删除</el-button>
                                </div>
                            </el-row>
                        </el-card>
                    </div>
                </div>
            </el-scrollbar>
        </div>
    </div>
</template>

<script setup>

import {Delete} from "@element-plus/icons-vue";
import {defineProps, onMounted, ref} from "vue";
import request from "@/utils/request";
import {UToast} from "undraw-ui";
import {useRouter} from "vue-router";

const props = defineProps(['uid']);
const refresh = ref(0);

onMounted(() => {
    fetchNList();
});

const nList = ref([]);
function fetchNList() {
    request.get("/notice/friendSuccess/getFriendSuccessList", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            nList.value = res.data;
            nList.value = Array.from(res.data).sort((a, b) => {
                return new Date(b.time) - new Date(a.time);
            })
            console.log("消息列表", nList.value)
        } else {
            console.error("获取消息列表失败！")
        }
    })
}

//删除消息
function deleteIt(noticeID) {
    console.log("删除", noticeID)
    request.delete("/notice/friendSuccess/delete", {params: {nid: noticeID}}).then(res => {
        if (res.status === 200) {
            UToast({message: '删除成功!' , type: 'success'})
            fetchNList();
            refresh.value++;
        } else {
            UToast({message: '删除失败!', type: 'error'})
        }
    })
}

function deleteAll() {
    request.delete("/notice/friendSuccess/deleteAll", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            UToast({message: '清空成功!', type: 'success'})
            fetchNList();
            refresh.value++;
        } else {
            UToast({message: '清空失败!', type: 'error'})
        }
    })
}

//跳转
const router = useRouter()
function jumpIMG(uid) {
    router.push({name: 'PersonalPage', params: {userId: uid}});
}
</script>

<style scoped>
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

.likes {
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

.reply-info {
    margin-right: 10px;
    font-weight: bold;
    color: #565656;
}

.comment-actions button {
    margin-right: 10px;
}
</style>