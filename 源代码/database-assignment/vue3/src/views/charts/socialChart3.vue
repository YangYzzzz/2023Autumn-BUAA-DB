<template>
  <div class="echarts-box">
    <div id="socialChart3" :style="{ width: this.width, height: this.height }"></div>
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
  name: "socialChart3",
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
      let socialData
      await request.get("/chart/getSocialGraph").then(res => {
        if (res.success) {
          socialData = res.data
        }
      })
      let data = []
      let links = []
      for (let i = 0; i < socialData.name.length; i++) {
        data.push({name: socialData.name[i], draggable: true})
      }
      for (let i = 0; i < socialData.source.length; i++) {
        links.push({source: socialData.source[i], target: socialData.target[i]})
      }
      let chart = myEcharts.init(document.getElementById("socialChart3"), "purple-passion");
      chart.setOption({
        color: ["#d558f1"],
        title: {
          text: '社交网络图',
          subtext: '展示社交关系',
          left: 'center',
        },
        tooltip: {},
        series: [{
          type: 'graph', // 指定系列类型为关系图
          layout: 'force', // 使用力导向布局
          symbolSize: 20, // 节点大小
          roam: false, // 开启鼠标缩放和平移漫游
          edgeSymbol: ['circle', 'circle'], // 边两端的标记类型
          edgeSymbolSize: [4, 4], // 边两端的标记大小
          edgeLabel: {
            normal: {
              textStyle: {
                fontSize: 4 // 边的标签字体大小
              }
            }
          },
          force: {
            repulsion: 1000, // 节点之间的斥力因子
            edgeLength: [20, 30] // 边的长度范围
          },
          draggable: true, // 节点是否可拖拽
          data: data,
          links: links,
          label: {
            show: true
          },
          lineStyle: { // 边的样式
            normal: {
              opacity: 0.8,
              width: 1,
              curveness: 0
            }
          }
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