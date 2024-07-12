<template>
    <el-col :span="4">
        <div class="child" @click="toLast()">
            <el-icon size="80px">
                <Back/>
            </el-icon>
        </div>
    </el-col>
    <el-col :span="6">
        <el-card :key="index"
                 :body-style="{ padding: '0px' }"
                 style="border-radius:20px;margin-top: 55px"
        >
            <img
                    :src="matchInfo.backgroundUrl === null ? defaultUrl : matchInfo.backgroundUrl"
                    class="image"
                    style="width: 100%; height: 300px; object-fit: cover;"
                    alt=""
            />

            <div style="margin: 15px 20px 20px;">
                <el-scrollbar height="350px">
                    <el-descriptions
                            class="margin-top"

                            :column="1"
                            :size="'default'"
                            border
                    >
                        <template #title>
                            <el-row>
                                <el-avatar :src="matchInfo.avatarUrl === null ? defaultAva : matchInfo.avatarUrl"
                                           :size="80"
                                           shape="square"
                                           @click="jumpIMG(matchInfo.id)"
                                />
                                <h2 style="margin-top: 20px;margin-left: 20px">{{ matchInfo.nickname }}</h2>
                            </el-row>
                        </template>
                        <template #extra>
                            <el-button type="success" @click="dialogFriend=true;newNotice.userId = matchInfo.id">
                                添加好友
                            </el-button>
                        </template>

                        <el-descriptions-item>
                            <template #label>性别</template>
                            {{ matchInfo.sex }}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>个性签名</template>
                            {{ matchInfo.signature }}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>籍贯</template>
                            {{ matchInfo.address }}
                        </el-descriptions-item>

                        <el-descriptions-item>
                            <template #label>学号</template>
                            {{ matchInfo.studentId }}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>真实姓名</template>
                            {{ matchInfo.username }}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>学院</template>
                            {{ matchInfo.major }}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>年级</template>
                            {{ matchInfo.grade }}
                        </el-descriptions-item>

                    </el-descriptions>
                </el-scrollbar>
            </div>
        </el-card>
        <el-dialog v-model="dialogFriend" title="编辑申请信息">
            <el-form-item label="内容" label-width="100px">
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
    </el-col>


    <el-col :span="1"/>


    <el-col :span="9">
        <el-card v-if="matchInfo.imageBoardUrls.length <= 0"
                 style="margin: 50% 30% 50% 30%;width: fit-content">
            TA 还没有添加照片哦~
        </el-card>
        <div v-else class="block text-center" style="margin-top: 50px">
            <el-carousel height="700px">
                <el-carousel-item v-for="(photo, index) in matchInfo.imageBoardUrls" :key="index">
                    <el-image :src="photo" alt="Photo" fit="cover"
                              style="border-radius:20px;width: auto;height: 100%"/>
                </el-carousel-item>
            </el-carousel>
        </div>
    </el-col>
    <el-col :span="4">
        <div class="child-r" @click="toNext()">
            <el-icon size="80px">
                <Right/>
            </el-icon>
        </div>
    </el-col>
</template>


<script setup>
import {onMounted, reactive, ref} from "vue";
import {Back, Right} from "@element-plus/icons-vue";
import request from "@/utils/request";
import {UToast} from "undraw-ui";
import {useRouter} from "vue-router";

const defaultUrl = "../../../public/image/user_default_background.jpg";
const defaultAva = "../../../public/image/girl_default_avatar.jpeg"

const router = useRouter()
const storedUser = JSON.parse(localStorage.getItem('user'));

// 使用 defineProps 声明接收的属性
const props = defineProps(['resList', 'matchId']);
const len = props.resList.length;
const index = ref(0);

const dialogFriend = ref(false)
const newNotice = reactive({
    myId: storedUser.id,
    userId: 0,
    message: '',
    soulMatchId: props.matchId
})


const matchInfo = ref({
    "id": 2,
    "studentId": "21373035",
    "username": "jin",
    "password": "jinkaiming",
    "nickname": "金爷",
    "avatarUrl": "/public/image/boy_default_avatar.jpeg",
    "backgroundUrl": null,
    "sex": "女",
    "coupleId": null,
    "signature": null,
    "address": null,
    "major": "计算机学院",
    "grade": null,
    "birthday": null,
    "exp": 0,
    "spaceDescription": null,
    "role": "普通用户",
    "age": null,
    "constellation": null,
    "score": 0.6,
    "imageBoardUrls": [
        "/public/image/backend_background.png",
        "/public/image/backend_background.png",
        "/public/image/backend_background.png"
    ]
})

function toLast() {
    if (index.value > 0) {
        index.value--;
    } else {
        index.value = len;
    }
    console.log("上一个", index.value);
    matchInfo.value = props.resList[index.value]
}

function toNext() {
    if (index.value >= len - 1) {
        index.value = 0;
    } else {
        index.value++;
    }
    console.log("下一个", index.value);
    matchInfo.value = props.resList[index.value]
}

function jumpIMG(uid) {
    // 打开新窗口
    const url = router.resolve({name: 'PersonalPage', params: {userId: uid}});
    console.log(url.href);  // 输出为#/test-url
    window.open(url.href);
}

function addFriend() {
    console.log("addFriend", newNotice);
    request.post("/match/sendFriendApplication", newNotice).then(res => {
        if (res.status === 200) {
            UToast({message: '申请成功!', type: 'success'});
            newNotice.message = '';
            dialogFriend.value = false;
        } else {
            console.error('申请好友失败');
        }
    })
}

onMounted(() => {
    console.log("传入的参数", props.resList);
    matchInfo.value = props.resList[index.value]
})
</script>


<style scoped>
.child {
    width: 80px;
    height: 80px;
    position: absolute;
    background: rgba(255, 255, 255, 0.5);
    border-radius: 50%;
    top: 50%;
    bottom: 50%;
    left: 10%;
    right: 90%;
    margin-left: -40px;
    margin-top: -40px;
}

.child-r {
    width: 80px;
    height: 80px;
    position: absolute;
    background: rgba(255, 255, 255, 0.5);
    border-radius: 50%;
    top: 50%;
    bottom: 50%;
    left: 90%;
    right: 10%;
    margin-left: -40px;
    margin-top: -40px;
}
</style>