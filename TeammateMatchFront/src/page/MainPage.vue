<template>

  <van-grid :column-num="2">
    <van-grid-item icon="photo-o" to="/gameMenu">
      <img src="src/assets/lol.png" v-bind:width="width" v-bind:height=height v-on:click="setGameId(0)">
    </van-grid-item>
    <van-grid-item icon="photo-o" to="/gameMenu">
      <img src="src/assets/wzry.png" v-bind:width="width" v-bind:height=height v-on:click="setGameId(1)">
    </van-grid-item>
    <van-grid-item icon="photo-o" to="/gameMenu">
      <img src="src/assets/ys.png" v-bind:width="width" v-bind:height=height>
    </van-grid-item>
    <van-grid-item icon="photo-o" to="/gameMenu">
      <img src="src/assets/java.png" v-bind:width="width" v-bind:height=height>
    </van-grid-item>
  </van-grid>


</template>
<script setup>
const width=120;
const height=120;
import {setGameId} from "../global/global.js";

</script>