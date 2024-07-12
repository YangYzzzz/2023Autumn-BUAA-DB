<template>
  <div>
    <div id="postChart2" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";
export default {
  name: "postChart2",
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
      await request.get("/chart/getCommentHeat").then(res => {
        if (res.success) {
          postData = res.data
        }
      })
      let chart = myEcharts.init(document.getElementById("postChart2"), "purple-passion");
      chart.setOption({
        color: ['#ee20c5'],
        title: {
          text: '评论热度排行',
          subtext: '前20名(计算公式为 heat = 3*like + 5*comment)',
          left: 'center'
        },
        grid: {
          // containLabel: true // 确保标签被包含在图表区域内
          left: '15%',
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