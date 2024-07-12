<template>
  <div>
    <el-row :gutter="10" style="font-weight: bold">
      <el-col :span="6" v-for="item in statistics">
        <el-card>
          <template #header>
            <div style="font-size: 20px">
              <span>{{item.title}} </span>
              <span style="font-size: 10px">{{item.description}}</span>
            </div>
          </template>
          <div style="font-size: 30px; text-align: center">
            {{ item.content }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div style="padding: 10px 0">
      <el-input style="width: 200px; margin-top:0px; margin-right: 10px" placeholder="请输入用户姓名"
                suffix-icon="CirclePlus" v-model="senderUsername"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
      <el-button style="margin-left: 5px" type="primary" @click="exportExcel">导出</el-button>
    </div>

    <el-table
        :data="tableData"
        border
        stripe
        scrollbar-always-on
        highlight-current-row
        header-cell-class-name="headerBg"
        height="350">
      <el-table-column prop="id" label="ID">
      </el-table-column>
      <el-table-column prop="time" label="时间" sortable>
      </el-table-column>
      <el-table-column prop="username" label="灵魂匹配使用者">
      </el-table-column>
      <el-table-column prop="matchNum" label="灵魂匹配数" sortable>
      </el-table-column>
      <el-table-column prop="sendApplicationNum" label="满意数" sortable>
      </el-table-column>
      <el-table-column prop="successNum" sortable label="配对成功数">
      </el-table-column>
    </el-table>
    <div style="padding: 18px 0 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ActionTrace",
  data() {
    return {
      serverIP: "127.0.0.1",
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      statistics: [
        {
          "title": "灵魂匹配运行次数",
          "content": "0",
          "description": ""
        },
        {
          "title": "平均成功率",
          "content": "29.33%",
          "description": " 同意数/申请数"
        },
        {
          "title": "最想找到另一半",
          "content": "张宇骏",
          "description": " 使用次数最多"
        },
        {
          "title": "最花心",
          "content": "金楷茗",
          "description": " 一次匹配中发送好友申请最多"
        }
      ],
      data: ""
    }
  },
  created() {
    //请求分页查询数据
    this.load()
    this.loadCard()
  },
  methods: {
    loadCard() {
      request.get("/match/getStatisticInfo").then(res => {
          if (res.success) {
            this.statistics[0].content = res.data[0]
            this.statistics[1].content = res.data[1]
            this.statistics[2].content = res.data[2].username
            this.statistics[3].content = res.data[3].username
          }
      })
    },
    load() {
      console.log(this.receiverUsername)
      request.get("/match/page?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&username=" + this.username).then(res => {
        if (res.success) {
          this.tableData = res.data.first
          this.total = res.data.second
        } else {
          this.$message.error({
            message: "加载信息失败",
            showClose: true
          })
        }
      })
      // fetch("http://serverIP:8088/user/page?pageNum=" + this.pageNum +
      //     "&pageSize=" + this.pageSize + "&username=" + this.username + "&phone=" + this.phone + "&email" + this.email + "&address" + this.address).then(res => res.json()).then(res => {
      //   console.log(res)
      //   this.tableData = res.data
      //   this.total = res.total
      // })
    },
    reset() {
      this.username = ""
      this.load()
    },
    handleSizeChange(pageSize) { //改变一页的Size 每次改变都要重新从数据库读取数据
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) { //改变页码
      this.pageNum = pageNum
      this.load()
    },
    exportExcel() {
      window.open("http://" + this.serverIP + ":8080/notice/export") //通过窗口打开即可下载
      //request.get("/user/export") //http响应无需人为加入
    },
  }
}
</script>

<style>
</style>