import HomePage from "../page/HomePage.vue";
import Setting from "../page/Setting.vue";
import {createRouter,createWebHashHistory} from "vue-router";
import Friend from "../page/Friend.vue";
import Search from "../page/SearchTags.vue";
import MainPage from "../page/MainPage.vue";
import SearchTags from "../page/SearchTags.vue";
import SearchResult from "../page/SearchResult.vue";
import MyTags from "../page/MyTags.vue";
import EditMyself from "../page/EditMyself.vue";
import Login from "../page/Login.vue"
import Register from "../page/Register.vue"
import TagManage from "../page/TagManage.vue";

const routes = [
    { path: '/home', component: HomePage },
    { path: '/setting', component: Setting },
    { path: '/friend', component: Friend },


    { path: '/main', component: MainPage },
    { path: '/search', component: SearchTags },
    { path: '/result', component: SearchResult },
    { path: '/myTags', component: MyTags },
    { path: '/editMe', component:  EditMyself},
    { path: '/login', component:  Login},
    { path: '/register', component:  Register},
    { path: '/editTags', component:  TagManage},

]

// 创建路由实例并传递 `routes` 配置
const router = createRouter({
    // 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: createWebHashHistory(),
    routes,
})

export default router;