import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            redirect: '/square',
            component: () => import('../views/square.vue'),
        },
        {
            path: '/about',
            name: 'about',
            // route level code-splitting
            // this generates a separate chunk (About.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import('../views/AboutView.vue'),
            meta: {title: "关于我们"},
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/Login.vue'),
            meta: {title: "登录"},
        },
        {
            path: '/register',
            name: 'register',
            component: () => import("../views/Register.vue"),
            meta: {title: "注册"},
        },
        {
            path: '/home',
            name: "new_home",
            component: () => import('../views/Home.vue'),
            meta: {title: "主页"},
        },
        {
            path: '/personal&uid:userId',
            name: "PersonalPage",
            component: () => import('../views/user/PersonalPage.vue'),
            meta: {title: "个人主页", requireLogin: true},
        },
        {
            path: '/post&pid:postId', // 使用动态参数 postId
            name: 'PostDetail',
            component: () => import('../views/post/PostDetail.vue'),
            meta: {title: "帖子详情"},
        },
        {
            path: '/backend', // 使用动态参数 postId
            name: 'BackendHome',
            redirect: '/backend/userinfo',
            component: () => import('../views/backend/BackendManager.vue'),
            meta: {title: "后台首页"},
            children: [
                {
                    path: '/backend/userinfo',
                    name: 'BackendUserInfo',
                    component: () => import('../views/backend/BackendUserInfo.vue'),
                    meta: {title: "后台用户信息展示"},
                },
                {
                    path: '/backend/postinfo',
                    name: 'BackendPostInfo',
                    component: () => import('../views/backend/BackendPostInfo.vue'),
                    meta: {title: "后台帖子信息展示"},
                },
                {
                    path: '/backend/home',
                    name: 'BackendHome',
                    component: () => import('../views/backend/BackendHome.vue'),
                    meta: {title: "后台主页"},
                },
                {
                    path: '/backend/imageinfo',
                    name: 'BackendImage',
                    component: () => import('../views/backend/BackendImageInfo.vue'),
                    meta: {title: "后台图片信息展示"}
                },
                {
                    path: '/backend/labelinfo',
                    name: 'BackendLabel',
                    component: () => import('../views/backend/BackendLabelInfo.vue'),
                    meta: {title: "后台标签信息展示"}
                },
                {
                    path: '/backend/announcement',
                    name: 'BackendAnnouncement',
                    component: () => import('../views/backend/BackendAnnouncement.vue'),
                    meta: {title: "后台发布管理员公告"}
                },
                {
                    path: '/backend/matchinfo',
                    name: 'BackendMatch',
                    component: () => import('../views/backend/BackendMatchInfo.vue'),
                    meta: {title: "后台交友匹配信息展示"}
                },
                {
                    path: '/backend/actiontrace',
                    name: 'BackendActionTrace',
                    component: () => import('../views/backend/BackendActionTrace.vue'),
                    meta: {title: "后台用户行为追踪"}
                },
                {
                    path: '/backend/uservisible',
                    name: 'BackendUserVisible',
                    component: () => import('../views/backend/BackendUserVisible.vue'),
                    meta: {title: "后台用户可视化展示"}
                },
                {
                    path: '/backend/postvisible',
                    name: 'BackendPostVisible',
                    component: () => import('../views/backend/BackendPostVisible.vue'),
                    meta: {title: "后台帖子可视化展示"}
                },
                {
                    path: '/backend/socialvisible',
                    name: 'BackendSocialVisible',
                    component: () => import('../views/backend/BackendSocialVisible.vue'),
                    meta: {title: "后台社交信息可视化展示"}
                },
                {
                    path: '/backend/surprise',
                    name: 'BackendSurprise',
                    component: () => import('../views/backend/BackendSurprise.vue'),
                    meta: {title: "后台有趣应用"}
                },
            ]
        },
        {
            path: '/post&pid:postId', // 使用动态参数 postId
            name: 'PostDetail',
            component: () => import('../views/post/PostDetail.vue'),
            meta: {title: "帖子详情", requireLogin: true},
        },
        {
            path: '/message',
            name: "Message",
            component: () => import('../views/user/Notice.vue'),
            meta: {title: "消息中心", requireLogin: true},
        },
        {
            path: '/publish',
            name: "Publish",
            component: () => import('../views/post/Publish.vue'),
            meta: {title: "发布帖子", requireLogin: true},
        },
        {
            path: '/match',
            name: "Match",
            component: () => import('../views/match/ChooseTag.vue'),
            meta: {title: "选择标签", requireLogin: true},
        },
        {
            path: '/chat',
            name: "chat",
            component: () => import('../views/chat/chat.vue'),
            meta: {title: "聊天", requireLogin: true},
        },
        {
            path: '/square',
            name: "square",
            component: () => import('../views/square.vue'),
            meta: {title: "广场"},
        }
    ]
})

// router.js
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title; //显示页面标题为meta.title的内容
    }
    next();
});

// router.beforeEach((to, from, next) => {
//   localStorage.setItem("currentPathName", to.name.toString) //存储去到的子路由名称
//   console.log(to.name)
//   //触发store的数据更新
//   useCounterStore
//   // 未找到路由的情况 若已经登陆 则跳转404 若未登录 跳转登录界面
//   if (!to.matched.length) {
//     console.log(to.path)
//     const storeUser = localStorage.getItem("user")
//     if (storeUser) {
//       next('/404') //放行到from路径的路由
//       return
//     } else {
//       next('/login')
//       return
//     }
//   }
//   next()
// })
export default router
