<template>
    <u-comment :config="config" @submit="submit" @like="like" relative-time>
        <u-comment-nav v-model="latest" @sorted="sorted"></u-comment-nav>
    </u-comment>
</template>

<script setup lang="ts">
import request from "../../utils/request.js";
import emoji from '../../assets/emoji'
import {defineProps, onMounted, reactive, ref} from 'vue'
import {CommentApi, ConfigApi, dayjs, SubmitParamApi, UComment, UCommentNav, UToast} from 'undraw-ui'

// 使用 defineProps 声明接收的属性
const props = defineProps(['uid', 'pid']);

function fetchUserInfo(userId) {
    request.get('user/getInfo', {params: {id: userId}}).then(res => {
        if (res.status === 200) {
            config.user.username = res.data.username
            config.user.avatar = res.data.avatarUrl
        } else {
            console.error('error：获取用户信息失败！');
        }
    })
}

const config = reactive<ConfigApi>({
    user: {
        id: props.uid,
        username: 'jack',
        avatar: 'https://static.juzicon.com/avatars/avatar-200602130320-HMR2.jpeg?x-oss-process=image/resize,w_100',
        likeIds: [2, 4]  //当前用户点赞过的评论
    },
    emoji: emoji,
    comments: [],
    total: 0
})

//评论排序
const latest = ref(false)

function sorted() {
    console.log(latest.value ? "NEW" : "HOT")
    const newSort = Array.from(config.comments).sort((a, b) => {
        if (latest.value) { // 按时间排序
            return new Date(b.createTime) - new Date(a.createTime);
        } else { // 按点赞数排序
            return b.likes - a.likes;
        }
    });
    console.log(newSort);
    console.table(newSort.map(comment => ({likes: comment.likes})));
    config.comments = newSort;
}

// 提交评论事件
const post1 = "/comment/newFirstComment";
const post2 = "/comment/newReply"
const new_comment = reactive({
    content: "",
    userId: props.uid,
    replyPostId: "",
    replyCommentId: ""
})
const submit = ({content, parentId, finish}: SubmitParamApi) => {
    let new_cid = 100
    let isReply = parentId !== null
    // if(!isReply) {  //不用添加@xxx
    //     const indexOfStart = content.indexOf(":</span>");
    //     content = content.substring(indexOfStart+8);
    //     console.log("修改后："+content);
    // }
    new_comment.content = content;

    if (isReply) {
        new_comment.replyCommentId = parentId
        console.log('提交回复: ' + content + " " + parentId)
    } else {
        new_comment.replyPostId = props.pid
        console.log('提交评论: ' + content + " " + props.pid)
    }

    request.post((isReply ? post2 : post1), new_comment).then(res => {
        if (res.status === 200) {
            //todo new_cid = res.data.cid
            UToast({message: (isReply ? '回复' : '评论') + '成功!', type: 'success'})
            const comment: CommentApi = {
                id: String(new_cid),
                parentId: parentId,
                uid: props.uid,
                address: '',
                content: content,
                likes: 0,
                createTime: dayjs().subtract(5, 'seconds').toString(),
                user: {
                    username: config.user.username,
                    avatar: config.user.avatar,
                    level: 6,
                    homeLink: `/personal&uid${props.uid}`
                },
                reply: null
            }
            setTimeout(() => {
                finish?.(comment)
            }, 200)
        } else {
            UToast({message: (isReply ? '回复' : '评论') + '失败!', type: 'error'})
        }
    })
}

// 点赞按钮事件 将评论id返回后端判断是否点赞，然后处理点赞状态
function fetchUserLikes(uid, pid) {
    request.get("/comment/likeList", {params: {uid: uid, pid: pid}}).then(res => {
        if (res.status === 200) {
            config.user.likeIds = res.data
        } else {
            console.error('error：获取用户点赞信息失败！');
        }
    })
}

const like = (id: string, finish: () => void) => {
    request.get("/comment/changeLiked", {params: {uid: props.uid, cid: id}}).then(res => {
        if (res.status === 200) {
            finish()
        } else {
            console.error("改变点赞状态失败：" + id);
        }
    })
}

function fetchComments(pid) {
    request.get("/comment/getByPid", {params: {pid: pid}}).then(res => {
        if (res.status === 200) {
            config.comments = Array.from(res.data).sort((a, b) => {
                return b.likes - a.likes;
            }) as CommentApi[];
        } else {
            console.error("error：获取评论区失败！")
        }
    })
}

onMounted(() => {
    fetchUserInfo(props.uid);
    fetchComments(props.pid);
    fetchUserLikes(props.uid, props.pid);
})

</script>

<style>
</style>