<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px; margin-top:0px; margin-right: 10px" placeholder="请输入用户姓名(主体)"
                suffix-icon="CirclePlus" v-model="senderUsername"></el-input>
      <el-input style="width: 200px; margin-top:0px; margin-right: 10px" placeholder="请输入用户姓名(客体)"
                suffix-icon="CirclePlus" v-model="receiverUsername"></el-input>
      <el-select style="margin-right: 10px" v-model="type" placeholder="请输入通知类型" clearable>
        <el-option
            v-for="item in options"
            :key="item"
            :label="item"
            :value="item"
        />
      </el-select>
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
        height="520">
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="senderUsername" label="用户姓名(主体)" width="140">
      </el-table-column>
      <el-table-column prop="receiverUsername" label="用户姓名(客体)" width="140">
      </el-table-column>
      <el-table-column prop="type" label="行为类型" width="140">
      </el-table-column>
      <el-table-column prop="time" sortable label="行为时间" width="200">
      </el-table-column>
      <el-table-column prop="description" label="行为描述">
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
      senderUsername: "", // 主体
      receiverUsername: "",
      type: "", // 通知类型
      options: ["system", "friend", "like", "success", "reply"]
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load() {
      console.log(this.receiverUsername)
      request.get("/notice/page?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&senderUsername=" + this.senderUsername + "&receiverUsername=" + this.receiverUsername + "&type=" + this.type).then(res => {
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
      this.senderUsername = ""
      this.receiverUsername = ""
      this.type = ""
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
    }
  }
}
</script>

<style>
</style>