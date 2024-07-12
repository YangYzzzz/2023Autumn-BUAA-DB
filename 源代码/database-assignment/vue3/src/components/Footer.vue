<script setup>

</script>

<template>
  <div>
    <el-row :gutter="20" style="font-size: 15px; font-family: '微软雅黑'; min-height: 100px;
        color: rgb(252,250,251); border-top-style: solid; border-top-color: rgba(225,18,225,0.39); border-top-width: 1px; padding-top: 10px; text-align: center; background-color: rgb(245,181,238)">
      <el-col :span="8">
        <div>
          <span class="bold">
            作者
          </span>: YBW, ZYJ, MTX <br/> <span class="bold">
            坐标
          </span>: 北京航空航天大学 <br/>
          <span class="bold">
            Gitee
          </span>: <a href="https://gitee.com/database-assignment_1">
          https://gitee.com/database-assignment_1
        </a>
        </div>
      </el-col>
      <el-col :span="8">
        <div>
          <span class="bold"> WeChat</span>: a15522708771 <br/> <span class="bold">QQ</span>: 2110915639
        </div>
      </el-col>
      <el-col :span="8">
        <div class="bold" style="display: flex; align-items: center;  justify-content: center;">
          有问题请联系我们&nbsp
          <el-icon color="rgba(191,101,176,0.6)" size="23">
            <Phone/>
          </el-icon>
        </div>
        <div class="bold" style="display: flex; align-items: center;  justify-content: center;">
          请给出你的好评&nbsp
          <el-icon color="rgba(191,101,176,0.6)" size="23">
            <Sunny/>
          </el-icon>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>

.el-row {
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.bold {
  font-weight: bold;
}
</style>