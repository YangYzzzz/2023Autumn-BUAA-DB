<template>
    <div class="space-right">
        <div style="height: 10px"></div>
        <div class="right-title">
            <el-row>
                <div style="margin-top: 3px">
                    回复我的
                </div>
                <div class="flex-grow"/>
                <div>
                    <el-button type="danger" :icon="Delete" round @click="deleteAll()">清空</el-button>
                </div>
            </el-row>
        </div>
        <div>
            <el-scrollbar max-height="720px">
                <div class="replies" :key="refresh">
                    <div v-for="reply in replies" :key="reply.id" class="reply">
                        <el-card class="notice-card">
                            <div class="reply-header">
                                <img :src="reply.avatarUrl" alt="头像" @click="jumpIMG(reply.uid)">
                                <span class="reply-id">{{ reply.nickname }}</span>
                                <span class="reply-info">回复了你</span>
                            </div>
                            <el-link class="reply-text" type="info" :href="reply.originLink">
                                reply: {{ tooltip(reply.originContent) }}
                            </el-link>
                            <p class="reply-text">
                                <span v-if="reply.showId !== null">回复
                                    <span style="color: var(--u-color-success-dark-2);">{{getReplyName(reply.replyContent) }}&nbsp;</span>
                                </span>
                                <span class="reply">{{ getReplyRest(reply.replyContent) }}</span>
                            </p>
                            <!-- 操作按钮 -->
                            <el-row style="margin-left: 40px; margin-top: 5px">
                                <div class="reply-actions">
                                    <el-button round @click="likeIt(reply.replyCommentId)">
                                        <div v-html="isLike(reply.replyCommentId)"></div>
                                    </el-button>
                                    <el-button round
                                               @click="dialogFormVisible = true; openInput(reply.replyCommentId, reply.showId, reply.nickname)">
                                        <div v-html="svgComment"></div>
                                    </el-button>
                                </div>

                                <span class="reply-time" style="margin-top: 8px">{{
                                    new Date(reply.time).toString().substring(0, new Date(reply.time).toString().indexOf("GMT"))
                                    }}</span>
                                <div class="flex-grow"/>
                                <div>
                                    <el-button @click="deleteIt(reply.id)" type="danger" :icon="Delete" round plain>
                                        删除
                                    </el-button>
                                </div>
                            </el-row>
                        </el-card>
                    </div>
                </div>
            </el-scrollbar>
        </div>
        <el-dialog v-model="dialogFormVisible" title="回复">
            <el-form :model="form">
                <el-form-item label="回复内容" :label-width="formLabelWidth">
                    <el-input
                            v-model="form.content"
                            :autosize="{ minRows: 2, maxRows: 4 }"
                            type="textarea"
                            :placeholder="replyHolder()"
                            style="margin-right: 10%"
                    />
                </el-form-item>
            </el-form>
            <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取消</el-button>
                <el-button type="primary" @click="sendReply()">提交</el-button>
            </span>
            </template>
        </el-dialog>
    </div>

</template>

<script setup>

import {Delete} from "@element-plus/icons-vue";
import {defineProps, onMounted, reactive, ref} from "vue";
import request from "@/utils/request";
import {UToast} from "undraw-ui";
import {useRouter} from "vue-router";

const svgComment = '<svg width="14" height="14" viewBox="0 0 19 18" fill="none"><path clip-rule="evenodd" d="M.733 2.8a2 2 0 012-2h13.2a2 2 0 012 2v6.015a6 6 0 01-6 6H5.534a4 4 0 00-2.189.652L1.507 16.67a.5.5 0 01-.774-.418V2.8z" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/><path d="M4.5 5.62h5" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>'
const svgLike = '<svg width="16" height="16" viewBox="0 0 20 20" fill="none"><path d="M7.685 6.34a.1.1 0 00.091-.06l1.918-4.426S10.111.79 11.054.79c.944 0 1.407.394 1.407 1.566l.037 3.966a.1.1 0 00.1.1h5.392s.817 0 .47 1.145c-.348 1.146-1.541 7.085-1.541 7.085s-1.014 3.501-5.033 3.895l-9.255-.067a1 1 0 01-.993-1V7.374a1 1 0 01.993-1l5.054-.034zM5.416 6.62v11.755" stroke="#333" stroke-width="2"/></svg>'
const svgFill = '<svg width="16" height="16" viewBox="0 0 20 21" fill="none"><path fill-rule="evenodd" clip-rule="evenodd" d="M8.95 2.563l-1.726 3.98-1.158.008v13.748h-.648l6.462.047c3.079-.297 5.037-1.813 5.807-4.473.805-3.99 1.317-6.347 1.538-7.075.366-1.207-.032-2.178-1.235-2.178h-4.699l-.03-3.273c0-1.552-.77-2.358-2.207-2.358-.98 0-1.714.574-2.105 1.574zM4.765 20.294V6.56l-2.14.015a1.8 1.8 0 00-1.788 1.8v10.105a1.8 1.8 0 001.787 1.8l2.14.015z" fill="#FF6880"/></svg>'

const props = defineProps(['uid']);
const refresh = ref(0);
//回复列表
const likeIds = ref([])
const replies = ref([]);

//获得回复列表；提交刷新，将未读变为已读
function fetchReplyList() {
    request.get("/notice/reply/getReplyList", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            // replies.value = res.data;
            replies.value = Array.from(res.data).sort((a, b) => {
                return new Date(b.time) - new Date(a.time);
            })
        } else {
            console.error("获取reply列表失败！")
        }
    })
}

function tooltip(str) {
    let number = str.lastIndexOf('&nbsp;');
    if (number !== -1) {
        str = str.substring(number + 6)
    }
    if (str.length > 40) {
        return str.substring(0, 40) + '...'
    }
    return str
}
function getReplyName(str) {
    let number = str.indexOf('&nbsp;');
    return str.substring(0, number)
}

function getReplyRest(str) {
    let number = str.lastIndexOf('&nbsp;');
    if (number === -1) return str;
    return str.substring(number + 6)
}


//跳转
const router = useRouter()

function jumpIMG(uid) {
    router.push({name: 'PersonalPage', params: {userId: uid}});
}

//点赞
function fetchLikeList() {
    request.get("/notice/reply/getReplyLikes", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            likeIds.value = res.data
            console.log('获取用户点赞信息', likeIds.value)
        } else {
            console.error('error：获取用户点赞信息失败！');
        }
    })
}

function isLike(replyId) {
    console.log(replyId,"在likeIDs吗？",likeIds.value.indexOf(replyId))
    if (likeIds.value.indexOf(replyId) === -1) {
        return svgLike;
    } else return svgFill;
}

function likeIt(replyId) {
    request.get("/comment/changeLiked", {params: {uid: props.uid, cid: replyId}}).then(res => {
        if (res.status === 200) {
            console.log("点赞成功", replyId, props.uid)
            fetchReplyList();
            fetchLikeList();
            console.log(likeIds.value)
        } else {
            console.error("改变点赞状态失败：" + replyId);
        }
    })
}

//删除消息
function deleteIt(replyId) {
    console.log("删除", replyId)
    request.delete("/notice/reply/deleteReply", {params: {rid: replyId}}).then(res => {
        if (res.status === 200) {
            UToast({message: '删除成功!', type: 'success'})
            fetchReplyList();
            refresh.value++;
        } else {
            UToast({message: '删除失败!', type: 'error'})
        }
    })
}

function deleteAll() {
    request.delete("/notice/reply/deleteAll", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            UToast({message: '清空成功!', type: 'success'})
            fetchReplyList();
            refresh.value++;
        } else {
            UToast({message: '清空失败!', type: 'error'})
        }
    })
}

//回复的回复
const dialogFormVisible = ref(false)
const formLabelWidth = '100px'
const toName = ref("回复的用户名")
const form = reactive({
    content: "",
    userId: props.uid,
    replyCommentId: "",
    showId: "",
})

function replyHolder() {
    return "回复 @" + toName.value + " :";
}

function openInput(replyId, showId, itName) {
    toName.value = itName
    form.replyCommentId = replyId;
    form.showId = showId;
    form.content = '';
}

function sendReply() {
    if (!form.content) {
        UToast({message: '内容不能为空!', type: 'error'})
    } else {
        console.log('提交回复: ' + form.content + " " + form.replyCommentId)
        if (form.showId == null) {  //回复的是一级评论
            form.showId = form.replyCommentId
        }
        form.content = "@" + toName.value + ":&nbsp;" + form.content;
        request.post("/comment/newReply", form).then(res => {
            if (res.status === 200) {
                UToast({message: '回复成功!', type: 'success'})
            } else {
                UToast({message: '回复失败!', type: 'error'})
            }
        })
    }
    dialogFormVisible.value = false;
}

onMounted(() => {
    fetchReplyList();
    fetchLikeList();
});
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

.replies {
    margin-top: 15px;
    margin-left: 15px;
    margin-right: 15px;
}

.reply {
    margin-bottom: 10px;
}

.reply-header {
    display: flex;
    align-items: center;
}

.reply-header img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-right: 10px;
}

.reply-id {
    margin-right: 10px;
    font-weight: bold;
}

.reply-info {
    margin-right: 10px;
    font-weight: bold;
    color: #565656;
}

.reply-time {
    margin-left: 8px;
    color: #888;
}

.reply-text {
    margin-top: 5px;
    margin-left: 40px;
}

.reply-actions {
    margin-top: 5px;
}

.reply-actions button {
    margin-right: 10px;
}

.el-button--text {
    margin-right: 15px;
}

.el-select {
    width: 300px;
}

.el-input {
    width: 300px;
}

.dialog-footer button:first-child {
    margin-right: 10px;
}
</style>