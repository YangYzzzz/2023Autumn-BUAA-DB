<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px; margin-top:0px; margin-left: 5px" placeholder="请输入内容关键字"
                suffix-icon="CirclePlus"
                v-model="content"></el-input>
      <el-input style="width: 200px; margin-top:0px; margin-left: 5px" placeholder="请输入作者尊姓大名" suffix-icon="CirclePlus"
                v-model="username"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>


    <div style="margin-bottom: 10px">
      <el-popconfirm
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="InfoFilled"
          cancel-button-type="warning"
          title="您确定批量删除这些数据么"
          @confirm="delBatch"
      >
        <template #reference>
          <el-button type="danger" slot="reference" style="margin-left: 10px">批量删除 <el-icon><Close /></el-icon>
          </el-button>
        </template>
      </el-popconfirm>

      <el-button type="primary" @click="exportExcel">导出</el-button>
    </div>

    <el-table
        :data="tableData"
        border
        stripe
        scrollbar-always-on
        highlight-current-row
        header-cell-class-name="headerBg"
        height="440"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection" fixed>
      </el-table-column>
      <el-table-column prop="id" label="ID">
      </el-table-column>
      <el-table-column prop="username" label="作者姓名">
      </el-table-column>
      <el-table-column prop="time" sortable label="发布时间">
      </el-table-column>
      <el-table-column prop="content" label="内容(非图片)">
      </el-table-column>
      <el-table-column prop="likeNum" sortable label="点赞量">
      </el-table-column>
      <el-table-column prop="commentNum" sortable label="评论数">
      </el-table-column>

      <el-table-column label="操作" width="300px">
        <template v-slot="scope">
          <el-button type="success" @click="lookDetail(scope.row)">查看详情 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              cancel-button-type="warning"
              icon="InfoFilled"
              title="确定删除这条数据么"
              @confirm="del(scope.row.id)"
          >
            <template #reference>
              <el-button type="danger" class="ml-5">删除 <i class="el-icon-remove-outline"></i>
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
          :total="total">
      </el-pagination>
    </div>

<!--    <el-dialog title="编辑文章" v-model="dialogFormVisible" center width="60%">-->
<!--      <el-form label-width="70px">-->
<!--        <el-form-item label="文章标题">-->
<!--          <el-input v-model="form.title" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="文章内容">-->
<!--          <mavon-editor :navigation="true" v-model="form.content" ref="md" :ishljs="true" @imgAdd="imgAdd"></mavon-editor>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="发布日期">-->
<!--          <el-input disabled v-model="form.time" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="文章作者">-->
<!--          <el-input disabled v-model="form.user" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogEditFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="saveEdit">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "User",
  data() {
    return {
      serverIP: "127.0.0.1",
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      title: "",
      content: "",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      dialogCourseVisible: false,
      dialogStuVisible: false
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load() {
      request.get("/post/page?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&title=" + this.title +
          "&username=" + this.username + "&content=" + this.content).then(res => {
        if (res.success) {
          this.tableData = res.data.first
          this.tableData.forEach(function(data) {
            data.content = data.content.slice(0, 15) + "..."
          });
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
    save() {
      if (!(this.form.username || this.form.sex || this.form.major
          || this.form.grade || this.form.nickname)) {
        this.$message.error({
          message: "保存失败",
          showClose: true
        })
        return
      }
      request.post("/user/updateOrInsert", this.form).then(res => {
        if (res.success) {
          this.$message.success({
            message: "保存成功",
            showClose: true
          })
          this.dialogFormVisible = false;
          this.load()
        } else {
          this.$message.error({
            message: "保存失败",
            showClose: true
          })
        }
      })
    },
    reset() {
      this.username = "";
      this.title = "";
      this.content = "";
      this.load();
    },
    handleSizeChange(pageSize) { //改变一页的Size 每次改变都要重新从数据库读取数据
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) { //改变页码
      this.pageNum = pageNum
      this.load()
    },
    openEmptyDialog() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    handleEdit(row) { //将当前列的值赋予弹窗里面去
      this.form = JSON.parse(JSON.stringify(row)); //处理绑定的情况
      this.dialogFormVisible = true;
    },
    del(id) {
      request.delete("/post/del?id=" + id).then(res => {
        if (res.success === true) {
          this.$message.success({
            message: "删除成功",
            showClose: true
          })
          this.load()
        } else {
          this.$message.error({
            message: "删除失败",
            showClose: true
          })
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id) // [{}, {}, {}] => [1, 2, 3] 扁平化处理
      request.post("/post/del/batch", ids).then(res => {
        if (res.success) {
          this.$message.success({
            message: "批量删除成功",
            showClose: true
          })
          this.load()
        } else {
          this.$message.error({
            message: "批量删除失败",
            showClose: true
          })
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    exportExcel() {
      window.open("http://" + this.serverIP  + ":8080/post/export") //通过窗口打开即可下载
      //request.get("/user/export") //http响应无需人为加入
    },
    lookDetail(row) {
      this.$router.push("/post&pid" + row.id)
    }
  }
}
</script>

<style>
</style>