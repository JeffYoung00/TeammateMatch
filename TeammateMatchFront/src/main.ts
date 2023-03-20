import 'vant/lib/index.css';
import './style.css'
import 'vant/es/toast/style';
import router from './config/route'
import axios from "axios";
axios.defaults.withCredentials=true;

import App from './App.vue'
const app=createApp(App);

import { createApp } from 'vue'
import Vant from 'vant'
app.use(Vant);

app.use(router);

app.mount('#app');

// app.config.globalProperties.$userIn={
//     id:-1,
//     userName:"",
//     gender:"",
//     avatarUrl:"",
//     email:"",
//     userState:0,
//     createTime:"",
//     phone:"",
//     userRole:0
// };