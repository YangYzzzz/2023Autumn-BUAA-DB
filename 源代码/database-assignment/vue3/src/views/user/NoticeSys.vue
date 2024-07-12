<template>
    <div class="space-right">
        <div style="height: 10px"></div>
        <div class="right-title">
            <el-row>
                <div style="margin-top: 3px">
                    系统消息
                </div>
                <div class="flex-grow"/>
                <div>
                    <el-button @click="deleteAll()" type="danger" :icon="Delete" round>清空</el-button>
                </div>
            </el-row>
        </div>
        <div>
            <el-scrollbar max-height="720px">
                <div class="replies">
                    <div v-for="sysNotice in sysList" :key="sysNotice.id" class="reply">
                        <el-card class="notice-card">
                            <p class="reply-text"> sys: {{ sysNotice.message }}</p>
                            <!-- 操作按钮 -->
                            <el-row style="margin-left: 15px; margin-top: 5px">
                                <span class="reply-time" style="margin-top: 8px">{{ new Date(sysNotice.time).toString().substring(0,new Date(sysNotice.time).toString().indexOf("GMT")) }}</span>
                                <div class="flex-grow"/>
                                <div>
                                    <el-button @click="deleteIt(sysNotice.id)" type="danger" :icon="Delete" round plain>删除</el-button>
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

import {Delete} from "@element-plus/icons-vue";
import {defineProps, onMounted, ref} from "vue";
import request from "@/utils/request";
import {UToast} from "undraw-ui";

const props = defineProps(['uid']);
const refresh = ref(0);

onMounted(() => {
    fetchSysList();
});
const com_tmp = {
    id: 1,//系统消息的id
    time: "系统消息时间",
    message: "系统通知内容"
}
const sysList = ref([]);

function fetchSysList() {
    request.get("/notice/sys/getSysList", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            // sysList.value = res.data;
            sysList.value = Array.from(res.data).sort((a, b) => {
                return new Date(b.time) - new Date(a.time);
            })
            console.log("回复列表", res.data)
        } else {
            console.error("获取reply列表失败！")
        }
    })
}

//删除消息
function deleteIt(sysId) {
    console.log("删除", sysId)
    request.delete("/notice/sys/deleteSys", {params: {uid: props.uid, sid: sysId}}).then(res => {
        if (res.status === 200) {
            UToast({message: '删除成功!', type: 'success'})
            fetchSysList();
            refresh.value++;
        } else {
            UToast({message: '删除失败!', type: 'error'})
        }
    })
}

function deleteAll() {
    request.delete("/notice/sys/deleteAll", {params: {uid: props.uid}}).then(res => {
        if (res.status === 200) {
            UToast({message: '清空成功!', type: 'success'})
            fetchSysList();
            refresh.value++;
        } else {
            UToast({message: '清空失败!', type: 'error'})
        }
    })
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

.replies {
    margin-top: 15px;
    margin-left: 15px;
    margin-right: 15px;
}

.reply {
    margin-bottom: 10px;
}

.reply-header {
    display: flex;
    align-items: center;
}

.reply-header img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-right: 10px;
}


.reply-time {
    color: #888;
}

.reply-text {
    margin-top: 5px;
    margin-left: 15px;
}

.reply-actions button {
    margin-right: 10px;
}
</style>