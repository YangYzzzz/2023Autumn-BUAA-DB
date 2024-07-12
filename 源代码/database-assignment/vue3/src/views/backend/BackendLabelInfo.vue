<script>
import request from "@/utils/request";

export default {
  name: "BackendLabel",
  data() {
    return {
      serverIP: "127.0.0.1",
      tableData: [],
      name: "",
      form: {},
      addForm: {},
      multipleSelection: [],
      dialogFormVisible: false,
      dialogAddFormVisible: false,
      labelTypeRadio: 1
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load() {
      request.get("/label/getLabel?name=" + this.name).then(res => {
        if (res.success && res.status === 200) {
          this.tableData = res.data
          this.tableData.forEach(function (data) {
            data.id = data.type
          })
        } else if (res.success && res.status === 201) {
          this.tableData = res.data
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
    saveModify() {
      if (!(this.form.id || this.form.name || this.form.type || this.form.weight)) {
        this.$message.error({
          message: "修改标签失败，属性不许为空",
          showClose: true
        })
        return
      }
      if (isNaN(this.form.weight)) {
        this.$message.error({
          message: "修改标签失败，权重请填写数字",
          showClose: true
        })
        return
      }
      request.post("/label/updateLabel", this.form).then(res => {
        if (res.success) {
          this.$message.success({
            message: "修改标签成功",
            showClose: true
          })
          this.dialogFormVisible = false;
          this.load()
        } else {
          this.$message.error({
            message: "修改标签失败",
            showClose: true
          })
        }
      })
    },
    saveAdd() {
      if (!(this.addForm.name || this.addForm.type || this.addForm.weight)) {
        this.$message.error({
          message: "新建标签失败, 属性不许为空",
          showClose: true
        })
        return
      }
      if (!isNaN(this.addForm.type)) {
        this.$message.error({
          message: "新建标签失败, 类型不许为数字",
          showClose: true
        })
        return
      }
      if (isNaN(this.addForm.weight)) {
        this.$message.error({
          message: "新建标签失败，权重请填写数字",
          showClose: true
        })
      }
      request.post("/label/addNewLabel", this.addForm).then(res => {
        if (res.success) {
          this.$message.success({
            message: "新建标签成功",
            showClose: true
          })
          this.dialogAddFormVisible = false;
          this.load()
        } else {
          this.$message.error({
            message: "新建标签失败",
            showClose: true
          })
        }
      })
    },
    reset() {
      this.name = "";
      this.load();
    },
    openEmptyDialog() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    openAddEmptyDialog() {
      this.dialogAddFormVisible = true;
      this.addForm = {};
    },
    handleEdit(row) { //将当前列的值赋予弹窗里面去
      if (isNaN(row.id)) {
        this.$message.error({
          message: "一级标签不许查看",
          showClose: true
        })
      } else {
        this.form = JSON.parse(JSON.stringify(row)); //处理绑定的情况
        this.dialogFormVisible = true;
      }
    },
    del(id) {
      if (isNaN(id)) {
        this.$message.error({
          message: "一级标签无法删除",
          showClose: true
        })
        return
      }
      request.delete("/label/del?id=" + id).then(res => {
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
      let ids = this.multipleSelection.map(v => v.id).filter(value => !isNaN(value)) // [{}, {}, {}] => [1, 2, 3] 扁平化处理
      console.log(ids)
      request.post("/label/del/batch", ids).then(res => {
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
      window.open("http://" + this.serverIP + ":8080/label/export") //通过窗口打开即可下载
      //request.get("/user/export") //http响应无需人为加入
    },
  }
}
</script>

<style>
</style>

<template>
  <div style="padding: 10px 0">
    <el-input style="width: 200px; margin-top:0px; margin-left: 5px" placeholder="请输入标签名" suffix-icon="CirclePlus"
              v-model="name"></el-input>
    <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
    <el-button style="margin-left: 5px" type="warning" @click="reset">请及时重置</el-button>
  </div>

  <div style="margin-bottom: 10px">
    <el-button type="primary" @click="openAddEmptyDialog">新增 <i class="el-icon-circle-plus-outline"></i></el-button>

    <el-popconfirm
        confirm-button-text='确定'
        cancel-button-text='我再想想'
        icon="InfoFilled"
        cancel-button-type="warning"
        title="您确定批量删除这些标签么"
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
      scrollbar-always-on
      row-key="id"
      highlight-current-row
      header-cell-class-name="headerBg"
      height="500"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection">
    </el-table-column>
    <el-table-column prop="id" label="ID">
    </el-table-column>
    <el-table-column prop="type" label="标签类型">
    </el-table-column>
    <el-table-column prop="name" label="标签名称">
    </el-table-column>
    <el-table-column prop="weight" label="标签权重">
    </el-table-column>
    <el-table-column prop="usedTime" sortable label="使用次数">
    </el-table-column>

    <el-table-column label="操作" width="300px">
      <template v-slot="scope">
        <el-button type="success" @click="handleEdit(scope.row)">修改标签信息 <i class="el-icon-edit"></i></el-button>
        <el-popconfirm
            confirm-button-text='确定'
            cancel-button-text='我再想想'
            cancel-button-type="warning"
            icon="InfoFilled"
            title="确定删除这个标签么"
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

  <el-dialog title="修改标签信息" v-model="dialogFormVisible" center width="30%">
    <el-form label-width="70px">
      <el-form-item label="标签ID">
        <el-input v-model="form.id" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item label="标签名">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="标签类型">
        <el-select v-model="form.type" placeholder="请选择标签类型" style="width: 100%" clearable>
          <el-option
              v-for="item in tableData.map(value => value.type)"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签权重">
        <el-input v-model="form.weight" autocomplete="off"></el-input>
      </el-form-item>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveModify">确 定</el-button>
      </div>
    </el-form>
  </el-dialog>

  <el-dialog title="新增标签" v-model="dialogAddFormVisible" center width="35%">
    <el-form label-width="100px">
      <el-form-item label="标签名">
        <el-input v-model="addForm.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="标签权重">
        <el-input v-model="addForm.weight" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-radio-group v-model="labelTypeRadio">
          <el-radio label=1>使用新标签类型</el-radio>
          <el-radio label=2>使用已有标签类型</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="标签类型" v-if="labelTypeRadio == 2">
        <el-select v-model="addForm.type" placeholder="请选择标签类型" style="width: 100%" clearable>
          <el-option
              v-for="item in tableData.map(value => value.type)"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="新标签类型" v-if="labelTypeRadio == 1">
        <el-input v-model="addForm.type" autocomplete="off"></el-input>
      </el-form-item>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button @click="dialogAddFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveAdd">确 定</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<style scoped>

</style>