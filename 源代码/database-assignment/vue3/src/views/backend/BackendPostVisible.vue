<template>
  <div style="padding-top: 20px; padding-left: 30px">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <post-chart1 :width="'600px'" :height="'500px'"></post-chart1>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <post-chart2 :width="'600px'" :height="'500px'"></post-chart2>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import postChart1 from "@/views/charts/postChart1.vue";
import postChart2 from "@/views/charts/postChart2.vue";
export default {
  name: "BackendPostVisible",
  components: {postChart1, postChart2}
}
</script>
<style scoped>

</style>