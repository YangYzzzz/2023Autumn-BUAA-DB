<script setup>

import BackendSider from "@/components/backend/BackendSider.vue";
import BackendHeader from "@/components/backend/BackendHeader.vue";
import girl_avatar from '../../../public/image/girl_default_avatar.jpeg';
import {ref} from "vue";

// 需要从localstroage得到
const curUser = JSON.parse(localStorage.getItem('user'));
const avatarUrl = ref(curUser.avatarUrl)
const username = ref(curUser.username)
</script>

<template>
  <div class="backend_background">
    <div style="background-color: rgba(255, 255, 255, 0.4);">
      <el-container>
        <el-aside width="200px"><BackendSider/></el-aside>
        <el-container>
          <el-header style="padding: 0; border-bottom-style: solid;
                   border-bottom-width:2px;border-bottom-color: #181818"><BackendHeader :avatarUrl="avatarUrl" :username="username"/></el-header>
          <el-main style="background-color: white"><RouterView :curUser="curUser"/></el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<style scoped>
.backend_background {
  background-image: url('../../../public/image/backend_background.png');
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
}

</style>