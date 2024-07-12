<script>
import socialChart2 from "@/views/charts/socialChart2.vue";
import socialChart1 from "@/views/charts/socialChart1.vue";
import socialChart3 from "@/views/charts/socialChart3.vue";

export default {
  name: "BackendSocialVisible",
  components: {socialChart1, socialChart2, socialChart3}
}
</script>

<template>
  <div style="padding-top: 20px; padding-left: 30px">
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card>
          <social-chart1 :width="'600px'" :height="'400px'"></social-chart1>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <social-chart2 :width="'600px'" :height="'400px'"></social-chart2>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <social-chart3 :width="'1200px'" :height="'900px'"></social-chart3>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>

</style>