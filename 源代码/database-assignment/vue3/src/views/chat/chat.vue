<template>
  <div class="backGround">
    <Header/>
    <el-container class="layout-container-demo" style="height: 500px;">
      <el-aside width="300px" class="left">
        <el-scrollbar>
          <el-menu default-openeds="['1']">
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <Share/>
                </el-icon>
                好友列表
              </template>
              <el-menu-item-group>
                <el-menu-item v-for="friend in friendList" :key="friend.id" @click="changeFriendId(friend.id)">
                  <img :src="friend.avatarUrl" class="img">
                  <div class="name">
                    <el-text class="mx-1"> {{ friend.nickname }}</el-text>
                  </div>
                  <div class="circle" v-if="unReadMap[friend.id]!==0">
                    <span class="number">
                      {{ unReadMap[friend.id] }}
                    </span>
                  </div>
                </el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
          </el-menu>
        </el-scrollbar>
      </el-aside>
      <div class="container">
        <div class="title">
          <div class="titleName" :key="fresh1">
            <el-text class=titleFont>
              {{ curNickName }}
              <span v-if="curIntimacy < 100 && isClick" :style="{ color: '#FFCC00' }">💛{{curIntimacy}}</span>
              <span v-if="curIntimacy >= 100 && curIntimacy < 200&& isClick" :style="{ color: 'blue' }">💙{{curIntimacy}}</span>
              <span v-if="curIntimacy >= 200 && curIntimacy < 300&& isClick" :style="{ color: 'purple' }">💜{{curIntimacy}}</span>
              <span v-if="curIntimacy >= 300 && curIntimacy < 400&& isClick" :style="{ color: 'orange' }">🧡{{curIntimacy}}</span>
              <span v-if="curIntimacy >= 400 && curIntimacy < 500&& isClick" :style="{ color: 'red' }">❤{{curIntimacy}}</span>
              <span v-if="curIntimacy >= 500 && curIntimacy < 600&& isClick" :style="{ color: 'red' }">💗{{curIntimacy}}</span>
              <span v-if="curIntimacy >= 500 && curIntimacy < 600&& isClick" :style="{ color: 'red' }">💘{{curIntimacy}}</span>
              <span v-if="curIntimacy >= 600&& isClick" :style="{ color: 'red' }">💕{{curIntimacy}}</span>
            </el-text>
          </div>
        </div>
        <div class="content" :key="fresh" ref="messagesContainer" v-if="isClick === true">
          <div v-for="record in recordList" :key="record.id">
            <div class="item item-left" v-if="record.senderId !== Id">
              <div class="avatar"><img :src="records.friendAvatarUrl" class="img"></div>
              <div class="bubble bubble-left">
                <el-text class="mx-1">{{ record.content }}</el-text>
              </div>
            </div>
            <div class="item item-right" v-if="record.senderId === Id">
              <div class="bubble bubble-right">
                <el-text class="mx-1 c">{{ record.content }}</el-text>
              </div>
              <div class="avatar">
                <img :src="records.selfAvatarUrl" class="img"></div>
            </div>
          </div>
        </div>
        <div class="content" :key="fresh" ref="messagesContainer" v-if="isClick === false">
            <div class = unClick>
              选择一个好朋友开始聊天吧！！
            </div>
        </div>
        <el-row class="input-area">
          <textarea name="text" id="textarea" v-model="text"></textarea>
          <div class="button-area">
            <el-button type="primary" :icon="Check" circle @click="send"/>
          </div>
        </el-row>
      </div>
    </el-container>
  </div>
</template>

<script setup>
import {nextTick, onMounted, reactive, ref, toRef, watch, watchEffect} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request";
import Header from "@/components/Header.vue";
import {Share} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import {Check} from '@element-plus/icons-vue'

const router = useRouter()
const storedUser = JSON.parse(localStorage.getItem('user'));
const Id = storedUser.id
const fresh = ref(0)
const fresh1 = ref(0)
let friendList = reactive([]);
const recordList = ref([]);
let curFriendId = toRef(reactive({}), 'value');
let records = reactive({})
let text = ""
let isClick = false;
let curNickName = reactive("")
let unReadMap = reactive({});
const messagesContainer = ref(null)
const flag = ref(0)
let curIntimacy = reactive(0);
onMounted(async () => {
  await Initial(); // 使用 await 等待 Initial() 函数执行完毕
  setInterval(getRecords, 500);
  await nextTick()
  scrollToBottom()
});

function scrollToBottom() {
  const element = messagesContainer.value;
  if (element) {
    element.scrollTop = element.scrollHeight
  }
}

// watch(recordList, async () => {
//   // 当消息变化时，等待 DOM 更新
//   await nextTick();
//   console.log("watch!")
//   scrollToBottom();
// }, {deep: true, flush: 'post'});
async function Initial() {
  await getFriends(); // 使用 await 等待异步操作完成
  curFriendId = friendList[0].id;
  //curNickName = friendList[0].nickname;
  await getRecords();
  await request.post("/chat/readAll?senderId=" + Id + "&receiverId=" + curFriendId)
      .then(res => {
        if (res.status === 200) {
          // 处理响应
        }
      });
  await getUnReadMessage();
}

async function getFriends() {
  return new Promise((resolve, reject) => {
    request.get("/chat/friend", {params: {uid: Id}}).then(res => {
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

async function getRecords() {
  console.log(flag.value)
  const prevFriendList = recordList.value
  fresh.value++;
  fresh.value = 0;
  if (isClick) {
    await request.get("/chat/getChatRecord", {params: {uid: Id, friendId: curFriendId}}).then(res => {
      if (res.status === 200) {
        records = res.data;
        recordList.value = res.data["chats"];
        // console.log(recordList)
        if (recordList.value.length !== prevFriendList.length) {
          setTimeout(() => {
            scrollToBottom();
          }, 500);
        }
      } else {
        ElMessage.error({
          message: "好友列表导入失败",
          showClose: false
        })
      }
    })
    if (flag.value === 1) {
      // await nextTick()
      // document.body.offsetHeight;
      setTimeout(() => {
        scrollToBottom();
      }, 500);
      flag.value = 0
    }
  }
}

async function changeFriendId(id) {
  await getFriends()//先更新friendList列表
  curFriendId = id;
  for (let friend of friendList) {
    if (friend.id === id) {
      curNickName = friend.nickname;
      curIntimacy = friend.intimacy;
      fresh1.value++;
      break;
    }
  }
  request.post("/chat/readAll?senderId=" + Id + "&receiverId=" + curFriendId)
      .then(res => {
        if (res.status === 200) {
        }
      });
  await getUnReadMessage();
  fresh1.value = 0;
  isClick = true;
  flag.value = 1
}

function send() {
  if (text !== "" && isClick === true) {
    request
        .post("/chat/sendChatRecord", {
          senderId: Id,
          receiverId: curFriendId,
          content: text,
        })
        .then((res) => {
          if (res.status === 200) {
            getRecords();
            for (let friend of friendList) {
              if (friend.id === id) {
                curIntimacy = friend.intimacy;
                fresh1.value++;
                break;
              }
            }fresh1.value++;
            fresh.value++;
            fresh.value = 0;
          }
        });
  }
  text = "";
  clearTextarea(); // 调用清空textarea的函数
}

function clearTextarea() {
  document.getElementById("textarea").value = ""; // 通过id获取textarea元素，并将其value设置为空字符串
}

async function getUnReadMessage() {
  let max = friendList.length;
  for (let cur = 0; cur < max; cur++) {
    request.get("/chat/getChatRecord", {params: {uid: Id, friendId: friendList[cur].id}})//获取所有信息
        .then(res => {
          if (res.status === 200) {
            let cnt = 0;
            for (let record of res.data["chats"]) {//数一下未读数目
              if (record.read === false && record.receiverId === Id) cnt += 1;
            }
            let j = friendList[cur].id;
            unReadMap[j] = cnt;
          }
        })
  }
}
</script>

<style scoped>
@import "../../style/chat.css";
@import "../../style/wechat.css";
</style>