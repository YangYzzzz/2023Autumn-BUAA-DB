<template>
  <div>
    <div id="userchart5" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";

export default {
  name: "userChart5",
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
      let userAddressData
      await request.get("/chart/getAddress").then(res => {
        if (res.success) {
          userAddressData = res.data.slice(0, 5)
        }
      })
      let chart = myEcharts.init(document.getElementById("userchart5"), "purple-passion");
      chart.setOption({
        title: {
          text: '用户家乡统计',
          subtext: '排名前5',
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
        grid: {
          top: '100',
        },
        series: [
          {
            name: '性别',
            type: 'pie',
            radius: ['30%', '70%'],
            data: userAddressData,
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: 'left',
              fontFamily: "微软雅黑"
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