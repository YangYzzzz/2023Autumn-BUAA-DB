<template>
  <div style="padding-top: 20px; padding-left: 30px">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <home-chart1 :width="'600px'" :height="'500px'"></home-chart1>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <home-chart2 :width="'600px'" :height="'500px'"></home-chart2>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import homeChart1 from "@/views/charts/homeChart1.vue";
import homeChart2 from "@/views/charts/homeChart2.vue";
export default {
  name: "BackendHome",
  components: {homeChart1, homeChart2}
}
</script>
<style scoped>

</style>