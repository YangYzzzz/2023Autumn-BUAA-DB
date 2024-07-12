<template>
    <Header/>
    <el-row class="background-container">
        <el-col :span="4"/>
        <el-col :span="16">
            <el-card :key="refresh" style="margin: 50px 10px 10px 10px;">
                <template #header>
                    <div class="card-header">

                        <el-link type="warning"
                                 style="font-size: 20px;font-weight: lighter;"
                                 @click="toWall = false"
                                 v-if="toWall"
                        >
                            <el-icon>
                                <Switch/>
                            </el-icon>&nbsp; 照片墙更新
                        </el-link>
                        <el-link type="warning"
                                 style="font-size: 20px;font-weight: lighter;"
                                 @click="fetchOldPhotos()"
                                 v-else
                        >
                            <el-icon>
                                <Switch/>
                            </el-icon>&nbsp; 新的帖子
                        </el-link>
                        <el-button
                                class="button"
                                type="success"
                                plain
                                style="margin-right: 20px;width: 70px"
                                @click="submitIt()"
                        >
                            发表
                        </el-button>
                    </div>
                </template>
                <div v-if="toWall===false">
                    <h4 style="margin-bottom: 15px">添加文字内容</h4>
                    <el-input
                            v-model="newContent"
                            :autosize="{ minRows: 3, maxRows: 6 }"
                            type="textarea"
                            placeholder="请输入正文..."
                    />
                </div>

                <div v-if="toWall">
                    <h4 style="margin-bottom: 15px">管理原有照片</h4>
                    <el-upload
                            v-model:file-list="photoList"
                            action="http://localhost:8080/post/upload"
                            list-type="picture-card"
                            :on-preview="handleOldPreview"
                            :on-remove="handleOldRemove"
                            :before-upload="handleOldUpload"
                    >
                        <el-icon>
                            <Plus/>
                        </el-icon>
                    </el-upload>

                    <el-dialog v-model="showVisible">
                        <el-image fit="fill" :src="showImageUrl" alt="Preview Image"/>
                    </el-dialog>
                </div>

                <el-divider/>
                <div>
                    <h4 v-if="toWall" style="margin-bottom: 15px">添加新的图片</h4>
                    <h4 v-else style="margin-bottom: 15px">添加图片内容</h4>
                    <el-upload
                            action="http://localhost:8080/post/upload"
                            list-type="picture-card"
                            :on-success="uploadFile"
                            :on-preview="handlePictureCardPreview"
                            :on-remove="handleRemove"
                    >
                        <el-icon>
                            <Plus/>
                        </el-icon>
                    </el-upload>

                    <el-dialog v-model="dialogVisible">
                        <el-image fit="fill" :src="dialogImageUrl" alt="Preview Image"/>
                    </el-dialog>
                </div>

                <el-divider v-if="toWall===false"/>
                <el-switch
                        v-if="toWall===false"
                        v-model="isPrivate"
                        class="mb-2"
                        active-text="仅自己可见"
                        inactive-text="发布到广场"
                />
            </el-card>
        </el-col>
        <el-col :span="4"/>
    </el-row>
</template>

<script lang="ts" setup>
import Header from "../../components/Header.vue";
import {reactive, ref, onMounted} from 'vue'
import {Plus, Switch} from '@element-plus/icons-vue'
import type {UploadFile, UploadProps, UploadUserFile} from 'element-plus'
import request from "../../utils/request.js";
import {UToast} from "undraw-ui";

const refresh = ref(0)
const toWall = ref(false)
const dialogImageUrl = ref('')
const dialogVisible = ref(false)
//其他内容
const storedUser = JSON.parse(localStorage.getItem('user'));
const newContent = ref('')
const isPrivate = ref(false)

const fileList = reactive([])
const myMap = new Map();
const postData = reactive({
    userId: storedUser.id,
    type: false,
    title: '',
    content: '',
    imageList: []
})
const wallData = reactive({
    userId: storedUser.id,
    imageList: []
})

const uploadFile = (res, file) => {
    console.log("图片file", file)
    if (res.status === 200) {
        console.log("图片url", res.data)
        fileList.push(res.data);
        myMap.set(file, res.data);
    } else {
        UToast({message: '上传失败!', type: 'error'})
    }
}
const handleRemove = (file: UploadFile) => {
    let url = myMap.get(file)
    myMap.delete(file);
    let index = fileList.indexOf(url)
    fileList.splice(index, 1);
    console.log("删除后", fileList)
}

const handlePictureCardPreview = (file: UploadFile) => {
    console.log("图片列表", fileList)
    dialogImageUrl.value = file.url!
    dialogVisible.value = true
}

// *********** 原有照片
const showImageUrl = ref('')
const showVisible = ref(false)
const photo_tmp = {
    name: 'food.jpeg',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
}
const photoList = ref<UploadUserFile[]>([]);
const deleteList = reactive([])
const deleteBody = reactive({
    userId: storedUser.id,
    deleteList: []
})

const handleOldRemove = (file: UploadFile) => {
    deleteList.push(file.url);
    console.log("删除后", deleteList);
}

const handleOldUpload: UploadProps['beforeUpload'] = (rawFile) => {
    UToast({message: '请在下方上传新图片!', type: 'error'})
    return false;
}

const handleOldPreview: UploadProps['onPreview'] = (uploadFile) => {
    console.log("photo列表", photoList)
    showImageUrl.value = uploadFile.url
    console.log("显示的url:", showImageUrl.value)
    showVisible.value = true
}

// *********** 原有
function fetchOldPhotos() {
    request.get("/imageBoard/getImgList", {params: {userId: storedUser.id}}).then(res => {
        if (res.status === 200) {
            photoList.value = res.data;
            console.log("原有照片", res.data);
        }
    })
    toWall.value = true;
}

// *********** 提交

function newPost() {
    if (!newContent.value && fileList.length === 0) {
        UToast({message: '内容不能为空!', type: 'error'})
        return;
    }
    postData.content = newContent.value
    postData.imageList = fileList
    postData.type = isPrivate.value
    console.log("开始发帖")
    request.post("/post/newPost", postData).then(res => {
        if (res.status === 200) {
            //提交
            postData.userId = storedUser.id;
            postData.type = false;
            postData.title = '';
            postData.content = '';
            postData.imageList = [];
            fileList.length = 0;
            newContent.value = '';
            isPrivate.value = false;
            myMap.clear()
            UToast({message: '发帖成功', type: 'success'})
            refresh.value++;
        } else {
            console.error("发表失败")
        }
    })
}

const isOk1 = ref(true)
const isOk2 = ref(true)

function newPhotos() {
    if (fileList.length === 0 && deleteList.length === 0) {
        UToast({message: '没有修改照片墙!', type: 'error'})
        return;
    }
    isOk1.value = true;
    isOk2.value = true;
    if (fileList.length !== 0) {
        isOk1.value = false;
        console.log(fileList)
        wallData.imageList = fileList;
        request.post("/imageBoard/newPhotos", wallData).then(res => {
            console.log(fileList)
            if (res.status === 200) {
                //提交
                fileList.length = 0;
                wallData.imageList = [];
                myMap.clear()
                isOk1.value = true;
                console.log("添加成功");
            } else {
                console.error("发表失败")
            }
        })
    }
    if (deleteList.length !== 0) {
        isOk2.value = false;
        deleteBody.deleteList = deleteList;
        request.post("/imageBoard/deletePhotos", deleteBody).then(res => {
            if (res.status === 200) {
                //提交
                deleteList.length = 0;
                isOk2.value = true;
                console.log("删除成功");
            } else {
                console.error("发表失败")
            }
        })
    }
    if (isOk1.value && isOk2.value) {
        UToast({message: '修改照片墙成功', type: 'success'});
        refresh.value++;
    }
}

function submitIt() {
    if (toWall.value) newPhotos();
    else newPost();
}

function addAccess() {
    request.get("access/addAccess",
        {params: {userId: storedUser.id, pageName: "Publish"}}).then(res => {
        if (res.status === 200) {
            console.log("访问量+1")
        }
    })
}

onMounted(() => {
    addAccess();
});
</script>

<style scoped>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.background-container {
    /* 设置背景图的URL，可以替换为你自己的背景图路径 */
    background-image: url("../../image/bg-post.jpg");

    /* 设置背景图的样式，比如平铺、居中等 */
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    /* 设置容器的高度，确保背景图充满整个容器 */
    min-height: calc(100vh - 60px);
}
</style>