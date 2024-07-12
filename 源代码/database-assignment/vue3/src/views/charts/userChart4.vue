<template>
  <div>
    <div id="userchart4" :style="{ width: this.width, height: this.height }"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";

export default {
  name: "userChart4",
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
      let chinaMap
      chinaMap = await request.get("https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json")
      echarts.registerMap("china", chinaMap)
      let mapData
      await request.get("/chart/getAddress").then(res => {
        mapData = res.data
      })
      let chart = myEcharts.init(document.getElementById("userchart4"), "purple-passion");
      chart.setOption({
        tooltip: {},
        title: {
          text: '用户家乡统计',
          left: 'center',
          top: '0'
        },
        visualMap: {
          min: 0,
          max: 20,
          left: 'left',
          top: 'bottom',
          text: ['High', 'Low'],
          seriesIndex: [0],
          inRange: {
            color: ['#e9c7f3', '#7803a6']
          },
          calculable: true
        },
        geo: {
          map: 'china',
          roam: true,
          layoutCenter: ['50%', '65%'],  // 地图中心在 div 中的位置
          layoutSize: '120%',
          label: {
            normal: {
              show: false,
              fontSize: '13',
              color: 'rgba(0,0,0,0.7)'
            }
          },
          itemStyle: {
            normal: {
              borderColor: 'rgba(243,25,25,0.2)'
            },
            emphasis: {
              areaColor: '#f61739',
              shadowOffsetX: 0,
              shadowOffsetY: 0,
              shadowBlur: 20,
              borderWidth: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
        series: [{
          name: '数值',
          type: 'map',
          geoIndex: 0,
          data: mapData,
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