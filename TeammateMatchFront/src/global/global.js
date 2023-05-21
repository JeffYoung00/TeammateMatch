import axios from "axios";
import {showFailToast, showSuccessToast} from "vant";

let gameId=-1;
let not_login_user={
    userName: "*NOT_LOGIN*",
    gender: "/",
    email: "/",
    phone: "/",
    avatarUrl: "src/assets/default.png"
};
let me=null;
let height=300;
const setMe=(user)=>{
    me=user;
}
const getMe=()=>{
    return me;
}
const getGameId=()=>{
    return gameId;
}
const setGameId=(id)=>{
    gameId=id;
}
export {
    getGameId,
    setGameId,
    getMe,
    setMe,
    not_login_user
}

