<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import watermark from "./utils/watermark";
export default  {
  name:  'App',
  computed:{
    nickName(){
      return this.$store.getters.nickName;
    },
    userId(){
      return this.$store.getters.userId;
    }
  },
  watch:{
    nickName(){
      if(this.nickName&&this.userId){
        this.msg=this.nickName+''+this.userId;
        this.setWaterMark()
      }
    },
    userId(){
      if(this.nickName&&this.userId){
        this.msg=this.nickName+''+this.userId;
        this.setWaterMark()
      }
    }
  },
  data() {
    return {
      msg:""
    }
  },
  methods:{
    setWaterMark(){
      watermark.set(this.msg)
    }
  },
}
</script>
<style lang="scss">
@import "./assets/common/font/font.css";
</style>
