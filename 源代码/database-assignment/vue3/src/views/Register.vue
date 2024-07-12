<template>
  <div id = background>
    <div class = centerArea>
      <img v-if="page!==3" src = "../assets/login.jpg" class = centerLeft>
      <div v-if="page!==3" class = centerRight>
        <div class = top>
          注册
        </div>
        <div v-if="page===1" class = textArea>
          学号：<input v-model="user.studentId" class = inputs placeholder="请输入您的学号">
          姓名：<input v-model="user.username" class = inputs placeholder="请输入您的真实姓名">
          昵称：<input v-model="user.nickname" class = inputs placeholder="给自己起一个绰号吧">
          <span class="block,inputs">
                <span class="demonstration">生日：</span>
                <el-date-picker class = calender
                                v-model="user.birthday"
                                type="date"
                                placeholder="选择你的生日"
                                :size="size"
                />
              </span>
          性别：<select class = sex v-model="user.sex" >
          <option value="none" selected disabled hidden></option>
          <option>男</option><option>女</option></select>
          <div class =  next>
              <el-button round class="button1" @click = "addPage">
                下一页
              </el-button>
          </div>
        </div>
        <div v-if="page===2" class = textArea>
          年级：<input v-model="user.grade" class = inputs placeholder="请输入您的年级,如'大三'">
          专业：<input v-model="user.major" class = inputs placeholder="">
          家乡：<input v-model="user.address" class = inputs placeholder="说一下自己的家乡吧">
          密码：<input v-model="user.password" class = inputs placeholder="请输入密码">
          重新输入密码：<input v-model="user.repassword" class = reload placeholder="请输入密码" type = "password">
          <div class =  next>
            <el-row class="mb-4">
              <el-button round class = "button1" @click = "finishRegistr">
                完成
              </el-button>
            </el-row>
          </div>
        </div>
      </div>
      <!-- 标签选择界面!-->
      <div v-if="page===3" class = centerRight1>
        <div class = "top1">
          选取您的兴趣标签
        </div>
        <div class = labelArea>
          兴趣
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="interest in vector['兴趣']" :key="interest.first" class = "scrollbar-demo-item" @click = "clickLabel(interest.first)">
                {{ interest.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
        <div class = labelArea>
          特殊经历
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="experience in vector['特殊经历']" :key="experience.first" class = "scrollbar-demo-item" @click = "clickLabel(experience.first)">
                {{ experience.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
        <div class = labelArea>
          个人特质
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="personal in vector['个人特质']" :key="personal.first" class = "scrollbar-demo-item" @click = "clickLabel(personal.first)">
                {{ personal.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
        <div class = labelArea>
          音乐
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="music in vector['音乐']" :key="music.first" class = "scrollbar-demo-item" @click = "clickLabel(music.first)">
                {{ music.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
        <div class = labelArea>
          游戏
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="game in vector['游戏']" :key="game.first" class = "scrollbar-demo-item" @click = "clickLabel(game.first)">
                {{ game.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
      </div>

      <div v-if="page===3" class="centerLeft1">
        <div class = labelArea>
          阅读
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="read in vector['阅读']" :key="read.first" class = "scrollbar-demo-item" @click = "clickLabel(read.first)">
                {{ read.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
        <div class = labelArea>
          二次元
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="anime in vector['二次元']" :key=" anime.first" class = "scrollbar-demo-item" @click = "clickLabel(anime.first)">
                {{ anime.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
        <div class = labelArea>
          专业领域
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <el-checkbox-button v-for="special in vector['专业领域']" :key="special.first" class = "scrollbar-demo-item" @click = "clickLabel(special.first)">
                {{ special.second }}
              </el-checkbox-button>
            </div>
          </el-scrollbar>
        </div>
        <div class = next1>
            <el-button round class = "button1" @click = "finishChooseLabel">
              完成
            </el-button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request";
import {onMounted, ref} from 'vue'
import {useRouter} from "vue-router";
const router = useRouter()
const name = "Register.vue";
const page = ref(1);
const vector = ref({
});

const user = reactive({
  studentId:"",
  username:"",
  repassword:"",
  password:"",
  nickname:"",
  birthday:"",
  sex:"",
  grade:"",/*年级*/
  major:"",
  address:""
})
const labelMap = reactive({

})
function addPage(){
  page.value++;
  if(page.value > 3) {
    page.value = 1;
  }
}
function clickLabel(a) {
  let b = labelMap[a];
  if(b === 1) labelMap[a] = 0;
  else labelMap[a] = 1;
}
function finishChooseLabel() {
  labelMap[0] = user.studentId;
  const list = [];
  list.push(user.studentId);
  for(let i = 1; i <= 88; i = i + 1) {
    if(labelMap[i] === 1) list.push(i);
  }
  request.post("/user/chooseLabel", list)
      .then(response => {
        // 处理成功响应的逻辑
        if(response.status === 200) {
          ElMessage.success({
            message: "请进行一场甜蜜之旅叭",
            showClose: false
          })
          router.push("/");
        }
        else {
          ElMessage.error({
            message: "出现了意料之外的问题呢",
            showClose: false
          })
        }
      })
}
function finishRegistr() {
  if(user.repassword !== user.password) {
    ElMessage.error({
      message: "重新输入密码与密码不符",
      showClose: false
    })
    return;
  }
  console.log(user);
  if(dealEmpty() === false){//加入空白项检查
    page.value--;
    return;
  }
  dealBirthday();
  request.post("/user/register", user)
      .then(response => {
        // 处理成功响应的逻辑
        if(response.status === 200) {
          ElMessage.success({
            message: "注册成功",
            showClose: false
          })
          page.value++;
        }
        else {
          ElMessage.error({
            message: "该学号已被注册",
            showClose: false
          })
          page.value--;
        }
      })
}
onMounted(getLabels);
function getLabels() {
  request.get("/label").then(res => {
    if(res.status === 200) {
      vector.value = res.data;
    }
  })
}
function dealBirthday() {
  const date = new Date(user.birthday);
  user.birthday = date.toISOString().split('T')[0];
}
function dealEmpty() {
  if(user.birthday === ""|| user.password === "" || user.sex === "" || user.repassword === ""
  || user.address === "" || user.grade === "" || user.major === "" || user.nickname === "" ||
  user.studentId === "" || user.username === "") {
    ElMessage.error({
      message: "存在未填项",
      showClose: true
    })
    return false;
  }
  return true;
}
onMounted(labelMapInit);
function labelMapInit() {
  for(let i = 1; i <= 88;i = i+1) {

    labelMap[i] = 0;
  }
}
</script>
<style scoped>
@import "../style/login.css";
</style>
