<template>
  <div>
    <div id="homeChart2" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";
export default {
  name: "homeChart2",
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
      await request.get("/chart/getWebPageAccess?day=" + 30).then(res => {
        if (res.success) {
          webData = res.data
        }
      })
      let chart = myEcharts.init(document.getElementById("homeChart2"), "purple-passion");
      chart.setOption({
        color: ['#f5257b'],
        title: {
          text: '网页访问流量统计',
          subtext: '近一个月访问总量',
          left: 'center'
        },
        grid: {
          // containLabel: true, // 确保标签被包含在图表区域内
          bottom: '10%'
        },
        tooltip: {},
        legend: {
          data:['Heat'],
          left: 'left'
        },
        xAxis: {
          data: webData.map(v => v.name),
          type: 'category',
          axisLabel: {
            show: true,
            interval: 0,
            fontStyle: 'italic',
          },
          name: '网页名'
        },
        yAxis: {
        },
        series: [{
          name: 'Heat',
          type: 'bar',
          data: webData.map(v => v.count),
        }]
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