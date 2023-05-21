import HomePage from "../page/HomePage.vue";
import Setting from "../page/Setting.vue";
import {createRouter,createWebHashHistory} from "vue-router";
import Friend from "../page/Friend.vue";
import MainPage from "../page/MainPage.vue";
import SearchTags from "../page/SearchTags.vue";
import SearchResult from "../page/UserList.vue";
import MyTags from "../page/MyTags.vue";
import EditMyself from "../page/EditMyself.vue";
import Login from "../page/Login.vue"
import Register from "../page/Register.vue"
import TagManage from "../page/TagManage.vue";
import UserList from "../page/UserList.vue";
import GameMenu from "../page/GameMenu.vue";
import MyTeam from "../page/MyTeam.vue";
import CreateTeam from "../page/CreateTeam.vue";
import SearchTeam from "../page/SearchTeam.vue";

const routes = [
    { path: '/homePage', component: HomePage },
    { path: '/setting', component: Setting },
    { path: '/friend', component: Friend },


    { path: '/mainPage', component: MainPage },
    { path: '/searchTags', component: SearchTags },
    { path: '/searchResult', component: SearchResult },
    { path: '/myTags', component: MyTags },
    { path: '/editMyself', component:  EditMyself},
    { path: '/login', component:  Login},
    { path: '/register', component:  Register},
    { path: '/tagManage', component:  TagManage},

    { path: '/userList', component:  UserList},
    { path: '/userList', component:  UserList},
    { path: '/gameMenu', component:  GameMenu},

    {path:'/myTeam',component: MyTeam},
    {path:'/createTeam',component: CreateTeam},
    {path:'/searchTeam',component: SearchTeam},

]

// 创建路由实例并传递 `routes` 配置
const router = createRouter({
    // 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: createWebHashHistory(),
    routes,
})

export default router;