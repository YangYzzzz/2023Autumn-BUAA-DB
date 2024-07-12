<template>
  <div class="echarts-box">
    <div id="homeChart1" :style="{ width: this.width, height: this.height }"></div>
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
  name: "homeChart1",
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
      let webData
      await request.get("/chart/getWebAccessByDay?day=" + 30).then(res => {
        if (res.success) {
          webData = res.data
        }
      })
      let chart = myEcharts.init(document.getElementById("homeChart1"), "purple-passion");
      chart.setOption({
        color: ['#e112e1'],
        title: {
          text: "网站访问流量统计",
          subtext: "近一个月访问量",
          left: "center",
        },
        grid: {
          bottom: '10%'
        },
        xAxis: {
          type: "category",
          data: webData.map(v => v.date)
        },
        tooltip: {
          trigger: "axis"
        },
        yAxis: {
          type: "value",
          minInterval: 1,
        },
        series: [
          {
            data: webData.map(v => v.count),
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