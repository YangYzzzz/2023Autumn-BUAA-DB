<template>
  <Header/>
  <div class = body>
    <div class = "center">
      <div class="box-container">
        <el-card class="box-card">
          <h3>兴趣广场</h3>
        </el-card>
        <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            :ellipsis="false"
            @select="handleSelect"
        >
          <el-menu-item index="1" @click = "changeAll()">所有动态</el-menu-item>
          <el-menu-item index="2" @click = "changeOnlyFriend()">仅看好友</el-menu-item>
          <div class = "empty"></div>
          <el-sub-menu index="3">
            <template #title>排序</template>
            <el-menu-item index="3-1" @click = "sortByTime()">按时间排序</el-menu-item>
            <el-menu-item index="3-2" @click = "sortByHot()">按热度排序</el-menu-item>
          </el-sub-menu>
        </el-menu>
        <div class = "postArea" :key="fresh1">
          <template v-for="post in onBoardList" :key="fresh1">
            <el-card style="margin: 0px 0px 10px 0px;border-radius: 5px">
              <!--帖子的发布人、发布时间-->
              <div class="post-header">
                <img :src="post.avatarUrl" alt="User Avatar" class="user-avatar"/>
                <div class="user-info">
                  <p class="username">{{ post.nickname }}</p>
                  <p class="post-time">{{ post.time }}</p>
                </div>
              </div>
              <!--帖子的内容：文字+图片-->
              <div class="post-content">
                {{ post.content }}
                <div class="post-images">
                  <div class="image-grid">
                    <div v-for="(image, index) in post.imageUrls" :key="index" class="image-cell">
                      <img :src="image" alt="Image" @click="gotoPostDetail(post.id)"/>
                    </div>
                  </div>
                </div>
              </div>
              <!--帖子的操作：点赞、评论-->
              <el-row style="margin-top: 25px;">
                <el-col :span="12">
                  <el-button v-if=isLike(post.id) class="center-button"
                             @click="likePost(post.id)">
                    <div v-html="svgFill"></div>
                    <div style="margin-left: 3px">点赞 {{ post.likeNum }}</div>
                  </el-button>
                  <el-button v-else plain class="center-button" @click="likePost(post.id)">
                    <div v-html="svgLike"></div>
                    <div style="margin-left: 3px">点赞 {{ post.likeNum }}</div>
                  </el-button>
                </el-col>
                <el-col :span="12">
                  <el-button @click="gotoPostDetail(post.id)" class="center-button">
                    <div v-html="svgComment"></div>
                    <div style="margin-left: 3px">评论 {{ post.commentNum }}</div>
                  </el-button>
                </el-col>
              </el-row>
            </el-card>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Header from "@/components/Header.vue";
import request from "@/utils/request";
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
const posts = ref([]);
const allPosts = ref([]);
const router = useRouter()
const fresh1 = ref(0)
const storedUser = JSON.parse(localStorage.getItem('user'));
const userId = storedUser.id;
const svgComment = '<svg width="14" height="14" viewBox="0 0 19 18" fill="none"><path clip-rule="evenodd" d="M.733 2.8a2 2 0 012-2h13.2a2 2 0 012 2v6.015a6 6 0 01-6 6H5.534a4 4 0 00-2.189.652L1.507 16.67a.5.5 0 01-.774-.418V2.8z" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/><path d="M4.5 5.62h5" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>'
const svgLike = '<svg width="14" height="14" viewBox="0 0 20 20" fill="none"><path d="M7.685 6.34a.1.1 0 00.091-.06l1.918-4.426S10.111.79 11.054.79c.944 0 1.407.394 1.407 1.566l.037 3.966a.1.1 0 00.1.1h5.392s.817 0 .47 1.145c-.348 1.146-1.541 7.085-1.541 7.085s-1.014 3.501-5.033 3.895l-9.255-.067a1 1 0 01-.993-1V7.374a1 1 0 01.993-1l5.054-.034zM5.416 6.62v11.755" stroke="#333" stroke-width="2"/></svg>'
const svgFill = '<svg width="14" height="14" viewBox="0 0 20 21" fill="none"><path fill-rule="evenodd" clip-rule="evenodd" d="M8.95 2.563l-1.726 3.98-1.158.008v13.748h-.648l6.462.047c3.079-.297 5.037-1.813 5.807-4.473.805-3.99 1.317-6.347 1.538-7.075.366-1.207-.032-2.178-1.235-2.178h-4.699l-.03-3.273c0-1.552-.77-2.358-2.207-2.358-.98 0-1.714.574-2.105 1.574zM4.765 20.294V6.56l-2.14.015a1.8 1.8 0 00-1.788 1.8v10.105a1.8 1.8 0 001.787 1.8l2.14.015z" fill="#FF6880"/></svg>'
let friendList = reactive([]);//当前的好友列表
let onBoardList =ref([]);
onMounted(fetchPosts)
function fetchPosts() {
  fetchLikeList();
  request.get("/post/square/getAllPost").then(res => {
    if (res.status === 200) {
      allPosts.value = res.data;
      onBoardList.value = res.data;
    } else {
      console.error('error：获取用户帖子集失败');
    }
  })
}
function sortByTime() {
  console.log(onBoardList.value);
  onBoardList.value.sort((a, b) => {
    const timeA = new Date(a.time);
    const timeB = new Date(b.time);
    return timeB - timeA;
  });
  fresh1.value--;
}
function sortByHot() {
  onBoardList.value.sort((a, b) => {
    let value1 = a.clickNum * 5 + a.commentNum * 10;
    let value2 = b.clickNum * 5 + b.commentNum * 10;
    return value2 - value1;
  });
  fresh1.value--;
}
function changeAll() {
  //fresh1.value--;
  fetchPosts();
  fresh1.value++;
}
async function changeOnlyFriend() {
  await getFriends();
  onBoardList.value = [];
  console.log(allPosts.value);
  console.log(friendList);
  for(let post of allPosts.value) {
    if(isFriend(post.userId)) {
      onBoardList.value.push(post);
    }
  }
  fresh1.value++;
}
function isFriend(posterId) {
  for(let friend of friendList) {
    if(friend.id === posterId) return true;
  }
  return false;
}
function isLike(post_id) {
  return likeList.value.indexOf(post_id) !== -1;
}
const likeList = ref([3, 4])
function fetchLikeList() {
  console.log(storedUser.id)
  request.get("/post/square/getAllLikeList", {params: {uid: storedUser.id, osId: userId}}).then(res => {
    if (res.status === 200) {
      likeList.value = res.data
    } else {
      console.error('error：获取点赞状态失败！');
    }
  })
}
function likePost(post_id) {
  request.get("/post/changeLiked", {params: {uid: userId, pid: post_id}}).then(res => {
    if (res.status === 200) {
      fetchPosts();
      fetchLikeList();
      console.info('成功更改点赞状态');
      console.log(likeList.value)
    } else {
      console.error('error：更改点赞状态失败！');
    }
  })
}

const gotoPostDetail = (post_id) => { // 点击评论，跳转到帖子详情页
  router.push({name: 'PostDetail', params: {postId: post_id}});
};
const userData = ref({
  id: 1,
  studentId: 21373037,
  username: "杨博文",
  nickname: "小杨",
  avatarUrl: null,
  backgroundUrl: null,
  sex: null,
  coupleId: null,
  signature: null,
  address: null,
  major: "计算机学院",
  grade: null
});
const form = reactive({
  id: userId,
  nickname: '',
  signature: '',
  major: '',
  grade: '',
  avatarUrl: '',
  backgroundUrl: ''
})

async function fetchUserData() {
  const res = await request.get('user/getInfo', {params: {id: userId}});
  if (res.status === 200) {
    userData.value = res.data;
    form.nickname = res.data.nickname
    form.grade = res.data.grade
    form.major = res.data.major
    form.signature = res.data.signature
    form.avatarUrl = res.data.avatarUrl
    form.backgroundUrl = res.data.backgroundUrl
  } else {
    console.error('error：获取用户信息失败！');
  }
}
async function getFriends() {
  return new Promise((resolve, reject) => {
    request.get("/chat/friend", { params: { uid: userId } }).then(res => {
      if (res.status === 200) {
        friendList.splice(0); // 清空 friendList，确保它是响应式的
        for (const friend of res.data) {
          friendList.push(friend); // 逐个添加好友到 friendList
        }
        resolve();
      } else {
        ElMessage.error({
          message: "好友列表导入失败",
          showClose: false
        });
        reject();
      }
    });
  });
}
</script>

<style scoped>
@import "../style/square.css";
</style>