<template>
  <div style="height: 100vh; font-family: 黑体,serif; font-weight: bold">
    <div style="font-size: 23px; font-weight: bold; vertical-align: center; padding-top: 10px; padding-left: 50px">导航菜单</div>
    <el-menu
        active-text-color="#ee2add"
        background-color="transparent"
        default-active="2"
        text-color="#000"
        router="True"
        style="border-right-width: 0"
        @open="handleOpen"
        @close="handleClose"
    >
      <el-menu-item index="/backend/home">
        <el-icon><House /></el-icon>
        <span>主页</span>
      </el-menu-item>
      <el-sub-menu index="1">
        <template #title>
          <el-icon><List /></el-icon>
          <span>信息管理</span>
        </template>
        <el-menu-item index="/backend/userinfo">用户信息管理</el-menu-item>
        <el-menu-item index="/backend/postinfo">帖子信息管理</el-menu-item>
        <el-menu-item index="/backend/imageinfo">图片信息管理</el-menu-item>
        <el-menu-item index="/backend/labelinfo">标签信息管理</el-menu-item>
<!--        记录匹配数据，标签匹配度，最终得分等信息，最后再议-->
        <el-menu-item index="/backend/matchinfo">匹配交友信息管理</el-menu-item>
        <el-menu-item index="/backend/actiontrace">用户行为追踪</el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="2">
        <template #title>
          <el-icon><Picture /></el-icon>
          <span>可视化数据展示</span>
        </template>
<!--        统计各项指标 经验等-->
        <el-menu-item index="/backend/uservisible">用户信息可视化</el-menu-item>
        <el-menu-item index="/backend/postvisible">帖子信息可视化</el-menu-item>
<!--        搭建社交网络? 社交达人? 被拒绝次数最多的人？-->
        <el-menu-item index="/backend/socialvisible">社交信息可视化</el-menu-item>
      </el-sub-menu>


      <el-menu-item index="/backend/announcement">
        <el-icon>
          <document/>
        </el-icon>
        <span>发布公告</span>
      </el-menu-item>

      <el-menu-item index="/backend/surprise">
        <el-icon><SwitchFilled /></el-icon>
        <span>有趣应用</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script lang="ts" setup>
import {
  Document,
  Menu as IconMenu,
} from '@element-plus/icons-vue'
import HomeView from "@/views/HomeView.vue";

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>
