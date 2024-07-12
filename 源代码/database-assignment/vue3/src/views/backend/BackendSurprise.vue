<template>
  <div>
    <div style="font-size: 40px; font-weight: bold; text-align: center; margin-top: 0">有趣的应用</div>
    <div style="font-size: 20px; font-weight: bold">请上传您的建筑物正面图片</div>
    <el-upload
        action="http://localhost:8080/python/pix2pix"
        list-type="picture-card"
        :on-success="uploadFile"
    >
      <el-icon>
        <Plus/>
      </el-icon>
    </el-upload>
  </div>
  <div style="font-weight: bold; font-size: 20px; padding-top: 10px; padding-bottom: 5px">转化后如下图所示
  </div>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div><el-image :src="basicUrl" style="width: 256px; height: 256px" :alt="原始图片"></el-image></div>
          <el-button type="success" size="large" @click="saveBasicImage" disabled v-if="basicUrl==''"> 保存图片
          </el-button>
          <el-button type="success" size="large" @click="saveBasicImage" v-else> 保存图片
          </el-button>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div><el-image :src="targetUrl" style="width: 256px; height: 256px" :alt="转化后图片"></el-image></div>
          <el-button type="success" size="large" @click="saveTargetImage" disabled v-if="targetUrl==''"> 保存图片
          </el-button>
          <el-button type="success" size="large" @click="saveTargetImage" v-else> 保存图片
          </el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from "@/utils/request";
import {Plus} from "@element-plus/icons-vue";
export default {
  name: "BackendSurprise",
  data() {
    return{
      basicUrl: "",
      targetUrl: ""
    }
  },
  props: {
    curUser: Object
  },
  methods: {
    uploadFile(res, file) {
      this.basicUrl = res.data.basicUrl
      this.targetUrl = res.data.targetUrl
    },
    saveBasicImage() {
      console.log(this.curUser.id)
      let imageInfo = {
        "userId": this.curUser.id,
        "imageList": [this.basicUrl]
      }
      request.post("/imageBoard/newPhotos", imageInfo).then(res => {
        if (res.success) {
          this.$message.success({
            message: "保存成功",
            showClose: true
          })
        } else {
          this.$message.error({
            message: "保存失败",
            showClose: true
          })
        }
      })
    },
    saveTargetImage() {
      let imageInfo = {
        "userId": this.curUser.id,
        "imageList": [this.targetUrl]
      }
      console.log(imageInfo)
      request.post("/imageBoard/newPhotos", imageInfo).then(res => {
        if (res.success) {
          this.$message.success({
            message: "保存成功",
            showClose: true
          })
        } else {
          this.$message.error({
            message: "保存失败",
            showClose: true
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>