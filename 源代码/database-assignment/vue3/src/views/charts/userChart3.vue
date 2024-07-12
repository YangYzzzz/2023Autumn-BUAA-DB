<template>
  <div>
    <div id="userchart3" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";

export default {
  name: "userChart2",
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
      await request.get("/chart/getConstellation").then(res => {
        if (res.success) {
          userData = res.data
        }
      })
      let chart = myEcharts.init(document.getElementById("userchart3"), "purple-passion");
      chart.setOption({
        title: {
          text: '用户星座比例',
          subtext: '看看哪个星座最爱用',
          left: 'center',
          top: '0'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: '性别',
            type: 'pie',
            radius: ['30%', '70%'],
            data: userData,
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            labelLine: {
              show: false
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            roseType: 'area'
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