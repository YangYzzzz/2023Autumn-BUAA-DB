<template>
  <!--上方：导航栏-->
    <Header/>
  <!--主要内容，分为左右两大块，左边为个人信息，右边为发过的帖子-->
    <el-row class="background-container">
        <!--        :style="{backgroundImage: 'url(' + userData.backgroundUrl + ')'}"-->
        <el-col :span="2"></el-col>
        <!--个人信息-->
        <el-col :span="6">
            <el-card :body-style="{ padding: '0px' }" style="height: 700px; margin-top: 36px">
                <img
                        :src="userData.backgroundUrl === null ? '../../../public/image/user_default_background.jpg' : userData.backgroundUrl "
                        class="image"
                        style="width: 100%; height: 300px; object-fit: cover;"
                        alt=""
                />

                <div style="margin: 15px 20px 20px;">
                    <el-scrollbar height="300px">
                        <el-descriptions
                                class="margin-top"
                                :column="1"
                                :size="'default'"
                                border
                        >
                            <template #title>
                                <el-row>
                                    <el-avatar :src="userData.avatarUrl" :size="80" shape="square"/>
                                    <h2 style="margin-top: 20px;margin-left: 20px">{{ userData.nickname }}</h2>
                                </el-row>
                            </template>
                            <template #extra>
                                <el-button type="primary"
                                           @click="dialogFormVisible = true"
                                           v-if="Number(storedUser.id) === Number(userId)"
                                >
                                    修改信息
                                </el-button>
                                <el-button type="success" v-else-if="isFriend()" plain disabled>
                                    已是好友
                                </el-button>
                                <el-button v-else type="success" @click="dialogFriend = true">
                                    添加好友
                                </el-button>

                                <el-dialog v-model="dialogFriend" title="编辑申请信息">
                                    <el-form-item label="内容" :label-width="formLabelWidth">
                                        <el-input v-model="newNotice.message" clearable style="width: 500px;"
                                                  placeholder="打个招呼吧~"/>
                                    </el-form-item>
                                    <template #footer>
                                    <span class="dialog-footer">
                                      <el-button @click="dialogFriend = false">取消</el-button>
                                      <el-button type="primary" @click="addFriend()">发送请求</el-button>
                                    </span>
                                    </template>
                                </el-dialog>

                                <el-dialog v-model="dialogFormVisible" title="修改个人信息" :style="{ marginTop: page === 2 ? '50px' : '' }">
                                    <el-form v-if="page===1" :model="form">
                                        <el-form-item label="昵称" :label-width="formLabelWidth">
                                            <el-input v-model="form.nickname" clearable style="width: 500px;"/>
                                        </el-form-item>

                                        <el-form-item label="个性签名" :label-width="formLabelWidth">
                                            <el-input v-model="form.signature" clearable style="width: 500px;"/>
                                        </el-form-item>

                                        <el-form-item label="学院" :label-width="formLabelWidth">
                                            <el-autocomplete
                                                    v-model="form.major"
                                                    :fetch-suggestions="searchCollege"
                                                    clearable
                                                    class="inline-input"
                                                    style="width: 500px"
                                                    :placeholder="userData.major"
                                                    @select="handleSelect"
                                            />
                                        </el-form-item>

                                        <el-form-item label="年级" :label-width="formLabelWidth">
                                            <el-select v-model="form.grade" :placeholder="userData.grade"
                                                       style="width: 500px">
                                                <el-option label="大一" value="大一"/>
                                                <el-option label="大二" value="大二"/>
                                                <el-option label="大三" value="大三"/>
                                                <el-option label="大四" value="大四"/>
                                                <el-option label="研究生" value="研究生"/>
                                                <el-option label="博士生" value="博士生"/>
                                            </el-select>
                                        </el-form-item>

                                        <el-row>
                                            <el-form-item label="头像" :label-width="formLabelWidth">
                                                <el-upload
                                                        class="avatar-uploader"
                                                        action="http://localhost:8080/post/upload"
                                                        :show-file-list="false"
                                                        :on-success="handleAvatarSuccess"
                                                >
                                                    <el-image fit="cover" v-if="form.avatarUrl" :src="form.avatarUrl"
                                                              class="avatar" alt=""/>
                                                    <el-icon v-else class="avatar-uploader-icon">
                                                        <Plus/>
                                                    </el-icon>
                                                </el-upload>
                                            </el-form-item>

                                            <el-form-item label="背景图" :label-width="formLabelWidth">
                                                <el-upload
                                                        class="avatar-uploader"
                                                        action="http://localhost:8080/post/upload"
                                                        :show-file-list="false"
                                                        :on-success="handleBackSuccess"
                                                >
                                                    <el-image fit="cover" v-if="form.backgroundUrl"
                                                              :src="form.backgroundUrl"
                                                              class="avatar" alt=""/>
                                                    <el-icon v-else class="avatar-uploader-icon">
                                                        <Plus/>
                                                    </el-icon>
                                                </el-upload>
                                            </el-form-item>
                                        </el-row>
                                    </el-form>

                                    <el-form v-else>
                                        <div v-for="(value, key) in vector">
                                            <div class="l-title">{{ key }}</div>
                                            <el-checkbox-group v-model="checkboxGroup1" >
                                                <el-scrollbar height="40px">
                                                    <div class="scrollbar-flex-content1">
                                                        <el-checkbox-button v-for="item in value" :key="item.first"
                                                                            :label="item.first">
                                                            {{ item.second }}
                                                        </el-checkbox-button>
                                                    </div>
                                                </el-scrollbar>
                                            </el-checkbox-group>
                                        </div>
                                    </el-form>

                                    <template #footer>
                                    <el-button v-if="page===1" @click="nextPage()" type="success" style="float: left!important;" round>下一页</el-button>
                                    <el-button v-else @click="page=1" type="success" style="float: left!important;" round>上一页</el-button>
                                    <span class="dialog-footer">

                                      <el-button @click="dialogFormVisible = false">取消</el-button>
                                      <el-button type="primary" @click="changeInfo()">提交修改</el-button>
                                    </span>
                                    </template>
                                </el-dialog>
                            </template>

                            <el-descriptions-item>
                                <template #label>性别</template>
                                {{ userData.sex }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template #label>个性签名</template>
                                {{ userData.signature }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template #label>籍贯</template>
                                {{ userData.address }}
                            </el-descriptions-item>

                            <el-descriptions-item>
                                <template #label>学号</template>
                                {{ userData.studentId }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template #label>真实姓名</template>
                                {{ userData.username }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template #label>学院</template>
                                {{ userData["major"] }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template #label>年级</template>
                                {{ userData["grade"] }}
                            </el-descriptions-item>

                        </el-descriptions>
                    </el-scrollbar>
                    <el-scrollbar height="60px" style="margin-top: 10px">
                        <div>
                            <el-tag
                                v-for="(tag,id) in dynamicTags"
                                :key="id"
                                class="mx-1"
                                :type="getTagType(id)"
                                style="margin-left: 5px"
                            >
                                {{ tag }}
                            </el-tag>
                        </div>
                    </el-scrollbar>
                </div>
            </el-card>
        </el-col>

        <el-col :span="1"></el-col>

        <el-col :span="13" style="margin-top: 26px">
            <!--帖子-->
            <div v-if="!choosePicture" style="height: 700px">
                <el-scrollbar height="700px">
                    <template v-for="post in posts">
                        <el-card style="margin: 10px 10px 10px 10px;">
                            <!--帖子的发布人、发布时间-->
                            <div class="post-header">
                                <img :src="userData.avatarUrl" alt="User Avatar" class="user-avatar"/>
                                <div class="user-info">
                                    <p class="username">{{ userData.nickname }}</p>
                                    <p class="post-time">{{ post.time }}</p>
                                </div>
                            </div>
                            <!--帖子的内容：文字+图片-->
                            <div class="post-content">
                                {{ post.content }}
                                <div class="post-images">
                                    <div class="image-grid">
                                        <div v-for="(image, index) in post.imageUrls" :key="index" class="image-cell">
                                            <img :src="image" alt="Image" @click="gotoPostDetail(post.id)"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--帖子的操作：点赞、评论-->
                            <el-row style="margin-top: 25px;">
                                <el-col :span="12">
                                    <el-button v-if=isLike(post.id) class="center-button"
                                               @click="likePost(post.id)">
                                        <div v-html="svgFill"></div>
                                        <div style="margin-left: 3px">点赞 {{ post.likeNum }}</div>
                                    </el-button>
                                    <el-button v-else plain class="center-button" @click="likePost(post.id)">
                                        <div v-html="svgLike"></div>
                                        <div style="margin-left: 3px">点赞 {{ post.likeNum }}</div>
                                    </el-button>

                                    <!--                                    <el-button @click="likePost(post.id)"-->
                                    <!--                                               :class="{'center-button':true, 'button-active': isLike(post.id)} ">-->
                                    <!--                                        <div v-html="svgLike"></div>-->
                                    <!--                                        <div style="margin-left: 3px">点赞 {{ post.likeNum }}</div>-->
                                    <!--                                    </el-button>-->
                                </el-col>
                                <el-col :span="12">
                                    <el-button @click="gotoPostDetail(post.id)" class="center-button">
                                        <div v-html="svgComment"></div>
                                        <div style="margin-left: 3px">评论 {{ post.commentNum }}</div>
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-card>
                    </template>

                </el-scrollbar>
            </div>

            <div class="block text-center" v-if="choosePicture" style="height: 700px">
                <el-carousel height="700px" :autoplay="false">
                    <el-carousel-item v-for="(photo, index) in photos" :key="index">
                        <img :src="photo.imgUrl" alt="Photo"
                             style="display:flex; margin:0 auto; height: 100%; object-fit: cover;"/>
                    </el-carousel-item>
                </el-carousel>
            </div>
            <!--选择按钮-->
            <el-row style="margin-top: 10px">
                <div class="center-button ">
                    <el-switch
                            v-model="choosePicture"
                            class="mb-2"
                            active-text="照片墙"
                            inactive-text="帖子集"
                    />
                </div>
            </el-row>
        </el-col>

        <el-col :span="2"></el-col>
    </el-row>
</template>

<script setup>
import Header from '../../components/Header.vue'
import {onBeforeMount, onMounted, reactive, ref, watch} from "vue";
import {useRouter} from "vue-router";
import request from "@/utils/request";
import {UToast} from "undraw-ui";
import {Plus} from "@element-plus/icons-vue";

const router = useRouter()
const storedUser = JSON.parse(localStorage.getItem('user'));
const userId = router.currentRoute.value.params.userId;

//显示信息
const userData = ref({
    id: 1,
    studentId: 21373037,
    username: "杨博文",
    nickname: "小杨",
    avatarUrl: null,
    backgroundUrl: null,
    sex: null,
    coupleId: null,
    signature: null,
    address: null,
    major: "计算机学院",
    grade: null
});

async function fetchUserData() {
    const res = await request.get('user/getInfo', {params: {id: userId}});
    if (res.status === 200) {
        userData.value = res.data;
        form.nickname = res.data.nickname
        form.grade = res.data.grade
        form.major = res.data.major
        form.signature = res.data.signature
        form.avatarUrl = res.data.avatarUrl
        form.backgroundUrl = res.data.backgroundUrl
    } else {
        console.error('error：获取用户信息失败！');
    }
    getMyLabel();
}

//tags
const dynamicTags = ref(['Tag 1', 'Tag 2', 'Tag 3', 'tag 4', '123', 'Tag 1', 'Tag 2', 'Tag 3', 'tag 4', '123'])
function getTagType(tid) {
    switch (tid%5) {
        case 0: return "primary";
        case 1: return "success";
        case 2: return "info";
        case 3: return "warning";
        case 4: return "danger";
    }
}
const page = ref(1);
const checkboxGroup1 = ref([])
const vector = ref({});

function nextPage() {
    getLabels();
    getMyTag();
    page.value=2;
}
function getLabels() {
    request.get("/label").then(res => {
        if (res.status === 200) {
            vector.value = res.data;
        }
    })
}
function getMyTag() {
    request.get("/label/getUserLabelList", {params: {uid: storedUser.id}}).then(res => {
        if (res.status === 200) {
            checkboxGroup1.value = res.data
            console.log("获得个人tag成功", checkboxGroup1.value)
        }
    })
}

function getMyLabel() {
    request.get("/label/getUserLabelNameList", {params: {uid: storedUser.id}}).then(res => {
        if (res.status === 200) {
            dynamicTags.value = res.data
            console.log("获得个人label成功", dynamicTags)
        }
    })
}

//修改信息
const colleges = ref([{value: '材料科学与工程学院'},
    {value: '电子信息工程学院'},
    {value: '自动化科学与电气工程学院'},
    {value: '能源与动力工程学院'},
    {value: '航空科学与工程学院'},
    {value: '计算机学院'},
    {value: '机械工程及自动化学院'},
    {value: '经济管理学院'},
    {value: '数学科学学院'},
    {value: '生物与医学工程学院人文社会科学学院（公共管理学院）'},
    {value: '外国语学院'},
    {value: '交通科学与工程学院'},
    {value: '可靠性与系统工程学院'},])
const searchCollege = (queryString, cb) => {
    const results = queryString
        ? colleges.value.filter(createFilter(queryString))
        : colleges.value
    // call callback function to return suggestions
    cb(results)
}
const createFilter = (queryString) => {
    return (restaurant) => {
        return (
            restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        )
    }
}
const handleSelect = (item) => {
    console.log(item)
}

const dialogFormVisible = ref(false)
const formLabelWidth = '100px'
const form = reactive({
    id: userId,
    nickname: '',
    signature: '',
    major: '',
    grade: '',
    avatarUrl: '',
    backgroundUrl: '',
    checkTags:[]
})

function handleAvatarSuccess(res, file) {
    console.log("图片file", file)
    if (res.status === 200) {
        console.log("图片url", res.data)
        form.avatarUrl = res.data;
    } else {
        UToast({message: '上传失败!', type: 'error'})
    }
}

function handleBackSuccess(res, file) {
    console.log("图片file", file)
    if (res.status === 200) {
        console.log("图片url", res.data)
        form.backgroundUrl = res.data;
    } else {
        UToast({message: '上传失败!', type: 'error'})
    }
}

function changeInfo() {
    form.checkTags = checkboxGroup1.value;
    console.log("form",form)
    request.post('/user/changeInfo', form).then(res => {
        if (res.status === 200) {
            UToast({message: '修改成功!', type: 'success'})
            dialogFormVisible.value = false;
            fetchUserData()
        } else {
            console.error('error：改变用户信息失败');
        }
    })
}

//帖子相关
const svgComment = '<svg width="14" height="14" viewBox="0 0 19 18" fill="none"><path clip-rule="evenodd" d="M.733 2.8a2 2 0 012-2h13.2a2 2 0 012 2v6.015a6 6 0 01-6 6H5.534a4 4 0 00-2.189.652L1.507 16.67a.5.5 0 01-.774-.418V2.8z" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/><path d="M4.5 5.62h5" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>'
const svgLike = '<svg width="14" height="14" viewBox="0 0 20 20" fill="none"><path d="M7.685 6.34a.1.1 0 00.091-.06l1.918-4.426S10.111.79 11.054.79c.944 0 1.407.394 1.407 1.566l.037 3.966a.1.1 0 00.1.1h5.392s.817 0 .47 1.145c-.348 1.146-1.541 7.085-1.541 7.085s-1.014 3.501-5.033 3.895l-9.255-.067a1 1 0 01-.993-1V7.374a1 1 0 01.993-1l5.054-.034zM5.416 6.62v11.755" stroke="#333" stroke-width="2"/></svg>'
const svgFill = '<svg width="14" height="14" viewBox="0 0 20 21" fill="none"><path fill-rule="evenodd" clip-rule="evenodd" d="M8.95 2.563l-1.726 3.98-1.158.008v13.748h-.648l6.462.047c3.079-.297 5.037-1.813 5.807-4.473.805-3.99 1.317-6.347 1.538-7.075.366-1.207-.032-2.178-1.235-2.178h-4.699l-.03-3.273c0-1.552-.77-2.358-2.207-2.358-.98 0-1.714.574-2.105 1.574zM4.765 20.294V6.56l-2.14.015a1.8 1.8 0 00-1.788 1.8v10.105a1.8 1.8 0 001.787 1.8l2.14.015z" fill="#FF6880"/></svg>'
const post_tmp = {
    "id": 1,
    "userId": 1,
    "time": "2023-11-26 09:05:36",
    "title": "1",
    "content": "1",
    "type": false,
    "likeNum": 6,
    "clickNum": 0,
    "commentNum": 2,
    "top": false,
    "nickname": null,
    "avatarUrl": null,
    "imageUrls": [
        "/public/image/boy_default_avatar.jpeg",
        "/public/image/boy_default_avatar.jpeg",
        "/public/image/boy_default_avatar.jpeg"
    ]
}
const posts = ref([]);

function fetchPosts() {
    request.get("/post/getByUid", {params: {uid: userId}}).then(res => {
        if (res.status === 200) {
            // posts.value = res.data;
            posts.value = Array.from(res.data).sort((a, b) => {
                return new Date(b.time) - new Date(a.time);
            })
        } else {
            console.error('error：获取用户帖子集失败');
        }
    })
}

//*********** 点赞 ***********//
const likeList = ref([])

function fetchLikeList() {
    request.get("/post/getLikeList", {params: {uid: storedUser.id, osId: userId}}).then(res => {
        if (res.status === 200) {
            likeList.value = res.data
        } else {
            console.error('error：获取点赞状态失败！');
        }
    })
}

function isLike(post_id) {
    //console.log("是否点赞", post_id, likeList.value)
    return likeList.value.indexOf(post_id) !== -1;
}

function likePost(post_id) {
    console.log("帖子ID：" + post_id)
    request.get("/post/changeLiked", {params: {uid: storedUser.id, pid: post_id}}).then(res => {
        if (res.status === 200) {
            fetchPosts();
            fetchLikeList();
            console.info('成功更改点赞状态');
        } else {
            console.error('error：更改点赞状态失败！');
        }
    })
}

const gotoPostDetail = (post_id) => { // 点击评论，跳转到帖子详情页
    router.push({name: 'PostDetail', params: {postId: post_id}});
};

//*********** 照片墙 ***********//
const choosePicture = ref(false);
const photos = ref([]);

function fetchImageBoard() {
    request.get("/imageBoard/getByUid", {params: {uid: userId}}).then(res => {
        if (res.status === 200) {
            photos.value = res.data;
        } else {
            console.error('error：获取用户照片墙失败');
        }
    })
}

//添加好友
const isf = ref(false)
const dialogFriend = ref(false)
const newNotice = reactive({
    myId: storedUser.id,
    userId: Number(userId),
    message: ''
})

function isFriend() {
    request.get("/friend/isFriend", {params: {myId: storedUser.id, uid: userId}}).then(res => {
        if (res.status === 200) {
            isf.value = res.data;
            return true;
        } else {
            console.error('error：判断好友');
        }
    })
    return isf.value;
}

function addFriend() {
    console.log("addFriend", newNotice);
    request.post("/notice/friend/addFriend", newNotice).then(res => {
        if (res.status === 200) {
            UToast({message: '发送成功!', type: 'success'});
            newNotice.message = '';
            dialogFriend.value = false;
        } else {
            console.error('申请好友失败');
        }
    })
}

function addAccess() {
    request.get("access/addAccess",
        {params: {userId: storedUser.id, pageName: "PersonalPage"}}).then(res => {
        if (res.status === 200) {
            console.log("访问量+1")
        }
    })
}

const routeParams = ref(router.currentRoute.value.params);
watch(() => {
    routeParams.value = router.currentRoute.value.params;
    // 在这里可以处理参数变化时的逻辑
    if(routeParams.value.userId !== userId) {
        // 例如，你可以在这里触发重新加载数据的操作
        location.reload()
    }
}, { deep: true });


onMounted(() => {
    addAccess();
    fetchPosts();
    fetchLikeList();
    fetchImageBoard();
});

onBeforeMount(() => {
    fetchUserData();
});
</script>

<style scoped>
.background-container {
    /* 设置背景图的URL，可以替换为你自己的背景图路径 */
    background-image: url('../../image/bg-pp.jpg');

    /* 设置背景图的样式，比如平铺、居中等 */
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    /* 设置容器的高度，确保背景图充满整个容器 */
    min-height: calc(100vh - 60px);
}

.post-header {
    display: flex;
    align-items: center;
}

.user-avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 10px;
    object-fit: cover;
}

.user-info {
    display: flex;
    flex-direction: column;
}

.username {
    font-weight: bold;
}

.post-time {
    color: #888;
}

.post-content {
    margin-top: 5px;
    margin-left: 60px;
    margin-right: 60px;
}

.button-active {
    background-color: rgb(64, 158, 255); /* 设置按钮的激活状态样式 */
    color: #fff;
}

.center-button {
    display: block;
    margin: 0 auto;
}

.post-images {
    margin-top: 10px;
    margin-right: 40px;
}

.image-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr); /* 九宫格，3列 */
    gap: 5px; /* 图片之间的间距 */
}

.image-cell {
    position: relative;
    overflow: hidden;
    padding-top: 100%; /* 图片容器的宽度和高度比例 */
    border-radius: 10px; /* 添加圆角 */
}

.image-cell img {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover; /* 图像填充整个容器 */
}

.avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
    border-radius: 10px; /* 可以根据需要调整圆角的大小 */
    overflow: hidden; /* 保证圆角效果 */
}

.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 200px;
    height: 200px;
    text-align: center;
    background-color: white;
    margin: 30% 40%;
}

.l-title {
    font-size: 15px;
    font-weight: bold;
    margin-bottom: 5px;
}

.scrollbar-flex-content1 {
    display: flex;
}
</style>