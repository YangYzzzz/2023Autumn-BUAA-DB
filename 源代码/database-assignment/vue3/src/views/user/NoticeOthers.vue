<template>
    <div class="space-right">
        <div style="height: 10px"></div>
        <div class="right-title">
            <el-row>
                <div style="margin-top: 3px">
                    新的朋友
                </div>
                <div class="flex-grow"/>
                <div>
                    <el-button @click="deleteAll" type="danger" :icon="Delete" round>清空</el-button>
                </div>
            </el-row>
        </div>
        <div>
            <el-scrollbar max-height="720px">
                <div class="notices" :key="refresh">
                    <div v-for="notice in notices" :key="notice.id" class="notice">
                        <el-card class="notice-card">


                            <div class="notice-header">
                                <img :src="notice.avatarUrl" alt="头像" class="ava-img" @click="jumpIMG(notice.uid)">
                                <span class="notice-id">{{ notice.nickname }}</span>
                                <span class="notice-info">申请成为好友</span>
                            </div>

                            <el-row style="margin-left: 40px; margin-top: 5px">
                                <el-link type="info" style="font-size: 16px"> 申请信息：{{ (notice.message) }}</el-link>

                                <div class="flex-grow"/>
                                <!-- 操作按钮 -->
                                <span class="notice-time" style="margin-top: 5px;margin-right: 40px">{{
                                    new Date(notice.time).toString().substring(0, new Date(notice.time).toString().indexOf("GMT"))
                                    }}</span>
                                <div>
                                    <el-button @click="deleteIt(notice.id)" type="danger" :icon="Delete" round plain>
                                        删除
                                    </el-button>
                                    <el-button v-if="notice.isFriend===false"
                                               @click="addIt(notice.uid, notice.soulMatchId)" type="success"
                                               :icon="SuccessFilled" round
                                               plain>通过
                                    </el-button>
                                    <el-button v-else type="success" :icon="SuccessFilled"
                                               round plain disabled>
                                        已通过
                                    </el-button>
                                </div>
                            </el-row>
                        </el-card>
                    </div>
                </div>
            </el-scrollbar>
        </div>
    </div>
</template>

<script setup>

import {Delete, SuccessFilled} from "@element-plus/icons-vue";
import {defineProps, onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import request from "@/utils/request";
import {UToast} from "undraw-ui";

const com_tmp = {
    id: 234,
    uid: 1,
    avatarUrl: "https://static.juzicon.com/user/avatar-bf22291e-ea5c-4280-850d-88bc288fcf5d-220408002256-ZBQQ.jpeg",
    nickname: "申请用户名",
    time: "申请时间",
    message: "申请信息",
    isFriend: false,
    soulMatchId: ''
}
const notices = ref([]);
const router = useRouter()
const refresh = ref(0)
const props = defineProps(['uid']);
const newAddMatch = reactive({
    myId: props.uid,
    userId: 0,
    soulMatchId: 0
})

onMounted(() => {
    fetchNList();
});

function fetchNList() {
    request.get("/notice/friend/getFriendList", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            // notices.value = res.data;
            notices.value = Array.from(res.data).sort((a, b) => {
                return new Date(b.time) - new Date(a.time);
            })
        } else {
            console.error("获取notice列表失败！")
        }
    })
}

function deleteAll() {
    request.delete("/notice/friend/deleteAll", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            UToast({message: '清空成功!', type: 'success'})
            fetchNList();
            console.log(notices.value);
            refresh.value++;
        } else {
            UToast({message: '清空失败!', type: 'error'})
        }
    })
}


function jumpIMG(uid) {
    router.push({name: 'PersonalPage', params: {userId: uid}});
}

function deleteIt(nid) {
    console.log("删除", nid)
    request.delete("/notice/friend/delete", {params: {nid: nid}}).then(res => {
        if (res.status === 200) {
            UToast({message: '删除成功!', type: 'success'})
            fetchNList();
            console.log(notices.value);
            refresh.value++;
        } else {
            UToast({message: '删除失败!', type: 'error'})
        }
    })
}

function addIt(uid, matchId) {
    if (matchId === "") {
        request.post("/friend/addFriend?myId=" + props.uid + "&uid=" + uid).then(res => {
            if (res.status === 200) {
                UToast({message: '成为好友!', type: 'success'})
                console.log('成为好友', uid)
                fetchNList();
                refresh.value++;
            } else {
                console.error('error：没成好友');
            }
        })
    } else {
        newAddMatch.userId = uid;
        newAddMatch.soulMatchId = Number(matchId);
        console.log("post", newAddMatch);
        request.post("/match/addFriend", newAddMatch).then(res => {
            if (res.status === 200) {
                UToast({message: '成为好友!', type: 'success'})
                console.log('来自匹配的好友', uid)
                fetchNList();
                refresh.value++;
            } else {
                console.error('error：没成好友');
            }
        })
    }

}

</script>

<style scoped>
.right-title {
    margin: 0 15px 0 15px;
    padding: 10px 20px 10px 20px;
    background-color: #fff; /* 纯白色背景 */
    border-radius: 10px; /* 圆角半径，根据需要调整 */
    border: 1px solid #ccc; /* 可选：添加边框 */
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 可选：添加阴影效果 */
    font-weight: bold; /* 加粗 */
    color: #757575; /* 浅灰色 */
}

.notice-card {
    border-radius: 10px !important; /* 圆角半径，根据需要调整 */
}

.notices {
    margin-top: 15px;
    margin-left: 15px;
    margin-right: 15px;
}

.notice {
    margin-bottom: 10px;
}

.notice-header {
    display: flex;
    align-items: center;
}

.comment-header {
    display: flex;
    align-items: center;
}

.ava-img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-right: 10px;
}

.notice-id {
    margin-right: 10px;
    font-weight: bold;
}

.notice-info {
    margin-right: 10px;
    font-weight: bold;
    color: #565656;
}

.notice-time {
    margin-left: 8px;
    color: #888;
}

.notice-actions button {
    margin-right: 10px;
}
</style>