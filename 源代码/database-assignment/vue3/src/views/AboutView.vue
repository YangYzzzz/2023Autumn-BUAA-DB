<template>
    <Header/>
  <div class="about">
    <h1>这里是与爱同航💌制作小组</h1>
    <h2 class="h2">你能够来到这里，说明我们真的很有缘分呢🥰</h2>
    <div class = "mainBody">
        我们是来自北航计算机学院的大三学生。其中Yang负责后端搭建和后台数据展示
      morningTxT和Yukiemi负责前端搭建，这是我们的数据库大作业😋
    </div>
    <div class = "mainBody">
      可能你因为感兴趣点进了这个网站，那么请尽情探索吧👏， 你可以领略青春的舞动，你可以和朋友知无不言，你可以邂逅那个独一无二的灵魂。希望你可以在与爱同航中收获快乐，收获爱情，收获幸福💕
    </div>
    <div class = "mainBody">
      总之，Have Fun!
    </div>
    <div class = "mainBody1">
      如果有任何技术层面的问题和一些改善的建议，又或者是想找我们聊聊，可以联系我们的技术总监Yang，微信a15522708771。
    </div>
    <div class = "mainBody">
    如果您在与爱同航中寻找到了真爱，也非常希望可以给我们打赏一杯奶茶😘
    </div>
  </div>
</template>

<style>
.about {
  margin-top: 50px;
  margin-left: 300px;
}
.h2 {
  margin-top: 20px;
  margin-left:  40px;
}
.mainBody {
  margin-top: 20px;
  margin-left: 40px;
  font-size: 20px;
  width: 800px;
}
.mainBody1 {
  margin-top: 60px;
  margin-left:  40px;
  font-size: 20px;
  width: 800px;
}
</style>
<script setup>
import Header from "@/components/Header.vue";
import {onMounted} from "vue";
import request from "@/utils/request";

const storedUser = JSON.parse(localStorage.getItem('user'));

onMounted(() => {
    addAccess();
});

function addAccess() {
    request.get("access/addAccess",
        {params: {userId: storedUser.id, pageName: "AboutView"}}).then(res => {
        if (res.status === 200) {
            console.log("访问量+1")
        }
    })
}
</script>