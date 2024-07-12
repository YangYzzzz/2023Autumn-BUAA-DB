<template>
    <Header/>
    <el-row class="background-container">
        <el-col :span="4"></el-col>
        <el-col :span="16">
            <el-card style="margin: 15px 10px 10px 10px;">
                <!--帖子的发布人、发布时间-->
                <div class="post-header">
                    <img :src=user_avatar alt="User Avatar" class="user-avatar" @click="toPersonal"/>

                    <div class="user-info">
                        <p class="username">{{ user_name }}</p>
                        <p class="post-time">{{ post.time }}</p>
                    </div>
                    <div class="flex-grow"/>
                    <div style="margin-right: 20px">
                        <el-button type="primary" :icon="Histogram" round plain>浏览量：{{ post.clickNum }}</el-button>
                    </div>
                </div>
                <!--帖子的内容：文字+图片-->
                <div class="post-content">
                    {{ post.content }}
                    <div class="post-images">
                        <div class="image-grid">
                            <div v-for="(image, index) in post.imageUrls" :key="index" class="image-cell">
                                <img :src="image" alt="Image" @click="showIt(image)"/>
                            </div>
                        </div>
                    </div>
                </div>
                <!--帖子的操作：点赞、评论-->
                <el-row style="margin-top: 25px;">
                    <el-col :span="12">
                        <el-button v-if=getLike() class="center-button" @click="likePost(post.id)">
                            <div v-html="svgFill"></div>
                            <div style="margin-left: 3px">点赞 {{ post.likeNum }}</div>
                        </el-button>
                        <el-button v-else plain class="center-button" @click="likePost(post.id)">
                            <div v-html="svgLike"></div>
                            <div style="margin-left: 3px">点赞 {{ post.likeNum }}</div>
                        </el-button>
                    </el-col>
                    <el-col :span="12">
                        <el-button class="center-button">
                            <div v-html="svgComment"></div>
                            <div style="margin-left: 3px">评论 {{ post.commentNum }}</div>
                        </el-button>
                    </el-col>
                </el-row>

                <!-- 评论区 -->
                <newComment :uid="storedUser.id" :pid="postId"/>

            </el-card>
            <el-dialog v-model="showVisible">
                <el-image fit="fill" :src="showImageUrl" alt="Preview Image"/>
            </el-dialog>
        </el-col>
        <el-col :span="4"></el-col>
    </el-row>
</template>

<script setup>
import {useRouter} from "vue-router";
import Header from "@/components/Header.vue";
import request from "@/utils/request";
import {onMounted, ref} from "vue";
import NewComment from "@/views/post/newComment.vue";
import {Histogram} from "@element-plus/icons-vue";

const router = useRouter()
const storedUser = JSON.parse(localStorage.getItem('user'));
const postId = router.currentRoute.value.params.postId;

//帖子信息
const svgComment = '<svg width="14" height="14" viewBox="0 0 19 18" fill="none"><path clip-rule="evenodd" d="M.733 2.8a2 2 0 012-2h13.2a2 2 0 012 2v6.015a6 6 0 01-6 6H5.534a4 4 0 00-2.189.652L1.507 16.67a.5.5 0 01-.774-.418V2.8z" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/><path d="M4.5 5.62h5" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>'
const svgLike = '<svg width="14" height="14" viewBox="0 0 20 20" fill="none"><path d="M7.685 6.34a.1.1 0 00.091-.06l1.918-4.426S10.111.79 11.054.79c.944 0 1.407.394 1.407 1.566l.037 3.966a.1.1 0 00.1.1h5.392s.817 0 .47 1.145c-.348 1.146-1.541 7.085-1.541 7.085s-1.014 3.501-5.033 3.895l-9.255-.067a1 1 0 01-.993-1V7.374a1 1 0 01.993-1l5.054-.034zM5.416 6.62v11.755" stroke="#333" stroke-width="2"/></svg>'
const svgFill = '<svg width="14" height="14" viewBox="0 0 20 21" fill="none"><path fill-rule="evenodd" clip-rule="evenodd" d="M8.95 2.563l-1.726 3.98-1.158.008v13.748h-.648l6.462.047c3.079-.297 5.037-1.813 5.807-4.473.805-3.99 1.317-6.347 1.538-7.075.366-1.207-.032-2.178-1.235-2.178h-4.699l-.03-3.273c0-1.552-.77-2.358-2.207-2.358-.98 0-1.714.574-2.105 1.574zM4.765 20.294V6.56l-2.14.015a1.8 1.8 0 00-1.788 1.8v10.105a1.8 1.8 0 001.787 1.8l2.14.015z" fill="#FF6880"/></svg>'

const post = ref({});
const user_Id = ref()
const user_name = ref();
const user_avatar = ref();

function fetchUserData(userId) {
    request.get('user/getInfo', {params: {id: userId}}).then(res => {
        if (res.status === 200) {
            user_name.value = res.data.nickname;
            user_avatar.value = res.data.avatarUrl;
        } else {
            console.error('error：获取用户信息失败！');
        }
    })
}

function toPersonal() {
    router.push({name: 'PersonalPage', params: {userId: user_Id.value}});
}

function fetchPost() {
    request.get("/post/getByPid", {params: {pid: postId}}).then(res => {
        if (res.status === 200) {
            post.value = res.data;
            user_Id.value = res.data.userId;
            console.log(postId, "图片内容", post.value)
            fetchUserData(res.data.userId);
        } else {
            console.error('获取帖子信息失败');
        }
    })
}

//点赞
const isLike = ref(false);

function getLike() {
    request.get("/post/getLiked", {params: {uid: storedUser.id, pid: postId}}).then(res => {
        if (res.status === 200) {
            isLike.value = res.data
        } else {
            console.error('error：获取点赞状态失败！');
            return false;
        }
    })
    return isLike.value
}

function likePost() {
    request.get("/post/changeLiked", {params: {uid: storedUser.id, pid: postId}}).then(res => {
        if (res.status === 200) {
            isLike.value = res.data === "点赞成功";
            fetchPost();
        } else {
            console.error('error：更改点赞状态失败！');
        }
    })
}

//照片
const showVisible = ref(false)
const showImageUrl = ref()

function showIt(imgUrl) {
    showVisible.value = true;
    showImageUrl.value = imgUrl
}

function addAccess() {
    request.get("access/addAccess",
        {params: {userId: storedUser.id, pageName: "PostDetail"}}).then(res => {
        if (res.status === 200) {
            console.log("访问量+1")
        }
    })
}

onMounted(() => {
    addAccess();
    fetchPost();
});
</script>

<style scoped>
.background-container {
    /* 设置背景图的URL，可以替换为你自己的背景图路径 */
    background-image: url('../../image/bg-pp.jpg');

    /* 设置背景图的样式，比如平铺、居中等 */
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    /* 设置容器的高度，确保背景图充满整个容器 */
    min-height: calc(100vh - 60px);
}
.post-header {
    display: flex;
    align-items: center;
}

.user-avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 10px;
}

.user-info {
    display: flex;
    flex-direction: column;
}

.username {
    font-weight: bold;
}

.post-time {
    color: #888;
}

.post-content {
    margin-top: 20px;
    margin-left: 60px;
    margin-right: 60px;
}

.button-active {
    background-color: rgb(64, 158, 255); /* 设置按钮的激活状态样式 */
    color: #fff;
}

.center-button {
    display: block;
    margin: 0 auto;
}

.post-images {
    margin-top: 10px;
    margin-right: 40px;
}

.image-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr); /* 九宫格，4列 */
    gap: 5px; /* 图片之间的间距 */
}

.image-cell {
    position: relative;
    overflow: hidden;
    padding-top: 100%; /* 图片容器的宽度和高度比例 */
    border-radius: 10px; /* 添加圆角 */
}

.image-cell img {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover; /* 图像填充整个容器 */
}

.comment-bar {
    margin-top: 25px;
    display: flex;
    margin-bottom: 10px;
}

.comment-bar input {
    flex: 1;
    margin-right: 10px;
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

.replies {
    margin-top: 10px;
    margin-left: 40px;
}

.comment-actions {
    margin-top: 5px;
}

.comment-actions button {
    margin-right: 10px;
}
</style>