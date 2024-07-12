<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px; margin-top:0px" placeholder="请输入姓名"
                suffix-icon="CirclePlus" v-model="username"></el-input>
      <el-input style="width: 200px; margin-top:0px; margin-left: 5px" placeholder="请输入院系"
                suffix-icon="CirclePlus"
                v-model="major"></el-input>
      <el-input style="width: 200px; margin-top:0px; margin-left: 5px" placeholder="请输入性别" suffix-icon="CirclePlus"
                v-model="sex"></el-input>
      <el-input style="width: 200px; margin-top:0px; margin-left: 5px" placeholder="请输入学号" suffix-icon="CirclePlus"
                v-model="studentId"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>


    <div style="margin-bottom: 10px">
      <el-button type="primary" @click="openEmptyDialog">新增 <i class="el-icon-circle-plus-outline"></i></el-button>

      <el-popconfirm
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="InfoFilled"
          cancel-button-type="warning"
          title="您确定批量删除这些数据么"
          @confirm="delBatch"
      >
        <template #reference>
          <el-button type="danger" slot="reference" style="margin-right: 10px; margin-left: 10px">批量删除 <el-icon><Close /></el-icon>
          </el-button>
        </template>
      </el-popconfirm>

      <el-upload
          :action="'http://'+serverIP+':8080/user/import'" style="margin-right: 10px; display: inline-block"
          :show-file-list="false" accept=".xlsx" :on-success="handleImportSuccess" :on-error="handleImportError">
        <el-button type="primary">导入 </el-button>
      </el-upload>

      <el-button type="primary" @click="exportExcel">导出</el-button>
    </div>

    <el-table
        :data="tableData"
        border
        stripe
        header-cell-class-name="headerBg"
        height="440"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection">
      </el-table-column>
      <el-table-column prop="id" label="ID">
      </el-table-column>
      <el-table-column prop="username" label="姓名">
      </el-table-column>
      <el-table-column prop="avatarUrl" label="头像">
        <template v-slot="scope">
          <el-avatar shape="square" :size="'small'" :src="scope.row.avatarUrl"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="身份">
        <template v-slot="scope">
          <el-tag type="danger" v-if="scope.row.role === '管理员'">{{scope.row.role }}</el-tag>
          <el-tag type="primary" v-if="scope.row.role === '普通用户'">{{ scope.row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="major" label="专业">
      </el-table-column>
      <el-table-column prop="studentId" label="学号">
      </el-table-column>
      <el-table-column prop="sex" label="性别">
      </el-table-column>
      <el-table-column prop="grade" label="年级">
      </el-table-column>

      <el-table-column label="操作" width="300px">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="primary" @click="lookDetail(scope.row.id)">进入主页查看 <i class="el-icon-edit"></i></el-button>
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
          :total="parseInt(total)">
      </el-pagination>
    </div>

    <el-dialog title="用户信息" v-model="dialogFormVisible" center width="30%">
      <el-form label-width="70px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%" clearable>
            <el-option
                v-for="item in roleOptions"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="form.major" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="form.studentId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择性别" style="width: 100%" clearable>
            <el-option
                v-for="item in sexs"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年级">
          <el-select v-model="form.grade" placeholder="请选择年级" style="width: 100%" clearable>
            <el-option
                v-for="item in gradeOptions"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "BackendUser",
  data() {
    return {
      serverIP: "127.0.0.1",
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      major: "",
      sex: "",
      studentId: "",
      form: {},
      multipleSelection: [],
      sexs: ["男", "女"],
      dialogFormVisible: false,
      roleOptions: ["普通用户", "管理员"],
      gradeOptions: ["大一", "大二", "大三", "大四"],
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
      request.get("/user/page?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&username=" + this.username +
          "&major=" + this.major + "&sex=" + this.sex + "&studentId=" + this.studentId).then(res => {
        if (res.success) {
          console.log(res)
          this.tableData = res.data.first
          console.log(this.tableData)
          this.total = res.data.second
          console.log(this.total)
        } else {
          this.$message.error({
            message: '加载信息失败',
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
        if (res.success === true) {
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
      this.sex = "";
      this.studentId = "";
      this.major = "";
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
      request.delete("/user/del?id=" + id).then(res => {
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
      console.log(ids)
      request.post("/user/del/batch", ids).then(res => {
        if (res.success === true) {
          this.$message.success({
            message: "批量删除成功",
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
      this.load()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    exportExcel() {
      window.open("http://" + this.serverIP  + ":8080/user/export") //通过窗口打开即可下载
    },
    handleImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    handleImportError() {
      this.$message.error("导入失败")
    },
    lookDetail(id) {
      this.$router.push("/personal&uid" + id)
    }
  }
}
</script>

<style>
</style>