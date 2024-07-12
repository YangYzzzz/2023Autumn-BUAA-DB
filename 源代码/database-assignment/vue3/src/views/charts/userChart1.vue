<template>
  <div class="echarts-box">
    <div id="userChart1" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";
export default {
  name: "userChart1",
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
      await request.get("/chart/getUserDataByMonth").then(res => {
        if (res.success) {
          userData = res.data
        }
      })
      let chart = myEcharts.init(document.getElementById("userChart1"), "purple-passion");
      chart.setOption({
        title: {
          text: "用户注册流量统计",
          left: "center",
        },
        grid: {
          bottom: '10%'
        },
        xAxis: {
          type: "category",
          data: userData.map(v => v.month)
        },
        tooltip: {
          trigger: "axis"
        },
        yAxis: {
          type: "value"
        },
        series: [
          {
            data: userData.map(v => v.num),
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