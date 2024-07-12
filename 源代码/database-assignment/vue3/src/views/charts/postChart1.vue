<template>
  <div>
    <div id="postChart1" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";
export default {
  name: "postChart1",
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
      let postData
      await request.get("/chart/getPostHeat").then(res => {
        if (res.success) {
          postData = res.data
        }
      })
      let chart = myEcharts.init(document.getElementById("postChart1"), "purple-passion");
      chart.setOption({
        color: ['#fd0707'],
        title: {
          text: '帖子热度排行',
          subtext: '前20名(计算公式为 heat = 1*click + 5*like + 10*comment)',
          left: 'center'
        },
        grid: {
          // containLabel: true, // 确保标签被包含在图表区域内
          bottom: '20%'
        },
        tooltip: {},
        legend: {
          data:['Heat'],
          left: 'left'
        },
        xAxis: {
          data: postData.xAxis,
          type: 'category',
          axisLabel: {
            show: true,
            interval: 0,
            fontStyle: 'italic',
            rotate: 45
          },
          name: '路由'
        },
        yAxis: {
          name: '热度值'
        },
        series: [{
          name: 'Heat',
          type: 'bar',
          data: postData.data,
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