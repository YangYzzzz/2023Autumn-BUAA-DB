<script setup>

</script>
<script>
  export default {
    props: {
      avatarUrl: URL,
      username: String
    },
    methods: {
      handleCommand(choice) {
        if (choice === "quit") {
          this.$message.success({
            message: "退出成功",
            showClose: true
          })
          this.$router.push("/login")
        } else if (choice === 'frontend') {
          this.$router.push("/square")
        }
      },
      goBack() {
        this.$router.push("/backend/home")
      }
    }
  }
</script>
<template>
  <div style="display: flex; justify-content: space-between; align-items: center; padding-top: 5px">
    <el-page-header @back="goBack" :icon="null" style="padding-left: 10px">
      <template #content>
        <span> Home </span>
      </template>
    </el-page-header>
    <span style="font-size: 30px; font-weight: bold; color: darkmagenta; font-family:fantasy;">"与爱同航"后台管理系统</span>
      <el-dropdown style="color:#181818; padding-right: 20px; display: inline" @command="handleCommand">
        <span>
          <img :src="avatarUrl" referrerpolicy="no-referrer" alt=""
               style="max-height: 40px; max-width: 40px; border-radius: 50%; position: relative; top: 2px; right: 5px">
          <span style="cursor: pointer; font-weight: bold;">{{ username }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="quit"><span>退出</span></el-dropdown-item>
            <el-dropdown-item command="frontend"><span>转到前台</span></el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
  </div>
</template>

<style scoped>

</style>