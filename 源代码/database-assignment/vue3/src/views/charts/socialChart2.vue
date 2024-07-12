<template>
  <div>
    <el-row :gutter="10" style="padding-bottom: 20px">
      <el-col :span="8">
        <el-card shadow="always">
          <template #header>
            <div style="text-align: center; font-weight: bold; font-size: 15px">统计对数</div>
          </template>
          <div style="text-align: center; font-weight: bold; font-size: 20px">{{ total }}</div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="always">
          <template #header>
            <div style="text-align: center; font-weight: bold; font-size: 15px">亲密度最高</div>
          </template>
          <div style="text-align: center; font-weight: bold; font-size: 15px">
            <span>{{ socialData.username1 }}❤️</span> <span
              style="color: crimson; font-size: 20px">{{ socialData.max }}</span>
            <span>❤️{{ socialData.username2 }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div id="socialChart2" :style="{ width: this.width, height: this.height}"></div>
    <div>
      <el-row :gutter="10" style="text-align: center;">
        <el-col :span="8">
          <el-input v-model="bin" placeholder="间隔" type="small" style="width: 120px"/>
        </el-col>
        <el-col :span="8">
          <el-input v-model="min" placeholder="最小值" type="small" style="width: 120px"/>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="update">更新图表</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {onMounted, onUnmounted} from "vue";
import request from "@/utils/request";

export default {
  name: "socialChart2",
  props: ["width", "height"],
  data() {
    return {
      socialData: "",
      min: 100,
      total: 0,
      bin: ""
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get("/chart/getSocialIntimacy?min=" + this.min).then(res => {
        if (res.success) {
          this.socialData = res.data
          console.log(this.socialData)
          this.total = res.data.data.length
          this.min = ""
        }
      })
    },
    async update() {
      console.log(this.bin)
      console.log(this.min)
      this.updateChart(this.min, this.bin)
    }
  },

  setup() {
    let myEcharts = echarts;
    onMounted(() => {
      initChart();
    });

    onUnmounted(() => {
      myEcharts.dispose;
    });

    function createHistogramData(rawData, binSize) {
      // 1. 计算最小值和最大值
      const min = Math.min(...rawData);
      const max = Math.max(...rawData);
      // 2. 计算分桶数量
      const binCount = Math.ceil((max - min) / binSize);
      // 3. 初始化直方图数据数组
      const histogramData = Array.from({length: binCount}, () => 0);

      // 4. 遍历数据并计算每个数据点落在哪个桶内
      rawData.forEach(value => {
        const binIndex = Math.floor((value - min) / binSize);
        histogramData[binIndex]++;
      });

      // 5. 生成直方图的x轴数据（桶的范围表示）
      const categories = histogramData.map((count, index) => {
        const binMin = min + index * binSize;
        const binMax = binMin + Number(binSize);
        return `${binMin}-${binMax}`;
      });

      // 6. 返回x轴数据和y轴数据（桶的计数）
      return {
        category: categories,
        data: histogramData
      };
    }

    async function updateChart(min, bin) {
      let histogramData
      await request.get("/chart/getSocialIntimacy?min=" + min).then(res => {
        if (res.success) {
          histogramData = createHistogramData(res.data.data, bin)
          console.log(histogramData)
        }
      })
      // 将该函数导出后，myEcharts变量会被保留，该函数可以在setup外正常使用
      let chart = myEcharts.init(document.getElementById("socialChart2"), "purple-passion");
      chart.setOption({
        color: ['#1bff51'],
        title: {
          text: '亲密度直方图',
          subtext: '支持自由调节Bin,Min',
          left: 'center',
        },
        grid: {
          // containLabel: true, // 确保标签被包含在图表区域内
          right: '16%',
          bottom: '8%'
        },
        tooltip: {},
        legend: {
          data: ['Num'],
          left: 'left'
        },
        xAxis: {
          axisLabel: {
            show: true,
            fontStyle: 'italic',
            rotate: 0
          },
          name: '亲密度',
          data: histogramData.category
        },
        yAxis: {},
        series: [{
          name: 'Num',
          type: 'bar',
          data: histogramData.data,
          barWidth: '100%', // 可以调整为合适的百分比来控制柱子宽度
          barGap: '0%', // 不同系列的柱子之间没有间隙
        }]
      });
      window.onresize = function () {
        chart.resize();
      };
    }
    async function initChart() {
      await updateChart(100, 5)
    }

    return {
      initChart,
      createHistogramData,
      updateChart
    };
  },

};
</script>