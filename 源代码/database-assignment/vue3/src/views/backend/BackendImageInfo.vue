<template>
  <div>
    <div style="padding-bottom: 15px">
      <el-input style="width: 200px; margin-top:0px" placeholder="请输入姓名检索"
                suffix-icon="CirclePlus" v-model="username"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>

    <el-row :gutter="10">
      <el-col :span="6" v-for="imageItem in imageBoardData" :key="imageItem.id">
        <el-card style="font-family: 黑体; margin-bottom: 10px">
          <template #header>
            <div style="font-weight: bold" class="card-header">
              <span><el-icon><User/></el-icon> {{ imageItem.username }}</span>
              <span style="text-align: left">
                <el-popover trigger="hover" placement="auto" :width="160" :tabindex="imageItem.id">
                  <p>确定这张照片涉及色情，暴力，要删除这张照片么?</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="small" type="primary" @click="delImage(imageItem.id)">确定</el-button>
                  </div>
                  <template #reference>
                    <el-button type="danger" :icon="Delete" size="small"/>
                  </template>
                </el-popover>
              </span>
            </div>
            <div>
              <el-icon>
                <Clock/>
              </el-icon>
              <span> {{ imageItem.time }}</span>
            </div>
          </template>

          <el-image style="width: 200px; height: 200px" :src="imageItem.imgUrl" fit="fill">
            <template #error>
              <div class="image-slot">
                <el-icon>
                  <icon-picture/>
                </el-icon>
              </div>
            </template>
          </el-image>
        </el-card>
      </el-col>
    </el-row>

    <div style="padding: 18px 0 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[8, 12, 16, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

import {
  Delete,
} from '@element-plus/icons-vue'

export default {
  name: "BackendImageInfo",
  computed: {
    Delete() {
      return Delete
    }
  },
  data() {
    return {
      pageNum: 1,
      pageSize: 12,
      imageBoardData: [],
      username: [],
      total: 0
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get("/imageBoard/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&username=" + this.username).then(res => {
        if (res.success) {
          this.imageBoardData = res.data.first
          this.total = res.data.second
        } else {
          this.$message.error({
            showClose: true,
            message: "加载图片信息失败"
          })
        }
      })
    },
    handleSizeChange(pageSize) { //改变一页的Size 每次改变都要重新从数据库读取数据
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) { //改变页码
      this.pageNum = pageNum
      this.load()
    },
    reset() {
      this.username = "";
      this.load();
    },
    delImage(id) {
      request.delete("/imageBoard/del?id=" + id).then(res => {
        if (res.success) {
          this.$message.success({
            showClose: true,
            message: "删除图片成功"
          })
          this.load()
        } else {
          this.$message.error({
            showClose: true,
            message: "删除图片失败"
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>