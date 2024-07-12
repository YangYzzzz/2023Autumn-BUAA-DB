<template>
  <div>
    <div style="text-align: center; font-size: 30px; font-weight: bold;">
      您好，尊敬的
      <span style="color: rgba(255,49,250,0.6)">
        {{ curUser.username }}
      </span>
      管理员先生
    </div>

    <div style="margin-bottom: 10px; margin-top: 20px">
      <el-button type="primary" @click="openDialog">发布管理员通告 <i class="el-icon-circle-plus-outline"></i></el-button>

      <el-popconfirm
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="InfoFilled"
          cancel-button-type="warning"
          title="您确定批量删除这些数据么"
          @confirm="delBatch"
      >
        <template #reference>
          <el-button type="danger" slot="reference" style="margin-left: 10px">批量删除
          </el-button>
        </template>
      </el-popconfirm>

      <el-button type="primary" @click="exportExcel">导出</el-button>
    </div>

    <el-table
        :data="announcementTableData"
        border
        stripe
        header-cell-class-name="headerBg"
        height="440"
        @selection-change="handleAnnouncementSelectionChange">
      <el-table-column type="selection">
      </el-table-column>
      <el-table-column prop="id" label="ID">
      </el-table-column>
      <el-table-column prop="time" label="通知时间" sortable>
      </el-table-column>
      <el-table-column prop="message" label="公告内容">
      </el-table-column>
      <el-table-column prop="receiverNum" label="接收人数" sortable>
      </el-table-column>
      <el-table-column prop="type" label="类型">
      </el-table-column>
      <el-table-column label="操作" width="300px">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              cancel-button-type="warning"
              icon="InfoFilled"
              title="确定撤回这条公告么"
              @confirm="del(scope.row.id)"
          >
            <template #reference>
              <el-button type="danger" class="ml-5">撤回 <i class="el-icon-remove-outline"></i>
              </el-button>
            </template>
          </el-popconfirm>
          <!--              <el-button type="danger" slot="reference" class="ml-5" @click="del(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>-->

        </template>
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
          :total="parseInt(total)">
      </el-pagination>
    </div>


    <el-dialog title="昭告天下(可在右栏选择想要发送的对象)" v-model="dialogVisible" center width="50%" height="50%">
      <el-row :gutter="10">
        <el-col :span="12">
          <div style="padding-top: 0px">
            <el-input
                v-model="announcement"
                type="textarea"
                :autosize="{ minRows: 20, maxRows: 40}"
                placeholder="请输入管理员通告内容"
            />
          </div>
        </el-col>

        <el-col :span="12">
          <div style="margin-bottom: 10px">
            <el-input style="width: 150px; margin-top:0px" placeholder="请输入姓名"
                               suffix-icon="CirclePlus" v-model="username"></el-input>
            <el-button style="margin-left: 5px" type="primary" @click="loadUser">搜索</el-button>
            <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
          </div>
          <div>
            <el-table
                :data="userTableData"
                border
                stripe
                header-cell-class-name="headerBg"
                height="500"
                @selection-change="handleUserSelectionChange"
            >
              <el-table-column type="selection">
              </el-table-column>
              <el-table-column prop="avatarUrl" label="头像">
                <template v-slot="scope">
                  <el-avatar shape="square" :size="'small'" :src="scope.row.avatarUrl"></el-avatar>
                </template>
              </el-table-column>
              <el-table-column prop="username" label="姓名">
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>

      <div slot="footer" class="dialog-footer" style="text-align: center; padding-top: 10px">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="postAnnouncement">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "BackendAnnouncement",
  props: {
    curUser: Object
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  data() {
    return {
      announcement: "",
      userIds: [],
      dialogVisible: false,
      announcementTableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      userId: 1,
      mulAnnouncementSelection: [],
      mulUserIds: [],
      userTableData: [],
      serverIP: "127.0.0.1",
      username: ""
    }
  },
  methods: {
    reset() {
      this.username = ""
    },
    load() {
      request.get("/manager/page?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&userId=" + this.userId).then(res => {
        if (res.success) {
          console.log(res)
          this.announcementTableData = res.data.first
          this.total = res.data.second
        } else {
          this.$message.error({
            message: '加载管理员通知失败',
            showClose: true
          })
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    loadUser() {
      request.get("/user/getNormalUser?username=" + this.username).then(res => {
        if (res.success) {
          this.userTableData = res.data
        } else {
          this.$message.error({
            message: '加载普通用户信息失败',
            showClose: true
          })
        }
      })
    },
    openDialog() {
      this.dialogVisible = true
      this.loadUser()
    },
    handleSizeChange(pageSize) { //改变一页的Size 每次改变都要重新从数据库读取数据
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) { //改变页码
      this.pageNum = pageNum
      this.load()
    },
    handleAnnouncementSelectionChange(val) {
      this.mulAnnouncementSelection = val;
    },
    handleUserSelectionChange(val) {
      this.mulUserIds = val;
      console.log(this.mulUserIds)
    },
    closeDialog() {
      this.dialogVisible = false
      this.mulUserIds = []
    },
    async postAnnouncement() {
      let receivers = this.mulUserIds.map(v => v.id)
      const info = {"userId": this.userId, "message": this.announcement, "receivers": receivers}
      await request.post("/manager/sendManagerAnnouncement", info).then(res => {
        if (res.success) {
          this.$message.success({
            message: '发送通告成功',
            showClose: true
          })
        } else {
          this.$message.error({
            message: '发送通告失败',
            showClose: true
          })
        }
      })

      this.dialogVisible = false
      this.announcement = ""
      this.load()
    },

    del(id) {
      request.delete("/manager/del?id=" + id).then(res => {
        if (res.success) {
          this.$message.success({
            message: '撤回公告成功',
            showClose: true
          })
        } else {
          this.$message.error({
            message: '撤回公告失败',
            showClose: true
          })
        }
      })
    },

    delBatch() {
      let ids = this.mulAnnouncementSelection.map(v => v.id) // [{}, {}, {}] => [1, 2, 3] 扁平化处理
      request.post("/manager/del/batch", ids).then(res => {
        if (res.success) {
          this.$message.success({
            message: "批量撤回通告成功",
            showClose: true
          })
          this.load()
        } else {
          this.$message.error({
            message: "批量撤回通告失败",
            showClose: true
          })
        }
      })
      this.load()
    },

    exportExcel() {
      window.open("http://" + this.serverIP  + ":8080/manager/export?uid=" + this.curUser.id) //通过窗口打开即可下载
    },
  }
}
</script>
<style scoped>

</style>