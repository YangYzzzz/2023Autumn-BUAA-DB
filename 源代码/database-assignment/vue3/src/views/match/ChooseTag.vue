<template>
    <Header/>
    <el-row v-if="isChoose" class="background-container">
        <el-col :span="3"/>
        <el-col :span="18">
            <el-card style="margin: 20px 10px 10px 10px;border-radius: 20px!important;padding:0 20px 20px 20px">
                <template #header>
                    <div class="card-header">
                        <el-link style="font-size: 20px;font-weight: lighter;color:rebeccapurple;">
                            <el-icon>
                                <Refresh/>
                            </el-icon>
                            &nbsp;灵魂匹配-选取您感兴趣的人：
                        </el-link>
                        <div>
                            <el-button type="primary" plain style="margin-right: 10px;width: 70px"
                                       @click="getMyTag()">
                                我的标签
                            </el-button>
                            <el-button type="warning" plain style="margin-right: 10px;width: 70px"
                                       @click="clearTag()">
                                清空选择
                            </el-button>
                        </div>
                    </div>
                </template>

                <el-scrollbar height="70vh">
                    <div v-for="(value, key) in vector">
                        <div>
                            <div class="l-title">{{ key }}</div>
                            <el-checkbox-group v-model="checkboxGroup1" size="large">
                                <el-scrollbar height="50px">
                                    <div class="scrollbar-flex-content">
                                        <el-checkbox-button v-for="tag in value" :key="tag.first"
                                                            :label="tag.first"
                                                            class="scrollbar-demo-item">
                                            {{ tag.second }}
                                        </el-checkbox-button>
                                    </div>
                                </el-scrollbar>
                            </el-checkbox-group>
                        </div>
                    </div>
                </el-scrollbar>


                <div style="float:right;margin-top: 5px">
                    匹配人数：
                    <el-input-number v-model="tags.num" class="mx-4" :min="1" :max="10" style="margin-right: 20px">
                    </el-input-number>
                    <el-button round type="success" style="margin-right: 20px" @click="jumpBut()">
                        开始匹配&nbsp;
                        <el-icon>
                            <DArrowRight/>
                        </el-icon>
                    </el-button>
                </div>
            </el-card>
        </el-col>
        <el-col :span="3"/>
    </el-row>

    <el-row v-else class="background-container">
        <match-result :resList="resList" :matchId="matchId"/>
    </el-row>
</template>


<script setup>
import Header from "@/components/Header.vue";
import {onMounted, reactive, ref} from "vue";
import request from "@/utils/request";
import {DArrowRight, Refresh} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import MatchResult from "@/views/match/matchResult.vue";

const storedUser = JSON.parse(localStorage.getItem('user'));
const router = useRouter()

const isChoose = ref(true)
const checkboxGroup1 = ref([])
const vector = ref({});
const tags = reactive({
    uid: storedUser.id,
    num: 5,
    tagList: []
})

onMounted(() => {
    addAccess();
    getLabels();
});

function addAccess() {
    request.get("access/addAccess",
        {params: {userId: storedUser.id, pageName: "Match"}}).then(res => {
        if (res.status === 200) {
            console.log("访问量+1")
        }
    })
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

function clearTag() {
    checkboxGroup1.value = []
}

const resList = ref();
const matchId = ref();

function jumpBut() {
    tags.tagList = checkboxGroup1.value;
    //提交tag，跳转
    request.post("/match/getMatchResult", tags).then(res => {
        if (res.status === 200) {
            resList.value = res.data.first;
            matchId.value = res.data.second;
            console.log("提交tags成功", res.data);
            isChoose.value = false;
        }
    })
}
</script>

<style scoped>
.background-container {
    /* 设置背景图的URL，可以替换为你自己的背景图路径 */
    background-image: url("https://images.pexels.com/photos/5108999/pexels-photo-5108999.jpeg");

    /* 设置背景图的样式，比如平铺、居中等 */
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    /* 设置容器的高度，确保背景图充满整个容器 */
    min-height: calc(100vh - 60px);
}


.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.l-title {
    font-size: 15px;
    font-weight: bold;
    margin-bottom: 5px;
}

.scrollbar-flex-content {
    display: flex;
}
</style>