<script setup>
import {RouterView} from "vue-router";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
</script>

<template>
  <div>
    <el-container style="min-height: 100vh; width: 100vw">
      <el-header>
        <Header/>
      </el-header>
      <el-main>
        <RouterView/>
      </el-main>
      <el-footer>
        <Footer/>
      </el-footer>
    </el-container>
  </div>
</template>
