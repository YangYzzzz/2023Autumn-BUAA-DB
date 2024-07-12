<template>
  <div style="padding-top: 20px; padding-left: 30px">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <user-chart1 :width="'380px'" :height="'300px'"></user-chart1>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <user-chart2 :width="'380px'" :height="'300px'"></user-chart2>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <user-chart3 :width="'380px'" :height="'300px'"></user-chart3>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="padding-top: 20px">
      <el-col :span="8">
        <el-card>
          <user-chart4 :width="'360px'" :height="'300px'"></user-chart4>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <user-chart5 :width="'360px'" :height="'300px'"></user-chart5>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <user-chart6 :width="'360px'" :height="'300px'"></user-chart6>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userChart1 from "@/views/charts/userChart1.vue";
import userChart2  from "@/views/charts/userChart2.vue";
import userChart3  from "@/views/charts/userChart3.vue";
import userChart4  from "@/views/charts/userChart4.vue";
import userChart5  from "@/views/charts/userChart5.vue";
import userChart6  from "@/views/charts/userChart6.vue";
export default {
  name: "BackendUserVisible",
  components: {
    userChart1,
    userChart2,
    userChart3,
    userChart4,
    userChart5,
    userChart6
  }
}
</script>

<style>
</style>
