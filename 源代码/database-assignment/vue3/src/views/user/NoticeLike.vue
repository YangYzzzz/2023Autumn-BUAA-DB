<template>
    <div class="space-right">
        <div style="height: 10px"></div>
        <div class="right-title">
            <el-row>
                <div style="margin-top: 3px">
                    收到的赞
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
                    <div v-for="like in likes" :key="like.id" class="comment">
                        <el-card class="notice-card">
                            <div class="comment-header">
                                <img :src="like.avatarUrl" alt="头像" @click="jumpIMG(like.uid)">
                                <span class="comment-id">{{ like.nickname }}</span>
                                <span class="reply-info"> 给你点了赞</span>
                            </div>
                            <!-- 操作按钮 -->
                            <el-row style="margin-left: 40px; margin-top: 5px">
                                <el-link type="info" :href="like.originLink"> like: {{ tooltip(like.originContent) }}</el-link>
                                <div class="flex-grow"/>
                                <div>
                                    <el-button @click="deleteIt(like.id)" type="danger" :icon="Delete" round plain>删除</el-button>
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
    fetchLikeList();
});

const com_tmp = {
    id: 1,//点赞消息的id
    time: "点赞时间",
    uid: '3',
    nickname: "点赞用户昵称",
    avatarUrl: "https://static.juzicon.com/user/avatar-bf22291e-ea5c-4280-850d-88bc288fcf5d-220408002256-ZBQQ.jpeg",

    originContent: "我的评论或者帖子的文字内容，如果太长可以加省略号，点击跳转到相应位置",
    originLink: "/post&pid3#anchor41",
}
const likes = ref([]);
function fetchLikeList() {
    request.get("/notice/like/getLikeList", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            likes.value = res.data;
            likes.value = Array.from(res.data).sort((a, b) => {
                return new Date(b.time) - new Date(a.time);
            })
            console.log("点赞列表", likes.value)
        } else {
            console.error("获取点赞列列表失败！")
        }
    })
}

function tooltip(str) {
    let number = str.lastIndexOf('&nbsp;');
    if (number !== -1) {
        str = str.substring(number + 6)
    }
    if (str.length > 40) {
        return str.substring(0,40) + '...'
    }
    return str
}

//删除消息
function deleteIt(likeId) {
    console.log("删除", likeId)
    request.delete("/notice/like/deleteLike", {params: {uid: props.uid, lid: likeId}}).then(res => {
        if (res.status === 200) {
            UToast({message: '删除成功!' , type: 'success'})
            fetchLikeList();
            refresh.value++;
        } else {
            UToast({message: '删除失败!', type: 'error'})
        }
    })
}

function deleteAll() {
    request.delete("/notice/like/deleteAll", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            UToast({message: '清空成功!', type: 'success'})
            fetchLikeList();
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