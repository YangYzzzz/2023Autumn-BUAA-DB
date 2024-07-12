<template>
  <div>
    <div id="userchart2" :style="{ width: this.width, height: this.height }"></div>
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
      let userData, total
      await request.get("/chart/getBoyGirl").then(res => {
        if (res.success) {
          userData = res.data
          total = userData[0].num + userData[1].num
        }
      })
      let chart = myEcharts.init(document.getElementById("userchart2"), "purple-passion");
      chart.setOption({
        color: ['#ef06bf', '#3c42e0'],
        title: {
          text: '用户男女比例',
          subtext: '总用户量: ' + total,
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
          bottom: '10%'
        },
        series: [
          {
            name: '性别',
            type: 'pie',
            radius: ['30%', '70%'],
            data: [
              {value: userData[0].num, name: '女'},
              {value: userData[1].num, name: '男'}
            ],
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