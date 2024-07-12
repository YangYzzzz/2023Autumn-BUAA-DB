<template>
  <div class="echarts-box">
    <div id="userChart6" :style="{ width: this.width, height: this.height }"></div>
  </div>
<!--  <div>-->
<!--    <el-row :gutter="10">-->
<!--      <el-col :span="12" style="text-align: center;">-->
<!--        <el-input v-model="bin" placeholder="近几天" type="small" style="width: 120px"/>-->
<!--      </el-col>-->
<!--      <el-col :span="12" style="text-align: center;">-->
<!--        <el-button type="primary" @click="update">更新图表</el-button>-->
<!--      </el-col>-->
<!--    </el-row>-->
<!--  </div>-->
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";
export default {
  name: "userChart6",
  props: ["width", "height"],
  setup() {
    let myEcharts = echarts;
    onMounted(() => {
      initChart();
    });

    onUnmounted(() => {
      myEcharts.dispose;
    });

    async function initChart() {
      let userData
      await request.get("/chart/getActiveUserDataByDay?day=" + 30).then(res => {
        if (res.success) {
          userData = res.data
        }
      })
      let chart = myEcharts.init(document.getElementById("userChart6"), "purple-passion");
      chart.setOption({
        color: ['#e112e1'],
        title: {
          text: "活跃用户统计",
          subtext: "统计近一个月活跃用户数",
          left: "center",
        },
        grid: {
          bottom: '10%'
        },
        xAxis: {
          type: "category",
          data: userData.map(v => v.date)
        },
        tooltip: {
          trigger: "axis"
        },
        yAxis: {
          type: "value",
          minInterval: 1
        },
        series: [
          {
            data: userData.map(v => v.count),
            type: "line",
            smooth: true,
            itemStyle: {
              normal: {
                label: {
                  show: true,
                  position: "top",
                  formatter: "{c}"
                }
              }
            }
          }
        ]
      });
      window.onresize = function () {
        chart.resize();
      };
    }

    return {
      initChart
    };
  },

};
</script>