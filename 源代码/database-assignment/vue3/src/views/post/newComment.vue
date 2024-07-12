<template>
    <el-divider style="margin-bottom: 15px!important; margin-top: 15px!important;"/>
    <div v-click-outside="hideReplyBtn" @click="inputFocus" class="my-reply" :key="refresh1">
        <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>
        <div class="reply-info">
            <div
                    tabindex="0"
                    contenteditable="true"
                    id="CommentInput"
                    spellcheck="false"
                    placeholder="输入评论..."
                    class="reply-input"
                    @focus="showReplyBtn"
                    @input="onNewComment($event)"
            >
            </div>
        </div>
        <div class="reply-btn-box" v-show="btnShow">
            <el-button class="reply-btn" @click="sendComment" type="primary">发表评论</el-button>
        </div>
    </div>

    <el-divider style="margin-bottom: 15px!important; margin-top: 15px!important;"/>
    <u-comment-nav v-model="latest" @sorted="sorted" style="padding-right: 15px!important;"></u-comment-nav>

    <div :key="refresh2">
        <div v-for="(comment,i) in comments" :key="i" :id="'anchor'+comment.id" class="author-title reply-father">
            <el-avatar class="header-img" :size="40" :src="comment.user.avatar" @click="jumpIMG(comment.uid)"></el-avatar>
            <div class="author-info">
                <span class="author-name">{{ comment.user.username }}</span>
                <span class="author-time">{{ timeStr(comment.createTime) }}</span>
            </div>
            <!--评论信息-->
            <div class="icon-btn">
                        <span @click="showReplyInput(comment.user.username, comment.id)">
                            <i class="iconfont" v-html="svgComment"/>{{ comment.reply.total }}
                        </span>
                <span @click="changeLike(comment.id)">
                <i class="iconfontR" v-html="isLike(comment.id)"/>{{ comment.likes }}
            </span>

            </div>
            <!--评论内容-->
            <div class="talk-box">
                <p>
                    <span class="reply">{{ comment.content }}</span>
                </p>
            </div>

            <!------ 添加回复 ------>
            <div v-show="_inputShow(comment.id)" class="my-reply my-comment-reply">
                <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>
                <div class="reply-info">
                    <div tabindex="0" contenteditable="true" spellcheck="false" placeholder="输入回复..."
                         @input="onDivInput($event)" class="reply-input reply-comment-input"></div>
                </div>
                <div class="reply-btn-box">
                    <el-button class="reply-btn"
                               @click="sendCommentReply(comment.id, comment.id, comment.user.username)"
                               type="primary">
                        发表评论
                    </el-button>
                </div>
            </div>

            <!------ 回复区 ------>
            <div class="reply-box">
                <div v-for="(reply,j) in comment.reply.list" :key="j" :id="'anchor'+reply.id" class="author-title">
                    <el-avatar class="header-img" :size="40" :src="reply.user.avatar"  @click="jumpIMG(reply.uid)" ></el-avatar>
                    <div class="author-info">
                        <span class="author-name">{{ reply.user.username }}</span>
                        <span class="author-time">{{ timeStr(reply.createTime) }}</span>
                    </div>
                    <div class="icon-btn">
                    <span @click="showReplyInput(reply.user.username, reply.id)">
                        <i class="iconfont" v-html="svgComment"></i>
                    </span>
                        <span @click="changeLike(reply.id)">
                        <i class="iconfontR" v-html="isLike(reply.id)"></i>{{ reply.likes }}
                    </span>
                    </div>
                    <div class="talk-box">
                        <p>
                        <span v-if="reply.parentId !== comment.id">回复
                            <span style="color: var(--u-color-success-dark-2);">{{getReplyName(reply.content) }}&nbsp;</span>
                        </span>
                            <span class="reply">{{ getReplyRest(reply.content) }}</span>
                        </p>
                    </div>
                    <!------ 添加回复 ------>
                    <div v-show="_inputShow(reply.id)" class="my-reply my-comment-reply">
                        <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>
                        <div class="reply-info">
                            <div tabindex="0" contenteditable="true" spellcheck="false"
                                 placeholder="输入回复..."
                                 @input="onDivInput($event)" class="reply-input reply-comment-input"></div>
                        </div>
                        <div class="reply-btn-box">
                            <el-button class="reply-btn"
                                       @click="sendCommentReply(reply.id, comment.id, reply.user.username)"
                                       type="primary">
                                发表评论
                            </el-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script setup>
import {nextTick, onMounted, reactive, ref} from 'vue';
import {UCommentNav, UToast} from "undraw-ui";
import request from "@/utils/request";
import {useRouter} from "vue-router";

const svgComment = '<svg width="20" height="20" viewBox="0 0 19 18" fill="none"><path clip-rule="evenodd" d="M.733 2.8a2 2 0 012-2h13.2a2 2 0 012 2v6.015a6 6 0 01-6 6H5.534a4 4 0 00-2.189.652L1.507 16.67a.5.5 0 01-.774-.418V2.8z" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/><path d="M4.5 5.62h5" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>'
const svgLike = '<svg width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M7.685 6.34a.1.1 0 00.091-.06l1.918-4.426S10.111.79 11.054.79c.944 0 1.407.394 1.407 1.566l.037 3.966a.1.1 0 00.1.1h5.392s.817 0 .47 1.145c-.348 1.146-1.541 7.085-1.541 7.085s-1.014 3.501-5.033 3.895l-9.255-.067a1 1 0 01-.993-1V7.374a1 1 0 01.993-1l5.054-.034zM5.416 6.62v11.755" stroke="#333" stroke-width="2"/></svg>'
const svgFill = '<svg width="20" height="20" viewBox="0 0 20 21" fill="none"><path fill-rule="evenodd" clip-rule="evenodd" d="M8.95 2.563l-1.726 3.98-1.158.008v13.748h-.648l6.462.047c3.079-.297 5.037-1.813 5.807-4.473.805-3.99 1.317-6.347 1.538-7.075.366-1.207-.032-2.178-1.235-2.178h-4.699l-.03-3.273c0-1.552-.77-2.358-2.207-2.358-.98 0-1.714.574-2.105 1.574zM4.765 20.294V6.56l-2.14.015a1.8 1.8 0 00-1.788 1.8v10.105a1.8 1.8 0 001.787 1.8l2.14.015z" fill="#FF6880"/></svg>'
const vClickOutside = {
    mounted(el, binding) {
        function eventHandler(e) {
            if (el.contains(e.target)) {
                return false
            }
            // 如果绑定的参数是函数，正常情况也应该是函数，执行
            if (binding.value && typeof binding.value === 'function') {
                binding.value(e)
            }
        }

        // 用于销毁前注销事件监听
        el.__click_outside__ = eventHandler
        // 添加事件监听
        document.addEventListener('click', eventHandler)
    },
    beforeUnmount(el) {
        // 移除事件监听
        document.removeEventListener('click', el.__click_outside__)
        // 删除无用属性
        delete el.__click_outside__
    }
}

// 使用 defineProps 声明接收的属性
const props = defineProps(['uid', 'pid']);
onMounted(() => {
    fetchUserInfo(props.uid);
    fetchComments();
    fetchUserLikes();
    scrollToAnchor();
})

const jumpTo = window.location.hash;

function scrollToAnchor() {
    console.log("jumpto",jumpTo.substring(1));
    setTimeout(() => {
        window.location.hash = jumpTo;
        //可以将滚动位置定制为合适的位置
        const targetElement = document.getElementById(jumpTo.substring(1));
        if (targetElement) {
            // 计算目标元素的垂直偏移量
            const offsetTop = targetElement.offsetTop;
            // 将页面滚动到目标位置
            window.scrollTo({
                top: offsetTop,
                behavior: 'smooth'
            });
        }
    }, 200);
}

const refresh1 = ref(0);
const refresh2 = ref(0);
//******** 用户信息 ********
const myName = ref();
const myHeader = ref();

function fetchUserInfo(userId) {
    request.get('user/getInfo', {params: {id: userId}}).then(res => {
        if (res.status === 200) {
            myName.value = res.data.username
            myHeader.value = res.data.avatarUrl
        } else {
            console.error('error：获取用户信息失败！');
        }
    })
}

//跳转
const router = useRouter()
function jumpIMG(uid) {
    router.push({name: 'PersonalPage', params: {userId: uid}});
}

//******** 提交评论 ********
const btnShow = ref(false);
const new_comment = reactive({
    content: "",
    userId: props.uid,
    replyPostId: props.pid
})
const onNewComment = (e) => {
    new_comment.content = e.target.innerHTML;
};
const sendComment = () => {
    if (!new_comment.content) {
        UToast({message: '内容不能为空!', type: 'error'})
    } else {
        console.log('提交评论: ' + new_comment.content + " " + props.pid)
        request.post("/comment/newFirstComment", new_comment).then(res => {
            if (res.status === 200) {
                UToast({message: '评论成功!', type: 'success'})
                refresh1.value++;
                fetchComments();
                fetchUserLikes();
                refresh2.value++;
            } else {
                UToast({message: '评论失败!', type: 'error'})
            }
        })
    }
};

//--------- 评论输入框 ---------
const inputFocus = () => {
    const CommentInput = document.getElementById('CommentInput');
    CommentInput.style.padding = '8px 8px';
    CommentInput.style.border = '2px solid #59b1ff';
    CommentInput.focus();
};

const showReplyBtn = () => {
    btnShow.value = true;
};

const hideReplyBtn = () => {
    btnShow.value = false;
    const CommentInput = document.getElementById('CommentInput');
    CommentInput.style.padding = '10px';
    CommentInput.style.border = 'none';
};


//******** 提交reply ********
const new_reply = reactive({
    content: "",
    userId: props.uid,
    replyCommentId: "",
    showId: ""
})
const onDivInput = (e) => {
    let str = e.target.innerHTML;
    str = str.replace(/<\/div>/g, "").replace(/<div>/g, " ");
    new_reply.content = str;
    console.log("new_reply",str);
};
const sendCommentReply = (replyId, showId, replyName) => {
    if (!new_reply.content) {
        UToast({message: '内容不能为空!', type: 'error'})
    } else {
        console.log('提交回复: ' + new_reply.content + " " + replyId)
        new_reply.replyCommentId = replyId;
        new_reply.showId = showId;
        new_reply.content = "@" + replyName + ":&nbsp;" + new_reply.content;
        request.post("/comment/newReply", new_reply).then(res => {
            if (res.status === 200) {
                UToast({message: '回复成功!', type: 'success'})
                fetchComments();
                fetchUserLikes();
                refresh2.value++;
            } else {
                UToast({message: '回复失败!', type: 'error'})
            }
        })
    }
};
//是否显示评论框
const inputShow = ref()
const showReplyInput = (name, id) => {
    console.log("点击评论")
    if (inputShow.value === id) {    //已显示
        inputShow.value = -1;
    } else {
        inputShow.value = id;
    }
};
const _inputShow = (commentId) => inputShow.value === commentId;


//********* 评论区 *********
const comments = ref([]);

function fetchComments() {
    request.get("/comment/getByPid", {params: {pid: props.pid}}).then(res => {
        if (res.status === 200) {
            comments.value = Array.from(res.data).sort((a, b) => {
                return b.likes - a.likes;
            })
        } else {
            console.error("error：获取评论区失败！")
        }
    })
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

//评论排序
const latest = ref(false)

function sorted() {
    console.log(latest.value ? "NEW" : "HOT")
    const newSort = Array.from(comments.value).sort((a, b) => {
        if (latest.value) { // 按时间排序
            return new Date(b.createTime) - new Date(a.createTime);
        } else { // 按点赞数排序
            return b.likes - a.likes;
        }
    });
    console.table(newSort.map(comment => ({likes: comment.likes})));
    comments.value = newSort;
}

//********** 点赞 *********
// 点赞按钮事件 将评论id返回后端判断是否点赞，然后处理点赞状态
const likeIds = ref([])

function isLike(cid) {
    if (likeIds.value.indexOf(cid) !== -1) {
        return svgFill;
    } else return svgLike;
}

function fetchUserLikes() {
    request.get("/comment/likeList", {params: {uid: props.uid, pid: props.pid}}).then(res => {
        if (res.status === 200) {
            likeIds.value = res.data
        } else {
            console.error('error：获取用户点赞信息失败！');
        }
    })
}

const changeLike = (commentId) => {
    console.log("点赞")
    request.get("/comment/changeLiked", {params: {uid: props.uid, cid: commentId}}).then(res => {
        if (res.status === 200) {
            fetchComments();
            fetchUserLikes();
        } else {
            console.error("改变点赞状态失败：" + commentId);
        }
    })
}


const timeStr = (date) => {
    //获取js 时间戳
    let newTime = new Date();
    let oldTime = new Date(date);
    //去掉 js 时间戳后三位，与php 时间戳保持一致
    let time = (newTime - oldTime) / 1000;
    //存储转换值
    let s;
    if (time < 60) {//一分钟内
        return '刚刚';
    } else if ((time < 60 * 60) && (time >= 60)) {
        //超过一分钟少于1小时
        s = Math.floor(time / 60);
        return s + "分钟前";
    } else if ((time < 60 * 60 * 24) && (time >= 60 * 60)) {
        //超过1小时少于24小时
        s = Math.floor(time / 60 / 60);
        return s + "小时前";
    } else if ((time < 60 * 60 * 24 * 30) && (time >= 60 * 60 * 24)) {
        //超过1天少于30天内
        s = Math.floor(time / 60 / 60 / 24);
        return s + "天前";
    } else {
        //超过30天ddd
        return oldTime.getFullYear() + "年" + (oldTime.getMonth() + 1) + "月" + oldTime.getDate() + "日  "
            + oldTime.getHours() + ":" + oldTime.getMinutes() + ":" + oldTime.getSeconds();
    }
};

</script>


<style scoped>
.my-reply {
    padding: 10px;

    .header-img {
        display: inline-block;
        vertical-align: top;
    }

    .reply-info {
        display: inline-block;
        margin-left: 15px;
        width: 90%;

        @media screen and (max-width: 1200px) {
            width: 80%;
        }

        .reply-input {
            min-height: 20px;
            line-height: 22px;
            padding: 10px 10px;
            background-color: #f1f1f1;
            border-radius: 5px;

            &:empty:before {
                content: attr(placeholder);
            }

            &:focus:before {
                content: none;
            }

            &:focus {
                padding: 8px 8px;
                border: 2px solid #59b1ff;
                background-color: white;
                box-shadow: none;
                outline: none;
            }
        }
    }

    .reply-btn-box {
        height: 25px;
        margin: 10px 0;

        .reply-btn {
            position: relative;
            float: right;
            margin-right: 4%;
        }
    }
}

.my-comment-reply {
    margin-left: 50px;

    .reply-input {
        width: auto;
    }
}

.author-title:not(:last-child) {
    border-bottom: 1px solid rgba(178, 186, 194, 0.3);
}

.author-title {
    padding: 10px;

    .header-img {
        display: inline-block;
        vertical-align: top;
    }

    .author-info {
        display: inline-block;
        margin-left: 15px;
        width: 60%;
        height: 40px;
        line-height: 20px;

        > span {
            display: block;
            cursor: pointer;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .author-name {
            color: #000;
            font-size: 18px;
            font-weight: bold;
        }

        .author-time {
            font-size: 14px;
            color: #888;
        }
    }

    .icon-btn {
        width: 15%;
        padding: 0 !important;
        float: right;

        @media screen and (max-width: 1200px) {
            width: 20%;
            padding: 7px;
        }

        > span {
            cursor: pointer;
        }

        .iconfont {
            margin: 0 5px 0 0;
        }

        .iconfontR {
            margin: 0 5px 0 25px;
        }
    }

    .talk-box {
        margin: 0 55px;

        > p {
            margin: 0;
        }

        .reply {
            font-size: 16px;
            color: #000;
        }
    }

    .reply-box {
        margin: 10px 0 0 50px;
        background-color: #f1f1f1;
    }
}
</style>
