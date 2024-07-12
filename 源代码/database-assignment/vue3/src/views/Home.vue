<template>
    <!--上方：导航栏-->
    <Header/>

    <!--主要内容，分为左右两大块-->
    <el-row>
        <el-col :span="2"><div class="grid-content ep-bg-purple" /></el-col>
        <el-col :span="12"><div class="grid-content ep-bg-purple-light" />
            <el-card :body-style="{ padding: '0px' }">
                <div class="demo-image">
                    <div class="block">
                        <span class="demonstration"></span>
                        <el-image style="height: 600px"
                                  :src="'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'"
                                  fit="cover" />
                    </div>
                </div>
                <div style="padding: 14px">
                    <span>小标题</span>
                    <div class="bottom">
                        <time class="time">{{ currentDate }}</time>
                        <el-button text class="button">Operating</el-button>
                    </div>
                </div>
            </el-card>
        </el-col>
        <el-col :span="2"><div class="grid-content ep-bg-purple-light" /></el-col>
        <el-col :span="6"><div class="grid-content ep-bg-purple" />
            <el-carousel height="150px">
                <el-carousel-item v-for="item in 4" :key="item">
                    <h3 class="small justify-center" text="2xl">{{ item }}</h3>
                </el-carousel-item>
            </el-carousel>
            <el-card :body-style="{ padding: '0px' }" style="margin-top: 20px">
                <img
                    src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                    class="image"
                />
                <div style="padding: 14px">
                    <span>Yummy hamburger</span>
                    <div class="bottom">
                        <time class="time">{{ currentDate }}</time>
                        <el-button text class="button">Operating</el-button>
                    </div>
                </div>
            </el-card>

        </el-col>
        <el-col :span="2"><div class="grid-content ep-bg-purple-light" /></el-col>
    </el-row>

</template>

<script lang="ts" setup>
import Header from '../components/Header.vue'
import { ref } from 'vue'

const currentDate = new Date().toDateString()

const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
</script>

<style>
.flex-grow {
    flex-grow: 1;
}
.el-row {
    margin-bottom: 20px;
}
.el-row:last-child {
    margin-bottom: 0;
}
.el-col {
    border-radius: 4px;
}

.grid-content {
    border-radius: 4px;
    min-height: 36px;
}
.time {
    font-size: 12px;
    color: #999;
}

.bottom {
    margin-top: 13px;
    line-height: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.button {
    padding: 0;
    min-height: auto;
}

.image {
    width: 100%;
    display: block;
}
.menu_item {
    margin-top: 15px;
    margin-left: 10px;
    margin-right: 30px;
}

.demonstration {
    color: var(--el-text-color-secondary);
}

.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
    text-align: center;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
}
</style>
